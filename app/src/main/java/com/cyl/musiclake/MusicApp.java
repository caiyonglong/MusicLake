package com.cyl.musiclake;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

import com.cyl.musiclake.api.music.MusicApi;
import com.cyl.musiclake.api.net.ApiManager;
import com.cyl.musiclake.api.net.RequestCallBack;
import com.cyl.musiclake.bean.HotSearchBean;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.MPBroadcastReceiver;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.data.PlaylistLoader;
import com.cyl.musiclake.di.component.ApplicationComponent;
import com.cyl.musiclake.di.component.DaggerApplicationComponent;
import com.cyl.musiclake.di.module.ApplicationModule;
import com.cyl.musiclake.socket.SocketManager;
import com.cyl.musiclake.ui.download.TasksManager;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.NetworkUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.player.MusicPlayerManager;
import com.music.lake.musiclib.player.MusicPlayerService;
import com.tencent.bugly.Bugly;
import com.tencent.tauth.Tencent;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * tinker热更新需要
 */
public class MusicApp extends MultiDexApplication {
    @SuppressLint("StaticFieldLeak")
    private static MusicApp sInstance;

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    //QQ第三方登录
    public static Tencent mTencent;

    public static Gson GSON = new Gson();

    public static List<HotSearchBean> hotSearchList;

    public static synchronized MusicApp getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static List<Activity> activities = new ArrayList<>();

    public static int count = 0;
    public static int ActivityCount = 0;
    private ApplicationComponent mApplicationComponent;

    private MPBroadcastReceiver mpBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mContext = this;
        registerListener();
        initApplicationComponent();
        initSDK();
        initFileDownload();
    }

    private void initSDK() {
        MusicPlayerManager.getInstance().init(this);
        MusicPlayerManager.getInstance().setMusicRequestListener((musicInfo, call) -> {
            if (musicInfo.getUri() == null || !Objects.equals(musicInfo.getType(), Constants.LOCAL) || musicInfo.getUri().equals("") || musicInfo.getUri().equals("null")) {
                if (!NetworkUtils.isNetworkAvailable(this)) {
                    ToastUtils.show("网络不可用，请检查网络连接");
                } else {
                    ApiManager.request(MusicApi.INSTANCE.getMusicInfo(musicInfo), new RequestCallBack<BaseMusicInfo>() {
                        @Override
                        public void success(BaseMusicInfo result) {
                            LogUtil.e("MusicApp", "-----" + result.toString());
                            if (result.getUri() != null) {
                                call.onMusicValid(result.getUri());
                            }
                        }

                        @Override
                        public void error(String msg) {
                            LogUtil.e("MusicApp", "播放异常-----" + msg);
                        }
                    });
                }
            } else {
                call.onActionDirect();
            }
        });
        LitePal.initialize(this);
        initBugly();
        initDB();
        //内存检测
//        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this);
//        }

        mpBroadcastReceiver = new MPBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(MusicPlayerService.ACTION_SERVICE);
        intentFilter.addAction(MusicPlayerService.META_CHANGED);
        intentFilter.addAction(MusicPlayerService.PLAY_QUEUE_CHANGE);
        //注册广播
        registerReceiver(mpBroadcastReceiver, intentFilter);
    }

    /**
     * 初始化文件下载
     */
    private void initFileDownload() {
        FileDownloadLog.NEED_LOG = BuildConfig.DEBUG;
        FileDownloader.setupOnApplicationOnCreate(this);
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Bugly.init(getApplicationContext(), Constants.BUG_APP_ID, BuildConfig.DEBUG);
    }


    /**
     * 初始化ApplicationComponent
     */
    private void initApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initDB() {
        //线程初始化数据库，优化启动速度
        new Thread(() -> {
            NavigationHelper.INSTANCE.scanFileAsync(this);
            PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_QUEUE_ID, getString(R.string.playlist_queue));
            PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_HISTORY_ID, getString(R.string.item_history));
            PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_LOVE_ID, getString(R.string.item_favorite));
        }).run();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogUtil.d("onTerminate");
        super.onTerminate();
        //结束下载任务
        TasksManager.INSTANCE.onDestroy();
        FileDownloader.getImpl().pauseAll();
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activities.add(activity);
                ActivityCount++;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) { //后台切换到前台
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>App切到前台");
                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>App切到后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityCount--;
                activities.remove(activity);
                if (ActivityCount == 0) {
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>APP 关闭");
                    SocketManager.INSTANCE.toggleSocket(false);
                }
            }
        });
    }

}
