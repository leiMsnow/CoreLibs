package com.gsty.corelibs.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;

import com.gsty.corelibs.base.ActivityContainer;
import com.gsty.corelibs.base.activity.swipeback.SwipeBackActivity;
import com.gsty.corelibs.base.activity.swipeback.SwipeBackLayout;
import com.gsty.corelibs.utils.KeyBoardUtils;

/**
 * 基础activity，处理通用功能：
 * 1.记录打开的activity
 * 2.控制activity右滑关闭
 */
public abstract class BasisActivity extends SwipeBackActivity {

    protected Context mContext;
    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContainer.getInstance().addActivity(this);
        mContext = this;
        // 使得音量键控制媒体声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyBoardUtils.hideSoftKeyboard(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityContainer.getInstance().removeActivity(this);
    }
}