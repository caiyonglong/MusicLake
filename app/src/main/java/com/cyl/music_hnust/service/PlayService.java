package com.cyl.music_hnust.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.model.OnlinePlaylist;
import com.cyl.music_hnust.utils.MusicUtils;
import com.cyl.music_hnust.utils.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者：yonglong on 2016/8/11 19:16
 * 邮箱：643872807@qq.com
 * 版本：2.5 播放service
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener {



    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Music mPlayingMusic = null;
    private List<Music> myMusicList = new ArrayList<>();
    private int mPlayingPosition;

    private boolean isPause = false;

    private Handler mHandler = new Handler();

    //播放模式：0顺序播放、1随机播放、2单曲循环
    private int play_mode;
    private final int PLAY_MODE_ORDER = 0;
    private final int PLAY_MODE_RANDOM = 1;
    private final int PLAY_MODE_LOOP = 2;
    private int NOTIFICATION_ID = 0x123;
    private int DELAY_TIME = 1000;

    private int curTime = 0;


    private IBinder mBinder = new MyBinder();

    public void setOnPlayEventListener(OnPlayerListener listener) {
        mListener = listener;
    }

    private OnPlayerListener mListener;
    private NotificationManager mNotificationManager;

    //缓存的歌单
    public List<OnlinePlaylist> mSongLists = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer.setOnCompletionListener(this);
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);// 获取电话通讯服务
        telephonyManager.listen(new ServicePhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);// 创建一个监听对象，监听电话状态改变事件


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    /**
     * 每次启动时扫描音乐
     */
    public void updateMusicList() {
        MusicUtils.scanMusic(this,myMusicList);
        if (getMusicList().isEmpty()) {
            return;
        }
//        updatePlayingPosition();
//        mPlayingMusic = mPlayingMusic == null ? getMusicList().get(mPlayingPosition) : mPlayingMusic;
    }

    /**
     * 下一首
     */
    public void next() {
        play_mode = Preferences.getPlayMode();
        switch (play_mode) {
            case PLAY_MODE_ORDER:
                playMusic(mPlayingPosition+1);
                break;
            case PLAY_MODE_RANDOM:
                Random random = new Random();
                playMusic(random.nextInt(myMusicList.size()));
                break;
            case PLAY_MODE_LOOP:
                playMusic(mPlayingPosition);
                break;
        }
    }

    /**
     * 上一首
     */
    public void prev() {
        play_mode = Preferences.getPlayMode();
        switch (play_mode) {
            case PLAY_MODE_ORDER:
                playMusic(mPlayingPosition-1);
                break;
            case PLAY_MODE_RANDOM:
                Random random = new Random();
                playMusic(random.nextInt(getMusicList().size()));
                break;
            case PLAY_MODE_LOOP:
                playMusic(mPlayingPosition);
                break;
        }
    }


    /**
     * 播放音乐
     *
     * @param position
     */
    public int playMusic(int position) {
        if (getMusicList().isEmpty()) {
            return -1;
        }

        if (position < 0) {
            position = getMusicList().size() - 1;
        } else if (position >= getMusicList().size()) {
            position = 0;
        }

        mPlayingPosition = position;
        playMusic(getMusicList().get(mPlayingPosition));
        return mPlayingPosition;

    }

    public void playMusic(Music music) {
        mPlayingMusic = music;

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mPlayingMusic.getUri());
            mediaPlayer.prepare();
            start();
            if (mListener != null) {
                mListener.onChange(music);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void playPause() {
        if (isPlaying()) {
            pause();
        } else if (isPause()) {
            resume();
        } else {
            playMusic(getmPlayingPosition());
        }
    }

    private void start() {
        mediaPlayer.start();
        isPause = false;
        mHandler.post(mRunnable);
//        updateNotification(mPlayingMusic);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (!isPlaying()) {
            return;
        }
        mediaPlayer.pause();
        isPause = true;

        mHandler.removeCallbacks(mRunnable);
        curTime = mediaPlayer.getCurrentPosition();
        if (mListener != null) {
            mListener.onPlayerPause();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying() && mediaPlayer != null;
    }


    /**
     * 恢复播放
     */
    public void resume() {
        if (isPlaying()) {
            return;
        }
        start();
        isPause = false;
        seekTo(curTime);
        if (mListener != null) {
            mListener.onPlayerResume();
        }
    }

    /**
     * 得到当前播放进度
     */
    public int getCurrent() {
        try {
            if (isPlaying()) {
                return mediaPlayer.getCurrentPosition();
            } else {
                return curTime;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "播放出错",
                    Toast.LENGTH_SHORT).show();
            return curTime;
        }
    }

    /**
     * 跳到输入的进度
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPause()) {
            mediaPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onUpdate(msec);
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mListener = null;
        return true;
    }

    public void stop() {
        pause();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        mNotificationManager.cancel(NOTIFICATION_ID);
        stopSelf();
    }

    public boolean isPause() {
        return isPause;
    }

    /**
     * 获取当前播放列表
     *
     * @return
     */
    public List<Music> getMusicList() {
        return myMusicList;
    }


    /**
     * 设置当前播放列表
     *
     * @param myMusicList
     */
    public void setMyMusicList(List<Music> myMusicList) {
        this.myMusicList = myMusicList;
    }


    /**
     * 获取正在播放歌曲的位置
     *
     * @return
     */
    public int getmPlayingPosition() {
        return mPlayingPosition;
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public Music getPlayingMusic() {
        return mPlayingMusic;
    }

    public class MyBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null) {
                mListener.onUpdate(mediaPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, DELAY_TIME);
        }
    };

    private class ServicePhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:   //通话状态
                case TelephonyManager.CALL_STATE_RINGING:   //通话状态
                    pause();
                    break;
            }
        }
    }

    /**
     * 更新通知栏
     */
//    private void updateNotification(Music music) {
//        mNotificationManager.cancel(NOTIFICATION_ID);
//        startForeground(NOTIFICATION_ID, SystemUtils.createNotification(this, music));
//    }
//
//    private void cancelNotification(Music music) {
//        stopForeground(true);
//        mNotificationManager.notify(NOTIFICATION_ID, SystemUtils.createNotification(this, music));
//    }

}
