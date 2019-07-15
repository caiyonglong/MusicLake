package com.cyl.musiclake.utils;

import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

/**
 * Created by D22434 on 2017/9/26.
 */

public class ColorUtil {

    public static int getStatusBarColor(int color) {
        float[] arrayOfFloat = new float[3];
        Color.colorToHSV(color, arrayOfFloat);
        arrayOfFloat[2] *= 0.9F;
        return Color.HSVToColor(arrayOfFloat);
    }

    public static int getBlackWhiteColor(int color) { //根据颜色的亮度转换为黑白色
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness >= 0.5) {
            return Color.WHITE;
        } else return Color.BLACK;
    }

    public static int getOpaqueColor(@ColorInt int paramInt) {
        return 0xFF000000 | paramInt;
    }

    public static @Nullable
    Palette.Swatch getMostPopulousSwatch(Palette palette) {
        Palette.Swatch mostPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
                    mostPopulous = swatch;
                }
            }
        }
        return mostPopulous;
    }
}
