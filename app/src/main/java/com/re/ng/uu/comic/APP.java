package com.re.ng.uu.comic;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.re.ng.uu.comic.http.bean.UserInfo;
import com.re.ng.uu.comic.util.StartActUtil;

import org.litepal.LitePal;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Date    : 2020-10-29
 */
public class APP extends Application {

    public static final String TAG = "UUComic";
    private UserInfo userInfo;

    @SuppressLint("StaticFieldLeak")
    private static APP instance;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        LitePal.getDatabase();
        instance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.detectFileUriExposure();
        }
        //初始化sdk
        JAnalyticsInterface.init(this);
    }

    public static APP getInstance() {
        return instance;
    }

    public boolean checkLogin(Context context) {
        return checkLogin(context, true);
    }

    public boolean checkLogin(Context context, boolean toLogin) {
        if (isUserLogin()) {
            return true;
        } else {
            if (toLogin) {
                StartActUtil.toLogin(context);
            }
            return false;
        }
    }

    public boolean isUserLogin() {
        return userInfo != null;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        storeUserInfo();
    }

    private void storeUserInfo() {
        if(userInfo != null){
            LitePal.deleteAll(UserInfo.class);
            userInfo.save();
        }
//        UserInfo userInfo = LitePal.findFirst(UserInfo.class);
//        LogUtil.d("userInfo = "+userInfo);
    }

    public String getUToken() {
        if (isUserLogin()) {
            return userInfo.getUtoken();
        }
        return null;
    }
}
