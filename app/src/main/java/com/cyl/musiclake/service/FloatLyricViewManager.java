package com.cyl.musiclake.service;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cyl.musiclake.R;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.view.lyric.FloatLyricView;
import com.cyl.musiclake.view.lyric.LyricInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FloatLyricViewManager {
    private static FloatLyricView mFloatLyricView;
    private static WindowManager.LayoutParams mFloatLyricViewParams;
    private static WindowManager mWindowManager;
    private Handler handler = new Handler();
    private static LyricInfo mLyricInfo;
    private static boolean isFirstSettings = false;

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private static Timer timer;
    private Context mContext;

    public FloatLyricViewManager(Context context) {
        mContext = context;
    }

    public static void stopFloatLyric() {
        // Service被终止的同时也停止定时器继续运行
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void startFloatLyric() {
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 1);
        }
    }

    public static void setLyric(LyricInfo mLyricInfo) {
        isFirstSettings = true;
        FloatLyricViewManager.mLyricInfo = mLyricInfo;
        LogUtil.e("lyric = ", mLyricInfo.getSong_album() + "---" + mLyricInfo.getSong_lines().size());
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            // 当前界面不是本应用界面，且没有悬浮窗显示，则创建悬浮窗。
            if (!isHome() && !isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        createFloatLyricView(mContext);
                    }
                });
            } else if (isHome() && isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        removeFloatLyricView(mContext);
                    }
                });
            } else if (isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateLyric(mContext);
                    }
                });
            }
        }

    }

    /**
     * 判断当前界面是否是应用界面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return rti.get(0).topActivity.getPackageName().equals(mContext.getPackageName());
    }


    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createFloatLyricView(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mFloatLyricView == null) {
            mFloatLyricView = new FloatLyricView(context);
            isFirstSettings = true;
            if (mFloatLyricViewParams == null) {
                mFloatLyricViewParams = new WindowManager.LayoutParams();
                mFloatLyricViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mFloatLyricViewParams.format = PixelFormat.RGBA_8888;
                mFloatLyricViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mFloatLyricViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                mFloatLyricViewParams.width = FloatLyricView.viewWidth;
                mFloatLyricViewParams.height = FloatLyricView.viewHeight;
                mFloatLyricViewParams.x = screenWidth;
                mFloatLyricViewParams.y = screenHeight / 2;
            }
            mFloatLyricView.setParams(mFloatLyricViewParams);
            windowManager.addView(mFloatLyricView, mFloatLyricViewParams);
        } else if (isFirstSettings && mFloatLyricView != null) {
            mFloatLyricView.lyricTextView.setLyricInfo(mLyricInfo);
            isFirstSettings = false;
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeFloatLyricView(Context context) {
        if (mFloatLyricView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mFloatLyricView);
            mFloatLyricView = null;
        }
    }

    /**
     * 隐藏窗体view
     *
     * @param context 必须为应用程序的Context.
     */
    public static void hiddenFloatBackground(Context context) {
        if (mFloatLyricView != null) {
            View view = mFloatLyricView.findViewById(R.id.small_bg);
            RelativeLayout rl = (RelativeLayout) mFloatLyricView.findViewById(R.id.rl_layout);
            LinearLayout ll = (LinearLayout) mFloatLyricView.findViewById(R.id.ll_layout);
            if (rl.getVisibility() == View.INVISIBLE) {
                rl.setVisibility(View.VISIBLE);
                ll.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            } else {
                rl.setVisibility(View.INVISIBLE);
                ll.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public static void updateLyric(Context context) {
        if (mFloatLyricView != null) {
            mFloatLyricView.lyricTextView.setCurrentTimeMillis(PlayManager.getCurrentPosition());
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return mFloatLyricView != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
