package com.gsty.corelibs.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.gsty.corelibs.utils.KeyBoardUtils;

public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyBoardUtils.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
