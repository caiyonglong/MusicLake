package com.cyl.musiclake.player;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.music.MusicApi;
import com.cyl.musiclake.api.net.ApiManager;
import com.cyl.musiclake.api.net.RequestCallBack;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.PlayHistoryLoader;
import com.cyl.musiclake.data.PlayQueueLoader;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.event.StatusChangedEvent;
import com.cyl.musiclake.player.playback.PlayProgressListener;
import com.cyl.musiclake.player.playqueue.PlayQueueManager;
import com.cyl.musiclake.ui.music.playpage.PlayerActivity;
import com.cyl.musiclake.ui.widget.appwidgets.StandardWidget;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.NetworkUtils;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 作者：yonglong on 2016/8/11 19:16
 * 邮箱：643872807@qq.com
 * 版本：2.5 播放service
 */
public class MusicPlayerService extends Service {
    private static final String TAG = "MusicPlayerService";

    public static final String ACTION_SERVICE = "com.cyl.music_lake.service";// 广播标志
    //    通知栏
    public static final String ACTION_NEXT = "com.cyl.music_lake.notify.next";// 下一首广播标志
    public static final String ACTION_PREV = "com.cyl.music_lake.notify.prev";// 上一首广播标志
    public static final String ACTION_PLAY_PAUSE = "com.cyl.music_lake.notify.play_state";// 播放暂停广播
    public static final String ACTION_CLOSE = "com.cyl.music_lake.notify.close";// 播放暂停广播
    public static final String ACTION_IS_WIDGET = "ACTION_IS_WIDGET";// 播放暂停广播

    public static final String ACTION_LYRIC = "com.cyl.music_lake.notify.lyric";// 播放暂停广播

    public static final String PLAY_STATE_CHANGED = "com.cyl.music_lake.play_state";// 播放暂停广播

    public static final String PLAY_STATE_LOADING_CHANGED = "com.cyl.music_lake.play_state_loading";// 播放loading

    public static final String DURATION_CHANGED = "com.cyl.music_lake.duration";// 播放时长

    public static final String TRACK_ERROR = "com.cyl.music_lake.error";
    public static final String SHUTDOWN = "com.cyl.music_lake.shutdown";
    public static final String REFRESH = "com.cyl.music_lake.refresh";

    public static final String PLAY_QUEUE_CLEAR = "com.cyl.music_lake.play_queue_clear"; //清空播放队列
    public static final String PLAY_QUEUE_CHANGE = "com.cyl.music_lake.play_queue_change"; //播放队列改变

    public static final String META_CHANGED = "com.cyl.music_lake.metachanged";//状态改变(歌曲替换)
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


    public static final int TRACK_WENT_TO_NEXT = 2; //下一首
    public static final int RELEASE_WAKELOCK = 3; //播放完成
    public static final int TRACK_PLAY_ENDED = 4; //播放完成
    public static final int TRACK_PLAY_ERROR = 5; //播放出错

    public static final int PREPARE_ASYNC_UPDATE = 7; //PrepareAsync装载进程
    public static final int PLAYER_PREPARED = 8; //mediaplayer准备完成

    public static final int AUDIO_FOCUS_CHANGE = 12; //音频焦点改变
    public static final int VOLUME_FADE_DOWN = 13; //音量改变减少
    public static final int VOLUME_FADE_UP = 14; //音量改变增加


    private final int NOTIFICATION_ID = 0x123;
    private long mNotificationPostTime = 0;
    private int mServiceStartId = -1;

    /**
     * 错误次数，超过最大错误次数，自动停止播放
     */
    private int playErrorTimes = 0;
    private int MAX_ERROR_TIMES = 1;

    private static final boolean DEBUG = true;

    private MusicPlayerEngine mPlayer = null;
    public PowerManager.WakeLock mWakeLock;
    private PowerManager powerManager;

    public Music mPlayingMusic = null;
    private List<Music> mPlayQueue = new ArrayList<>();
    private List<Integer> mHistoryPos = new ArrayList<>();
    private int mPlayingPos = -1;
    private int mNextPlayPos = -1;
    private String mPlaylistId = Constants.PLAYLIST_QUEUE_ID;

