package com.gsty.corelibs.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gsty.corelibs.api.model.ApiResult;
import com.gsty.corelibs.base.activity.BaseApiActivity;
import com.gsty.corelibs.api.callback.IApiCallback;
import com.gsty.corelibs.api.model.ApiErrorResult;

import de.greenrobot.event.EventBus;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseApiFragment extends BaseTemplateFragment implements IApiCallback {

    private BaseApiActivity mBaseApiActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getActivity() instanceof BaseApiActivity)
            mBaseApiActivity = (BaseApiActivity) getActivity();
    }

    @Override
    public void onStartApi() {
        if (mBaseApiActivity != null)
            mBaseApiActivity.onStartApi();
    }

    @Override
    public void onComplete(Object obj) {
        if (mBaseApiActivity != null)
            mBaseApiActivity.onComplete(obj);
    }

    @Override
    public void onFailure(ApiErrorResult result) {
        if (result.getDisplayType() == DisplayType.View ||
                result.getDisplayType() == DisplayType.ALL) {
            setEmptyView(result);
            result.setDisplayType(DisplayType.None);
        }
        if (mBaseApiActivity != null)
            mBaseApiActivity.onFailure(result);
    }

    public void onEventMainThread(Object obj) {

    }

    @Override
    public void setEmptyView(ApiErrorResult result) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
