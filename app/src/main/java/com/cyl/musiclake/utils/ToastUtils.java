package com.cyl.musiclake.utils;

import android.content.Context;
import android.widget.Toast;

import com.cyl.musiclake.MusicApp;


/**
 * 提示工具类
 */
public class ToastUtils {

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    public static void show(String info) {
        Toast.makeText(MusicApp.getAppContext(), info, Toast.LENGTH_SHORT).show();
    }


    public static void show(Context context, int info) {
        Toast.makeText(context, info + "", Toast.LENGTH_SHORT).show();
    }
}
