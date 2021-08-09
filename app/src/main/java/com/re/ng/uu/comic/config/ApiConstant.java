package com.re.ng.uu.comic.config;

import android.text.TextUtils;

/**
 * Date    : 2020-10-30
 */
public class ApiConstant {

    public static String UU_API_URL = "https://app.mmm68.com/";
    public static String UU_API_URL2 = "https://m.qqr35.com/";
    public static String UU_BASE_URL = "https://qqr35.com/";
    public static String UU_IMG_URL = "http://krv76.com/";

    public static String getFormatUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        if (url.contains("http")) {
            return url;
        }
        return UU_BASE_URL + url;
    }
}
