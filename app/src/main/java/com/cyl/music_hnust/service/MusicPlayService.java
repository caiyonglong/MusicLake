package com.cyl.music_hnust.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.cyl.music_hnust.IMusicService;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.download.NetworkUtil;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.OnlinePlaylist;
import com.cyl.music_hnust.ui.activity.MainActivity;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.NetworkUtils;
import com.cyl.music_hnust.utils.Preferences;
import com.cyl.music_hnust.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者：yonglong on 2016/8/11 19:16
 * 邮箱：643872807@qq.com
 * 版本：2.5 播放service
 */
public class MusicPlayService extends Service {

    public static final String ACTION_SERVICE = "com.cyl.music_hnust.service";// 广播标志
    public static final String ACTION_NEXT = "com.cyl.music_hnust.notify.next";// 下一首广播标志
    public static final String ACTION_PREV = "com.cyl.music_hnust.notify.prev";// 上一首广播标志
    public static final String ACTION_PLAY = "com.cyl.music_hnust.notify.play";// 播放广播标志

    private MediaPlayer mPlayer;
    private Music mPlayingMusic = null;
    private List<Music> mPlaylist = new ArrayList<>();
    private int mPlayingPosition;

    RemoteViews mRemoteViews;

    private boolean isPause = false;

    //播放模式：0顺序播放、1随机播放、2单曲循环
    private int play_mode;
    private final int PLAY_MODE_ORDER = 0;
    private final int PLAY_MODE_RANDOM = 1;
    private final int PLAY_MODE_LOOP = 2;
    private int NOTIFICATION_ID = 0x123;

    private int curTime = 0;
    //广播接收者
    ServiceReceiver mServiceReceiver;

    public Notification mNotify;
    private IMusicServiceStub mBindStub = new IMusicServiceStub(this);

