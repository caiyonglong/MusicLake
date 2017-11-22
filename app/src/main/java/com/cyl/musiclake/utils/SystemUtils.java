package com.cyl.musiclake.utils;

import android.os.Build;

/**
 * android系统工具类
 * 主要功能判断android系统的版本、判断后台Service是否运行
 * 作者：yonglong on 2016/8/12 16:45
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SystemUtils {
    //判断是否是android 6.0
    public static boolean isJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
    //判断是否是android 6.0
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    //判断是否是android 5.0
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    //判断是否是android 4.0
    public static boolean isKITKAT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
