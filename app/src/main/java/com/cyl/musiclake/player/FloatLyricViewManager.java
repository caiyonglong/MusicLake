package com.cyl.musiclake.player;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import com.cyl.musiclake.utils.LogUtil;
import android.view.Gravity;
import android.view.WindowManager;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.view.lyric.FloatLyricView;
import com.cyl.musiclake.view.lyric.LyricInfo;
import com.cyl.musiclake.view.lyric.LyricParseUtils;

import java.io.File;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FloatLyricViewManager {
    private static final String TAG = "FloatLyricViewManager";
    private static FloatLyricView mFloatLyricView;
    private static WindowManager.LayoutParams mFloatLyricViewParams;
    private static WindowManager mWindowManager;
    private Handler handler = new Handler();
    private LyricInfo mLyricInfo;
    private boolean isFirstSettingLyric; //第一次设置歌词

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

    /**
     * 设置歌词
     *
     * @param lyricInfo
     */
    public void setLyric(LyricInfo lyricInfo) {
        mLyricInfo = lyricInfo;
        isFirstSettingLyric = true;
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            // 当前界面不是本应用界面，且没有悬浮窗显示，则创建悬浮窗。
            if (!isHome() && !isWindowShowing()) {
                handler.post(() -> createFloatLyricView(mContext));
            } else if (isHome() && isWindowShowing()) {
                handler.post(() -> removeFloatLyricView(mContext));
            } else if (isWindowShowing()) {
                handler.post(() -> updateLyric(mContext));
            }
        }

    }

    /**
     * 判断当前界面是否是应用界面
     */
    private boolean isHome() {
        try {
            String topPackageName = getProcess();
            if (topPackageName != null) {
                return topPackageName.equals(mContext.getPackageName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public void createFloatLyricView(Context context) {
        WindowManager windowManager = getWindowManager(context);
        Point size = new Point();
        //获取屏幕宽高
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        if (mFloatLyricView == null) {
            mFloatLyricView = new FloatLyricView(context);
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
//            loadLyric();
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
            if (isFirstSettingLyric) {
                mFloatLyricView.mTitle.setText(PlayManager.getSongName());
                mFloatLyricView.mLyricText.setLyricInfo(mLyricInfo);
                isFirstSettingLyric = false;
            }
            mFloatLyricView.mLyricText.setCurrentTimeMillis(PlayManager.getCurrentPosition());
            mFloatLyricView.mLyricText.setDurationMillis(PlayManager.getDuration());
        }
    }

    public void loadLyric() {
        Music music = PlayManager.getPlayingMusic();
        if (music == null) {
            return;
        }
        //先判断本地是否存在歌词
        String lrcPath = FileUtils.getLrcDir() + music.getTitle() + "-" + music.getArtist() + ".lrc";
        if (FileUtils.exists(lrcPath)) {
            LogUtil.e("lrcPath");
            setLyric(LyricParseUtils.setLyricResource(new File(lrcPath)));
        } else {
            Observable<String> observable = MusicApi.INSTANCE.getLyricInfo(music);
            if (observable == null) {
                setLyric(null);
            } else {
                observable.subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String lyricInfo) {
                        LogUtil.e(TAG, lyricInfo);
                        setLyric(LyricParseUtils.setLyricResource(lyricInfo));
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLyric(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
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

    private String getProcess() throws Exception {
        if (Build.VERSION.SDK_INT >= 21) {
            return getProcessNew();
        } else {
            return getProcessOld();
        }
    }

    private String topPackageName = null;

    //API 21 and above
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String getProcessNew() throws Exception {
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) MusicApp.getAppContext().getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 5, time);
        // Sort the stats by the last time used

        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        return topPackageName;
    }

    //API below 21
    @SuppressWarnings("deprecation")
    private String getProcessOld() throws Exception {
        String topPackageName = null;
        ActivityManager activity = (ActivityManager) MusicApp.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTask = activity.getRunningTasks(1);
        if (runningTask != null) {
            ActivityManager.RunningTaskInfo taskTop = runningTask.get(0);
            ComponentName componentTop = taskTop.topActivity;
            topPackageName = componentTop.getPackageName();
        }
        return topPackageName;
    }
}
