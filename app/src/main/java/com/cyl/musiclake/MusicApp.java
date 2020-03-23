package com.cyl.musiclake;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musiclake.api.music.MusicApi;
import com.cyl.musiclake.api.net.ApiManager;
import com.cyl.musiclake.api.net.RequestCallBack;
import com.cyl.musiclake.bean.HotSearchBean;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.MPBroadcastReceiver;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.data.PlayQueueLoader;
import com.cyl.musiclake.data.PlaylistLoader;
import com.cyl.musiclake.di.component.ApplicationComponent;
import com.cyl.musiclake.di.component.DaggerApplicationComponent;
import com.cyl.musiclake.di.module.ApplicationModule;
import com.cyl.musiclake.socket.SocketManager;
import com.cyl.musiclake.ui.download.TasksManager;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FloatLyricViewManager;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.NetworkUtils;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.music.lake.musiclib.MusicPlayerConfig;
import com.music.lake.musiclib.MusicPlayerManager;
import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.listener.BindServiceCallBack;
import com.music.lake.musiclib.listener.MusicUrlRequest;
import com.music.lake.musiclib.notification.NotifyManager;
import com.music.lake.musiclib.service.MusicPlayerService;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.bugly.Bugly;
import com.tencent.tauth.Tencent;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * tinker热更新需要
 */
public class MusicApp extends MultiDexApplication {
    @SuppressLint("StaticFieldLeak")
    private static MusicApp sInstance;

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    private final String TAG = "MusicApp";
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
        Log.d(TAG, "onCreate");
        sInstance = this;
        mContext = this;
        registerListener();
        initApplicationComponent();
        initSDK();
        initFileDownload();
    }

    private void initSDK() {
        LitePal.initialize(this);
        initBugly();
        initDB();
        //初始化WebView
        BaseApiImpl.INSTANCE.initWebView(this);
        initLogin();
        initMusicPlayer();
        mpBroadcastReceiver = new MPBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(MusicPlayerService.ACTION_SERVICE);
        intentFilter.addAction(MusicPlayerService.META_CHANGED);
        intentFilter.addAction(MusicPlayerService.PLAY_QUEUE_CHANGE);
        intentFilter.addAction(NotifyManager.ACTION_MUSIC_NOTIFY);
        intentFilter.addAction(NotifyManager.ACTION_CLOSE);
        intentFilter.addAction(NotifyManager.ACTION_LYRIC);
        //注册广播
        registerReceiver(mpBroadcastReceiver, intentFilter);
    }

    private void initMusicPlayer() {
        MusicPlayerConfig config = new MusicPlayerConfig.Builder()
                .setUseCache(true)
                .setUseExoPlayer(true)
                .setUrlRequest(musicUrlRequest)
                .create();
        MusicPlayerManager.getInstance().init(this, config);
        MusicPlayerManager.getInstance().initialize(this, new BindServiceCallBack() {
            @Override
            public void onSuccess() {
                //同步上次播放队列
                if (!MusicPlayerManager.getInstance().getPlayList().equals(PlayQueueLoader.INSTANCE.getPlayQueue())) {
                    MusicPlayerManager.getInstance().updatePlaylist(PlayQueueLoader.INSTANCE.getPlayQueue(), SPUtils.getPlayPosition());
                }
            }

            @Override
            public void onFailed() {

            }
        });
        FloatLyricViewManager.getInstance().init(this);
    }

    private void initLogin() {
        //创建微博实例
        WbSdk.install(this, new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
        //腾讯
        MusicApp.mTencent = Tencent.createInstance(Constants.APP_ID, this);
        //初始化socket，因后台服务器压力大，暂时注释
//        SocketManager.INSTANCE.initSocket();
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

    private MusicUrlRequest musicUrlRequest = (musicInfo, call) -> {
        if (musicInfo.getUri() == null || !Objects.equals(musicInfo.getType(), Constants.LOCAL) || musicInfo.getUri().equals("") || musicInfo.getUri().equals("null")) {
            if (!NetworkUtils.isNetworkAvailable(MusicApp.getAppContext())) {
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
                        call.onActionDirect();
                    }
                });
            }
        } else {
            call.onActionDirect();
        }
        CoverLoader.INSTANCE.loadBitmap(MusicApp.getAppContext(), musicInfo.getCoverUri(), new Function1<Bitmap, Unit>() {
            @Override
            public Unit invoke(Bitmap bitmap) {
                call.onMusicBitmap(bitmap);
                return null;
            }
        });
    };

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
