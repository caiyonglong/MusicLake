package com.cyl.music_hnust.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.MainActivity;
import com.cyl.music_hnust.download.NetworkUtil;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.OnlinePlaylist;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.MusicUtils;
import com.cyl.music_hnust.utils.NetworkUtils;
import com.cyl.music_hnust.utils.Preferences;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    //广播接收者
    ServiceReceiver mServiceReceiver;
    public static final String ACTION_SERVICE = "com.cyl.music_hnust.service";// 广播标志
    public static final String ACTION_NEXT = "com.cyl.music_hnust.notify.next";// 广播标志
    public static final String ACTION_PREV = "com.cyl.music_hnust.notify.prev";// 广播标志
    public static final String ACTION_PLAY = "com.cyl.music_hnust.notify.play";// 广播标志

    public Notification notif;


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

        //实例化过滤器，设置广播
        mServiceReceiver = new ServiceReceiver();
        IntentFilter intentFilter = new IntentFilter(ACTION_SERVICE);

        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        intentFilter.addAction(ACTION_PLAY);
        //注册广播
        registerReceiver(mServiceReceiver, intentFilter);


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
//        MusicUtils.getAllSongs(this, getMusicList());
        myMusicList = MusicUtils.getAllSongs(this);
        if (getMusicList().isEmpty()) {
            return;
        }
//        updatePlayingPosition();
        mPlayingMusic = mPlayingMusic == null ? getMusicList().get(mPlayingPosition) : mPlayingMusic;
    }

    /**
     * 下一首
     */
    public void next() {


        play_mode = Preferences.getPlayMode();
        switch (play_mode) {
            case PLAY_MODE_ORDER:
                if (checkNetwork(mPlayingPosition + 1))
                    playMusic(mPlayingPosition + 1);
                break;
            case PLAY_MODE_RANDOM:
                Random random = new Random();
                if (checkNetwork(random.nextInt(getMusicList().size())))
                    playMusic(random.nextInt(myMusicList.size()));
                break;
            case PLAY_MODE_LOOP:
                playMusic(mPlayingPosition);
                break;
        }
    }

    public boolean checkNetwork(int position) {

        Log.e("----Service", getMusicList().size() + "====");

        if (position <= 0) {
            position = getMusicList().size() - 1;
        } else if (position >= getMusicList().size()) {
            position = 0;
        }

        boolean mobileNetworkPlay = Preferences.enableMobileNetworkPlay();
        try {
            if (getMusicList().get(position).getType() == Music.Type.LOCAL) {
                return true;
            } else if (NetworkUtils.is4G(getApplicationContext()) && !mobileNetworkPlay) {
                ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
                return false;
            } else if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                return true;
            } else {
                ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上一首
     */
    public void prev() {
        play_mode = Preferences.getPlayMode();
        switch (play_mode) {
            case PLAY_MODE_ORDER:
                if (checkNetwork(mPlayingPosition - 1))
                    playMusic(mPlayingPosition - 1);
                break;
            case PLAY_MODE_RANDOM:
                Random random = new Random();
                if (checkNetwork(random.nextInt(getMusicList().size())))
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
        showNotification();
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
            ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
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
        showNotification();
    }

    private void start() {
        mediaPlayer.start();
        isPause = false;
        mHandler.post(mRunnable);
        showNotification();
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

        Log.e("----Service", myMusicList.size() + "====");
        return myMusicList;
    }


    /**
     * 设置当前播放列表
     *
     * @param myMusicList
     */
    public void setMyMusicList(List<Music> myMusicList) {
        this.myMusicList = myMusicList;
        Log.e("----Service", myMusicList.size() + "====");
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

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public long getmPlayingMusicId() {
        if (mPlayingMusic != null) {
            try {
                return mPlayingMusic.getId();
            } catch (Exception e) {
            }
        }
        return -1;
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
    public void showNotification() {
        notif = buildNotification();
        startForeground(NOTIFICATION_ID, notif);
    }

    private Notification buildNotification() {
        Music music = getPlayingMusic();
        final String title = music.getTitle();
        final String albumName = music.getAlbum();
        final String artistName = music.getArtist();
        final boolean isPlaying = isPlaying();
        String text = TextUtils.isEmpty(artistName)
                ? artistName : artistName + " - " + albumName;

        int playButtonResId = isPlaying
                ? R.drawable.ic_pause_white_18dp : R.drawable.ic_play_arrow_white_18dp;

        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        nowPlayingIntent.putExtra(ACTION_SERVICE, true);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Bitmap artwork;
//        artwork = ImageLoader.getInstance().loadImageSync(ImageUtils.getAlbumArtUri(music.getAlbumId()).toString(),ImageUtils.getAlbumDisplayOptions());
//
//        if (artwork == null) {
//            artwork = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.ic_empty_music2);
//        }
        Bitmap cover = null;
        if (music.getCover() == null) {
            cover = ImageLoader.getInstance().loadImageSync(ImageUtils.getAlbumArtUri(music.getAlbumId()).toString(), ImageUtils.getAlbumDisplayOptions());
//            cover = CoverLoader.getInstance().loadThumbnail(music.getCoverUri());
        } else {
            cover = music.getCover();
        }

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_empty_music2)
                .setLargeIcon(cover)
                .setContentIntent(clickIntent)
                .setContentTitle(title)
                .setContentText(text)
                .addAction(R.drawable.ic_skip_previous_white_24dp,
                        "",
                        retrievePlaybackAction(ACTION_PREV))
                .addAction(playButtonResId, "",
                        retrievePlaybackAction(ACTION_PLAY))
                .addAction(R.drawable.ic_skip_next_white_24dp,
                        "",
                        retrievePlaybackAction(ACTION_NEXT));

        if (SystemUtils.isLollipop()) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2, 3);
            builder.setStyle(style);
        }
        Notification n = builder.build();

        return n;
    }

    private final PendingIntent retrievePlaybackAction(final String action) {
        Intent intent = new Intent(action);
        return PendingIntent.getBroadcast(this, 0, intent, 0);
    }

    private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            final String command = intent.getStringExtra(CMDNAME);
            handleCommandIntent(intent);
        }
    }

    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();


        if (ACTION_NEXT.equals(action)) {
            next();
        } else if (ACTION_PREV.equals(action)) {
            prev();
        } else if (ACTION_PLAY.equals(action)) {
            playPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mServiceReceiver);
        stop();
    }
}
