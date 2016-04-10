package com.gsty.corelibs.base.fragment;

import android.view.View;
import android.view.ViewGroup;

import com.gsty.corelibs.api.callback.IApiCallback;
import com.gsty.corelibs.api.RequestApiListener;
import com.gsty.corelibs.api.model.ApiErrorResult;
import com.gsty.corelibs.api.model.ApiResult;
import com.gsty.corelibs.utils.Constants;
import com.gsty.corelibs.utils.DensityUtils;
import com.gsty.corelibs.utils.EmptyViewUtils;
import com.gsty.corelibs.utils.SPUtils;
import com.ray.corelibs.R;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseToolBarFragment extends BaseApiFragment implements  RequestApiListener {

    private View mEmptyParentView;

    private View mToolbar;

    @Override
    public void setEmptyView(ApiErrorResult result) {

        mEmptyParentView = mView.findViewById(R.id.rl_empty_view_parent);
        if (mEmptyParentView == null) {
            mEmptyParentView = EmptyViewUtils.createEmptyView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) mView).addView(mEmptyParentView, layoutParams);
        } else {
            EmptyViewUtils.setAlphaEmptyView(mEmptyParentView);
        }
        EmptyViewUtils.showEmptyView(result, mEmptyParentView, getToolbarHeight(), this);

    }

    @Override
    public void onStartApi() {
        hideEmptyView();
        super.onStartApi();
    }

    /**
     * 隐藏空数据布局
     */
    protected void hideEmptyView() {
        EmptyViewUtils.hideEmptyView(mEmptyParentView);
    }


    protected int getToolbarHeight() {
        int height = 0;
        if (mToolbar != null && mToolbar.getVisibility() != View.GONE) {
            height = DensityUtils.dp2px(mContext, 56);
        }
        return height;
    }

}
