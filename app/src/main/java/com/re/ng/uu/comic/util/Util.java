package com.re.ng.uu.comic.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.view.activity.WebPurchaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Date    : 2020-12-15
 */
public class Util {

    public static void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static void openBrowser(Context context, String linkUrl) {
        try {
            Uri uri = Uri.parse(linkUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.no_browser_tips), Toast.LENGTH_LONG).show();
        }
    }

    public static void openCustomerService(Context context) {
        openBrowser(context, "https://direct.lc.chat/12218097/");
    }

    public static void openHtmlWithWebview(Context context, String html){
        WebPurchaseActivity.Companion.intentEnter(context, html);
    }

    public static void openHtmlWithBrowser(Context context, String html) {
        try {
            File mFolder = new File(context.getExternalFilesDir(null) + "/html");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }
            File file = new File(mFolder.getPath()+"/bak.html");
            if(file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(html.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.fromFile(file);
//            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            LogUtil.d("openBrowser uri "+uri.getPath());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "text/html");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.no_browser_tips), Toast.LENGTH_LONG).show();
        }
    }
}
