package com.cyl.musiclake.service;

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
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.media.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.data.source.PlayHistoryLoader;
import com.cyl.musiclake.data.source.PlayQueueLoader;
import com.cyl.musiclake.ui.main.MainActivity;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.app.NotificationCompat.Builder;

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
    public static final String ACTION_TOGGLE_PAUSE = "com.cyl.music_lake.notify.play_state";// 播放暂停广播

    public static final String ACTION_LYRIC = "com.cyl.music_lake.notify.lyric";// 播放暂停广播

    public static final String PLAY_STATE_CHANGED = "com.cyl.music_lake.play_state";// 播放暂停广播
    public static final String DURATION_CHANGED = "com.cyl.music_lake.duration";// 播放时长

    public static final String PLAYLIST_CHANGED = "com.cyl.music_lake.playlist_change";
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

    public static final int TRACK_WENT_TO_NEXT = 2; //下一首
    public static final int RELEASE_WAKELOCK = 3; //播放完成
    public static final int TRACK_PLAY_ENDED = 4; //播放完成
    public static final int TRACK_PLAY_ERROR = 5; //播放出错

    public static final int PREPARE_ASYNC_UPDATE = 7; //PrepareAsync装载进程
    public static final int PLAYER_PREPARED = 8; //mediaplayer准备完成
    public static final int PREPARE_QQ_MUSIC = 9; //获取QQ音乐播放状态

    public static final int AUDIO_FOCUS_CHANGE = 12; //音频焦点改变
    public static final int VOLUME_FADE_DOWN = 13; //音频焦点改变
    public static final int VOLUME_FADE_UP = 14; //音频焦点改变

    public static final int SCHEDULE_TASK = 15; //定时关闭改变

    private final int NOTIFICATION_ID = 0x123;
    private long mNotificationPostTime = 0;
    private int mServiceStartId = -1;

    private static final boolean DEBUG = true;

    private MusicPlayerEngine mPlayer = null;
    public PowerManager.WakeLock mWakeLock;
    private PowerManager powerManager;


    public Music mPlayingMusic = null;
    private List<Music> mPlaylist = new ArrayList<>();
    private List<Integer> mHistoryPos = new ArrayList<>();
    private int mPlayingPos = -1;
    private int mNextPlayPos = -1;

    //播放模式：0顺序播放、1随机播放、2单曲循环
    private int mRepeatMode = 0;
    private final int PLAY_MODE_LOOP = 0;
    private final int PLAY_MODE_RANDOM = 1;
    private final int PLAY_MODE_REPEAT = 2;

    //广播接收者
    ServiceReceiver mServiceReceiver;
    HeadsetReceiver mHeadsetReceiver;
    HeadsetPlugInReceiver mHeadsetPlugInReceiver;

    private FloatLyricViewManager mFloatLyricViewManager;

    private MediaSessionManager mediaSessionManager;
    private AudioAndFocusManager audioAndFocusManager;


    private NotificationManager mNotificationManager;
    private Builder mNotificationBuilder;
    private Notification mNotification;
    private IMusicServiceStub mBindStub = new IMusicServiceStub(this);
    private boolean isRunningForeground = false;
    private boolean isMusicPlaying = false;
    //暂时失去焦点，会再次回去音频焦点
    private boolean mPausedByTransientLossOfFocus = false;

    public static boolean mShutdownScheduled = false;
    public static int totalTime = 0;

    boolean mServiceInUse = false;
    //工作线程和Handler
    private MusicPlayerHandler mHandler;
    private HandlerThread mWorkThread;
    //主线程Handler
    private Handler mMainHandler;

    private boolean showLyric;


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
                        service.setAndRecordPlayPos(service.mNextPlayPos);
                        mMainHandler.post(service::next);
