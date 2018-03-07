package com.cyl.musiclake.common;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

/**
 * Author   : D22434
 * version  : 2018/1/22
 * function : 图片切换动画
 */

public class TransitionAnimationUtils {

    /**
     * 图片渐变切换动画
     *
     * @param imageView
     * @param bitmapDrawable
     */
    public static void startChangeAnimation(ImageView imageView, Drawable bitmapDrawable) {
        Drawable oldDrawable = imageView.getDrawable();
        Drawable oldBitmapDrawable = null;
        if (oldDrawable == null) {
            oldBitmapDrawable = new ColorDrawable(Color.TRANSPARENT);
        } else if (oldDrawable instanceof TransitionDrawable) {
            oldBitmapDrawable = ((TransitionDrawable) oldDrawable).getDrawable(1);
        } else {
            oldBitmapDrawable = oldDrawable;
        }
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                oldBitmapDrawable,
                bitmapDrawable
        });
        imageView.setImageDrawable(td);
        td.startTransition(1000);
    }


    /**
     * 颜色渐变动画
     */
    public static void startColorAnimation(View mView, int newColor) {
        int olderColor = ((ColorDrawable) mView.getBackground()).getColor();
        ObjectAnimator objectAnimator;
        objectAnimator = ObjectAnimator.ofInt(mView,
                "backgroundColor", olderColor, newColor)
                .setDuration(800);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.start();
    }

    /**
     * 旋转动画
     */
    public static void startCoverChangeAnimation(ImageView mView, Bitmap bitmap) {
        float startY = mView.getBottom();
        float endY = mView.getHeight();
        ObjectAnimator objectAnimator;
        objectAnimator = ObjectAnimator.ofFloat(mView,
                "y", startY, endY)
                .setDuration(1000);
        objectAnimator.setInterpolator(new AccelerateInterpolator());


        objectAnimator.start();
    }
}
