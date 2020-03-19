package com.music.lake.musiclib.player;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.danikula.videocache.HttpProxyCacheServer;
import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.cache.CacheFileNameGenerator;
import com.music.lake.musiclib.listener.BindServiceCallBack;
import com.music.lake.musiclib.listener.MusicPlayEventListener;
import com.music.lake.musiclib.listener.MusicPlayerController;
import com.music.lake.musiclib.listener.MusicRequest;
import com.music.lake.musiclib.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by D22434 on 2017/9/20.
 */

public class MusicPlayerManager implements MusicPlayerController {

    private MusicServiceBinder mBinder = null;
    private Application application;
    private ServiceToken mToken;

    private MusicRequest request;

    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
    }

    private volatile static MusicPlayerManager manager;

    public static MusicPlayerManager getInstance() {
        if (manager == null) {
            synchronized (MusicPlayerManager.class) {
                if (manager == null) {
                    manager = new MusicPlayerManager();
                }
            }
        }
        return manager;
    }

    private MusicPlayerManager() {
    }

    public void init(Application application) {
        this.application = application;
    }

    public Context getAppContext() {
        return application.getApplicationContext();
    }


    public ServiceToken initialize(Context context, BindServiceCallBack callBack) {
        if (context instanceof Activity) {
            mToken = bindToService(context, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mBinder = (MusicServiceBinder) iBinder;
                    mBinder.setMusicRequestListener(request);
                    callBack.onSuccess();
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mBinder = null;
                    callBack.onFailed();
                    LogUtil.d("BaseActivity", "onServiceDisconnected");
                }
            });
        }
