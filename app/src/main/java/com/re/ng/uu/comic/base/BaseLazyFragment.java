package com.re.ng.uu.comic.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public abstract class BaseLazyFragment extends Fragment {
    protected BaseActivity mBaseActivity;  //贴附的activity,Fragment中可能用到
    protected View mRootView;           //根view
    protected boolean isVisible = false;
    protected boolean isPrepared = false;
    protected boolean isLoaded = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getContext()).inflate(setLayoutResourceId(), null);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        isPrepared = true;
        lazyLoad();
    }

    protected abstract int setLayoutResourceId();

    //初始化View
    protected abstract void initView();

    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible(){
    }

    protected String getString(EditText editText){
        return editText.getText().toString();
    }

    protected void showToast(String msg) {
        Toast.makeText(mBaseActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void startActivity(Class clazz) {
        Intent intent = new Intent(mBaseActivity, clazz);
        startActivity(intent);
    }
}
