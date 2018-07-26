package com.cyl.musiclake;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.data.PlaylistLoader;
import com.cyl.musiclake.data.download.TasksManager;
import com.cyl.musiclake.di.component.ApplicationComponent;
import com.cyl.musiclake.di.component.DaggerApplicationComponent;
import com.cyl.musiclake.di.module.ApplicationModule;
import com.cyl.musiclake.player.MusicPlayerService;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.UpdateUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tauth.Tencent;

import org.litepal.LitePal;

import java.net.Proxy;

public class MusicApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static MusicApp sInstance;
    private PlayManager.ServiceToken mToken;
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    private Tencent mTencent;

    private ApplicationComponent mApplicationComponent;
    public Point screenSize = new Point();

    public static synchronized MusicApp getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mContext = this;
        initApplicationComponent();
        LitePal.initialize(this);
        UpdateUtils.init(this);
        SPUtils.init(this);
        mTencent = Tencent.createInstance(Constants.APP_ID, this);
        initBugly();
        initDB();
        registerListener();
        initFileDownload();
        BaseApiImpl.INSTANCE.initWebView(this);
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            manager.getDefaultDisplay().getSize(screenSize);
        }
    }

    /**
     * 初始化文件下载
     */
    private void initFileDownload() {
        FileDownloadLog.NEED_LOG = true;
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .commit();
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Bugly.init(getApplicationContext(), Constants.BUG_APP_ID, true);
        Beta.checkUpgrade(false, false);
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
        PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_QUEUE_ID, "播放队列");
        PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_HISTORY_ID, "播放历史");
        PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_LOVE_ID, "我的收藏");
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

    public static int count = 0;
    public static int Activitycount = 0;

    /**
     * 注册监听
     */
    private void registerListener() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Activitycount++;
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
                Activitycount--;
                if (Activitycount == 0) {
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>APP 关闭");
//                    stopService(new Intent(activity, MusicPlayerService.class));
                }
            }
        });
    }



}