//                        service.updateCursor(service.mPlaylist.get(service.mPlayPos).mId);
//                        service.bumpSongCount(); //更新歌曲的播放次数
                        break;
                    case TRACK_PLAY_ENDED://mPlayer播放完毕且暂时没有下一首
                        if (service.mRepeatMode == PLAY_MODE_REPEAT) {
                            service.seekTo(0);
                            mMainHandler.post(service::play);
                        } else {
                            mMainHandler.post(service::next);
                        }
                        break;
                    case TRACK_PLAY_ERROR://mPlayer播放错误
                        LogUtil.e(TAG, msg.obj + "---");
                        mMainHandler.post(service::next);
                        break;
                    case RELEASE_WAKELOCK://释放电源锁
                        service.mWakeLock.release();
                        break;
                    case PREPARE_ASYNC_UPDATE:
                        int percent = (int) msg.obj;
                        Log.e(TAG, "Loading ... " + percent);
                        break;
                    case PREPARE_QQ_MUSIC:
                        Music music = mPlayingMusic;
                        MusicApi.getMusicInfo(music)
                                .subscribe(music1 -> {
                                    Log.e(TAG, mPlayingMusic.toString());
                                    String url = music1.getUri();
                                    mPlayer.setDataSource(url);
                                });
                        break;
                    case PLAYER_PREPARED:
                        //执行prepared之后 准备完成，更新总时长
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
                    case SCHEDULE_TASK:
                        LogUtil.e(TAG, "定时任务：" + time);
                        if (time == 0) {
                            service.stopSelf();
                            releaseServiceUiAndStop();
                            System.exit(0);
                        } else if (time == 10000) {
                            time = time - 1000;
                            ToastUtils.show("10秒后将自动关闭应用");
                        } else {
                            time = time - 1000;
                            notifyChange(SCHEDULE_CHANGED);
                        }
                        break;
                }
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化广播
        initReceiver();
        //初始化参数
        initConfig();
        //初始化音乐播放服务
        initMediaPlayer();
        //初始化电话监听服务
        initTelephony();
        //初始化通知
        initNotify();
    }

    /**
     * 参数配置，AudioManager、锁屏
     */
    private void initConfig() {

        //初始化主线程Handler
        mMainHandler = new Handler(Looper.getMainLooper());
        mRepeatMode = SPUtils.getPlayMode();

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
        audioAndFocusManager.requestAudioFocus();
    }


    /**
     * 释放通知栏;
     */
    private void releaseServiceUiAndStop() {
        if (isPlaying() || mHandler.hasMessages(TRACK_PLAY_ENDED)) {
            return;
        }

        if (DEBUG) Log.d(TAG, "Nothing is playing anymore, releasing notification");
        cancelNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionManager.release();

        if (!mServiceInUse) {
            savePlayQueue(true);
            stopSelf(mServiceStartId);
        }
    }

    /**
     * 重新加载当前进度
     */
    public void reloadPlayQueue() {
        mPlaylist.clear();
        mHistoryPos.clear();
        mPlaylist = PlayQueueLoader.getPlayQueue(this);
        mPlayingPos = SPUtils.getPlayPosition();
        mPlayer.seek(SPUtils.getPosition());
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
        mServiceReceiver = new ServiceReceiver();
        mHeadsetReceiver = new HeadsetReceiver();
        mHeadsetPlugInReceiver = new HeadsetPlugInReceiver();
        IntentFilter intentFilter = new IntentFilter(ACTION_SERVICE);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        intentFilter.addAction(SHUTDOWN);
        intentFilter.addAction(ACTION_TOGGLE_PAUSE);
        //注册广播
        registerReceiver(mServiceReceiver, intentFilter);
        registerReceiver(mHeadsetReceiver, intentFilter);
        registerReceiver(mHeadsetPlugInReceiver, intentFilter);
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
        Log.d(TAG, "Got new intent " + intent + ", startId = " + startId);
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

    private Timer timer;
    private TimerTask task;
    public static long time;

    /**
     * 开启定时
     *
     * @param totalTime 总时间 分钟单位
     */
    private void startRemind(int totalTime) {
        time = totalTime * 1000 * 60;
        if (timer == null) {
            timer = new Timer();
        }
        if (task != null) {
            task.cancel();
        }
        if (time == 0) return;
        mShutdownScheduled = true;
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mShutdownScheduled) {
                    Message message = Message.obtain();
                    message.what = SCHEDULE_TASK;
                    mHandler.sendMessage(message);
                }
            }
        };
        timer.schedule(task, 0, 1000);
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
    public void next() {
        synchronized (this) {
            mPlayingPos = getNextPosition();
            Log.e(TAG, "next: " + mPlayingPos);
            stop(false);
            playCurrentAndNext();
            notifyChange(META_CHANGED);
        }
    }

    /**
     * 上一首
     */
    public void prev() {
        synchronized (this) {
            mPlayingPos = getPreviousPosition();
            LogUtil.e(TAG, "prev: " + mPlayingPos);
            stop(false);
            playCurrentAndNext();
            notifyChange(META_CHANGED);
        }
    }

    /**
     * 播放当前歌曲
     */
    private void playCurrentAndNext() {
        synchronized (this) {
            if (mPlayingPos > mPlaylist.size() && mPlayingPos == -1) {
                return;
            }
            mPlayingMusic = mPlaylist.get(mPlayingPos);
            Observable<Music> observable = null;
            LogUtil.e(TAG, "-----" + mPlayingMusic.toString());
            if (mPlayingMusic.getUri() == null || mPlayingMusic.getUri().equals("")) {
                observable = MusicApi.getMusicInfo(mPlayingMusic);
            }
            if (observable != null) {
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Music>() {

                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Music music) {
                                LogUtil.e(TAG, "-----" + music.toString());
                                mPlayingMusic = music;
                                saveHistory();
                                isMusicPlaying = true;
                                mPlayer.setDataSource(mPlayingMusic.getUri());
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
            saveHistory();
            mHistoryPos.add(mPlayingPos);
            isMusicPlaying = true;
            mPlayer.setDataSource(mPlayingMusic.getUri());
            updateNotification();
            mediaSessionManager.updateMetaData(mPlayingMusic.getUri());

            final Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
            sendBroadcast(intent);

            if (mPlayer.isInitialized()) {
                mHandler.removeMessages(VOLUME_FADE_DOWN);
                mHandler.sendEmptyMessage(VOLUME_FADE_UP); //组件调到正常音量

                isMusicPlaying = true;
                notifyChange(PLAY_STATE_CHANGED);
            }
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
    private int getNextPosition() {
        if (mPlaylist == null || mPlaylist.isEmpty()) {
            return -1;
        }
        if (mPlaylist.size() == 1) {
            return 0;
        }
        if (mRepeatMode == PLAY_MODE_REPEAT) {
            if (mPlayingPos < 0) {
                return 0;
            }
        } else if (mRepeatMode == PLAY_MODE_LOOP) {
            if (mPlayingPos == mPlaylist.size() - 1) {
                return 0;
            } else if (mPlayingPos < mPlaylist.size() - 1) {
                return mPlayingPos + 1;
            }
        } else if (mRepeatMode == PLAY_MODE_RANDOM) {
            return new Random().nextInt(mPlaylist.size());
        }
        return mPlayingPos;
    }

    /**
     * 获取上一首位置
     *
     * @return
     */
    private int getPreviousPosition() {
        if (mPlaylist == null || mPlaylist.isEmpty()) {
            return -1;
        }
        if (mPlaylist.size() == 1) {
            return 0;
        }
        if (mRepeatMode == PLAY_MODE_REPEAT) {
            if (mPlayingPos < 0) {
                return 0;
            }
        } else if (mRepeatMode == PLAY_MODE_LOOP) {
            if (mPlayingPos == 0) {
                return mPlaylist.size() - 1;
            } else if (mPlayingPos > 0) {
                return mPlayingPos - 1;
            }
        } else if (mRepeatMode == PLAY_MODE_RANDOM) {
            mPlayingPos = new Random().nextInt(mPlaylist.size());
            return new Random().nextInt(mPlaylist.size());
        }
        return mPlayingPos;
    }

    /**
     * 根据位置播放音乐
     *
     * @param position
     */
    public void playMusic(int position) {
        if (position >= mPlaylist.size() || position == -1) {
            mPlayingPos = getNextPosition();
        } else {
            mPlayingPos = position;
        }
        if (mPlayingPos == -1)
            return;
        playCurrentAndNext();
        notifyChange(META_CHANGED);
    }

    /**
     * 音乐播放
     */
    public void play() {
        if (mPlayer.isInitialized()) {
            mPlayer.start();
            audioAndFocusManager.requestAudioFocus();
            mHandler.removeMessages(VOLUME_FADE_DOWN);
            mHandler.sendEmptyMessage(VOLUME_FADE_UP); //组件调到正常音量

            isMusicPlaying = true;
            updateNotification();
            notifyChange(PLAY_STATE_CHANGED);
        }
    }

    private int getAudioSessionId() {
        synchronized (this) {
            return mPlayer.getAudioSessionId();
        }
    }

    /**
     * 【在线音乐】加入播放队列并播放音乐
     *
     * @param music
     */
    public void play(Music music) {
        if (mPlayingPos == -1 || mPlaylist.size() == 0) {
            mPlaylist.add(music);
            mPlayingPos = 0;
        } else {
            mPlaylist.add(mPlayingPos, music);
        }
        Log.e(TAG, music.toString());
        mPlayingMusic = music;
        playCurrentAndNext();
        notifyChange(META_CHANGED);
    }


    /**
     * 播放暂停
     */
    public void playPause() {
        if (isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (DEBUG) Log.d(TAG, "Pausing playback");
        synchronized (this) {
            mHandler.removeMessages(VOLUME_FADE_UP);
            mHandler.sendEmptyMessage(VOLUME_FADE_DOWN);

            if (isPlaying()) {
                isMusicPlaying = false;
                notifyChange(PLAY_STATE_CHANGED);
                updateNotification();
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
     * @return
     */
    public boolean isPlaying() {
        return isMusicPlaying;
    }

    /**
     * 更新歌曲收藏状态
     *
     * @param music
     */
    public void updateFavorite(Music music) {
        AppRepository.updateFavoriteSongRepository(this, music)
                .subscribeOn(Schedulers.io())
                .subscribe(music1 -> {
                    notifyChange(PLAYLIST_CHANGED);
                });
    }

    /**
     * 跳到输入的进度
     */
    public void seekTo(int pos) {
        if (mPlayer != null && mPlayer.isInitialized() && mPlayingMusic != null) {
            mPlayer.seek(pos);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mServiceInUse = false;
        savePlayQueue(true);

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
            PlayQueueLoader.updateQueue(this, mPlaylist);
        }
        if (mPlayingMusic != null) {
            //保存歌曲id
            SPUtils.saveCurrentSongId(mPlayingMusic.getId());
        }
        //保存歌曲id
        SPUtils.setPlayPosition(mPlayingPos);
        //保存歌曲进度
        SPUtils.savePosition(mPlayer.position());
        //保存歌曲状态
        SPUtils.savePlayMode(mRepeatMode);
    }

    private void saveHistory() {
        PlayHistoryLoader.addSongToHistory(this, mPlayingMusic);
        notifyChange(PLAYLIST_CHANGED);
    }


    public void refresh() {
        mRepeatMode = SPUtils.getPlayMode();
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void removeFromQueue(int position) {
        Log.e(TAG, position + "---" + mPlayingPos + "---" + mPlaylist.size());
        if (position == mPlayingPos) {
            mPlaylist.remove(position);
            if (mPlaylist.size() == 0) {
                clearQueue();
            } else {
                playMusic(position);
            }
        } else if (position > mPlayingPos) {
            mPlaylist.remove(position);
        } else if (position < mPlayingPos) {
            mPlaylist.remove(position);
            mPlayingPos = mPlayingPos - 1;
        }
        notifyChange(PLAY_QUEUE_CHANGE);
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void clearQueue() {
        mPlayingMusic = null;
        isMusicPlaying = false;
        mPlayingPos = -1;
        mPlaylist.clear();
        mHistoryPos.clear();
        stop(true);
        notifyChange(META_CHANGED);
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
     * 发送更新广播
     *
     * @param what
     */
    private void notifyChange(final String what) {
        if (DEBUG) Log.d(TAG, "notifyChange: what = " + what);

        final Intent intent = new Intent(what);
        intent.putExtra("artist", getArtistName());
        intent.putExtra("album", getAlbumName());
        intent.putExtra("track", getTitle());
        intent.putExtra("playing", isPlaying());

        if (PLAY_STATE_CHANGED.equals(what)) {
            intent.putExtra("prepare", mPlayer.isPrepared());
        }
        sendBroadcast(intent);

        if (META_CHANGED.equals(what)) {
            mFloatLyricViewManager.loadLyric();
        }
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
     * @param playQueue
     */
    public void setPlayQueue(List<Music> playQueue) {
        mPlaylist.clear();
        mHistoryPos.clear();
        mPlaylist.addAll(playQueue);
    }


    /**
     * 获取播放队列
     *
     * @return
     */
    public List<Music> getPlayQueue() {
        if (mPlaylist.size() > 0) {
            return mPlaylist;
        }
        return mPlaylist;
    }


    /**
     * 获取当前音乐在播放队列中的位置
     *
     * @return
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

        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        nowPlayingIntent.setAction(Constants.DEAULT_NOTIFICATION);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }
        mNotificationBuilder = new Builder(this, initChannelId())
                .setSmallIcon(R.drawable.ic_icon)
                .setContentIntent(clickIntent)
                .setContentTitle(getTitle())
                .setContentText(text)
                .setWhen(mNotificationPostTime)
                .addAction(playButtonResId, "",
                        retrievePlaybackAction(ACTION_TOGGLE_PAUSE))
                .addAction(R.drawable.ic_skip_next,
                        "",
                        retrievePlaybackAction(ACTION_NEXT))
                .addAction(R.drawable.ic_lyric,
                        "",
                        retrievePlaybackAction(ACTION_LYRIC))
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this, PlaybackStateCompat.ACTION_STOP));


        if (SystemUtils.isJellyBeanMR1()) {
            mNotificationBuilder.setShowWhen(false);
        }
        if (SystemUtils.isLollipop()) {
            //线控
            isRunningForeground = true;
            mNotificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionManager.getMediaSession())
                    .setShowActionsInCompactView(0, 1, 2, 3);
            mNotificationBuilder.setStyle(style);
        }

        if (mPlayingMusic != null) {
            CoverLoader.loadImageViewByMusic(this, mPlayingMusic, bitmap -> {
                mNotificationBuilder.setLargeIcon(bitmap);
                mNotification = mNotificationBuilder.build();
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
            });
        }
        mNotification = mNotificationBuilder.build();
    }


    /**
     * 创建Notification ChannelID
     *
     * @return
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
            NotificationChannel mChannel = null;
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
            return mPlayingMusic.getId();
        } else {
            return null;
        }
    }


    public void showDesktopLyric(boolean show) {
        if (show) {
            mFloatLyricViewManager.startFloatLyric();
        } else {
            mFloatLyricViewManager.stopFloatLyric();
            mFloatLyricViewManager.removeFloatLyricView(this);
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
     * 更新状态栏通知
     */
    private void updateNotification() {
        initNotify();
        mFloatLyricViewManager.updatePlayStatus(isMusicPlaying);
        startForeground(NOTIFICATION_ID, mNotification);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
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
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, intent.getAction().toString());
            handleCommandIntent(intent);
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
        if (DEBUG) Log.d(TAG, "handleCommandIntent: action = " + action + ", command = " + command);

        if (CMD_NEXT.equals(command) || ACTION_NEXT.equals(action)) {
            next();
        } else if (CMD_PREVIOUS.equals(command) || ACTION_PREV.equals(action)) {
            prev();
        } else if (CMD_TOGGLE_PAUSE.equals(command) || PLAY_STATE_CHANGED.equals(action)
                || ACTION_TOGGLE_PAUSE.equals(action)) {
            if (isPlaying()) {
                pause();
                mPausedByTransientLossOfFocus = false;
            } else {
                play();
            }
        } else if (CMD_PAUSE.equals(command)) {
            pause();
            mPausedByTransientLossOfFocus = false;
        } else if (CMD_PLAY.equals(command)) {
            play();
        } else if (CMD_STOP.equals(command)) {
            pause();
            mPausedByTransientLossOfFocus = false;
            seekTo(0);
            releaseServiceUiAndStop();
        } else if (ACTION_LYRIC.equals(action)) {
            if (SystemUtils.isOpenSystemWindow() && SystemUtils.isOpenUsageAccess()) {
                showLyric = !showLyric;
                showDesktopLyric(showLyric);
            } else {
                //启动Activity让用户授权
                ToastUtils.show(this, "请前往设置中打开悬浮窗权限");
            }
        } else if (SCHEDULE_CHANGED.equals(action)) {
            totalTime = intent.getIntExtra("time", 0);
            LogUtil.e(TAG, SCHEDULE_CHANGED + "----" + totalTime);
            startRemind(totalTime);
        }
    }

    /**
     * 耳机插入广播接收器
     */
    public class HeadsetPlugInReceiver extends BroadcastReceiver {

        final IntentFilter filter;

        public HeadsetPlugInReceiver() {
            filter = new IntentFilter();

            if (Build.VERSION.SDK_INT >= 21) {
                filter.addAction(AudioManager.ACTION_HEADSET_PLUG);
            } else {
                filter.addAction(Intent.ACTION_HEADSET_PLUG);
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("state")) {
                //通过判断 "state" 来知道状态
                final boolean isPlugIn = intent.getExtras().getInt("state") == 1;
                Log.e(TAG, "耳机插入状态 ：" + isPlugIn);
            }
        }
    }


    /**
     * 耳机拔出广播接收器
     */
    private class HeadsetReceiver extends BroadcastReceiver {

        final IntentFilter filter;
        final BluetoothAdapter bluetoothAdapter;

        public HeadsetReceiver() {
            filter = new IntentFilter();
            filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY); //有线耳机拔出变化
            filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED); //蓝牙耳机连接变化

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isRunningForeground) {
                //当前是正在运行的时候才能通过媒体按键来操作音频
                switch (intent.getAction()) {
                    case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED:
                        if (bluetoothAdapter != null &&
                                BluetoothProfile.STATE_DISCONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) &&
                                isPlaying()) {
                            //蓝牙耳机断开连接 同时当前音乐正在播放 则将其暂停
                            pause();
                        }
                        break;
                    case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
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

        // Remove any sound effects
        final Intent audioEffectsIntent = new Intent(
                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(audioEffectsIntent);
        savePlayQueue(true);

        //释放mPlayer
        if (mPlayer != null) {
            mPlayer.stop();
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

        if (mWakeLock.isHeld())
            mWakeLock.release();
    }

}
