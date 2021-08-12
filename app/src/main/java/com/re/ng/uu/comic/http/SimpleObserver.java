package com.re.ng.uu.comic.http;

import android.app.Dialog;
import android.text.TextUtils;

import com.re.ng.uu.comic.http.bean.BaseBean;
import com.re.ng.uu.comic.util.LogUtil;
import com.re.ng.uu.comic.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Date    : 2020-10-29
 */
public class SimpleObserver<T> implements Observer<T> {
    private SmartRefreshLayout smartRefreshLayout;
    private Dialog dialog;

    public SimpleObserver() {
    }

    public SimpleObserver(SmartRefreshLayout smartRefreshLayout) {
        this.smartRefreshLayout = smartRefreshLayout;
    }

    public SimpleObserver(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T result) {
        closeLoading();
        if (result instanceof BaseBean) {
            LogUtil.d("AppLog Response=" + result.toString());
            if (!((BaseBean) result).isSuccess() && ((BaseBean) result).getErr() == 1) {
                String msg = ((BaseBean) result).getMsg();
                if (!TextUtils.isEmpty(msg)) {
//                    msg = "未知错误";
                    LogUtil.d("AppLog Response" + msg);
                    ToastUtil.show(msg);
                }

            }
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.d("AppLog Response", e.toString());
        closeLoading();
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        closeLoading();
    }

    private void closeLoading() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadMore();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
