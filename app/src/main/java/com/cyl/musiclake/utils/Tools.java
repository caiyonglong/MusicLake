package com.cyl.musiclake.utils;

import android.content.Context;

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/19 .
 */
public class Tools {

    public static String getString(Context context, int resourceId) {
        if (context == null) return "";
        return context.getResources().getString(resourceId);
    }
}
