package com.cyl.music_hnust.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.text.DecimalFormat;

/**
 * 作者：yonglong on 2016/8/12 16:45
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SystemUtils {


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

    /**
     * 初始化透明状态栏
     */
    public static void setSystemBarTransparent(AppCompatActivity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // LOLLIPOP解决方案
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int top = StatusBarCompat.getStatusBarHeight(this);
//            main_content.setPadding(0, top, 0, 0);
//        }

    }
    /**
     * 格式化时间
     *
     * @param time 时间值
     * @return 时间
     */
    public static String formatTime(long time) {
        // TODO Auto-generated method stub
        if (time == 0) {
            return "00:00";
        }
        time = time / 1000;
        int m = (int) (time / 60 % 60);
        int s = (int) (time % 60);
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }
    /**
     * 格式化文件大小
     *
     * @param size 文件大小值
     * @return 文件大小
     */
    public static String formatSize(long size) {
        // TODO Auto-generated method stub
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSize = "0B";
        if (size < 1024) {
            fileSize = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSize = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSize = df.format((double) size / 1048576) + "MB";
        } else {
            fileSize = df.format((double) size / 1073741824) + "GB";
        }
        return fileSize;
    }

    /**
     * 判断Service是否在运行
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
