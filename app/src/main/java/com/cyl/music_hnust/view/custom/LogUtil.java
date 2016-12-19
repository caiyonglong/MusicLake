package com.cyl.music_hnust.view.custom;

import android.util.Log;

/**
 */
public class LogUtil {
    /**
     * 默认的tag
     */
    public static final String defaultTag = "dota";
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    /**
     * 下面这个变量定义日志级别
     */
    public static final int LEVEL = VERBOSE;


    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void v(String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(defaultTag, msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(defaultTag, msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= INFO) {
            Log.i(defaultTag, msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= WARN) {
            Log.w(defaultTag, msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= ERROR) {
            Log.e(defaultTag, msg);
        }
    }

    public static void m(String msg){
        String methodName = new Exception().getStackTrace()[1].getMethodName();
        Log.v(defaultTag,methodName+":    "+msg);
    }
    public static void m(int msg){
        String methodName = new Exception().getStackTrace()[1].getMethodName();
        Log.v(defaultTag,methodName+":    "+msg+"");
    }

    public static void m(){
        String methodName = new Exception().getStackTrace()[1].getMethodName();
        Log.v(defaultTag,methodName);
    }

    public static void v(int msg) {
        LogUtil.v(msg + "");
    }

    public static void d(int msg) {
        LogUtil.d(msg + "");
    }

    public static void i(int msg) {
        LogUtil.i(msg + "");
    }

    public static void w(int msg) {
        LogUtil.w(msg + "");
    }

    public static void e(int msg) {
        LogUtil.e(msg + "");
    }
}
