package com.gsty.corelibs.transfer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.gsty.corelibs.base.BaseApplication;
import com.gsty.corelibs.utils.LogUtil;
import com.gsty.corelibs.utils.ToastUtil;

/**
 * activity跳转中心
 * Created by zhangleilei on 3/26/16.
 */
public class TransferCenter {


    private static TransferCenter transferCenter;
    private Context mContext;

    private static final String SCHEME_SYMBOL = "://";

    private TransferCenter(Context context) {
        this.mContext = context;
    }

    public static TransferCenter get() {
        if (transferCenter == null) {
            synchronized (TransferCenter.class) {
                if (transferCenter == null) {
                    transferCenter = new TransferCenter(BaseApplication.getInstance());
                }
            }
        }
        return transferCenter;
    }


    public void startNewActivity(String path, Bundle bundle) {
        try {

            String paths[] = path.split(SCHEME_SYMBOL);
            Uri uri = Uri.parse(paths[0] + SCHEME_SYMBOL + mContext.getApplicationInfo().packageName)
                    .buildUpon()
                    .appendPath(paths[1])
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (bundle != null)
                intent.putExtras(bundle);
            mContext.startActivity(intent);
        } catch (Exception e) {
            LogUtil.e("TransferCenter", e.toString());
            ToastUtil.getInstance(mContext).showToast("未找到相应的界面");
        }
    }

}
