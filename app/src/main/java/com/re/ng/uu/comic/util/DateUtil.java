package com.re.ng.uu.comic.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date    : 2019-12-17
 */
public class DateUtil {

    private static SimpleDateFormat formater1 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat formater2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static SimpleDateFormat formater3 = new SimpleDateFormat("MM-dd");

    public static Date getDate(int time) {
        return new Date(time * 1000L);
    }

    public static String format(Date date) {
        return formater1.format(date);
    }

    public static String format(int time) {
        return format(getDate(time));
    }

    public static String format(Date date, int type) {
        switch (type) {
            case 2:
                return format2(date);
            case 3:
                return formater3.format(date);
        }
        return formater1.format(date);
    }

    public static String format(int time, int type) {
        return format(getDate(time), type);
    }

    private static String format2(Date date) {
        return formater2.format(date);
    }

    public static int diffDays(Date date1,Date date2) {
        int second = (int) ((date2.getTime() - date1.getTime()) / 1000L);
        int base = 0;
        if(second % 3600*24 > 0){
            base = 1;
        }
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days+base;
    }

}
