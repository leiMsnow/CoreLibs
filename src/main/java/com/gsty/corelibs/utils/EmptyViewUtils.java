package com.gsty.corelibs.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsty.corelibs.api.base.BaseApi;
import com.gsty.corelibs.api.RequestApiListener;
import com.gsty.corelibs.api.model.ApiErrorResult;
import com.ray.corelibs.R;


/**
 * 如果想让空数据布局正常显示，父控件务必使用RelativeLayout或FrameLayout
 * Created by zhangleilei on 9/19/15.
 */
public class EmptyViewUtils {

    private EmptyViewUtils() {

    }

    /**
     * 获取空数据的背景图id
     *
     * @param result
     * @return
     */
    private static int getEmptyViewRes(ApiErrorResult result) {
        int resId = 0;

        // 无网络
        if (result.getErrorCode() == BaseApi.API_NO_NETWORK) {
            resId = R.mipmap.bg_empty_no_network;
        }
        // 服务器不通
        else if (result.getErrorCode() == BaseApi.API_URL_ERROR) {
            resId = R.mipmap.bg_empty_url_error;
        }

        return resId;
    }

    /**
     * 创建空数据布局
     */
    public static View createEmptyView(Context mContext) {
        View mEmptyParentView = LayoutInflater.from(mContext)
                .inflate(R.layout.view_empty, null);
        ObjectAnimator objectAnimator = ObjectAnimator.
                ofFloat(mEmptyParentView, "alpha", 0.0f, 1.0f)
                .setDuration(500);
        objectAnimator.start();
        return mEmptyParentView;
    }


    /**
     * 显示空数据布局
     *
     * @param result
     * @param mEmptyParentView
     * @param height
     * @param requestApiListener
     */
    public static void showEmptyView(ApiErrorResult result, View mEmptyParentView, int height
            , final RequestApiListener requestApiListener) {
        if (mEmptyParentView != null) {
            int resId = getEmptyViewRes(result);
            if (resId == 0) {
                hideEmptyView(mEmptyParentView);
                return;
            }
            // 设置文本
            TextView tvMsg = (TextView) mEmptyParentView.findViewById(R.id.tv_empty);
            tvMsg.setText(result.getErrorMessage());
            // 设置图片
            ImageView ivEmpty = (ImageView) mEmptyParentView.findViewById(R.id.iv_empty);
            ivEmpty.setImageResource(resId);
            // 设置监听
            View mEmptyView = mEmptyParentView.findViewById(R.id.rl_empty_view);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mEmptyView.getLayoutParams();
            lp.topMargin = height;
            mEmptyView.setLayoutParams(lp);

            mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestApiListener != null) {
                        requestApiListener.onRequest();
                    }
                }
            });
        }
    }

    /**
     * 显示空数据布局
     *
     * @param mEmptyParentView
     */
    public static void setAlphaEmptyView(View mEmptyParentView) {

        if (mEmptyParentView != null) {
            mEmptyParentView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 隐藏空数据布局
     */
    public static void hideEmptyView(final View mEmptyParentView) {
        if (mEmptyParentView != null) {
            mEmptyParentView.setVisibility(View.GONE);
        }
    }


}