    //广播接收者
    ServiceReceiver mServiceReceiver;
    HeadsetReceiver mHeadsetReceiver;
    StandardWidget mStandardWidget;
    HeadsetPlugInReceiver mHeadsetPlugInReceiver;
    IntentFilter intentFilter;

    private FloatLyricViewManager mFloatLyricViewManager;

    private MediaSessionManager mediaSessionManager;
    private AudioAndFocusManager audioAndFocusManager;


    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private Notification mNotification;
    private IMusicServiceStub mBindStub = new IMusicServiceStub(this);
    private boolean isRunningForeground = false;
    private boolean isMusicPlaying = false;
    //暂时失去焦点，会再次回去音频焦点
    private boolean mPausedByTransientLossOfFocus = false;

    //播放缓存进度
    private int percent = 0;

    boolean mServiceInUse = false;
    //工作线程和Handler
    private MusicPlayerHandler mHandler;
    private HandlerThread mWorkThread;
    //主线程Handler
    private Handler mMainHandler;

    private boolean showLyric;

    private static MusicPlayerService instance;

    //歌词定时器
    private Timer lyricTimer;

    public static MusicPlayerService getInstance() {
        return instance;
    }

    private static List<PlayProgressListener> listenerList = new ArrayList<>();

    public static void addProgressListener(PlayProgressListener listener) {
        listenerList.add(listener);
    }

    public static void removeProgressListener(PlayProgressListener listener) {
        listenerList.remove(listener);
    }

    private Disposable disposable = Observable
            .interval(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(v -> {
                for (int i = 0; i < listenerList.size(); i++) {
                    listenerList.get(i).onProgressUpdate(getCurrentPosition(), getDuration());
                }
            });

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
                        service.mPlayer.setVolume(mCurrentVolume);
                        break;
                    case VOLUME_FADE_UP:
                        mCurrentVolume += 0.01f;
                        if (mCurrentVolume < 1.0f) {
                            sendEmptyMessageDelayed(VOLUME_FADE_UP, 10);
                        } else {
                            mCurrentVolume = 1.0f;
                        }
                        service.mPlayer.setVolume(mCurrentVolume);
                        break;
                    case TRACK_WENT_TO_NEXT: //mplayer播放完毕切换到下一首
//                        service.setAndRecordPlayPos(service.mNextPlayPos);
                        mMainHandler.post(() -> service.next(true));
//                        service.updateCursor(service.mPlayQueue.get(service.mPlayPos).mId);
//                        service.bumpSongCount(); //更新歌曲的播放次数
                        break;
                    case TRACK_PLAY_ENDED://mPlayer播放完毕且暂时没有下一首
                        if (PlayQueueManager.INSTANCE.getPlayModeId() == PlayQueueManager.PLAY_MODE_REPEAT) {
                            service.seekTo(0, false);
                            mMainHandler.post(service::play);
                        } else {
                            mMainHandler.post(() -> service.next(true));
                        }
                        break;
                    case TRACK_PLAY_ERROR://mPlayer播放错误
                        LogUtil.e(TAG, "TRACK_PLAY_ERROR " + msg.obj + "---");
                        ToastUtils.show("歌曲播放地址异常，请切换其他歌曲");
                        playErrorTimes++;
                        if (playErrorTimes < MAX_ERROR_TIMES) {
                            mMainHandler.post(() -> service.next(true));
                        } else {
                            mMainHandler.post(service::pause);
                        }
                        break;
                    case RELEASE_WAKELOCK://释放电源锁
                        service.mWakeLock.release();
                        break;
                    case PREPARE_ASYNC_UPDATE:
                        percent = (int) msg.obj;
                        LogUtil.e(TAG, "PREPARE_ASYNC_UPDATE Loading ... " + percent);
                        notifyChange(PLAY_STATE_LOADING_CHANGED);
                        break;
                    case PLAYER_PREPARED:
                        //执行prepared之后 准备完成，更新总时长
                        //准备完成，可以播放
                        LogUtil.e(TAG, "PLAYER_PREPARED");
                        isMusicPlaying = true;
                        updateNotification(false);
                        notifyChange(PLAY_STATE_CHANGED);
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
        //初始化通知
        initNotify();
        //初始化音乐播放服务
        initMediaPlayer();
    }

