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
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.media.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.qq.QQApiServiceImpl;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.data.source.PlayHistoryLoader;
import com.cyl.musiclake.data.source.PlayQueueLoader;
import com.cyl.musiclake.ui.common.Constants;
import com.cyl.musiclake.ui.main.MainActivity;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.PreferencesUtils;
import com.cyl.musiclake.utils.SystemUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

    public static final String PLAY_STATE_CHANGED = "com.cyl.music_lake.play_state";// 播放暂停广播

    public static final String PLAYLIST_CHANGED = "com.cyl.music_lake.playlist_change";
    public static final String TRACK_ERROR = "com.cyl.music_lake.error";
    public static final String SHUTDOWN = "com.cyl.music_lake.shutdown";
    public static final String REFRESH = "com.cyl.music_lake.refresh";

    public static final String PLAY_QUEUE_CLEAR = "com.cyl.music_lake.play_queue_clear"; //清空播放队列
    public static final String PLAY_QUEUE_CHANGE = "com.cyl.music_lake.play_queue_change"; //播放队列改变

    public static final String META_CHANGED = "com.cyl.music_lake.metachanged";//状态改变(歌曲替换)

    public static final String CMD_TOGGLE_PAUSE = "com.cyl.music_lake.cmd.toggle_pause";//按键播放暂停
    public static final String CMD_NEXT = "com.cyl.music_lake.cmd.next";//按键下一首
    public static final String CMD_PREVIOUS = "com.cyl.music_lake.cmd.previous";//按键上一首
    public static final String CMD_PAUSE = "com.cyl.music_lake.cmd.pause";//按键暂停
    public static final String CMD_PLAY = "com.cyl.music_lake.cmd.play";//按键播放
    public static final String CMD_STOP = "com.cyl.music_lake.cmd.stop";//按键停止
    public static final String SERVICE_CMD = "com.cyl.music_lake.cmd.service";//状态改变
    public static final String FROM_MEDIA_BUTTON = "com.cyl.music_lake.cmd.media";//状态改变
    public static final String CMD_NAME = "com.cyl.music_lake.cmd.name";//状态改变

    public static final int TRACK_WENT_TO_NEXT = 2; //下一首
    public static final int RELEASE_WAKELOCK = 3; //播放完成
    public static final int TRACK_PLAY_ENDED = 4; //播放完成
    public static final int TRACK_PLAY_ERROR = 5; //播放出错
    public static final int QQ_MUSIC_URL = 6; //播放出错
    public static final int PREPARE_ASYNC_UPDATE = 7; //PrepareAsync装载进程
    public static final int PREPARE_ASYNC_ENDED = 8; //PrepareAsync异步装载完成
    public static final int PREPARE_QQ_MUSIC = 9; //获取QQ音乐播放状态
    public static final int SAVE_HISTORY = 10; //保存播放历史
    public static final int SAVE_PLAY_QUEUE = 11; //保存播放队列

    public static final int AUDIO_FOCUS_CHANGE = 12; //音频焦点改变
    public static final int VOLUME_FADE_DOWN = 13; //音频焦点改变
    public static final int VOLUME_FADE_UP = 14; //音频焦点改变

    private final int NOTIFICATION_ID = 0x123;
    private long mNotificationPostTime = 0;
    private int mServiceStartId = -1;

    private static final boolean DEBUG = true;

    private MusicPlayerEngine mPlayer = null;
    public PowerManager.WakeLock mWakeLock;


    public Music mPlayingMusic = null;
    private List<Music> mPlaylist = new ArrayList<>();
    private int mPlayingPos = -1;
    private int mNextPlayPos = -1;

    //播放模式：0顺序播放、1随机播放、2单曲循环
    private int mRepeatMode;
    private final int PLAY_MODE_RANDOM = 0;
    private final int PLAY_MODE_LOOP = 1;
    private final int PLAY_MODE_REPEAT = 2;
    //广播接收者
    ServiceReceiver mServiceReceiver;
    HeadsetReceiver mHeadsetReceiver;
    HeadsetPlugInReceiver mHeadsetPlugInReceiver;

    private AudioManager mAudioManager;

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private IMusicServiceStub mBindStub = new IMusicServiceStub(this);
    private MediaSessionCompat mSession;
    private boolean isRunningForeground = false;
    private boolean isMusicPlaying = false;
    //暂时失去焦点，会再次回去音频焦点
    private boolean mPausedByTransientLossOfFocus = false;

    private Bitmap artwork = null;
    boolean mServiceInUse = false;
    //工作线程和Handler
    private MusicPlayerHandler mHandler;
    private HandlerThread mWorkThread;
    //主线程Handler
    private Handler mMainHandler;
    //音频焦点
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
//            AudioManager.AUDIOFOCUS_GAIN:重新获取焦点
//            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:暂时失去焦点
//            AudioManager.AUDIOFOCUS_LOSS:时期焦点
            mHandler.obtainMessage(AUDIO_FOCUS_CHANGE, focusChange, 0).sendToTarget();
        }
    };

    private class MusicPlayerHandler extends Handler {
        private final WeakReference<MusicPlayerService> mService;
        private float mCurrentVolume = 1.0f;

        public MusicPlayerHandler(final MusicPlayerService service, final Looper looper) {
            super(looper);
            mService = new WeakReference<MusicPlayerService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MusicPlayerService service = mService.get();
            synchronized (service) {
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
                        service.notifyChange(META_CHANGED);
                        service.updateNotification();
                        service.setNextTrack();
                        service.play();
//                        service.updateCursor(service.mPlaylist.get(service.mPlayPos).mId);
//                        service.bumpSongCount(); //更新歌曲的播放次数
                        break;
                    case TRACK_PLAY_ENDED://mPlayer播放完毕且暂时没有下一首
                        if (service.mRepeatMode == PLAY_MODE_REPEAT) {
                            service.seekTo(0);
                            service.play();
                        } else {
                            service.next();
//                            service.gotoNext(false);
                        }
                        break;
                    case TRACK_PLAY_ERROR://mPlayer播放错误
                        break;
                    case RELEASE_WAKELOCK://释放电源锁
                        service.mWakeLock.release();
                        break;
                    case PREPARE_ASYNC_UPDATE:
                        int percent = (int) msg.obj;
                        Log.e(TAG, "Loading ... " + percent);
                        break;
                    case PREPARE_ASYNC_ENDED:
                        Log.e(TAG, "PREPARE_ASYNC_ENDED");
                        mMainHandler.post(service::play);
                        break;
                    case PREPARE_QQ_MUSIC:
                        boolean next = (boolean) msg.obj;
                        Music music = mPlayingMusic;
                        if (next) {
                            music = mPlaylist.get(mNextPlayPos);
                        }
                        QQApiServiceImpl.getMusicInfo(music)
                                .subscribe(music1 -> {
                                    Log.e(TAG, mPlayingMusic.toString());
                                    String url = music1.getUri();
                                    mMainHandler.post(() -> {
                                        if (next) {
                                            mPlayer.setNextDataSource(url);
                                        } else {
                                            mPlayer.setDataSource(url);
                                        }
                                    });
                                });
                        break;
                    case SAVE_HISTORY:
                        //保存到播放历史
                        PlayHistoryLoader.addSongToHistory(service, mPlayingMusic);
                        notifyChange(PLAYLIST_CHANGED);
                        break;
                    case SAVE_PLAY_QUEUE:
                        savePlayQueue(true);
                        break;
                    case AUDIO_FOCUS_CHANGE:
                        switch (msg.arg1) {
                            case AudioManager.AUDIOFOCUS_LOSS://时期焦点
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://暂时失去焦点
                                if (service.isPlaying()) {
                                    service.mPausedByTransientLossOfFocus =
                                            msg.arg1 == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
                                }
                                mMainHandler.post(() -> service.pause());
                                break;
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                                removeMessages(VOLUME_FADE_UP);
                                sendEmptyMessage(VOLUME_FADE_DOWN);
                                break;
                            case AudioManager.AUDIOFOCUS_GAIN://重新获取焦点
                                //重新获得焦点，且符合播放条件，开始播放
                                if (!service.isPlaying()
                                        && service.mPausedByTransientLossOfFocus) {
                                    service.mPausedByTransientLossOfFocus = false;
                                    mCurrentVolume = 0f;
                                    service.mPlayer.setVolume(mCurrentVolume);
                                    service.play();
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

    private void setAndRecordPlayPos(int mNextPlayPos) {
        mPlayingPos = mNextPlayPos;
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
        //初始化MediaSession
        setUpMediaSession();
        //初始化通知
        initNotify();
    }

    /**
     * 参数配置，锁屏
     */
    private void initConfig() {

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ComponentName mediaButtonReceiverComponent = new ComponentName(getPackageName(),
                MediaButtonIntentReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(mediaButtonReceiverComponent);

        mMainHandler = new Handler();
        mRepeatMode = PreferencesUtils.getPlayMode();


        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PlayerWakelockTag");
    }


    /**
     * 初始化和设置MediaSessionCompat
     * MediaSessionCompat用于告诉系统及其他应用当前正在播放的内容,以及接收什么类型的播放控制
     */
    private void setUpMediaSession() {
        mSession = new MediaSessionCompat(this, "Listener");
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPause() {
                pause();
                mPausedByTransientLossOfFocus = false;
            }

            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onSeekTo(long pos) {
                seekTo((int) pos);
            }

            @Override
            public void onSkipToNext() {
                next();
            }

            @Override
            public void onSkipToPrevious() {
                prev();
            }

            @Override
            public void onStop() {
                pause();
                seekTo(0);
                releaseServiceUiAndStop();
            }
        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
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
            mSession.setActive(false);

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
        mPlaylist = PlayQueueLoader.getPlayQueue(this);
        mPlayingPos = PreferencesUtils.getPlayPosition();
        mPlayer.seek(PreferencesUtils.getPosition());
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
        //初始化工作线程
        mWorkThread = new HandlerThread("MusicPlayerThread");
        mWorkThread.start();

        mHandler = new MusicPlayerHandler(this, mWorkThread.getLooper());

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
//                mShutdownScheduled = false;
                releaseServiceUiAndStop();
                return START_NOT_STICKY;
            }

            handleCommandIntent(intent);
        }
//        scheduleDelayedShutdown();
        if (intent != null && intent.getBooleanExtra(FROM_MEDIA_BUTTON, false)) {
//            MediaButtonIntentReceiver.completeWakefulIntent(intent);
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


    /**
     * 下一首
     */
    private void next() {
        synchronized (this) {
            mPlayingPos = getNextPosition();
            Log.e(TAG, "next: " + mPlayingPos);
            //设置nextMediaPlayer
            setNextTrack();
            playMusic(mPlayingPos);
        }
    }

    /**
     * 上一首
     */
    private void prev() {
        synchronized (this) {
            setNextTrack(mPlayingPos);
            mPlayingPos = getPreviousPosition();
            Log.e(TAG, "prev: " + mPlayingPos);
            playMusic(mPlayingPos);
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
            return new Random().nextInt(mPlaylist.size());
        }
        return mPlayingPos;
    }

    /**
     * 【本地音乐】根据位置播放音乐
     *
     * @param position
     */
    private void playMusic(int position) {
        if (position >= mPlaylist.size() || position == -1) {
            mPlayingPos = getNextPosition();
        } else {
            mPlayingPos = position;
        }
        if (mPlayingPos == -1)
            return;
        playMusic(mPlaylist.get(mPlayingPos));
    }

    /**
     * 播放音乐
     *
     * @param music
     */
    public void playMusic(Music music) {
        Log.e(TAG, music.toString());
        mPlayingMusic = music;
        mHandler.sendEmptyMessage(SAVE_HISTORY);

        //QQ音乐的播放地址有一段时间后失效，所以需要动态获取播放地址
        if (mPlayingMusic.getType() == Music.Type.QQ) {
            mHandler.obtainMessage(PREPARE_QQ_MUSIC, false);
        } else {
            mPlayer.setDataSource(mPlayingMusic.getUri());
        }
//        play();
    }

    /**
     * 音乐播放
     */
    private void play() {
        int status = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (DEBUG) Log.d(TAG, "Starting playback: audio focus request status = " + status);

        if (status != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }

        final Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(intent);

        mAudioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(),
                MediaButtonIntentReceiver.class.getName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mSession.setActive(true);

        if (mPlayer.isInitialized()) {
            mPlayer.start();
            mHandler.removeMessages(VOLUME_FADE_DOWN);
            mHandler.sendEmptyMessage(VOLUME_FADE_UP); //组件调到正常音量

            isMusicPlaying = true;
            updateNotification();
            notifyChange(META_CHANGED);
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
        playMusic(music);
    }


    /**
     * 播放暂停
     */
    private void playPause() {
        if (isPlaying()) {
            pause();
        } else if (isPause()) {
            play();
        } else {
            playMusic(mPlayingPos);
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

            if (isMusicPlaying) {
                notifyChange(META_CHANGED);
                isMusicPlaying = false;
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
     * 判断是否暂停状态
     *
     * @return
     */
    public boolean isPause() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            return true;
        }
        return false;
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
            PreferencesUtils.saveCurrentSongId(mPlayingMusic.getId());
        }
        //保存歌曲id
        PreferencesUtils.setPlayPosition(mPlayingPos);
        //保存歌曲进度
        PreferencesUtils.savePosition(mPlayer.position());
        //保存歌曲状态
        PreferencesUtils.savePlayMode(mRepeatMode);
    }


    private void refresh() {
        mRepeatMode = PreferencesUtils.getPlayMode();
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
        notifyChange(PLAY_QUEUE_CHANGE);
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public void clearQueue() {
        mPlayingMusic = null;
        isMusicPlaying = false;
        mPlaylist.clear();
        mPlayer.stop();

        notifyChange(PLAY_QUEUE_CLEAR);
    }

    /**
     * 获取正在播放进度
     */
    public long getCurrentPosition() {
        if (mPlayingMusic != null && mPlayer != null && mPlayer.isInitialized()) {
            return mPlayer.position();
        } else {
            return 0;
        }
    }

    /**
     * 获取总时长
     */
    public long getDuration() {
        if (mPlayingMusic != null && mPlayer != null && mPlayer.isInitialized()) {
            return mPlayer.duration();
        } else {
            return 0;
        }
    }

    /**
     * 缓存下一首不指定位置
     */
    private void setNextTrack() {
        setNextTrack(getNextPosition());
    }

    /**
     * 设置下首播放曲目的位置,并设置mplayer下次播放的datasource
     *
     * @param position
     */
    private void setNextTrack(int position) {
        mNextPlayPos = position;
        if (DEBUG) Log.d(TAG, "setNextTrack: next play position = " + mNextPlayPos);
        if (mNextPlayPos >= 0 && mPlaylist != null && mNextPlayPos < mPlaylist.size()) {
            //QQ音乐的播放地址有一段时间后失效，所以需要动态获取播放地址
            if (mPlaylist.get(mNextPlayPos).getType() == Music.Type.QQ) {
                mHandler.obtainMessage(PREPARE_QQ_MUSIC, true);
            } else {
                mPlayer.setNextDataSource(mPlaylist.get(mNextPlayPos).getUri());
            }
        } else {
            mPlayer.setNextDataSource(null);
        }
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
    private String getArtistName() {
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
    private Music getPlayingMusic() {
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
    private void setPlayQueue(List<Music> playQueue) {
        mPlaylist.clear();
        mPlaylist.addAll(playQueue);
    }


    /**
     * 获取播放队列
     *
     * @return
     */
    private List<Music> getPlayQueue() {
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
    private int getPlayPosition() {
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
                ? R.drawable.ic_pause : R.drawable.ic_play_arrow_white_18dp;

        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        nowPlayingIntent.setAction(Constants.DEAULT_NOTIFICATION);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }
        artwork = CoverLoader.getInstance().loadThumbnail(null);

        Builder builder = new Builder(this, initChannelId())
                .setSmallIcon(R.drawable.ic_icon)
                .setContentIntent(clickIntent)
                .setContentTitle(getTitle())
                .setContentText(text)
                .setWhen(mNotificationPostTime)
                .addAction(R.drawable.ic_skip_previous,
                        "",
                        retrievePlaybackAction(ACTION_PREV))
                .addAction(playButtonResId, "",
                        retrievePlaybackAction(ACTION_TOGGLE_PAUSE))
                .addAction(R.drawable.ic_skip_next,
                        "",
                        retrievePlaybackAction(ACTION_NEXT))
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this, PlaybackStateCompat.ACTION_STOP));


        if (SystemUtils.isJellyBeanMR1()) {
            builder.setShowWhen(false);
        }
        if (SystemUtils.isLollipop()) {
            //线控
            isRunningForeground = true;
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle()
                    .setMediaSession(mSession.getSessionToken())
                    .setShowActionsInCompactView(0, 1, 2, 3);
            builder.setStyle(style);
        }

        if (mPlayingMusic != null) {
            String coverUrl = mPlayingMusic.getCoverUri();
            GlideApp.with(this)
                    .asBitmap()
                    .load(coverUrl)
                    .error(R.drawable.default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            artwork = resource;
                            builder.setLargeIcon(artwork);
                            mNotification = builder.build();
                        }
                    });
        }
        mNotification = builder.build();
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


        mHandler.sendEmptyMessage(SAVE_PLAY_QUEUE);
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


        mAudioManager.abandonAudioFocus(audioFocusChangeListener);

        cancelNotification();

        //注销广播
        unregisterReceiver(mServiceReceiver);
        unregisterReceiver(mHeadsetReceiver);
        unregisterReceiver(mHeadsetPlugInReceiver);

        mWakeLock.release();
    }

    private class IMusicServiceStub extends IMusicService.Stub {
        private final WeakReference<MusicPlayerService> mService;

        private IMusicServiceStub(final MusicPlayerService service) {
            mService = new WeakReference<MusicPlayerService>(service);
        }

        @Override
        public void playOnline(Music music) throws RemoteException {
            mService.get().play(music);
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
        public void refresh() throws RemoteException {
            mService.get().refresh();
        }

        @Override
        public void update(Music music) throws RemoteException {
            mService.get().updateFavorite(music);
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
            return mService.get().getTitle();
        }

        @Override
        public String getSongArtist() throws RemoteException {
            return mService.get().getArtistName();
        }

        @Override
        public Music getPlayingMusic() throws RemoteException {
            return mService.get().getPlayingMusic();
        }

        @Override
        public void setPlayList(List<Music> playlist) throws RemoteException {
            mService.get().setPlayQueue(playlist);
        }

        @Override
        public List<Music> getPlayList() throws RemoteException {
            return mService.get().getPlayQueue();
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
            return mService.get().getPlayPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return (int) mService.get().getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return (int) mService.get().getCurrentPosition();
        }


        @Override
        public boolean isPlaying() throws RemoteException {
            return mService.get().isPlaying();
        }

        @Override
        public boolean isPause() throws RemoteException {
            return !mService.get().isPlaying();
        }
    }

}
