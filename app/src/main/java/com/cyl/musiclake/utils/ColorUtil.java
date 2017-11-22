package com.cyl.musiclake.utils;

import android.graphics.Color;

/**
 * Created by D22434 on 2017/9/26.
 */

public class ColorUtil {

    public static int getBlackWhiteColor(int color) { //根据颜色的亮度转换为黑白色
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness >= 0.5) {
            return Color.WHITE;
        } else return Color.BLACK;
    }
}