    /**
     * 参数配置，AudioManager、锁屏
     */
    @SuppressLint("InvalidWakeLockTag")
    private void initConfig() {

        //初始化主线程Handler
        mMainHandler = new Handler(Looper.getMainLooper());
        PlayQueueManager.INSTANCE.getPlayModeId();

        //初始化工作线程
        mWorkThread = new HandlerThread("MusicPlayerThread");
        mWorkThread.start();

        mHandler = new MusicPlayerHandler(this, mWorkThread.getLooper());

        //电源键
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PlayerWakelockTag");

        mFloatLyricViewManager = new FloatLyricViewManager(this);

        //初始化和设置MediaSessionCompat
        mediaSessionManager = new MediaSessionManager(mBindStub, this, mMainHandler);
        audioAndFocusManager = new AudioAndFocusManager(this, mHandler);
    }


    /**
     * 释放通知栏;
     */
    private void releaseServiceUiAndStop() {
        if (isPlaying() || mHandler.hasMessages(TRACK_PLAY_ENDED)) {
            return;
        }

        if (DEBUG) LogUtil.d(TAG, "Nothing is playing anymore, releasing notification");
        cancelNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionManager.release();

        if (!mServiceInUse) {
            savePlayQueue(false);
            stopSelf(mServiceStartId);
        }
    }

    /**
     * 重新加载当前进度
     */
    public void reloadPlayQueue() {
        mPlayQueue.clear();
        mHistoryPos.clear();
        mPlayQueue = PlayQueueLoader.INSTANCE.getPlayQueue();
        mPlayingPos = SPUtils.getPlayPosition();
        if (mPlayingPos >= 0 && mPlayingPos < mPlayQueue.size()) {
            mPlayingMusic = mPlayQueue.get(mPlayingPos);
            updateNotification(true);
            seekTo(SPUtils.getPosition(), true);
            notifyChange(META_CHANGED);
        }
        notifyChange(PLAY_QUEUE_CHANGE);
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
        mPlayer = new MusicPlayerEngine(this);
        mPlayer.setHandler(mHandler);
        reloadPlayQueue();
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
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
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
        return mBindStub;
    }

    private void setAndRecordPlayPos(int mNextPlayPos) {
        mPlayingPos = mNextPlayPos;
    }

    /**
     * 下一首
     */
    public void next(Boolean isAuto) {
        synchronized (this) {
            mPlayingPos = PlayQueueManager.INSTANCE.getNextPosition(isAuto, mPlayQueue.size(), mPlayingPos);
            LogUtil.e(TAG, "next: " + mPlayingPos);
            stop(false);
            playCurrentAndNext();
        }
    }

    /**
     * 上一首
     */
    public void prev() {
        synchronized (this) {
//            if (!NetworkUtils.isNetworkAvailable(this)) {
//                ToastUtils.show("网络不可用，请检查网络连接");
//                return;
//            }

            mPlayingPos = PlayQueueManager.INSTANCE.getPreviousPosition(mPlayQueue.size(), mPlayingPos);
            LogUtil.e(TAG, "prev: " + mPlayingPos);
            stop(false);
            playCurrentAndNext();
        }
    }

