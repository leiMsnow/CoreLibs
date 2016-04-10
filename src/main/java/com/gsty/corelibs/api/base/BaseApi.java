package com.gsty.corelibs.api.base;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gsty.corelibs.api.encrypt.Decrypt;
import com.gsty.corelibs.api.encrypt.Encrypt;
import com.gsty.corelibs.api.model.ApiResult;
import com.gsty.corelibs.api.model.TicketTimeoutEvent;
import com.gsty.corelibs.utils.GzipUtils;
import com.ray.corelibs.R;
import com.gsty.corelibs.base.BaseApplication;
import com.gsty.corelibs.api.callback.IApiCallback;
import com.gsty.corelibs.api.model.ApiErrorResult;
import com.gsty.corelibs.utils.Constants;
import com.gsty.corelibs.utils.LogUtil;
import com.gsty.corelibs.utils.NetUtils;
import com.gsty.corelibs.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * 输入接口：修改、创建的接口；
 * 输出接口：列表、详情接口；
 * <p/>
 * Created by zhangleilei on 15/7/8.
 */
public class BaseApi {

    protected Context mContext;
    private static BaseApi mApi;

    /**
     * 接口请求成功
     */
    public final static int API_SUCCESS = 0;
    /**
     * 无网络
     */
    public final static int API_NO_NETWORK = -1;

    public final static int TICKET_TIMEOUT = -3;
    /**
     * 服务器地址错误/无数据
     */
    public final static int API_URL_ERROR = -404;

    protected Map<String, Object> mParams;

    protected Gson mGson;


    public static BaseApi getInstance() {
        if (mApi == null) {
            synchronized (BaseApi.class) {
                if (mApi == null) {
                    mApi = new BaseApi();
                }
            }
        }
        return mApi;
    }

    /**
     * 声明Request请求
     */
//    private JsonObjectRequest jsonObjectRequest = null;
//    private StringRequest stringRequest = null;
    // 正式环境
    public static String HOST_MAIN = "http://mapi.yuexizhihui.com/YuexiMobileApi/";
    // 测试环境
    public static String HOST_TEST = "http://192.168.1.110:8080/YuexiMobileApi/";

    protected BaseApi() {
        this.mContext = BaseApplication.getInstance();
        this.mGson = new Gson();
    }

    /**
     * 获得服务器地址
     *
     * @return 服务器地址
     */
    public String getHostUrl() {
        return HOST_MAIN;
    }

    /**
     * 接口请求地址
     */
    private String getRequestUrl(String apiName) {
        return getHostUrl() + apiName;
    }


    protected void stringPostRequest(final String apiName, String jsonParams, boolean isZip,
                                     final IApiCallback callback) {

        Map<String, String> params = new HashMap<>();
        final String message = Encrypt.getRSA(jsonParams);
        final String sign = Encrypt.getMD5(message);
        if (isZip) {
            params = GzipUtils.gzip(message, sign);
        } else {
            params.put("message", message);
            params.put("sign", sign);
        }
        final String requestUrl = getRequestUrl(apiName);

        stringRequest(Request.Method.POST, requestUrl, apiName, params, callback);

    }

    protected void stringGetRequest(final String apiName, String jsonParams,
                                    final IApiCallback callback) {

        final String message = Encrypt.getRSA(jsonParams);
        final String sign = Encrypt.getMD5(message);
        final String requestUrl = getRequestUrl(apiName) + "message=" + message + "&sign=" + sign;

        stringRequest(Request.Method.GET, requestUrl, apiName, null, callback);
    }

