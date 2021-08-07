package com.re.ng.uu.comic.view.widget;

import android.animation.Animator;
import android.view.View;

/**
 * 管理TopLayout、BottomLayout、RightLayout、QualitySettingLayout的显示和隐藏
 */
public class PanelManage {
    private View mQualitySettingLayout;//画质设置栏
    private View mTopLayout;
    private View mBottomLayout;
    private View mRightLayout;
    private int mRightLayoutWidth;
    private int mTopLayoutHeight;//px
    private int mBottomLayoutHeight;
    private boolean isTopShowed;
    private boolean isBottomShowed;
    private boolean isRightShowed;
    private boolean isBottomAnimating;
    private boolean isTopAnimating;
    private boolean isRightAnimating;

    public PanelManage(View mTopLayout,
                View mBottomLayout,
                View mRightLayout,
                View mQualitySettingLayout) {
        this.mTopLayout = mTopLayout;
        this.mBottomLayout = mBottomLayout;
        this.mRightLayout = mRightLayout;
        this.mQualitySettingLayout = mQualitySettingLayout;
        //是否显示
        isTopShowed = true;
        isRightShowed = true;
        isBottomShowed = true;
        //属性动画执行中
        isTopAnimating = false;
        isRightAnimating = false;
        isBottomAnimating = false;

    }

    public void setQualityLayoutVisibility(boolean visible){
        mQualitySettingLayout.setVisibility(visible?View.VISIBLE:View.GONE);
    }

    public void showTopAndBottom(){
        if (isBottomAnimating || isTopAnimating || isRightShowed){
            return;
        }
        showTop();
        showBottom();
    }

    public  void hideTopAndBottom(){
        if (isBottomAnimating || isTopAnimating){
            return;
        }
        hideBottom();
        hideTop();
    }

    public void showRight() {
        if (isRightAnimating){
            return;
        }
        if (!isRightShowed){
            mRightLayout.animate()
                    .translationXBy(-mRightLayoutWidth)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRightAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isRightAnimating = false;
                            isRightShowed = true;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    public void hideRight() {
        if (isRightAnimating){
            return;
        }
        if (isRightShowed){
            mRightLayout.animate()
                    .translationXBy(mRightLayoutWidth)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRightAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isRightShowed = false;
                            isRightAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    private void showBottom() {
        if (!isBottomShowed){
            mBottomLayout
                    .animate()
                    .translationYBy(-mBottomLayoutHeight)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isBottomAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isBottomShowed = true;
                            isBottomAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    private void hideBottom() {
        if (isBottomShowed){
            mBottomLayout
                    .animate()
                    .translationYBy(mBottomLayoutHeight)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isBottomAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isBottomShowed = false;
                            isBottomAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    private void showTop() {
        if (!isTopShowed){
            mTopLayout.animate()
                    .translationYBy(mTopLayoutHeight)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isTopAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isTopShowed = true;
                            isTopAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    private void hideTop(){
        if (isTopShowed){
            mTopLayout.animate()
                    .translationYBy(-mTopLayoutHeight)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isTopAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isTopShowed = false;
                            isTopAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    public void setRightLayoutWidth(int mRightLayoutWidth) {
        this.mRightLayoutWidth = mRightLayoutWidth;
    }

    public void setTopLayoutHeight(int mTopLayoutHeight) {
        this.mTopLayoutHeight = mTopLayoutHeight;
    }

    public void setBottomLayoutHeight(int mBottomLayoutHeight) {
        this.mBottomLayoutHeight = mBottomLayoutHeight;
    }
}
