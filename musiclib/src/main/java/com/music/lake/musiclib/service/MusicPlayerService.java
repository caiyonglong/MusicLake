package com.music.lake.musiclib.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.music.lake.musiclib.MusicPlayerManager;
import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.listener.MusicPlayEventListener;
import com.music.lake.musiclib.listener.MusicPlayerController;
import com.music.lake.musiclib.listener.MusicRequestCallBack;
import com.music.lake.musiclib.listener.MusicUrlRequest;
import com.music.lake.musiclib.manager.AudioAndFocusManager;
import com.music.lake.musiclib.manager.MediaSessionManager;
import com.music.lake.musiclib.manager.PlayListManager;
import com.music.lake.musiclib.notification.NotifyManager;
import com.music.lake.musiclib.playback.PlaybackListener;
import com.music.lake.musiclib.player.BasePlayer;
import com.music.lake.musiclib.player.MusicExoPlayer;
import com.music.lake.musiclib.player.MusicMediaPlayer;
import com.music.lake.musiclib.utils.CommonUtils;
import com.music.lake.musiclib.utils.Constants;
import com.music.lake.musiclib.utils.LogUtil;
import com.music.lake.musiclib.utils.SystemUtils;
import com.music.lake.musiclib.utils.ToastUtils;
import com.music.lake.musiclib.widgets.appwidgets.StandardWidget;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.music.lake.musiclib.notification.NotifyManager.ACTION_CLOSE;
import static com.music.lake.musiclib.notification.NotifyManager.ACTION_IS_WIDGET;
import static com.music.lake.musiclib.notification.NotifyManager.ACTION_LYRIC;
import static com.music.lake.musiclib.notification.NotifyManager.ACTION_MUSIC_NOTIFY;
import static com.music.lake.musiclib.notification.NotifyManager.ACTION_NEXT;
import static com.music.lake.musiclib.notification.NotifyManager.ACTION_PLAY_PAUSE;
import static com.music.lake.musiclib.notification.NotifyManager.ACTION_PREV;
import static com.music.lake.musiclib.notification.NotifyManager.ACTION_SHUFFLE;

/**
 * 作者：yonglong on 2020/2/29
 * 邮箱：643872807@qq.com
 * 版本：3.0 播放service
 */
public class MusicPlayerService extends Service implements MusicPlayerController, PlaybackListener {
    private static final String TAG = "MusicPlayerService";

    public static final String ACTION_SERVICE = "com.cyl.music_lake.service";// 广播标志

    public static final String PLAY_STATE_CHANGED = "com.cyl.music_lake.play_state";// 播放暂停广播

    public static final String DURATION_CHANGED = "com.cyl.music_lake.duration";// 播放时长

    public static final String TRACK_ERROR = "com.cyl.music_lake.error";
    public static final String SHUTDOWN = "com.cyl.music_lake.shutdown";
    public static final String REFRESH = "com.cyl.music_lake.refresh";

    public static final String PLAY_QUEUE_CLEAR = "com.cyl.music_lake.play_queue_clear"; //清空播放队列
    public static final String PLAY_QUEUE_CHANGE = "com.cyl.music_lake.play_queue_change"; //播放队列改变

    public static final String META_CHANGED = "com.cyl.music_lake.meta_changed";//状态改变(歌曲替换)
    public static final String SCHEDULE_CHANGED = "com.cyl.music_lake.schedule";//定时广播

    public static final String CMD_TOGGLE_PAUSE = "toggle_pause";//按键播放暂停
    public static final String CMD_NEXT = "next";//按键下一首
    public static final String CMD_PREVIOUS = "previous";//按键上一首
    public static final String CMD_PAUSE = "pause";//按键暂停
    public static final String CMD_PLAY = "play";//按键播放
    public static final String CMD_STOP = "stop";//按键停止
    public static final String CMD_FORWARD = "forward";//按键停止
    public static final String CMD_REWIND = "reward";//按键停止
    public static final String SERVICE_CMD = "cmd_service";//状态改变
    public static final String FROM_MEDIA_BUTTON = "media";//状态改变
    public static final String CMD_NAME = "name";//状态改变
    public static final String UNLOCK_DESKTOP_LYRIC = "unlock_lyric"; //音量改变增加

    public static final int AUDIO_FOCUS_CHANGE = 12; //音频焦点改变
    public static final int VOLUME_FADE_DOWN = 13; //音量改变减少
    public static final int VOLUME_FADE_UP = 14; //音量改变增加

    private int mServiceStartId = -1;

    /**
     * 错误次数，超过最大错误次数，自动停止播放
     */
    private int playErrorTimes = 0;
    private int MAX_ERROR_TIMES = 1;

    private BasePlayer mPlayer = null;
    public PowerManager.WakeLock mWakeLock;
    private PowerManager powerManager;
    private TimerTask mPlayerTask;
    private Timer mPlayerTimer;

