package com.re.ng.uu.comic.base;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.re.ng.uu.comic.http.bean.EventMessage;
import com.re.ng.uu.comic.util.ActivityCollectorUtil;
import com.re.ng.uu.comic.util.DialogUtil;
import com.re.ng.uu.comic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG;
    public Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusTextColor(true);
        TAG = this.getClass().getSimpleName();
        ActivityCollectorUtil.addActivity(this);
        EventBus.getDefault().register(this);
        dialog = DialogUtil.showProgressDialog(this);
        dialog.setCancelable(true);

        LogUtil.e(getClass().getName() + " onCreate start === ");
    }

    /**
     * 设置状态栏字体颜色
     *
     * @param isDark
     */
    protected void setStatusTextColor(boolean isDark) {
        if (isDark) {
            //黑色字体
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            //白色字体
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollectorUtil.removeActivity(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    protected static String getString(EditText et) {
        return et.getText().toString();
    }

    public void showLoadingDialog() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMSG(EventMessage messageEvent) {

    }

}