//        else {
//            new IllegalStateException("Must pass in Activity type Context!");
//    }
        return mToken;
    }

    public void unInitialize(ServiceToken token) {
        if (token != null) {
            unbindFromService(token);
        }
    }

    public final ServiceToken bindToService(final Context context,
                                            final ServiceConnection callback) {
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        try {
            //TODO 修复Android 8.0启动service异常报错 Not allowed to start service Intent { cmp=com.cyl.musiclake/.player.MusicPlayerService }: app is in background uid UidRecord{f44b6ce u0a208 TPSL idle procs:1 seq(0,0,0)}
            final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
            contextWrapper.startService(new Intent(contextWrapper, MusicPlayerService.class));
            final ServiceBinder binder = new ServiceBinder(callback, contextWrapper.getApplicationContext());
            if (contextWrapper.bindService(new Intent().setClass(contextWrapper, MusicPlayerService.class), binder, 0)) {
                mConnectionMap.put(contextWrapper, binder);
                return new ServiceToken(contextWrapper);
            }
        } catch (Exception e) {

        }
        return null;
    }

    private void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder binder = mConnectionMap.get(mContextWrapper);
        if (binder == null) {
            return;
        }
        mContextWrapper.unbindService(binder);
        if (mConnectionMap.isEmpty()) {
            mBinder = null;
        }
    }

    public final boolean isPlaybackServiceConnected() {
        return mBinder != null;
    }


    //////////////////////////////////////////////////////////////////
    //* Start
    //* 播放相关接口
    ///////////////////////////////////////////////////////////////////


    @Override
    public void playMusicById(int index) {
        if (mBinder != null) {
            mBinder.playMusicById(index);
        }
    }

    @Override
    public void playMusic(@NotNull BaseMusicInfo song) {
        if (mBinder != null) {
            mBinder.playMusic(song);
        }
    }

    @Override
    public void playMusic(List<BaseMusicInfo> songs, int index) {
        if (mBinder != null) {
            mBinder.playMusic(songs, index);
        }
    }

    @Override
    public void playNextMusic() {
        if (mBinder != null) {
            mBinder.playNextMusic();
        }
    }

    @Override
    public void playPrevMusic() {
        if (mBinder != null) {
            mBinder.playPrevMusic();
        }
    }

    @Override
    public void restorePlay() {
        if (mBinder != null) {
            mBinder.restorePlay();
        }
    }

    @Override
    public void pausePlay() {
        if (mBinder != null) {
            mBinder.pausePlay();
        }
    }

    @Override
    public void stopPlay() {
        if (mBinder != null) {
            mBinder.stopPlay();
        }
    }

    @Override
    public int getLoopMode() {
        if (mBinder != null) {
            return mBinder.getLoopMode();
        }
        return 0;
    }

    @Override
    public BaseMusicInfo getNowPlayingMusic() {
        if (mBinder != null) {
            return mBinder.getNowPlayingMusic();
        }
        return null;
    }

    @Override
    public int getNowPlayingIndex() {
        if (mBinder != null) {
            return mBinder.getNowPlayingIndex();
        }
        return 0;
    }

    @Override
    public void removeFromPlaylist(int position) {
        if (mBinder != null) {
            mBinder.removeFromPlaylist(position);
        }
    }

    @Override
    public void clearPlaylist() {
        if (mBinder != null) {
            mBinder.clearPlaylist();
        }
    }

    @Override
    public long getPlayingPosition() {
        if (mBinder != null) {
            return mBinder.getPlayingPosition();
        }
        return 0;
    }

    @Override
    public void addMusicPlayerEventListener(@NotNull MusicPlayEventListener listener) {
        if (mBinder != null) {
            mBinder.addMusicPlayerEventListener(listener);
        }
    }

    @Override
    public void removeMusicPlayerEventListener(@NotNull MusicPlayEventListener listener) {
        if (mBinder != null) {
            mBinder.removeMusicPlayerEventListener(listener);
        }
    }


    @Override
    public int AudioSessionId() {
        if (mBinder != null) {
            return mBinder.AudioSessionId();
        }
        return 0;
    }

    @Override
    public void setLoopMode(int mode) {
        if (mBinder != null) {
            mBinder.setLoopMode(mode);
        }
    }

    @Override
    public void seekTo(long ms) {
        if (mBinder != null) {
            mBinder.seekTo(ms);
        }
    }

    @Override
    public List<BaseMusicInfo> getPlayList() {
        if (mBinder != null) {
            return mBinder.getPlayList();
        }
        return null;
    }

    @Override
    public boolean isPlaying() {
        if (mBinder != null) {
            return mBinder.isPlaying();
        }
        return false;
    }

    @Override
    public long getDuration() {
        if (mBinder != null) {
            return mBinder.getDuration();
        }
        return 0;
    }

    @Override
    public void showDesktopLyric(boolean show) {

    }

    @Override
    public void setMusicRequestListener(@NotNull MusicRequest request) {
        this.request = request;
    }


    //////////////////////////////////////////////////////////////////
    //* End
    //* 播放相关接口
    ///////////////////////////////////////////////////////////////////


    /**
     * AndroidVideoCache缓存设置
     */
    private HttpProxyCacheServer proxy;
    private String musicFilelCacheDir = null;
    private boolean mHasCache;

    public boolean isHasCache() {
        return mHasCache;
    }

    public void setHasCache(boolean mHasCache) {
        this.mHasCache = mHasCache;
    }

    public static HttpProxyCacheServer getProxy() {
        return MusicPlayerManager.getInstance().proxy == null ? (MusicPlayerManager.getInstance().proxy = MusicPlayerManager.getInstance().newProxy()) : MusicPlayerManager.getInstance().proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(application)
                .cacheDirectory(new File(musicFilelCacheDir))
                .fileNameGenerator(new CacheFileNameGenerator())
                .build();
    }

    public final class ServiceBinder implements ServiceConnection {
        private final ServiceConnection mCallback;
        private final Context mContext;

        public ServiceBinder(final ServiceConnection callback, final Context context) {
            mCallback = callback;
            mContext = context;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            mBinder = (MusicServiceBinder) service;
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            mBinder = null;
        }
    }

    public static final class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }
}