    /**
     * 播放当前歌曲
     */
    private void playCurrentAndNext() {
        synchronized (this) {
            if (mPlayingPos >= mPlayQueue.size() || mPlayingPos < 0) {
                return;
            }
            mPlayingMusic = mPlayQueue.get(mPlayingPos);
            //更新当前歌曲
            notifyChange(META_CHANGED);
            updateNotification(true);
            //更新播放播放状态
            isMusicPlaying = false;
            notifyChange(PLAY_STATE_CHANGED);
            updateNotification(false);

            LogUtil.e(TAG, "playingSongInfo:" + mPlayingMusic.toString());
            if (mPlayingMusic.getUri() == null || !Objects.equals(mPlayingMusic.getType(), Constants.LOCAL) || mPlayingMusic.getUri().equals("") || mPlayingMusic.getUri().equals("null")) {

                if (!NetworkUtils.isNetworkAvailable(this)) {
                    ToastUtils.show("网络不可用，请检查网络连接");
                } else {
                    ApiManager.request(MusicApi.INSTANCE.getMusicInfo(mPlayingMusic), new RequestCallBack<Music>() {
                        @Override
                        public void success(Music result) {
                            LogUtil.e(TAG, "-----" + result.toString());
                            mPlayingMusic = result;
                            saveHistory();
                            playErrorTimes = 0;
                            mPlayer.setDataSource(mPlayingMusic.getUri());
                        }

                        @Override
                        public void error(String msg) {
                            LogUtil.e(TAG, "播放异常-----" + msg);
                            checkPlayErrorTimes();
                        }
                    });
                }
            }
            saveHistory();
            mHistoryPos.add(mPlayingPos);
            if (mPlayingMusic.getUri() != null) {
                if (!mPlayingMusic.getUri().startsWith(Constants.IS_URL_HEADER) && !FileUtils.exists(mPlayingMusic.getUri())) {
                    checkPlayErrorTimes();
                } else {
                    playErrorTimes = 0;
                    mPlayer.setDataSource(mPlayingMusic.getUri());
                }
            }
            mediaSessionManager.updateMetaData(mPlayingMusic);
            audioAndFocusManager.requestAudioFocus();

            final Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
            sendBroadcast(intent);

            if (mPlayer.isInitialized()) {
                mHandler.removeMessages(VOLUME_FADE_DOWN);
                mHandler.sendEmptyMessage(VOLUME_FADE_UP); //组件调到正常音量
//                isMusicPlaying = true;
//                notifyChange(PLAY_STATE_CHANGED);
            }
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
        if (mPlayer != null && mPlayer.isInitialized()) {
            mPlayer.stop();
        }

        if (remove_status_icon) {
            cancelNotification();
        }

        if (remove_status_icon) {
            isMusicPlaying = false;
        }
    }

    /**
     * 获取下一首位置
     *
     * @return
     */
    private int getNextPosition(Boolean isAuto) {
        int playModeId = PlayQueueManager.INSTANCE.getPlayModeId();
        if (mPlayQueue == null || mPlayQueue.isEmpty()) {
            return -1;
        }
        if (mPlayQueue.size() == 1) {
            return 0;
        }
        if (playModeId == PlayQueueManager.PLAY_MODE_REPEAT && isAuto) {
            if (mPlayingPos < 0) {
                return 0;
            } else {
                return mPlayingPos;
            }
        } else if (playModeId == PlayQueueManager.PLAY_MODE_RANDOM) {
            return new Random().nextInt(mPlayQueue.size());
        } else {
            if (mPlayingPos == mPlayQueue.size() - 1) {
                return 0;
            } else if (mPlayingPos < mPlayQueue.size() - 1) {
                return mPlayingPos + 1;
            }
        }
        return mPlayingPos;
    }

    /**
     * 获取上一首位置
     *
     * @return
     */
    private int getPreviousPosition() {
        int playModeId = PlayQueueManager.INSTANCE.getPlayModeId();
        if (mPlayQueue == null || mPlayQueue.isEmpty()) {
            return -1;
        }
        if (mPlayQueue.size() == 1) {
            return 0;
        }
        if (playModeId == PlayQueueManager.PLAY_MODE_REPEAT) {
            if (mPlayingPos < 0) {
                return 0;
            }
        } else if (playModeId == PlayQueueManager.PLAY_MODE_RANDOM) {
            mPlayingPos = new Random().nextInt(mPlayQueue.size());
            return new Random().nextInt(mPlayQueue.size());
        } else {
            if (mPlayingPos == 0) {
                return mPlayQueue.size() - 1;
            } else if (mPlayingPos > 0) {
                return mPlayingPos - 1;
            }
        }
        return mPlayingPos;
    }

    /**
     * 根据位置播放音乐
     *
     * @param position
     */
    public void playMusic(int position) {
        if (position >= mPlayQueue.size() || position == -1) {
            mPlayingPos = PlayQueueManager.INSTANCE.getNextPosition(true, mPlayQueue.size(), position);
        } else {
            mPlayingPos = position;
        }
        if (mPlayingPos == -1)
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
            updateNotification(false);
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
     * @param music
     */
    public void play(Music music) {
        if (music == null) return;
        if (mPlayingPos == -1 || mPlayQueue.size() == 0) {
            mPlayQueue.add(music);
            mPlayingPos = 0;
        } else if (mPlayingPos < mPlayQueue.size()) {
            mPlayQueue.add(mPlayingPos, music);
        } else {
            mPlayQueue.add(mPlayQueue.size(), music);
        }
        //发送播放列表改变
        notifyChange(PLAY_QUEUE_CHANGE);
        LogUtil.e(TAG, music.toString());
        mPlayingMusic = music;
        playCurrentAndNext();
    }

    /**
     * 下一首播放
     *
     * @param music 设置的歌曲
     */
    public void nextPlay(Music music) {
        if (mPlayQueue.size() == 0) {
            play(music);
        } else if (mPlayingPos < mPlayQueue.size()) {
            mPlayQueue.add(mPlayingPos + 1, music);
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
     * @param musicList 歌单
     * @param id        歌曲位置id
     * @param pid       歌单id
     */
    public void play(List<Music> musicList, int id, String pid) {
        LogUtil.d(TAG, "musicList = " + musicList.size() + " id = " + id + " pid = " + pid + " mPlaylistId =" + mPlaylistId);
        if (musicList.size() <= id) return;
        if (mPlaylistId.equals(pid) && id == mPlayingPos) return;
        if (!mPlaylistId.equals(pid) || mPlayQueue.size() == 0 || mPlayQueue.size() != musicList.size()) {
            setPlayQueue(musicList);
            mPlaylistId = pid;
        }
        mPlayingPos = id;
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
        if (DEBUG) LogUtil.d(TAG, "Pausing playback");
        synchronized (this) {
            mHandler.removeMessages(VOLUME_FADE_UP);
            mHandler.sendEmptyMessage(VOLUME_FADE_DOWN);

            if (isPlaying()) {
                isMusicPlaying = false;
                notifyChange(PLAY_STATE_CHANGED);
                updateNotification(false);
                TimerTask task = new TimerTask() {
                    public void run() {
                        final Intent intent = new Intent(
                                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
                        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
                        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
                        sendBroadcast(intent); //由系统接收,通知系统audio_session将关闭,不再使用音效

                        mPlayer.pause();
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
        LogUtil.e(TAG, "seekTo " + pos);
        if (mPlayer != null && mPlayer.isInitialized() && mPlayingMusic != null) {
            mPlayer.seek(pos);
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
        savePlayQueue(false);

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
        if (full) {
            PlayQueueLoader.INSTANCE.updateQueue(mPlayQueue);
        }
        if (mPlayingMusic != null) {
            //保存歌曲id
            SPUtils.saveCurrentSongId(mPlayingMusic.getMid());
        }
        //保存歌曲id
        SPUtils.setPlayPosition(mPlayingPos);
        //保存歌曲进度
        SPUtils.savePosition(getCurrentPosition());

        LogUtil.e(TAG, "save 保存歌曲id=" + mPlayingPos + " 歌曲进度= " + getCurrentPosition());
    }


    private void saveHistory() {
        PlayHistoryLoader.INSTANCE.addSongToHistory(mPlayingMusic);
        savePlayQueue(false);
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void removeFromQueue(int position) {
        try {
            LogUtil.e(TAG, position + "---" + mPlayingPos + "---" + mPlayQueue.size());
            if (position == mPlayingPos) {
                mPlayQueue.remove(position);
                if (mPlayQueue.size() == 0) {
                    clearQueue();
                } else {
                    playMusic(position);
                }
            } else if (position > mPlayingPos) {
                mPlayQueue.remove(position);
            } else if (position < mPlayingPos) {
                mPlayQueue.remove(position);
                mPlayingPos = mPlayingPos - 1;
            }
            notifyChange(PLAY_QUEUE_CLEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void clearQueue() {
        mPlayingMusic = null;
        isMusicPlaying = false;
        mPlayingPos = -1;
        mPlayQueue.clear();
        mHistoryPos.clear();
        savePlayQueue(true);
        stop(true);
        notifyChange(META_CHANGED);
        notifyChange(PLAY_STATE_CHANGED);
        notifyChange(PLAY_QUEUE_CLEAR);
    }

    /**
     * 获取正在播放进度
     */
    public long getCurrentPosition() {
        if (mPlayer != null && mPlayer.isInitialized()) {
            return mPlayer.position();
        } else {
            return 0;
        }
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
        if (DEBUG) LogUtil.d(TAG, "notifyChange: what = " + what);
        switch (what) {
            case META_CHANGED:
                mFloatLyricViewManager.loadLyric(mPlayingMusic);
                updateWidget(META_CHANGED);
//                notifyChange(PLAY_STATE_CHANGED);
                EventBus.getDefault().post(new MetaChangedEvent(mPlayingMusic));
                break;
            case PLAY_STATE_CHANGED:
                updateWidget(ACTION_PLAY_PAUSE);
                mediaSessionManager.updatePlaybackState();
                EventBus.getDefault().post(new StatusChangedEvent(isPrepared(), isPlaying(), percent * getDuration()));
                break;
            case PLAY_QUEUE_CLEAR:
            case PLAY_QUEUE_CHANGE:
                EventBus.getDefault().post(new PlaylistEvent(Constants.PLAYLIST_QUEUE_ID, null));
                break;
            case PLAY_STATE_LOADING_CHANGED:
                //播放loading
                EventBus.getDefault().post(new StatusChangedEvent(isPrepared(), isPlaying(), percent * getDuration()));
                break;
        }
    }

    /**
     * 更新桌面小控件
     */
    private void updateWidget(String action) {
        Intent intent = new Intent(action);
        intent.putExtra(ACTION_IS_WIDGET, true);
        intent.putExtra(Extras.PLAY_STATUS, isPlaying());
        if (action.equals(META_CHANGED)) {
            intent.putExtra(Extras.SONG, mPlayingMusic);
        }
        sendBroadcast(intent);
    }

    /**
     * 获取标题
     *
     * @return
     */
    public String getTitle() {
        if (mPlayingMusic != null) {
            return mPlayingMusic.getTitle();
        }
        return null;
    }

    /**
     * 获取歌手专辑
     *
     * @return
     */
    public String getArtistName() {
        if (mPlayingMusic != null) {
            return mPlayingMusic.getArtist();
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
        if (mPlayingMusic != null) {
            return mPlayingMusic.getArtist();
        }
        return null;
    }

    /**
     * 获取当前音乐
     *
     * @return
     */
    public Music getPlayingMusic() {
        if (mPlayingMusic != null) {
            return mPlayingMusic;
        }
        return null;
    }


    /**
     * 设置播放队列
     *
     * @param playQueue 播放队列
     */
    public void setPlayQueue(List<Music> playQueue) {
        mPlayQueue.clear();
        mHistoryPos.clear();
        mPlayQueue.addAll(playQueue);
        notifyChange(PLAY_QUEUE_CHANGE);
        savePlayQueue(true);
    }


    /**
     * 获取播放队列
     *
     * @return 获取播放队列
     */
    public List<Music> getPlayQueue() {
        if (mPlayQueue.size() > 0) {
            return mPlayQueue;
        }
        return mPlayQueue;
    }


    /**
     * 获取当前音乐在播放队列中的位置
     *
     * @return 当前音乐在播放队列中的位置
     */
    public int getPlayPosition() {
        if (mPlayingPos >= 0) {
            return mPlayingPos;
        } else return 0;
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final String albumName = getAlbumName();
        final String artistName = getArtistName();
        String text = TextUtils.isEmpty(albumName)
                ? artistName : artistName + " - " + albumName;

        int playButtonResId = isMusicPlaying
                ? R.drawable.ic_pause : R.drawable.ic_play;
        String title = isMusicPlaying ? "暂停" : "播放";

        Intent nowPlayingIntent = new Intent(this, PlayerActivity.class);
        nowPlayingIntent.setAction(Constants.DEAULT_NOTIFICATION);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }
        mNotificationBuilder = new NotificationCompat.Builder(this, initChannelId())
                .setSmallIcon(R.drawable.ic_music)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(clickIntent)
                .setContentTitle(getTitle())
                .setContentText(text)
                .setWhen(mNotificationPostTime)
                .addAction(R.drawable.ic_skip_previous,
                        "上一首",
                        retrievePlaybackAction(ACTION_PREV))
                .addAction(playButtonResId, title,
                        retrievePlaybackAction(ACTION_PLAY_PAUSE))
                .addAction(R.drawable.ic_skip_next,
                        "下一首",
                        retrievePlaybackAction(ACTION_NEXT))
                .addAction(R.drawable.ic_lyric,
                        "歌词",
                        retrievePlaybackAction(ACTION_LYRIC))
                .addAction(R.drawable.ic_clear,
                        "关闭",
                        retrievePlaybackAction(ACTION_CLOSE))
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this, PlaybackStateCompat.ACTION_STOP));


        if (SystemUtils.isJellyBeanMR1()) {
            mNotificationBuilder.setShowWhen(false);
        }
        if (SystemUtils.isLollipop()) {
            //线控
            isRunningForeground = true;
            mNotificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionManager.getMediaSession())
                    .setShowActionsInCompactView(1, 0, 2, 3, 4);
            mNotificationBuilder.setStyle(style);
        }

        if (mPlayingMusic != null) {
            CoverLoader.INSTANCE.loadImageViewByMusic(this, mPlayingMusic, bitmap -> {
                mNotificationBuilder.setLargeIcon(bitmap);
                mNotification = mNotificationBuilder.build();
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                return null;
            });
        }
        mNotification = mNotificationBuilder.build();
    }


    /**
     * 创建Notification ChannelID
     *
     * @return 频道id
     */
    private String initChannelId() {
        // 通知渠道的id
        String id = "music_lake_01";
        // 用户可以看到的通知渠道的名字.
        CharSequence name = "音乐湖";
        // 用户可以看到的通知渠道的描述
        String description = "通知栏播放控制";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel;
            mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(false);
            mChannel.enableVibration(false);
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
        return id;
    }

    private PendingIntent retrievePlaybackAction(final String action) {
        Intent intent = new Intent(action);
        intent.setComponent(new ComponentName(this, MusicPlayerService.class));
        return PendingIntent.getService(this, 0, intent, 0);
    }

    public String getAudioId() {
        if (mPlayingMusic != null) {
            return mPlayingMusic.getMid();
        } else {
            return null;
        }
    }


    /**
     * 显示桌面歌词
     * 开个定时器定时刷新桌面歌词
     *
     * @param show
     */
    public void showDesktopLyric(boolean show) {
        if (show) {
            // 开启定时器，每隔0.5秒刷新一次
            if (lyricTimer == null) {
                lyricTimer = new Timer();
                lyricTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (isMusicPlaying) {
                            //正在播放时刷新
                            mFloatLyricViewManager.updateLyric(getCurrentPosition(), getDuration());
                        }
                    }
                }, 0, 1);
            }
        } else {
            if (lyricTimer != null) {
                lyricTimer.cancel();
                lyricTimer = null;
            }
            mFloatLyricViewManager.removeFloatLyricView(this);
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
     * 更新状态栏通知
     *
     * @param isChange 是否改变歌曲信息
     */
    private void updateNotification(boolean isChange) {
        if (isChange) {
            if (mPlayingMusic != null) {
                CoverLoader.INSTANCE.loadImageViewByMusic(this, mPlayingMusic, bitmap -> {
                    mNotificationBuilder.setLargeIcon(bitmap);
                    mNotification = mNotificationBuilder.build();
                    mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                    return null;
                });
            }
            mNotificationBuilder.setContentTitle(getTitle());
            mNotificationBuilder.setContentText(getArtistName());
            mNotificationBuilder.setTicker(getTitle() + "-" + getArtistName());
        }
        LogUtil.d(TAG, "播放状态 = " + isPlaying());
        if (isMusicPlaying)
            mNotificationBuilder.mActions.set(1, new NotificationCompat.Action(R.drawable.ic_pause, "",
                    retrievePlaybackAction(ACTION_PLAY_PAUSE)));
            //.getIconCompat() = R.drawable.ic_pause;
        else
            mNotificationBuilder.mActions.set(1, new NotificationCompat.Action(R.drawable.ic_play, "",
                    retrievePlaybackAction(ACTION_PLAY_PAUSE)));
        mNotification = mNotificationBuilder.build();
        mFloatLyricViewManager.updatePlayStatus(isMusicPlaying);

        if (mPlayingMusic != null) {
            startForeground(NOTIFICATION_ID, mNotification);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }

    /**
     * 取消通知
     */
    private void cancelNotification() {
        stopForeground(true);
        mNotificationManager.cancel(NOTIFICATION_ID);
        isRunningForeground = false;
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
//                Intent lockScreen = new Intent(MusicPlayerService.this, LockScreenPlayerActivity.class);
//                lockScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(lockScreen);
//            }

            if (!intent.getBooleanExtra(ACTION_IS_WIDGET, false)) {
                handleCommandIntent(intent);
            }
        }
    }


    /**
     * Intent处理
     *
     * @param intent
     */
    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        final String command = SERVICE_CMD.equals(action) ? intent.getStringExtra(CMD_NAME) : null;
        if (DEBUG)
            LogUtil.d(TAG, "handleCommandIntent: action = " + action + ", command = " + command);

        if (CMD_NEXT.equals(command) || ACTION_NEXT.equals(action)) {
            next(false);
        } else if (CMD_PREVIOUS.equals(command) || ACTION_PREV.equals(action)) {
            prev();
        } else if (CMD_TOGGLE_PAUSE.equals(command) || PLAY_STATE_CHANGED.equals(action)
                || ACTION_PLAY_PAUSE.equals(action)) {
            if (isPlaying()) {
                pause();
                mPausedByTransientLossOfFocus = false;
            } else {
                play();
            }
        } else if (UNLOCK_DESKTOP_LYRIC.equals(command)) {
            mFloatLyricViewManager.saveLock(false, true);
        } else if (CMD_PAUSE.equals(command)) {
            pause();
            mPausedByTransientLossOfFocus = false;
        } else if (CMD_PLAY.equals(command)) {
            play();
        } else if (CMD_STOP.equals(command)) {
            pause();
            mPausedByTransientLossOfFocus = false;
            seekTo(0, false);
            releaseServiceUiAndStop();
        } else if (ACTION_LYRIC.equals(action)) {
            startFloatLyric();
        } else if (ACTION_CLOSE.equals(action)) {
            stop(true);
            stopSelf();
            releaseServiceUiAndStop();
            System.exit(0);
        }
    }

    /**
     * 开启歌词
     */
    private void startFloatLyric() {
        if (SystemUtils.isOpenFloatWindow()) {
            showLyric = !showLyric;
            SPUtils.putAnyCommit(SPUtils.SP_KEY_FLOAT_LYRIC_LOCK, false);
            showDesktopLyric(showLyric);
        } else {
            SystemUtils.applySystemWindow();
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
        disposable.dispose();
        // Remove any sound effects
        final Intent audioEffectsIntent = new Intent(
                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(audioEffectsIntent);
        savePlayQueue(false);

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
        cancelNotification();

        //注销广播
        unregisterReceiver(mServiceReceiver);
        unregisterReceiver(mHeadsetReceiver);
        unregisterReceiver(mHeadsetPlugInReceiver);
        unregisterReceiver(mStandardWidget);

        if (mWakeLock.isHeld())
            mWakeLock.release();
    }

}
