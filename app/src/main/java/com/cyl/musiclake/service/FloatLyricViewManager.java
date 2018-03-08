package com.cyl.musiclake.service;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.cyl.musiclake.api.qq.QQApiServiceImpl;
import com.cyl.musiclake.api.xiami.XiamiServiceImpl;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.view.lyric.FloatLyricView;
import com.cyl.musiclake.view.lyric.LyricInfo;
import com.cyl.musiclake.view.lyric.LyricPraseUtils;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FloatLyricViewManager {
    private static final String TAG = "FloatLyricViewManager";
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

    public void stopFloatLyric() {
        // Service被终止的同时也停止定时器继续运行
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void updatePlayStatus(boolean isPlaying) {
        if (mFloatLyricView != null)
            mFloatLyricView.setPlayStatus(isPlaying);
    }

    public void startFloatLyric() {
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 1);
        }
    }

    public void setLyric(LyricInfo lyricInfo) {
        isFirstSettings = true;
        mLyricInfo = lyricInfo;
        if (lyricInfo != null)
            LogUtil.e("lyric = ", mLyricInfo.getSong_album() + "---" + mLyricInfo.getSong_lines().size());
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            LogUtil.e(TAG, "is showing = " + isHome());
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
        LogUtil.e(TAG, "top : " + mContext.getPackageName() + "----" + rti.get(0).topActivity.getPackageName());
        return rti.get(0).topActivity.getPackageName().equals(mContext.getPackageName());
    }


    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public void createFloatLyricView(Context context) {
        WindowManager windowManager = getWindowManager(context);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);

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
                mFloatLyricViewParams.width = mFloatLyricView.viewWidth;
                mFloatLyricViewParams.height = mFloatLyricView.viewHeight;
                mFloatLyricViewParams.x = screenWidth;
                mFloatLyricViewParams.y = screenHeight / 2;
            }
            mFloatLyricView.setParams(mFloatLyricViewParams);
            windowManager.addView(mFloatLyricView, mFloatLyricViewParams);
        } else if (isFirstSettings) {
            mFloatLyricView.mLyricText.setLyricInfo(mLyricInfo);
            isFirstSettings = false;
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public void removeFloatLyricView(Context context) {
        if (mFloatLyricView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mFloatLyricView);
            mFloatLyricView = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public void updateLyric(Context context) {
        if (mFloatLyricView != null) {
            mFloatLyricView.mLyricText.setCurrentTimeMillis(PlayManager.getCurrentPosition());
        }
    }

    public void loadLyric() {
        Music music = PlayManager.getPlayingMusic();
        if (music == null) {
            return;
        }
        //先判断本地是否存在歌词
        String lrcPath = FileUtils.getLrcDir() + music.getTitle() + "-" + music.getArtist() + ".lrc";
        LogUtil.e("lrcPath : " + lrcPath);
        if (FileUtils.exists(lrcPath)) {
            LogUtil.e("isFile");
            setLyric(LyricPraseUtils.setLyricResource(new File(lrcPath)));
        } else if (music.getType() == Music.Type.QQ) {
            QQApiServiceImpl.getQQLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String lyricInfo) {
                            Log.e(TAG, lyricInfo);
                            if (lyricInfo != null) {
                                LyricInfo tt = LyricPraseUtils.setLyricResource(lyricInfo);
                                tt.setDuration(PlayManager.getDuration());
                                setLyric(tt);
                            } else {
                                setLyric(null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            setLyric(null);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (music.getType() == Music.Type.XIAMI || music.getType() == Music.Type.BAIDU) {
            Log.e(TAG, music.getLrcPath());
            XiamiServiceImpl.getXimaiLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String lyricInfo) {
                            Log.e(TAG, lyricInfo);
                            if (lyricInfo != null) {
                                LyricInfo tt = LyricPraseUtils.setLyricResource(lyricInfo);
                                tt.setDuration(PlayManager.getDuration());
                                setLyric(tt);
                            } else {
                                setLyric(null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            setLyric(null);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            LogUtil.e("isFile");
            setLyric(null);
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