    public BaseMusicInfo mNowPlayingMusic = null;
    private List<BaseMusicInfo> mPlaylist = new ArrayList<>();
    private List<Integer> mHistoryPos = new ArrayList<>();
    private int mNowPlayingIndex = -1;
    private int mNextPlayPos = -1;
    private String mPlaylistId = Constants.PLAYLIST_QUEUE_ID;

    //广播接收者
    ServiceReceiver mServiceReceiver;
    HeadsetReceiver mHeadsetReceiver;
    StandardWidget mStandardWidget;
    HeadsetPlugInReceiver mHeadsetPlugInReceiver;
    IntentFilter intentFilter;

    public Bitmap coverBitmap;

    private MediaSessionManager mediaSessionManager;
    private AudioAndFocusManager audioAndFocusManager;

    private NotifyManager notifyManager;

    private MusicServiceBinder mBindStub = new MusicServiceBinder(this);
    private boolean isRunningForeground = false;
    private boolean isMusicPlaying = false;
    //暂时失去焦点，会再次回去音频焦点
    private boolean mPausedByTransientLossOfFocus = false;

    //准备好直接播放
    private boolean playWhenReady = true;

    //播放缓存进度
    private int percent = 0;

    boolean mServiceInUse = false;
    //工作线程和Handler
    private MusicPlayerHandler mHandler;
    private HandlerThread mWorkThread;
    //主线程Handler
    private Handler mMainHandler;

    private static MusicPlayerService instance;

    private MusicUrlRequest musicUrlRequest;

    //歌词定时器
    private Timer lyricTimer;

    public static MusicPlayerService getInstance() {
        return instance;
    }

    private List<MusicPlayEventListener> playbackListeners = new ArrayList<>();

    @Override
    public void playMusicById(int index) {
        playMusic(index);
    }

    @Override
    public void playMusic(BaseMusicInfo song) {
        play(song);
    }

    @Override
    public void playMusic(List<BaseMusicInfo> songs, int index) {
        play(songs, index, "");
    }

    @Override
    public void updatePlaylist(List<BaseMusicInfo> songs, int index) {
        playWhenReady = false;
        updatePlaylist(songs, index, "");
    }

    @Override
    public void playNextMusic() {
        next(false);
    }

    @Override
    public void playPrevMusic() {
        prev();
    }

    @Override
    public void restorePlay() {
        play();
    }

    @Override
    public void pausePlay() {
        playPause();
    }

    @Override
    public void stopPlay() {
        stop(true);
    }

    @Override
    public void setLoopMode(int mode) {
        PlayListManager.INSTANCE.setLoopMode(mode);
    }

    @Override
    public int getLoopMode() {
        return PlayListManager.INSTANCE.getLoopMode();
    }

    @Override
    public void seekTo(long ms) {
        seekTo(ms, false);
    }

    @NotNull
    @Override
    public BaseMusicInfo getNowPlayingMusic() {
        return mNowPlayingMusic;
    }

    @Override
    public int getNowPlayingIndex() {
        return mNowPlayingIndex;
    }

    @NotNull
    @Override
    public List<BaseMusicInfo> getPlayList() {
        return mPlaylist;
    }

    @Override
    public void removeFromPlaylist(int position) {
        removeFromQueue(position);
    }

    @Override
    public void clearPlaylist() {
        clearQueue();
    }

    /**
     * 获取正在播放进度
     */
    @Override
    public long getPlayingPosition() {
        if (mPlayer != null && mPlayer.isInitialized()) {
            return mPlayer.position();
        } else {
            return 0;
        }
    }

    @Override
    public void addMusicPlayerEventListener(@NotNull MusicPlayEventListener listener) {
        playbackListeners.add(listener);
    }

    @Override
    public void removeMusicPlayerEventListener(@NotNull MusicPlayEventListener listener) {
        playbackListeners.remove(listener);
    }

    @Override
    public void showDesktopLyric(boolean show) {

    }

    @Override
    public int AudioSessionId() {
        if (mPlayer != null && mPlayer.isInitialized()) {
            return mPlayer.getAudioSessionId();
        }
        return -1;
    }

    @Override
    public void setMusicRequestListener(@NotNull MusicUrlRequest request) {
        LogUtil.d(TAG, "setMusicRequestListener " + request);
        musicUrlRequest = request;
    }


    @Override
    public void onCompletionNext() {
        next(true);
    }

