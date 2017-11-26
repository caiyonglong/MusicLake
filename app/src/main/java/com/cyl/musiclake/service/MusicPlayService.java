package com.cyl.musiclake.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.graphics.Palette;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.R;
import com.cyl.musiclake.dataloaders.MusicLoader;
import com.cyl.musiclake.download.NetworkUtil;
import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.ui.activity.MainActivity;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.NetworkUtils;
import com.cyl.musiclake.utils.Preferences;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

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
    private static final String TAG = "MusicPlayService";

    public static final String ACTION_SERVICE = "com.cyl.music_hnust.service";// 广播标志
    public static final String ACTION_NEXT = "com.cyl.music_hnust.notify.next";// 下一首广播标志
    public static final String ACTION_PREV = "com.cyl.music_hnust.notify.prev";// 上一首广播标志
    public static final String PLAY_STATE_CHANGED = "com.cyl.music_hnust.play_state";// 播放广播标志
    public static final String ACTION_UPDATE = "com.cyl.music_hnust.notify.update";// 播放广播标志
    public static final String PLAYLIST_CHANGED = "com.cyl.music_hnust.playlist";
    public static final String TRACK_ERROR = "com.cyl.music_hnust.error";
    public static final String REFRESH = "com.cyl.music_hnust.refresh";
    public static final String PLAYLIST_CLEAR = "com.cyl.music_hnust.playlist_clear";
    public static final String META_CHANGED = "com.cyl.music_hnust.metachanged";
    private static final boolean DEBUG = true;

    private MediaPlayer mPlayer = null;
    private Music mPlayingMusic = null;
    private List<Music> mPlaylist = new ArrayList<>();
    private int mPlayingPos;

    RemoteViews mRemoteViews;

    private boolean isPause = false;

    //播放模式：0顺序播放、1随机播放、2单曲循环
    private int mRepeatMode;
    private final int PLAY_MODE_RANDOM = 0;
    private final int PLAY_MODE_LOOP = 1;
    private final int PLAY_MODE_REPEAT = 2;
    private int NOTIFICATION_ID = 0x123;

    //广播接收者
    ServiceReceiver mServiceReceiver;

    private NotificationManager nm;

    private IMusicServiceStub mBindStub = new IMusicServiceStub(this);
    private long mNotificationPostTime = 0;
    private MediaSessionCompat mSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initNotify();
        initReceiver();
        initMediaPlayer();
        initTelephony();
    }

    private void initData() {
        mPlaylist = MusicLoader.getPlayQueue(this);
        mPlayingPos = (int) Preferences.getCurrentSongId();
        if (mPlayingPos == -1) {
            mPlayingMusic = null;
        } else {
            mPlayingMusic = mPlaylist.get(mPlayingPos);
        }
    }

    /**
     * 初始化电话监听服务
     */
    private void initTelephony() {
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);// 获取电话通讯服务
        telephonyManager.listen(new ServicePhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);// 创建一个监听对象，监听电话状态改变事件
    }

    /**
     * 初始化音乐播放服务
     */
    private void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(mPlayer -> next());
        mPlayer.setOnErrorListener((mediaPlayer, i, i1) -> {
            playPause();
            return false;
        });
    }

    /**
     * 初始化广播
     */
    private void initReceiver() {
        //实例化过滤器，设置广播
        mServiceReceiver = new ServiceReceiver();
        IntentFilter intentFilter = new IntentFilter(ACTION_SERVICE);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        intentFilter.addAction(PLAY_STATE_CHANGED);
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
        mPlayingPos = getNextPosition();
        playMusic(mPlayingPos);
    }

    /**
     * 上一首
     */
    private void prev() {
        mPlayingPos = getPreviousPosition();
        Log.e(TAG, "mPlayingPos:" + mPlayingPos);
        playMusic(mPlayingPos);
    }

    private int getNextPosition() {
        if (mPlaylist == null || mPlaylist.isEmpty()) {
            return -1;
        }
        if (mPlaylist.size() == 1) {
            return mPlayingPos;
        }
        mRepeatMode = Preferences.getPlayMode();
        if (mRepeatMode == PLAY_MODE_REPEAT) {
            if (mPlayingPos < 0) {
                return 0;
            }
        } else if (mRepeatMode == PLAY_MODE_LOOP) {
            if (mPlayingPos == mPlaylist.size() - 1) {
                mPlayingPos = 0;
            } else if (mPlayingPos < mPlaylist.size() - 1) {
                mPlayingPos += 1;
            }
        } else if (mRepeatMode == PLAY_MODE_RANDOM) {
            mPlayingPos = new Random().nextInt(mPlaylist.size());
        }
        return mPlayingPos;
    }

    private int getPreviousPosition() {
        if (mPlaylist == null || mPlaylist.isEmpty()) {
            return -1;
        }
        if (mPlaylist.size() == 1) {
            return mPlayingPos;
        }
        mRepeatMode = Preferences.getPlayMode();
        if (mRepeatMode == PLAY_MODE_REPEAT) {
            if (mPlayingPos < 0) {
                return 0;
            }
        } else if (mRepeatMode == PLAY_MODE_LOOP) {
            if (mPlayingPos == 0) {
                mPlayingPos = mPlaylist.size() - 1;
            } else if (mPlayingPos > 0) {
                mPlayingPos -= 1;
            }
        } else if (mRepeatMode == PLAY_MODE_RANDOM) {
            mPlayingPos = new Random().nextInt(mPlaylist.size());
        }
        return mPlayingPos;
    }

    private boolean checkNetwork(Music music) {
        if (music.getType() == Music.Type.LOCAL) {
            return true;
        }
        boolean mobileNetworkPlay = Preferences.enableMobileNetworkPlay();
        try {
            if (NetworkUtils.is4G(getApplicationContext()) && !mobileNetworkPlay) {
                return false;
            } else if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据位置播放音乐
     *
     * @param position
     */
    private void playMusic(int position) {
        if (mPlaylist == null || mPlaylist.isEmpty()) {
            return;
        }
        if (mPlayingPos == -1) {
            return;
        }
        if (mPlaylist.get(mPlayingPos) == null) {
            return;
        }
        Log.e(TAG, "position" + position);
        playMusic(mPlaylist.get(position));
    }

    /**
     * 播放音乐
     *
     * @param music
     */
    public void playMusic(Music music) {
        mPlayingMusic = music;
        Log.d(TAG, mPlayingMusic.toString());
        showNotification();
        notifyChange(META_CHANGED);
        try {
            mPlayer.reset();
            mPlayer.setDataSource(mPlayingMusic.getUri());
            mPlayer.prepare();
            mPlayer.start();
            isPause = false;
        } catch (IOException e) {
            ToastUtils.show(getApplicationContext(), R.string.unable_to_play_exception);
        }
    }

    /**
     * 播放暂停
     */
    private void playPause() {
        if (isPlaying()) {
            pause();
        } else if (isPause()) {
            notifyChange(META_CHANGED);
            mPlayer.start();
            isPause = false;
            showNotification();
        } else {
            playMusic(mPlayingPos);
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (!isPlaying()) {
            return;
        }
        notifyChange(META_CHANGED);
        mPlayer.pause();
        isPause = true;
    }

    /**
     * 是否正在播放音乐
     *
     * @return
     */
    public boolean isPlaying() {
        if (mPlayer == null) {
            return false;
        } else if (mPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    /**
     * 跳到输入的进度
     */
    public void seekTo(int msec) {
        if (mPlayer != null && mPlayingMusic != null) {
            mPlayer.seekTo(msec);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }


    public boolean isPause() {
        return isPause;
    }


    private void refresh() {
        Preferences.saveCurrentSongId(mPlayingPos);
        MusicLoader.updateQueue(this, mPlaylist);
    }

    /**
     * 获取正在播放歌曲的位置
     *
     * @return
     */
    public int getmPlayingPosition() {
        return mPlayingPos;
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void removeFromQueue(int position) {
        Log.e(TAG, position + "---" + mPlayingPos + "---" + mPlaylist.size());
        if (position == mPlayingPos) {
            mPlaylist.remove(position);
            playMusic(position);
        } else if (position > mPlayingPos) {
            mPlaylist.remove(position);
        } else if (position < mPlayingPos) {
            mPlaylist.remove(position);
            mPlayingPos = mPlayingPos - 1;
        }
        notifyChange(META_CHANGED);
        notifyChange(REFRESH);
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void clearQueue() {
        mPlayingMusic = null;
        mPlaylist.clear();
        mPlayer.stop();
        notifyChange(META_CHANGED);
        notifyChange(PLAYLIST_CLEAR);
    }

    /**
     * 获取正在播放时间
     */
    public int getCurrentPosition() {
        if (mPlayingMusic != null) {
            return mPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 获取时长
     */
    public int getDuration() {
        if (mPlayingMusic != null) {
            return mPlayer.getDuration();
        } else {
            return 0;
        }
    }


    private void notifyChange(final String what) {
        if (DEBUG) Log.d(TAG, "notifyChange: what = " + what);
//
//        if (what.equals(POSITION_CHANGED)) {
//            return;
//        }

        final Intent intent = new Intent(what);
        intent.putExtra("artist", getArtistName());
        intent.putExtra("album", getAlbumName());
        intent.putExtra("track", getTrackName());
        intent.putExtra("playing", isPlaying());

        sendBroadcast(intent);

    }


    /**
     * 构建Notification
     *
     * @return
     */
    private Notification buildNotification() {
        final String albumName = getAlbumName();
        final String artistName = getArtistName();
        final boolean isPlaying = isPlaying();
        String text = TextUtils.isEmpty(albumName)
                ? artistName : artistName + " - " + albumName;

        int playButtonResId = isPlaying
                ? R.drawable.ic_pause : R.drawable.ic_play_arrow_white_18dp;

        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap artwork;
        artwork = CoverLoader.getInstance().loadThumbnail(mPlayingMusic.getCoverUri());

        if (artwork == null) {
//            artwork = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.icon_album_default);
        }

        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.default_cover)
                .setLargeIcon(artwork)
                .setContentIntent(clickIntent)
                .setContentTitle(getTrackName())
                .setContentText(text)
                .setWhen(mNotificationPostTime)
                .addAction(R.drawable.ic_skip_previous,
                        "",
                        retrievePlaybackAction(ACTION_PREV))
                .addAction(playButtonResId, "",
                        retrievePlaybackAction(PLAY_STATE_CHANGED))
                .addAction(R.drawable.ic_skip_next,
                        "",
                        retrievePlaybackAction(ACTION_NEXT));

        if (SystemUtils.isJellyBeanMR1()) {
            builder.setShowWhen(false);
        }
//        if (SystemUtils.isLollipop()) {
//            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
//            NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle()
//                    .setMediaSession(mSession.getSessionToken())
//                    .setShowActionsInCompactView(0, 1, 2, 3);
//            builder.setStyle(style);
//        }

        if (artwork != null && SystemUtils.isLollipop()) {
            builder.setColor(Palette.from(artwork).generate().getMutedColor(Color.GRAY));
        }

        return builder.build();
    }

    private CharSequence getTrackName() {
        if (mPlayingMusic != null) {
            return mPlayingMusic.getTitle();
        }
        return null;
    }

    private String getArtistName() {
        if (mPlayingMusic != null) {
            return mPlayingMusic.getAlbum();
        }
        return null;
    }

    private String getAlbumName() {
        if (mPlayingMusic != null) {
            return mPlayingMusic.getArtist();
        }
        return null;
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private PendingIntent retrievePlaybackAction(final String action) {
        Intent intent = new Intent(action);
        return PendingIntent.getBroadcast(this, 0, intent, 0);
    }


    /**
     * 取消通知
     */
    private void cancelNotification() {
        if (nm != null) {
            stopForeground(true);
            nm.cancel(NOTIFICATION_ID);
        }
    }

    /**
     * 电话监听
     */
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
        int notificationId = hashCode();
        if (SystemUtils.isLollipop())
            startForeground(notificationId, buildNotification());
    }

    /**
     * Service broadcastReceiver
     */
    private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, intent.getAction().toString());
        }
    }

    /**
     * @param intent
     */
    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        if (ACTION_NEXT.equals(action)) {
            next();
        } else if (ACTION_PREV.equals(action)) {
            prev();
        } else if (PLAY_STATE_CHANGED.equals(action)) {
            playPause();
        }
    }

    @Override
    public void onDestroy() {
        //注销广播
        unregisterReceiver(mServiceReceiver);
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        Log.d("TAG", "ondestory");
        MusicLoader.updateQueue(this, mPlaylist);
        Preferences.saveCurrentSongId(mPlayingPos);
        cancelNotification();
        stopSelf();

        super.onDestroy();
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
            mPlayingPos = id;
            mService.get().playMusic(mPlayingPos);
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
        public void refresh() throws RemoteException {
            mService.get().refresh();
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
            return mPlayingMusic.getTitle();
        }

        @Override
        public String getSongArtist() throws RemoteException {
            return mPlayingMusic.getArtist();
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
        public List<Music> getPlayList() throws RemoteException {
            return mService.get().mPlaylist;
        }

        @Override
        public void removeFromQueue(int position) throws RemoteException {

            mService.get().removeFromQueue(position);
        }

        @Override
        public void clearQueue() throws RemoteException {
            mService.get().clearQueue();
        }

        @Override
        public int position() throws RemoteException {
            return mService.get().mPlayingPos;
        }

        @Override
        public int getDuration() throws RemoteException {
            return mService.get().getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return mService.get().getCurrentPosition();
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