    /**
     * string类型请求列队
     *
     * @param method     默认为get方式
     * @param requestUrl 请求方法名
     * @param params     请求参数
     * @param callback   回调
     */
    private void stringRequest(int method, final String requestUrl,
                               final String apiName,
                               final Map<String, String> params,
                               final IApiCallback<ApiResult> callback) {
        if (callback != null)
            callback.onStartApi();

        if (TextUtils.isEmpty(requestUrl)) {
            ApiErrorResult errorResult = createEmptyResult(requestUrl, "请输入正确的请求方法名");
            if (callback != null)
                callback.onFailure(errorResult);
            return;
        }
        if (!NetUtils.isConnected(mContext)) {
            ApiErrorResult errorResult = createEmptyResult(requestUrl, getErrorMessage());
            if (callback != null)
                callback.onFailure(errorResult);
            return;
        }
        LogUtil.d("stringRequest-url:", requestUrl);
        LogUtil.d("stringRequest-params:", params == null ? "" : params.toString());
        // 创建request
        try {
            StringRequest stringRequest = new StringRequest(method, requestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String stringObject) {

                            String result = Decrypt.getFromRsa(stringObject);

                            LogUtil.d("onResponse-url:", requestUrl);
                            LogUtil.d("onResponse-url:", "onResponse-data: \n "
                                    + result);
                            try {

                                JSONObject jsonObject = new JSONObject(result);
                                ApiResult apiResult = new ApiResult();
                                apiResult.setErrcode(jsonObject.optInt("errcode"));
                                apiResult.setErrmsg(jsonObject.optString("errmsg"));

                                if (apiResult.getErrcode() == TICKET_TIMEOUT) {
                                    EventBus.getDefault().post(new TicketTimeoutEvent());
                                    ApiErrorResult errorResult = new ApiErrorResult();
                                    errorResult.setDisplayType(IApiCallback.DisplayType.Toast);
                                    errorResult.setErrorMessage(apiResult.getErrmsg());
                                    errorResult.setErrorCode(API_URL_ERROR);
                                    if (callback != null)
                                        callback.onFailure(errorResult);
                                    return;
                                }

                                apiResult.setExtraMap(jsonObject.optString("extraMap"));
                                apiResult.setApiName(apiName);

                                if (callback != null)
                                    callback.onComplete(apiResult);

                            } catch (JSONException e) {
                                // 请求失败,错误信息回调给调用方
                                ApiErrorResult errorResult = new ApiErrorResult();
                                errorResult.setDisplayType(IApiCallback.DisplayType.Toast);
                                errorResult.setErrorMessage(e.toString());
                                errorResult.setErrorCode(API_URL_ERROR);
                                if (callback != null)
                                    callback.onFailure(errorResult);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    LogUtil.d("onErrorResponse-url:", requestUrl);
                    LogUtil.d("onErrorResponse-info:", "volleyError-ServerError");

                    // 请求失败,错误信息回调给调用方
                    String errorMessage = getErrorMessage();
                    ApiErrorResult errorResult = new ApiErrorResult();
                    errorResult.setDisplayType(IApiCallback.DisplayType.ALL);
                    errorResult.setErrorMessage(errorMessage);
                    errorResult.setErrorCode(API_URL_ERROR);
                    errorResult.setApiName(apiName);
                    if (callback != null)
                        callback.onFailure(errorResult);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params == null ? super.getParams() : params;
                }
            };
            // 禁用缓存
            stringRequest.setShouldCache(false);
            // 添加请求到Volley队列
            BaseApplication.getInstance().getRequestQueue().add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            apiError(callback, e);
        }

    }


    private void apiError(IApiCallback callback, Exception e) {
        LogUtil.e("onError-jsonObjectRequest-json:", "解析json异常" + e.toString());
        ApiErrorResult errorResult = new ApiErrorResult();
        errorResult.setDisplayType(IApiCallback.DisplayType.Toast);
        errorResult.setErrorMessage("解析json异常");
        if (callback != null)
            callback.onFailure(errorResult);
    }

    /**
     * 服务器异常的错误提示
     *
     * @return
     */
    protected String getErrorMessage() {
        return mContext.getResources().getString(R.string.api_error);
    }

    /**
     * 将boolean转换成string的值，系统默认转换的是int值
     *
     * @param type
     * @return
     */
    protected String getTypeStr(boolean type) {
        return String.valueOf(type ? 1 : 0);
    }


    protected ApiErrorResult createEmptyResult(String apiName, String errorMessage) {
        ApiErrorResult errorResult = new ApiErrorResult();
        errorResult.setDisplayType(IApiCallback.DisplayType.Toast);
        errorResult.setErrorCode(API_URL_ERROR);
        errorResult.setErrorMessage(errorMessage);
        errorResult.setApiName(apiName);
        return errorResult;
    }


}