    //缓存的歌单
    public List<OnlinePlaylist> mSongLists = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initReceiver();
        initMediaPlayer();
        initTelephony();
//        initNotify();
    }

    private void initNotify() {

        String text = TextUtils.isEmpty(mPlayingMusic.getAlbum())
                ? mPlayingMusic.getArtist() : mPlayingMusic.getArtist() + " - " + mPlayingMusic.getAlbum();
        int playButtonResId = isPlaying()
                ? R.drawable.ic_pause_white_18dp : R.drawable.ic_play_arrow_white_18dp;
        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        nowPlayingIntent.putExtra(ACTION_SERVICE, true);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap cover = null;
        if (mPlayingMusic.getCover() == null) {
            cover = ImageLoader
                    .getInstance()
                    .loadImageSync(ImageUtils.getAlbumArtUri(mPlayingMusic.getAlbumId()).toString(),
                            ImageUtils.getAlbumDisplayOptions());
        } else {
            cover = mPlayingMusic.getCover();
        }

        /* 使用RemoteViews来布局 */
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.item_view_remote_noti);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_next, retrievePlaybackAction(ACTION_NEXT)); // 给停止按钮添加点击事件
        mRemoteViews.setOnClickPendingIntent(R.id.iv_prev, retrievePlaybackAction(ACTION_PREV)); // 给停止按钮添加点击事件
        mRemoteViews.setOnClickPendingIntent(R.id.iv_play_pause, retrievePlaybackAction(ACTION_PLAY)); // 给停止按钮添加点击事件

    }

    private void initTelephony() {
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);// 获取电话通讯服务
        telephonyManager.listen(new ServicePhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);// 创建一个监听对象，监听电话状态改变事件
    }

    private void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                next();
            }
        });
    }

    private void initReceiver() {
        //实例化过滤器，设置广播
        mServiceReceiver = new ServiceReceiver();
        IntentFilter intentFilter = new IntentFilter(ACTION_SERVICE);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        intentFilter.addAction(ACTION_PLAY);
        //注册广播
        registerReceiver(mServiceReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBindStub;
    }

    /**
     * 下一首
     */
    private void next() {
        play_mode = Preferences.getPlayMode();
        switch (play_mode) {
            case PLAY_MODE_ORDER:
                if (checkNetwork(mPlayingPosition + 1))
                    playMusic(mPlayingPosition + 1);
                break;
            case PLAY_MODE_RANDOM:
                Random random = new Random();
                if (checkNetwork(random.nextInt(getMusicList().size())))
                    playMusic(random.nextInt(mPlaylist.size()));
                break;
            case PLAY_MODE_LOOP:
                playMusic(mPlayingPosition);
                break;
        }
    }

    private boolean checkNetwork(int position) {

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
    private void prev() {
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
    private int playMusic(int position) {
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
            mPlayer.reset();
            mPlayer.setDataSource(mPlayingMusic.getUri());
            mPlayer.prepare();
            start();

        } catch (IOException e) {
            ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
        }
        sendBroadcast(new Intent(ACTION_PLAY));
    }


    private void playPause() {
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
        mPlayer.start();
        isPause = false;
        showNotification();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (!isPlaying()) {
            return;
        }
        mPlayer.pause();
        isPause = true;
        stopForeground(true);
        curTime = mPlayer.getCurrentPosition();
    }


    public boolean isPlaying() {
        return mPlayer.isPlaying() && mPlayer != null;
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
    }

    /**
     * 得到当前播放进度
     */
    public int getCurrent() {
        try {
            if (isPlaying()) {
                return mPlayer.getCurrentPosition();
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
            mPlayer.seekTo(msec);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void stop() {
        pause();
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        stopForeground(true);
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
    private List<Music> getMusicList() {
        Log.e("----Service", mPlaylist.size() + "====");
        return mPlaylist;
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
        if (mPlayingMusic == null)
            return;
        mNotify = buildNotification();
        startForeground(NOTIFICATION_ID, mNotify);
    }

    private Notification buildNotification() {

        String text = TextUtils.isEmpty(mPlayingMusic.getAlbum())
                ? mPlayingMusic.getArtist() : mPlayingMusic.getArtist() + " - " + mPlayingMusic.getAlbum();
        int playButtonResId = isPlaying()
                ? R.drawable.ic_pause_white_18dp : R.drawable.ic_play_arrow_white_18dp;
        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        nowPlayingIntent.putExtra(ACTION_SERVICE, true);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap cover = null;
        if (mPlayingMusic.getCover() == null) {
            cover = ImageLoader
                    .getInstance()
                    .loadImageSync(ImageUtils.getAlbumArtUri(mPlayingMusic.getAlbumId()).toString(),
                            ImageUtils.getAlbumDisplayOptions());
        } else {
            cover = mPlayingMusic.getCover();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_empty_music2)
                .setLargeIcon(cover)
                .setContentIntent(clickIntent)
                .setContentTitle(text)
                .addAction(R.drawable.ic_skip_previous_white_24dp,
                        "",
                        retrievePlaybackAction(ACTION_PREV))
                .addAction(playButtonResId, "",
                        retrievePlaybackAction(ACTION_PLAY))
                .addAction(R.drawable.ic_skip_next_white_24dp,
                        "",
                        retrievePlaybackAction(ACTION_NEXT));
        Notification notify = builder.build();

        return notify;
    }

    private final PendingIntent retrievePlaybackAction(final String action) {
        Intent intent = new Intent(action);
        return PendingIntent.getBroadcast(this, 0, intent, 0);
    }

    private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SERVICE)) {
                handleCommandIntent(intent);
            }
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

    private class IMusicServiceStub extends IMusicService.Stub {
        private final WeakReference<MusicPlayService> mService;

        private IMusicServiceStub(final MusicPlayService service) {
            mService = new WeakReference<MusicPlayService>(service);
        }

        @Override
        public void playOnline(Music music) throws RemoteException {
            mService.get().playMusic(music);
        }

        @Override
        public void play(int id) throws RemoteException {
            mService.get().playMusic(id);
        }

        @Override
        public void playPause() throws RemoteException {
            mService.get().playPause();
        }

        @Override
        public void prev() throws RemoteException {
            mService.get().prev();
        }

        @Override
        public void next() throws RemoteException {
            mService.get().next();
        }

        @Override
        public void setLoopMode(int loopmode) throws RemoteException {
        }

        @Override
        public void seekTo(int ms) throws RemoteException {
            mService.get().seekTo(ms);
        }

        @Override
        public String getSongName() throws RemoteException {
            return null;
        }

        @Override
        public String getSongArtist() throws RemoteException {
            return null;
        }

        @Override
        public Music getPlayingMusic() throws RemoteException {
            return mPlayingMusic;
        }

        @Override
        public void setPlayList(List<Music> playlist) throws RemoteException {
            mService.get().mPlaylist = playlist;
        }

        @Override
        public int position() throws RemoteException {
            return mService.get().getmPlayingPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return mService.get().getCurrent();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return mService.get().getCurrent();
        }


        @Override
        public boolean isPlaying() throws RemoteException {
            return mService.get().isPlaying();
        }

        @Override
        public boolean isPause() throws RemoteException {
            return mService.get().isPause();
        }

    }
}
