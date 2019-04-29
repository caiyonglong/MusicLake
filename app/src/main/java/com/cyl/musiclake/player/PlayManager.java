package com.cyl.musiclake.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.bean.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by D22434 on 2017/9/20.
 */

public class PlayManager {
    public static IMusicService mService = null;
    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
    }

    public static final ServiceToken bindToService(final Context context,
                                                   final ServiceConnection callback) {

        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        try {
            //TODO 修复Android 8.0启动service异常报错 Not allowed to start service Intent { cmp=com.cyl.musiclake/.player.MusicPlayerService }: app is in background uid UidRecord{f44b6ce u0a208 TPSL idle procs:1 seq(0,0,0)}
            final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
            contextWrapper.startService(new Intent(contextWrapper, MusicPlayerService.class));
            final ServiceBinder binder = new ServiceBinder(callback,
                    contextWrapper.getApplicationContext());
            if (contextWrapper.bindService(
                    new Intent().setClass(contextWrapper, MusicPlayerService.class), binder, 0)) {
                mConnectionMap.put(contextWrapper, binder);
                return new ServiceToken(contextWrapper);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.get(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            mService = null;
        }
    }

    public static final boolean isPlaybackServiceConnected() {
        return mService != null;
    }


    public static void nextPlay(Music music) {
        try {
            if (mService != null)
                mService.nextPlay(music);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void playOnline(Music music) {
        try {
            if (mService != null)
                mService.playMusic(music);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void play(int id) {
        try {
            if (mService != null)
                mService.play(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void play(int id, List<Music> musicList, String pid) {
        try {
            if (mService != null) {
                mService.playPlaylist(musicList, id, pid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static int getAudioSessionId() {
        try {
            if (mService != null)
                return mService.AudioSessionId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void playPause() {
        try {
            if (mService != null)
                mService.playPause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void prev() {
        try {
            if (mService != null)
                mService.prev();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void next() {
        try {
            if (mService != null)
                mService.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setLoopMode(int loopmode) {
        try {
            if (mService != null)
                mService.setLoopMode(loopmode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void seekTo(int ms) {
        try {
            if (mService != null)
                mService.seekTo(ms);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static int position() {
        try {
            if (mService != null)
                return mService.position();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCurrentPosition() {
        try {
            if (mService != null)
                return mService.getCurrentPosition();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDuration() {
        try {
            if (mService != null)
                return mService.getDuration();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getSongName() {
        try {
            if (mService != null)
                return mService.getSongName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "湖科音乐";
    }

    public static String getSongArtist() {
        try {
            if (mService != null)
                return mService.getSongArtist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "湖科音乐";
    }

    public static boolean isPlaying() {
        try {
            if (mService != null)
                return mService.isPlaying();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPause() {
        try {
            if (mService != null)
                return mService.isPause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Music getPlayingMusic() {
        try {
            if (mService != null)
                return mService.getPlayingMusic();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPlayingId() {
        try {
            if (mService != null && mService.getPlayingMusic() != null)
                return mService.getPlayingMusic().getMid();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    public static List<Music> getPlayList() {
        try {
            if (mService != null)
                return mService.getPlayList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void setPlayList(List<Music> playlist) {

    }

    public static void clearQueue() {
        try {
            if (mService != null) {
                mService.clearQueue();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromQueue(int adapterPosition) {
        try {
            if (mService != null)
                mService.removeFromQueue(adapterPosition);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void showDesktopLyric(boolean isShow) {
        try {
            if (mService != null)
                mService.showDesktopLyric(isShow);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static final class ServiceBinder implements ServiceConnection {
        private final ServiceConnection mCallback;
        private final Context mContext;


        public ServiceBinder(final ServiceConnection callback, final Context context) {
            mCallback = callback;
            mContext = context;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            mService = IMusicService.Stub.asInterface(service);
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            mService = null;
        }
    }

    public static final class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }
}
