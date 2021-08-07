package com.re.ng.uu.comic.util;

import android.widget.Toast;

import com.re.ng.uu.comic.APP;

/**
 * Date    : 2020-10-30
 */
public class ToastUtil {

    public static void testSmali(){
        boolean result = getResult();
        if(result){
            getResult();
        }
    }

    public static boolean getResult(){
        return false;
    }

    public static void show(String content){
        Toast.makeText(APP.getInstance(), content, Toast.LENGTH_LONG).show();
    }
}