    @Override
    public void onCompletionEnd() {
        if (PlayListManager.INSTANCE.getLoopMode() == PlayListManager.PLAY_MODE_REPEAT) {
            seekTo(0, false);
            play();
        } else {
            next(true);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.e(TAG, "PREPARE_ASYNC_UPDATE Loading ... " + percent);
        this.percent = percent;
    }

    @Override
    public void onPrepared() {
        LogUtil.e(TAG, "PLAYER_PREPARED");
        //执行prepared之后 准备完成，更新总时长
        //准备完成，可以播放
        isMusicPlaying = true;
        notifyManager.updateNotification(isMusicPlaying, false, null);
        notifyChange(PLAY_STATE_CHANGED);
    }

    @Override
    public void onError() {
        ToastUtils.show("歌曲播放地址异常，请切换其他歌曲");
        playErrorTimes++;
        next(true);
    }

    @Override
    public void onPlaybackProgress(long position, long duration, long buffering) {

    }

    @Override
    public void onLoading(boolean isLoading) {
        for (int i = 0; i < playbackListeners.size(); i++) {
            playbackListeners.get(i).onLoading(isLoading);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean isMusicPlaying) {
        this.isMusicPlaying = isMusicPlaying;
        if (!playWhenReady) {
            playWhenReady = true;
        }
        notifyChange(PLAY_STATE_CHANGED);
        notifyManager.updateNotification(isMusicPlaying, false, null);
        for (int i = 0; i < playbackListeners.size(); i++) {
            playbackListeners.get(i).onPlayerStateChanged(isMusicPlaying);
        }
    }

    public class MusicPlayerHandler extends Handler {
        private final WeakReference<MusicPlayerService> mService;
        private float mCurrentVolume = 1.0f;

        public MusicPlayerHandler(final MusicPlayerService service, final Looper looper) {
            super(looper);
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MusicPlayerService service = mService.get();
            synchronized (mService) {
                switch (msg.what) {
                    case VOLUME_FADE_DOWN:
                        mCurrentVolume -= 0.05f;
                        if (mCurrentVolume > 0.2f) {
                            sendEmptyMessageDelayed(VOLUME_FADE_DOWN, 10);
                        } else {
                            mCurrentVolume = 0.2f;
                        }
                        mMainHandler.post(() -> {
                            service.mPlayer.setVolume(mCurrentVolume);
                        });
                        break;
                    case VOLUME_FADE_UP:
                        mCurrentVolume += 0.01f;
                        if (mCurrentVolume < 1.0f) {
                            sendEmptyMessageDelayed(VOLUME_FADE_UP, 10);
                        } else {
                            mCurrentVolume = 1.0f;
                        }
                        mMainHandler.post(() -> {
                            service.mPlayer.setVolume(mCurrentVolume);
                        });
                        break;
                    case AUDIO_FOCUS_CHANGE:
                        switch (msg.arg1) {
                            case AudioManager.AUDIOFOCUS_LOSS://失去音频焦点
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://暂时失去焦点
                                if (service.isPlaying()) {
                                    mPausedByTransientLossOfFocus =
                                            msg.arg1 == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
                                }
                                mMainHandler.post(service::pause);
                                break;
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                                removeMessages(VOLUME_FADE_UP);
                                sendEmptyMessage(VOLUME_FADE_DOWN);
                                break;
                            case AudioManager.AUDIOFOCUS_GAIN://重新获取焦点
                                //重新获得焦点，且符合播放条件，开始播放
                                if (!service.isPlaying()
                                        && mPausedByTransientLossOfFocus) {
                                    mPausedByTransientLossOfFocus = false;
                                    mCurrentVolume = 0f;
                                    service.mPlayer.setVolume(mCurrentVolume);
                                    mMainHandler.post(service::play);
                                } else {
                                    removeMessages(VOLUME_FADE_DOWN);
                                    sendEmptyMessage(VOLUME_FADE_UP);
                                }
                                break;
                            default:
                        }
                        break;
                }
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e(TAG, "onCreate");
        instance = this;
        //初始化广播
        initReceiver();
        //初始化参数
        initConfig();
        //初始化电话监听服务
        initTelephony();
        //初始化音乐播放服务
        initMediaPlayer();
        //初始化通知
        initNotify();
    }

    /**
     * 参数配置，AudioManager、锁屏
     */
    @SuppressLint("InvalidWakeLockTag")
    private void initConfig() {

        //初始化主线程Handler
        mMainHandler = new Handler(Looper.getMainLooper());

        //初始化工作线程
        mWorkThread = new HandlerThread("MusicPlayerThread");
        mWorkThread.start();

        mHandler = new MusicPlayerHandler(this, mWorkThread.getLooper());

        //电源键
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PlayerWakelockTag");

        //初始化和设置MediaSessionCompat
        mediaSessionManager = new MediaSessionManager(mBindStub, this, mMainHandler);
        audioAndFocusManager = new AudioAndFocusManager(this, mHandler);
    }


    /**
     * 释放通知栏;
     */
    private void releaseServiceUiAndStop() {
        if (isPlaying()) {
            return;
        }

        LogUtil.d(TAG, "Nothing is playing anymore, releasing notification");

        notifyManager.close();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionManager.release();

        if (!mServiceInUse) {
//            savePlayQueue(false);
            stopSelf(mServiceStartId);
        }
    }

    /**
     * 重新加载当前进度
     */
//    public void reloadPlayQueue() {
//        mPlaylist.clear();
//        mHistoryPos.clear();
//        mPlaylist = PlayQueueLoader.INSTANCE.getPlayQueue();
//        mNowPlayingIndex = SPUtils.getPlayPosition();
//        if (mNowPlayingIndex >= 0 && mNowPlayingIndex < mPlaylist.size()) {
//            mPlayingMusic = mPlaylist.get(mNowPlayingIndex);
//            updateNotification(true);
//            seekTo(SPUtils.getPosition(), true);
//            notifyChange(META_CHANGED);
//        }
//        notifyChange(PLAY_QUEUE_CHANGE);
//    }

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
        if (MusicPlayerManager.getInstance().useExoPlayer) {
            mPlayer = new MusicExoPlayer(this);
        } else {
            mPlayer = new MusicMediaPlayer(this);
        }
        mPlayer.setPlayBackListener(this);
        mPlayerTask = new TimerTask() {
            public void run() {
                mMainHandler.post(() -> {
                    for (int i = 0; i < playbackListeners.size(); i++) {
                        playbackListeners.get(i).onPlaybackProgress(getPlayingPosition(), getDuration(), getBufferedPercentage());
                    }
                });
            }
        };
        mPlayerTimer = new Timer();
        mPlayerTimer.schedule(mPlayerTask, 0, 400);
    }

    /**
     * 初始化广播
     */
    private void initReceiver() {
        //实例化过滤器，设置广播
        intentFilter = new IntentFilter(ACTION_SERVICE);
        mServiceReceiver = new ServiceReceiver();
        mHeadsetReceiver = new HeadsetReceiver();
        mStandardWidget = new StandardWidget();
        mHeadsetPlugInReceiver = new HeadsetPlugInReceiver();
        intentFilter.addAction(ACTION_MUSIC_NOTIFY);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        intentFilter.addAction(META_CHANGED);
        intentFilter.addAction(SHUTDOWN);
        intentFilter.addAction(ACTION_PLAY_PAUSE);
        //注册广播
        registerReceiver(mServiceReceiver, intentFilter);
        registerReceiver(mHeadsetReceiver, intentFilter);
        registerReceiver(mHeadsetPlugInReceiver, intentFilter);
        registerReceiver(mStandardWidget, intentFilter);
    }

    /**
     * 启动Service服务，执行onStartCommand
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "Got new intent " + intent + ", startId = " + startId);
        mServiceStartId = startId;
        mServiceInUse = true;
        if (intent != null) {
            final String action = intent.getAction();
            if (SHUTDOWN.equals(action)) {
                LogUtil.e("即将关闭音乐播放器");
//                mShutdownScheduled = true;
                releaseServiceUiAndStop();
                return START_NOT_STICKY;
            }
            handleCommandIntent(intent);
        }
        return START_NOT_STICKY;
    }

    /**
     * 绑定Service
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (null == mBindStub) {
            mBindStub = new MusicServiceBinder(this);
        }
        return mBindStub;
    }

    /**
     * 下一首
     */
    public void next(Boolean isAuto) {
        synchronized (this) {
            mNowPlayingIndex = PlayListManager.INSTANCE.getNextPosition(isAuto, mPlaylist.size(), mNowPlayingIndex);
            LogUtil.e(TAG, "next: " + mNowPlayingIndex);
            stop(false);
            playCurrentAndNext();
        }
    }

    /**
     * 上一首
     */
    public void prev() {
        synchronized (this) {
            mNowPlayingIndex = PlayListManager.INSTANCE.getPreviousPosition(mPlaylist.size(), mNowPlayingIndex);
            LogUtil.e(TAG, "prev: " + mNowPlayingIndex);
            stop(false);
            playCurrentAndNext();
        }
    }

    /**
     * 播放当前歌曲
     */
    private void playCurrentAndNext() {
        synchronized (this) {
            LogUtil.e(TAG, "playCurrentAndNext: " + mNowPlayingIndex + "-" + mPlaylist.size());
            if (mNowPlayingIndex >= mPlaylist.size() || mNowPlayingIndex < 0) {
                return;
            }
            mNowPlayingMusic = mPlaylist.get(mNowPlayingIndex);
            mPlayer.setMusicInfo(mNowPlayingMusic);
            //更新当前歌曲
            isMusicPlaying = false;
            //检查歌曲播放地址或者专辑
            checkPlayOnValid();
            notifyChange(META_CHANGED);
            //更新播放播放状态
            notifyChange(PLAY_STATE_CHANGED);

            mHistoryPos.add(mNowPlayingIndex);
            mediaSessionManager.updateMetaData(mNowPlayingMusic);
            audioAndFocusManager.requestAudioFocus();

            final Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
            sendBroadcast(intent);

            if (mPlayer.isInitialized()) {
                mHandler.removeMessages(VOLUME_FADE_DOWN);
                mHandler.sendEmptyMessage(VOLUME_FADE_UP); //组件调到正常音量
            }
        }
    }

    private void checkPlayOnValid() {
        if (musicUrlRequest != null) {
            musicUrlRequest.checkNonValid(mNowPlayingMusic, new MusicRequestCallBack() {
                @Override
                public void onMusicBitmap(@NotNull Bitmap bitmap) {
                    coverBitmap = bitmap;
                    notifyManager.updateNotification(isMusicPlaying, true, bitmap);
                }

                @Override
                public void onMusicValid(@NotNull String url) {
                    LogUtil.e(TAG, "checkNonValid-----" + url);
                    mNowPlayingMusic.setUri(url);
                    playErrorTimes = 0;
                    mPlayer.playWhenReady = playWhenReady;
                    mPlayer.setDataSource(url);
                }

                @Override
                public void onActionDirect() {
                    playErrorTimes = 0;
                    mPlayer.playWhenReady = playWhenReady;
                    mPlayer.setDataSource(mNowPlayingMusic.getUri());
                }
            });
        }
    }

    /**
     * 异常播放，自动切换下一首
     */
    private void checkPlayErrorTimes() {
        if (playErrorTimes > MAX_ERROR_TIMES) {
            pause();
        } else {
            playErrorTimes++;
            ToastUtils.show("播放地址异常，自动切换下一首");
            next(false);
        }
    }

    /**
     * 停止播放
     *
     * @param remove_status_icon
     */
    public void stop(boolean remove_status_icon) {
        if (remove_status_icon && mPlayer != null && mPlayer.isInitialized()) {
            mPlayer.stop();
        }

        if (remove_status_icon) {
            notifyManager.close();
        }

        if (remove_status_icon) {
            isMusicPlaying = false;
        }
    }

    /**
     * 根据位置播放音乐
     *
     * @param position
     */
    public void playMusic(int position) {
        if (position >= mPlaylist.size() || position == -1) {
            mNowPlayingIndex = PlayListManager.INSTANCE.getNextPosition(true, mPlaylist.size(), position);
        } else {
            mNowPlayingIndex = position;
        }
        if (mNowPlayingIndex == -1)
            return;
        playCurrentAndNext();
    }

    /**
     * 音乐播放
     */
    public void play() {
        if (mPlayer.isInitialized()) {
            mPlayer.start();
            isMusicPlaying = true;
            notifyChange(PLAY_STATE_CHANGED);
            audioAndFocusManager.requestAudioFocus();
            mHandler.removeMessages(VOLUME_FADE_DOWN);
            mHandler.sendEmptyMessage(VOLUME_FADE_UP); //组件调到正常音量
            notifyManager.updateNotification(isMusicPlaying, false, null);
        } else {
            playCurrentAndNext();
        }
    }

    public int getAudioSessionId() {
        synchronized (this) {
            return mPlayer.getAudioSessionId();
        }
    }

    /**
     * 【在线音乐，搜索的音乐】加入播放队列并播放音乐
     *
     * @param baseMusicInfo
     */
    public void play(BaseMusicInfo baseMusicInfo) {
        if (baseMusicInfo == null) return;
        if (mNowPlayingIndex == -1 || mPlaylist.size() == 0) {
            mPlaylist.add(baseMusicInfo);
            mNowPlayingIndex = 0;
        } else if (mNowPlayingIndex < mPlaylist.size()) {
            mPlaylist.add(mNowPlayingIndex, baseMusicInfo);
        } else {
            mPlaylist.add(mPlaylist.size(), baseMusicInfo);
        }
        //发送播放列表改变
        notifyChange(PLAY_QUEUE_CHANGE);
        LogUtil.e(TAG, baseMusicInfo.toString());
        mNowPlayingMusic = baseMusicInfo;
        playCurrentAndNext();
    }

    /**
     * 下一首播放
     *
     * @param baseMusicInfo 设置的歌曲
     */
    public void nextPlay(BaseMusicInfo baseMusicInfo) {
        if (mPlaylist.size() == 0) {
            play(baseMusicInfo);
        } else if (mNowPlayingIndex < mPlaylist.size()) {
            mPlaylist.add(mNowPlayingIndex + 1, baseMusicInfo);
            //发送播放列表改变
            notifyChange(PLAY_QUEUE_CHANGE);
        }
    }

    /**
     * 切换歌单播放
     * 1、歌单不一样切换，不一样不切换
     * 2、相同歌单只切换歌曲
     * 3、相同歌曲不重新播放
     *
     * @param baseMusicInfoList 歌单
     * @param id                歌曲位置id
     * @param pid               歌单id
     */
    public void play(List<BaseMusicInfo> baseMusicInfoList, int id, String pid) {
        LogUtil.d(TAG, "musicList = " + baseMusicInfoList.size() + " id = " + id + " pid = " + pid + " mPlaylistId =" + mPlaylistId);
        if (baseMusicInfoList.size() <= id) return;

        if (mPlaylistId.equals(pid) && id == mNowPlayingIndex) return;

        setPlayQueue(baseMusicInfoList);

        mNowPlayingIndex = id;

        playCurrentAndNext();
    }

    private void updatePlaylist(List<BaseMusicInfo> baseMusicInfoList, int id, String pid) {
        LogUtil.d(TAG, "musicList = " + baseMusicInfoList.size() + " id = " + id + " pid = " + pid + " mPlaylistId =" + mPlaylistId);
        if (baseMusicInfoList.size() <= id) return;
        if (mPlaylistId.equals(pid) && id == mNowPlayingIndex) return;
        setPlayQueue(baseMusicInfoList);

        mNowPlayingIndex = id;

        if (mNowPlayingIndex < mPlaylist.size()) {
            mNowPlayingMusic = mPlaylist.get(mNowPlayingIndex);
        }
        playCurrentAndNext();
    }


    /**
     * 播放暂停
     */
    public void playPause() {
        if (isPlaying()) {
            pause();
        } else {
            if (mPlayer.isInitialized()) {
                play();
            } else {
                playCurrentAndNext();
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        LogUtil.d(TAG, "Pausing playback");
        mPausedByTransientLossOfFocus = false;
        synchronized (this) {
            mHandler.removeMessages(VOLUME_FADE_UP);
            mHandler.sendEmptyMessage(VOLUME_FADE_DOWN);

            if (isPlaying()) {
                isMusicPlaying = false;
                notifyChange(PLAY_STATE_CHANGED);
                notifyManager.updateNotification(isMusicPlaying, false, null);
                TimerTask task = new TimerTask() {
                    public void run() {
                        LogUtil.d(TAG, "TimerTask ");
                        final Intent intent = new Intent(
                                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
                        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
                        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
                        sendBroadcast(intent); //由系统接收,通知系统audio_session将关闭,不再使用音效

                        mMainHandler.post(() -> mPlayer.pause());
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 200);
            }
        }
    }

    /**
     * 是否正在播放音乐
     *
     * @return 是否正在播放音乐
     */
    public boolean isPlaying() {
        return isMusicPlaying;
    }

    /**
     * 跳到输入的进度
     */
    public void seekTo(long pos, boolean isInit) {
        LogUtil.e(TAG, "seekTo " + pos * getDuration() / 100);
        if (mPlayer != null && mPlayer.isInitialized() && mNowPlayingMusic != null) {
            mPlayer.seekTo(pos * getDuration() / 100);
            LogUtil.e(TAG, "seekTo 成功");
        } else if (isInit) {
//            playCurrentAndNext();
//            mPlayer.seek(pos);
//            mPlayer.pause();
            LogUtil.e(TAG, "seekTo 失败");
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e(TAG, "onUnbind");
        mServiceInUse = false;
//        savePlayQueue(false);
        releaseServiceUiAndStop();
        stopSelf(mServiceStartId);
        return true;
    }

    /**
     * 保存播放队列
     *
     * @param full 是否存储
     */
    private void savePlayQueue(boolean full) {
        if (mNowPlayingMusic != null) {
            //保存歌曲id
            CommonUtils.saveCurrentSongId(mNowPlayingMusic.getMid());
        }
        //保存歌曲id
        CommonUtils.setPlayPosition(mNowPlayingIndex);

        LogUtil.e(TAG, "save 保存歌曲位置=" + mNowPlayingIndex);
    }

    /**
     * 从歌单移除歌曲
     */
    public void removeFromQueue(int position) {
        try {
            LogUtil.e(TAG, position + "---" + mNowPlayingIndex + "---" + mPlaylist.size());
            if (position == mNowPlayingIndex) {
                mPlaylist.remove(position);
                if (mPlaylist.size() == 0) {
                    clearQueue();
                } else {
                    playMusic(position);
                }
            } else if (position > mNowPlayingIndex) {
                mPlaylist.remove(position);
            } else {
                mPlaylist.remove(position);
                LogUtil.e(TAG, position + "--remove-" + mNowPlayingIndex + "---" + mPlaylist.size());
                mNowPlayingIndex = mNowPlayingIndex - 1;
                LogUtil.e(TAG, position + "--remove-" + mNowPlayingIndex + "---" + mPlaylist.size());
            }
            notifyChange(PLAY_QUEUE_CLEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, position + "---" + mNowPlayingIndex + "---" + mPlaylist.size());
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void clearQueue() {
        mNowPlayingMusic = null;
        isMusicPlaying = false;
        mNowPlayingIndex = -1;
        mPlaylist.clear();
        mHistoryPos.clear();
//        savePlayQueue(true);
        stop(true);
        notifyChange(META_CHANGED);
        notifyChange(PLAY_STATE_CHANGED);
        notifyChange(PLAY_QUEUE_CLEAR);
    }


    /**
     * 获取总时长
     */
    public long getDuration() {
        if (mPlayer != null && mPlayer.isInitialized() && mPlayer.isPrepared()) {
            return mPlayer.duration();
        }
        return 0;
    }

    /**
     * 获取缓冲时长 0-100
     */
    public int getBufferedPercentage() {
        if (mPlayer != null && mPlayer.isInitialized() && mPlayer.isPrepared()) {
            return mPlayer.bufferedPercentage();
        }
        return 0;
    }

    /**
     * 是否准备播放
     *
     * @return
     */
    public boolean isPrepared() {
        if (mPlayer != null) {
            return mPlayer.isPrepared();
        }
        return false;
    }

    /**
     * 发送更新广播
     *
     * @param what 发送更新广播
     */
    private void notifyChange(final String what) {
        LogUtil.d(TAG, "notifyChange: what = " + what);
        switch (what) {
            case META_CHANGED:
                updateWidget(META_CHANGED);
                break;
            case PLAY_STATE_CHANGED:
                updateWidget(ACTION_PLAY_PAUSE);
                mediaSessionManager.updatePlaybackState();
                break;
            case PLAY_QUEUE_CLEAR:
            case PLAY_QUEUE_CHANGE:
                updateWidget(PLAY_QUEUE_CHANGE);
                for (int i = 0; i < playbackListeners.size(); i++) {
                    playbackListeners.get(i).onUpdatePlayList(mPlaylist);
                }
                break;
        }
    }

    /**
     * 更新桌面小控件
     */
    private void updateWidget(String action) {
        Intent intent = new Intent(action);
        intent.putExtra(ACTION_IS_WIDGET, true);
        intent.putExtra(PLAY_STATE_CHANGED, isPlaying());
        intent.putExtra(PlayListManager.PLAY_MODE, getLoopMode());
        sendBroadcast(intent);
    }

    /**
     * 获取标题
     *
     * @return
     */
    public String getTitle() {
        if (mNowPlayingMusic != null) {
            return mNowPlayingMusic.getTitle();
        }
        return null;
    }

    /**
     * 获取歌手专辑
     *
     * @return
     */
    public String getArtistName() {
        if (mNowPlayingMusic != null) {
            return mNowPlayingMusic.getArtist();
//            return ConvertUtils.getArtistAndAlbum(mPlayingMusic.getArtist(), mPlayingMusic.getAlbum());
        }
        return null;
    }

    /**
     * 获取专辑名
     *
     * @return
     */
    private String getAlbumName() {
        if (mNowPlayingMusic != null) {
            return mNowPlayingMusic.getArtist();
        }
        return null;
    }

    /**
     * 获取当前音乐
     *
     * @return
     */
    public BaseMusicInfo getPlayingMusic() {
        if (mNowPlayingMusic != null) {
            return mNowPlayingMusic;
        }
        return null;
    }


    /**
     * 设置播放队列
     *
     * @param playQueue 播放队列
     */
    public void setPlayQueue(List<BaseMusicInfo> playQueue) {
        mPlaylist.clear();
        mHistoryPos.clear();
        mPlaylist.addAll(playQueue);
        notifyChange(PLAY_QUEUE_CHANGE);
//        savePlayQueue(true);
    }


    /**
     * 获取播放队列
     *
     * @return 获取播放队列
     */
    public List<BaseMusicInfo> getPlayQueue() {
        if (mPlaylist.size() > 0) {
            return mPlaylist;
        }
        return mPlaylist;
    }


    /**
     * 获取当前音乐在播放队列中的位置
     *
     * @return 当前音乐在播放队列中的位置
     */
    public int getPlayPosition() {
        if (mNowPlayingIndex >= 0) {
            return mNowPlayingIndex;
        } else return 0;
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        notifyManager = new NotifyManager(this);
        notifyManager.setBasePlayerImpl(mPlayer);
        if (SystemUtils.isJellyBeanMR1()) {
            notifyManager.setShowWhen(false);
        }
        if (SystemUtils.isLollipop()) {
            //线控
            isRunningForeground = true;
            androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionManager.getMediaSession())
                    .setShowActionsInCompactView(1, 0, 2, 3, 4);
            notifyManager.setStyle(style);
        }
        notifyManager.setupNotification();
    }

    public String getAudioId() {
        if (mNowPlayingMusic != null) {
            return mNowPlayingMusic.getMid();
        } else {
            return null;
        }
    }


    /**
     * 电话监听
     */
    private class ServicePhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            LogUtil.d(TAG, "TelephonyManager state=" + state + ",incomingNumber = " + incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:   //接听状态
                case TelephonyManager.CALL_STATE_RINGING:   //响铃状态
                    pause();
                    break;
            }
        }
    }


    /**
     * Service broadcastReceiver 监听service中广播
     */
    private class ServiceReceiver extends BroadcastReceiver {

//        public ServiceReceiver() {
//            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        }

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d(TAG, intent.getAction());
//            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//                LogUtil.e(TAG, "屏幕熄灭进入锁屏界面");
//            }
            if (!intent.getBooleanExtra(ACTION_IS_WIDGET, false)) {
                handleCommandIntent(intent);
            }
        }
    }


    /**
     * 处理各种广播
     */
    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        final String command = SERVICE_CMD.equals(action) ? intent.getStringExtra(CMD_NAME) : action;
        LogUtil.d(TAG, "handleCommandIntent: action = " + action + ", command = " + command);
        if (PLAY_STATE_CHANGED.equals(action)) {
            pausePlay();
            return;
        }
        if (command == null) return;
        switch (command) {
            case ACTION_MUSIC_NOTIFY:
                updateWidget(ACTION_MUSIC_NOTIFY);
                break;
            case ACTION_LYRIC:
                updateWidget(ACTION_LYRIC);
                break;
            case CMD_NEXT:
            case ACTION_NEXT:
                next(false);
                break;
            case CMD_PREVIOUS:
            case ACTION_PREV:
                prev();
                break;
            case CMD_TOGGLE_PAUSE:
            case ACTION_PLAY_PAUSE:
                pausePlay();
                break;
            case ACTION_CLOSE:
                stop(true);
                stopSelf();
                releaseServiceUiAndStop();
                System.exit(0);
                break;
            case CMD_PAUSE:
                pause();
                break;
            case ACTION_SHUFFLE:
                PlayListManager.INSTANCE.updateLoopMode();
                notifyChange(PLAY_STATE_CHANGED);
                break;
            case CMD_PLAY:
                play();
                break;
            case CMD_STOP:
                pause();
                mPausedByTransientLossOfFocus = false;
                seekTo(0, false);
                releaseServiceUiAndStop();
                break;
            case UNLOCK_DESKTOP_LYRIC:
                break;
            default:
                break;
        }
    }

    /**
     * 耳机插入广播接收器
     */
    public class HeadsetPlugInReceiver extends BroadcastReceiver {
        public HeadsetPlugInReceiver() {
            if (Build.VERSION.SDK_INT >= 21) {
                intentFilter.addAction(AudioManager.ACTION_HEADSET_PLUG);
            } else {
                intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("state")) {
                //通过判断 "state" 来知道状态
                final boolean isPlugIn = intent.getExtras().getInt("state") == 1;
                LogUtil.e(TAG, "耳机插入状态 ：" + isPlugIn);
            }
        }
    }

    /**
     * 耳机拔出广播接收器
     */
    private class HeadsetReceiver extends BroadcastReceiver {

        final BluetoothAdapter bluetoothAdapter;

        public HeadsetReceiver() {
            intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY); //有线耳机拔出变化
            intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED); //蓝牙耳机连接变化

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isRunningForeground) {
                //当前是正在运行的时候才能通过媒体按键来操作音频
                switch (intent.getAction()) {
                    case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED:
                        LogUtil.e("蓝牙耳机插拔状态改变");
                        if (bluetoothAdapter != null &&
                                BluetoothProfile.STATE_DISCONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) &&
                                isPlaying()) {
                            //蓝牙耳机断开连接 同时当前音乐正在播放 则将其暂停
                            pause();
                        }
                        break;
                    case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                        LogUtil.e("有线耳机插拔状态改变");
                        if (isPlaying()) {
                            //有线耳机断开连接 同时当前音乐正在播放 则将其暂停
                            pause();
                        }
                        break;

                }
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(TAG, "onDestroy");
//        disposable.dispose();
        // Remove any sound effects
        final Intent audioEffectsIntent = new Intent(
                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(audioEffectsIntent);
//        savePlayQueue(false);

        coverBitmap = null;
        //释放mPlayer
        if (mPlayer != null) {
            mPlayer.stop();
            isMusicPlaying = false;
            mPlayer.release();
            mPlayer = null;
        }

        // 释放Handler资源
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        // 释放工作线程资源
        if (mWorkThread != null && mWorkThread.isAlive()) {
            mWorkThread.quitSafely();
            mWorkThread.interrupt();
            mWorkThread = null;
        }

        audioAndFocusManager.abandonAudioFocus();
        notifyManager.close();

        //注销广播
        unregisterReceiver(mServiceReceiver);
        unregisterReceiver(mHeadsetReceiver);
        unregisterReceiver(mHeadsetPlugInReceiver);
        unregisterReceiver(mStandardWidget);

        if (mWakeLock.isHeld())
            mWakeLock.release();
    }
}
