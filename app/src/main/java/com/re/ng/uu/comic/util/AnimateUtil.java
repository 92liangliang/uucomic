package com.re.ng.uu.comic.util;

import android.widget.ImageView;

import com.re.ng.uu.comic.R;

public class AnimateUtil {
    /**
     * 点击喜欢时的动画
     * @param view
     * @param isLike 目前的状态（没点击前）
     */
    public static void likeAnimate(final ImageView view, boolean isLike, boolean isFullView) {
        if (isLike){
            //取消喜欢
            if(isFullView) {
                view.setImageResource(R.drawable.svg_white_normal_like);
            }else {
                view.setImageResource(R.drawable.svg_red_like_normal);
            }
        }else {
            //喜欢
            view.setImageResource(R.drawable.svg_red_like_pressed);
        }
        view.animate().scaleX(1.5f).scaleY(1.5f).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.animate().scaleX(1f).scaleY(1f);
            }
        });
    }
}
