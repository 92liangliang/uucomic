package com.re.ng.uu.comic.util;

import android.content.Context;
import android.content.Intent;

import com.re.ng.uu.comic.APP;
import com.re.ng.uu.comic.base.BaseActivity;
import com.re.ng.uu.comic.view.activity.BindPhoneActivity;
import com.re.ng.uu.comic.view.activity.BookDetailActivity;
import com.re.ng.uu.comic.view.activity.ComicActivity;
import com.re.ng.uu.comic.view.activity.ExChangerVipActivity;
import com.re.ng.uu.comic.view.activity.ForgetPwdActivity;
import com.re.ng.uu.comic.view.activity.LoginActivity;
import com.re.ng.uu.comic.view.activity.MainActivity;
import com.re.ng.uu.comic.view.activity.MoreActivity;
import com.re.ng.uu.comic.view.activity.OpenVipActivity;
import com.re.ng.uu.comic.view.activity.OrderHistoryActivity;
import com.re.ng.uu.comic.view.activity.RankingActivity;
import com.re.ng.uu.comic.view.activity.RechargeActivity;
import com.re.ng.uu.comic.view.activity.RegisterActivity;
import com.re.ng.uu.comic.view.activity.SearchActivity;
import com.re.ng.uu.comic.view.activity.SearchResultActivity;
import com.re.ng.uu.comic.view.activity.ShareActivity;
import com.re.ng.uu.comic.view.activity.TagBooksActivity;
import com.re.ng.uu.comic.view.activity.TimeActivity;
import com.re.ng.uu.comic.view.activity.WalletActivity;

import java.util.ArrayList;

/**
 * 对startActivity的封装
 */
public class StartActUtil {

    public static void toBookBillDetail(Context context, String bookBillId, ArrayList<String> coverBookIdList) {
//        Intent intent = new Intent(context,BookBillDetailActivity.class);
//        intent.putExtra("bookBillId",bookBillId);
//        intent.putStringArrayListExtra("coverBookIdList",coverBookIdList);
//        context.startActivity(intent);
    }

    public static void toBookDetail(Context context, String bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("bookId", bookId);
        context.startActivity(intent);
    }

    public static void toSearchAct(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    public static void toMainAct(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void toComicAct(Context context, String chapterId, String title) {
        Intent intent = new Intent(context, ComicActivity.class);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public static void toBookBillAct(Context context, int type) {
//        Intent intent = new Intent(context,BookBillActivity.class);
//        intent.putExtra("type",type);
//        context.startActivity(intent);
    }

    public static void toRankingAct(Context context) {
        Intent intent = new Intent(context, RankingActivity.class);
        context.startActivity(intent);
    }

    public static void toTimeAct(Context context) {
        Intent intent = new Intent(context, TimeActivity.class);
        context.startActivity(intent);
    }

    public static void toSearchResultAct(Context context, String title, String key, String type) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("key", key);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void toMoreAct(Context context, String title, String type) {
        Intent intent = new Intent(context, MoreActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void toSearchResultAct(Context context, String title, String type) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void toTagBooksAct(Context context, String title, String tag) {
        Intent intent = new Intent(context, TagBooksActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("tag", tag);
        context.startActivity(intent);
    }

    public static void toRegister(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void toLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void toResetPwd(Context context) {
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(intent);
    }

    public static void toLogin(Context context, String username, String password) {
        Intent intent = new Intent(context, TagBooksActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        context.startActivity(intent);
    }

    public static void toRecharge(Context context) {
        if (APP.getInstance().isUserLogin()) {
            Intent intent = new Intent(context, RechargeActivity.class);
            context.startActivity(intent);
        } else {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showToast("请先登录");
            }
            toLogin(context);
        }
    }

    public static void toShare(Context context) {
        if (APP.getInstance().isUserLogin()) {
            Intent intent = new Intent(context, ShareActivity.class);
            context.startActivity(intent);
        } else {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showToast("请先登录");
            }
            toLogin(context);
        }
    }

    public static void toBindPhone(Context context) {
        if (APP.getInstance().isUserLogin()) {
            Intent intent = new Intent(context, BindPhoneActivity.class);
            context.startActivity(intent);
        } else {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showToast("请先登录");
            }
            toLogin(context);
        }
    }

    public static void toWallet(Context context) {
        if (APP.getInstance().isUserLogin()) {
            Intent intent = new Intent(context, WalletActivity.class);
            context.startActivity(intent);
        } else {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showToast("请先登录");
            }
            toLogin(context);
        }
    }   public static void toExchange(Context context) {
        if (APP.getInstance().isUserLogin()) {
            Intent intent = new Intent(context, ExChangerVipActivity.class);
            context.startActivity(intent);
        } else {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showToast("请先登录");
            }
            toLogin(context);
        }
    }

    public static void toGetVIP(Context context) {
        if (APP.getInstance().isUserLogin()) {
            Intent intent = new Intent(context, OpenVipActivity.class);
            context.startActivity(intent);
        } else {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showToast("请先登录");
            }
            toLogin(context);
        }
    }

    public static void toOrderHistory(Context context, String type, String title) {
        if (APP.getInstance().isUserLogin()) {
            Intent intent = new Intent(context, OrderHistoryActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("title", title);
            context.startActivity(intent);
        } else {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showToast("请先登录");
            }
            toLogin(context);
        }
    }
}
