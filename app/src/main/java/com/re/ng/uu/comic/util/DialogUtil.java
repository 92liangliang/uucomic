package com.re.ng.uu.comic.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import com.jiangyy.easydialog.InputDialog;
import com.jiangyy.easydialog.UpdateDialog;
import com.ldoublem.loadingviewlib.view.LVCircularZoom;
import com.re.ng.uu.comic.R;
import com.scwang.smartrefresh.layout.util.DensityUtil;


/**
 * Created by Administrator on 2018/7/22 0022.
 */

public class DialogUtil {

    public static Dialog showProgressDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_load, null);
//        DYLoadingView dyLoadingView = new DYLoadingView(activity);
        LVCircularZoom loadingView = new LVCircularZoom(activity);
        loadingView.setViewColor(Color.parseColor("#FC4A4F"));

        builder.setCancelable(false);
        builder.setView(loadingView);
        AlertDialog dialog = builder.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        lp.width = 100;//定义宽度
        lp.height = 350;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnShowListener(dialogInterface -> {
            WindowManager.LayoutParams lp1 = dialog.getWindow().getAttributes();
            lp1.width = DensityUtil.dp2px(81f);//定义宽度
            dialog.getWindow().setAttributes(lp1);
        });
//        dyLoadingView.start();
        loadingView.startAnim();
        return dialog;
    }

    //
    public static void showWarningDialog(Activity activity, String title, String msg) {
        new UpdateDialog.Builder(activity)
            .setIcon(R.drawable.ic_warning)
            .setTitle(title, R.color.black)
            .setMessage(msg, R.color.black)
            .setPositiveButton(activity.getString(R.string.confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, R.color.black).show();
    }

    public static void showInputDialog(Activity activity, String title, String msg, View.OnClickListener listener) {
        new InputDialog.Builder(activity)
            .setHint(msg, R.color.black)
            .setInputType(EditorInfo.TYPE_CLASS_NUMBER)
            .setTitle(title, R.color.black)
            .setMessage("", R.color.black)
            .setPositiveButton(activity.getString(R.string.confirm), listener, R.color.black)
            .setNegativeButton(activity.getString(R.string.cancel), null, R.color.black)
            .show();
    }

    public static void showMessageDialog(Activity activity, String title, String msg, View.OnClickListener listener) {
        new UpdateDialog.Builder(activity)
            .setIcon(R.drawable.ic_warning)
            .setTitle(title, R.color.black)
            .setMessage(msg, R.color.black)
            .setNegativeButton(activity.getString(R.string.cancel), null, R.color.black)
            .setPositiveButton(activity.getString(R.string.confirm), listener, R.color.black)
            .show();
    }

    public static void showMessageDialog(Activity activity, String title, String msg, String positive, View.OnClickListener listener) {
        new UpdateDialog.Builder(activity)
            .setIcon(R.drawable.ic_warning)
            .setTitle(title, R.color.black)
            .setMessage(msg, R.color.black)
            .setNegativeButton(activity.getString(R.string.cancel), null, R.color.black)
            .setPositiveButton(positive, listener, R.color.black)
            .show();
    }

    public static void showMsgDialog(Activity activity, String title, String msg, String positive, View.OnClickListener listener) {
        new UpdateDialog.Builder(activity)
            .setIcon(R.drawable.ic_warning)
            .setTitle(title, R.color.black)
            .setMessage(msg, R.color.black)
            .setPositiveButton(positive, listener, R.color.black)
            .setCancelable(false)
            .setCanceledOnTouchOutside(false)
            .show();
    }

}
