package com.cyl.musiclake.player;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.utils.CoverLoader;

/**
 * MediaSession管理类
 * 主要管理Android 5.0以后线控和蓝牙远程控制播放
 */

@SuppressWarnings("ALL")
public class MediaSessionManager {

    private static final String TAG = "MediaSessionManager";

    //指定可以接收的来自锁屏页面的按键信息
    private static final long MEDIA_SESSION_ACTIONS =
            PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_SEEK_TO;


    private final IMusicServiceStub control;
    private final Context context;
    private MediaSessionCompat mMediaSession;
    private Handler mHandler;

    public MediaSessionManager(IMusicServiceStub control, Context context, Handler mHandler) {
        this.control = control;
        this.context = context;
        this.mHandler = mHandler;
        setupMediaSession();
    }

    /**
     * 初始化并激活 MediaSession
     */
    private void setupMediaSession() {
//        第二个参数 tag: 这个是用于调试用的,随便填写即可
        mMediaSession = new MediaSessionCompat(context, TAG);
        //指明支持的按键信息类型
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mMediaSession.setCallback(callback, mHandler);
        mMediaSession.setActive(true);
    }

    /**
     * 更新播放状态， 播放／暂停／拖动进度条时调用
     */
    public void updatePlaybackState() {
        int state = isPlaying() ? PlaybackStateCompat.STATE_PLAYING :
                PlaybackStateCompat.STATE_PAUSED;

        mMediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setActions(MEDIA_SESSION_ACTIONS)
                .setState(state, getCurrentPosition(), 1)
                .build());
    }

    private long getCurrentPosition() {
        try {
            return control.getCurrentPosition();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 是否在播放
     *
     * @return
     */
    protected boolean isPlaying() {
        try {
            return control.isPlaying();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新正在播放的音乐信息，切换歌曲时调用
     */
    public void updateMetaData(Music songInfo) {
        if (songInfo == null) {
            mMediaSession.setMetadata(null);
            return;
        }

        MediaMetadataCompat.Builder metaDta = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songInfo.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, songInfo.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, songInfo.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, songInfo.getArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, songInfo.getDuration());

        CoverLoader.INSTANCE.loadBigImageView(context, songInfo, bitmap -> {
            metaDta.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
            mMediaSession.setMetadata(metaDta.build());
            return null;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaDta.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getCount());
        }
        mMediaSession.setMetadata(metaDta.build());

    }

    private long getCount() {
        try {
            return control.getPlayList().size();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public MediaSessionCompat.Token getMediaSession() {
        return mMediaSession.getSessionToken();
    }

    /**
     * 释放MediaSession，退出播放器时调用
     */
    public void release() {
        mMediaSession.setCallback(null);
        mMediaSession.setActive(false);
        mMediaSession.release();
    }


    /**
     * API 21 以上 耳机多媒体按钮监听 MediaSessionCompat.Callback
     */
    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {

//        接收到监听事件，可以有选择的进行重写相关方法

        @Override
        public void onPlay() {
            try {
                control.playPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPause() {
            try {
                control.playPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSkipToNext() {
            try {
                control.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSkipToPrevious() {
            try {
                control.prev();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStop() {
            try {
                control.playPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSeekTo(long pos) {
            try {
                control.seekTo((int) pos);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
}
