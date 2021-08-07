package com.re.ng.uu.comic.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.re.ng.uu.comic.APP;
import com.re.ng.uu.comic.BuildConfig;

public class LogUtil {
    public static String tagPrefix = "";
    public static boolean showV = false;
    public static boolean showD = BuildConfig.DEBUG;
    public static boolean showI = false;
    public static boolean showW = false;
    public static boolean showE = false;
    public static boolean showWTF = false;

    /**
     * 得到tag（所在类.方法（L:行））     * @return
     */
    private static String generateTag() {
//        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
//        String callerClazzName = stackTraceElement.getClassName();
//        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
//        String tag = "%s.%s(L:%d)";
//        tag = String.format(tag, new Object[]{callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
//        //给tag设置前缀
//        tag = TextUtils.isEmpty(tagPrefix) ? tag : tagPrefix + ":" + tag;
//        return tag;
        return APP.TAG;
    }

    public static void v(String msg) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg, tr);
        }
    }

    public static void d(String msg) {
        if (showD) {
            String tag = APP.TAG;
            Log.e(tag, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (showD) {
            Log.e(tag, msg);
        }
    }

    public static void i(String msg) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (showD) {
            String tag = generateTag();
            Log.e(tag, msg, tr);
        }
    }

    public static void wtf(String msg) {
        if (showWTF) {
            String tag = generateTag();
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String msg, Throwable tr) {
        if (showWTF) {
            String tag = generateTag();
            Log.wtf(tag, msg, tr);
        }
    }

    public  static void printPrettyJson(String tag, String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = new JsonParser().parse(json);
        LogUtil.d(tag, gson.toJson(jsonElement));
    }
}
