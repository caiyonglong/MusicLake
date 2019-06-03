package com.cyl.musiclake;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musiclake.bean.HotSearchBean;
import com.cyl.musiclake.bean.data.PlaylistLoader;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.di.component.ApplicationComponent;
import com.cyl.musiclake.di.component.DaggerApplicationComponent;
import com.cyl.musiclake.di.module.ApplicationModule;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.socket.SocketManager;
import com.cyl.musiclake.ui.download.TasksManager;
import com.cyl.musiclake.ui.theme.ThemeStore;
import com.cyl.musiclake.utils.LogUtil;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tauth.Tencent;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * tinker热更新需要
 */
public class MusicApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static MusicApp sInstance;
    private PlayManager.ServiceToken mToken;
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    //QQ第三方登录
    public static Tencent mTencent;

    public static Gson GSON;

    //socket
    public static SocketManager socketManager;
    /**
     * socket是否打开
     */
    public static Boolean isOpenSocket = true;

    public static Boolean isShowingFloatView = false;

    public static List<HotSearchBean> hotSearchList;

    private ApplicationComponent mApplicationComponent;
    public static Point screenSize = new Point();

    public static synchronized MusicApp getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static List<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
        sInstance = this;
        mContext = this;
        initApplicationComponent();
        LitePal.initialize(this);
        mTencent = Tencent.createInstance(Constants.APP_ID, this);
        initBugly();
        initLogin();
        initDB();
        GSON = new Gson();
        registerListener();
        initFileDownload();
        BaseApiImpl.INSTANCE.initWebView(this);
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            manager.getDefaultDisplay().getSize(screenSize);
        }
        ThemeStore.getThemeMode();
    }

    private void initLogin() {
        //创建微博实例
        WbSdk.install(this, new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
        //腾讯
        mTencent = Tencent.createInstance(Constants.APP_ID, MusicApp.getAppContext());
        //初始化socket
        socketManager = new SocketManager();
        socketManager.initSocket();
    }

    /**
     * 初始化文件下载
     */
    private void initFileDownload() {
        FileDownloadLog.NEED_LOG = true;
        FileDownloader.setup(this);
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
        PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_QUEUE_ID, getString(R.string.playlist_queue));
        PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_HISTORY_ID, getString(R.string.item_history));
        PlaylistLoader.INSTANCE.createDefaultPlaylist(Constants.PLAYLIST_LOVE_ID, getString(R.string.item_favorite));
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
                activities.add(activity);
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
                activities.remove(activity);
                if (Activitycount == 0) {
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>APP 关闭");
                    if (socketManager != null) {
                        socketManager.toggleSocket(false);
                    }
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
