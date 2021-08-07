package com.re.ng.uu.comic.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rdc.bms.easy_rv_adapter.fragment.AbsFragment;
import com.re.ng.uu.comic.util.LogUtil;

public abstract class BaseAbsFragment extends AbsFragment {

    protected void showToast(String msg){
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LogUtil.e(getClass().getName()+" onCreate fragment start === ");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
