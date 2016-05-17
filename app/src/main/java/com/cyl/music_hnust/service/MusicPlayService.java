package com.cyl.music_hnust.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.cyl.music_hnust.MyActivity;
import com.cyl.music_hnust.PlayerActivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ToastUtil;
import com.cyl.music_hnust.view.RoundCorner;

import java.io.File;
import java.util.List;

public class MusicPlayService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private final IBinder mBinder = new LocalBinder();
    private Context context;
    /* MediaPlayer对象 */
    public MediaPlayer mMediaPlayer = null;
    private int currentTime = 0;//歌曲播放进度
    private int currentListItme = -1;//当前播放第几首歌
    private List<MusicInfo> songs;//要播放的歌曲集合
    //    public LrcProcess mLrcProcess;
//    public LrcView mLrcView;
    public Notification notif;
    public String LrcPath = null;
    public boolean hasLyric = false;// 是否有歌词

    private NotificationManager mNotificationManager;


    MyReceiver serviceReceiver;
    public static final String BROADCAST_ACTION_SERVICE = "com.cyl.music_hnust.service";// 广播标志
    public static final String NOTIFICATION_ACTION_NEXT = "com.cyl.music_hnust.notify.next";// 广播标志
    public static final String NOTIFICATION_ACTION_PLAY = "com.cyl.music_hnust.notify.play";// 广播标志


    @Override
    public void onCreate() {
        super.onCreate();


        //实例化过滤器，设置广播
        serviceReceiver = new MyReceiver();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION_SERVICE);

        intentFilter.addAction(MyActivity.CTL_ACTION);
        intentFilter.addAction(NOTIFICATION_ACTION_NEXT);
        intentFilter.addAction(NOTIFICATION_ACTION_PLAY);
        //注册广播
        registerReceiver(serviceReceiver, intentFilter);


        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);// 获取电话通讯服务
        telephonyManager.listen(new ServicePhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);// 创建一个监听对象，监听电话状态改变事件


        mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                // 播放完成一首之后进行下一首
                nextMusic();
            }

        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                if (songs != null && songs.size() > 0) {
                    File file = new File(getPath());
                    if (file.exists()) {
                        Toast.makeText(getApplicationContext(), "播放出错",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "文件已不存在",
                                Toast.LENGTH_SHORT).show();

                    }
                }
                return true;
            }
        });

    }


    /**
     * 得到当前播放进度
     */
    public int getCurrent() {
        try {
            if (mMediaPlayer.isPlaying()) {
                return mMediaPlayer.getCurrentPosition();
            } else {
                return currentTime;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "播放出错",
                    Toast.LENGTH_SHORT).show();
            return currentTime;
        }
    }

    /**
     * 跳到输入的进度
     */
    public void movePlay(int progress) {
        try {
            mMediaPlayer.seekTo(progress);
            currentTime = progress;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "播放出错",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据歌曲存储路径播放歌曲
     */
    public void playMusic(String path) {
        try {
            Intent it = new Intent(MyActivity.UPDATE_ACTION);
            it.putExtra("current", currentListItme);
            it.putExtra("update", 3);
            it.putExtra("name", getSongName());
            it.putExtra("artist", getSingerName());
            it.putExtra("pic", getSong().getAlbumPic() + "");
            sendBroadcast(it);

            /* 重置MediaPlayer */
            mMediaPlayer.reset();
            /* 设置要播放的文件的路径 */
            mMediaPlayer.setDataSource(path);
            // mMediaPlayer = MediaPlayer.create(this,
            // R.drawable.bbb);播放资源文件中的歌曲
            /* 准备播放 */
            mMediaPlayer.prepare();
            // initLrc(path);
            mMediaPlayer.start();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "播放出错",
                    Toast.LENGTH_SHORT).show();
        }

        showNotification();
    }


    /* 下一首 */
    public void nextMusic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mode = prefs.getString("mode_list", "0");
        switch (mode) {
            case "0"://顺序
                if (++currentListItme >= songs.size()) {
                    currentListItme = 0;
                }
                if (songs.get(currentListItme).getPath() != null) {
                    playMusic(songs.get(currentListItme).getPath());
                } else {
                    ToastUtil.show(context, "播放列表为空");
                }
                break;
            case "2"://单曲
                if (songs.get(currentListItme).getPath() != null) {
                    playMusic(songs.get(currentListItme).getPath());
                } else {
                    ToastUtil.show(context, "播放列表为空");
                }
                break;
            case "1"://随机
                currentListItme = (int) (Math.random() * songs.size());

                if (songs.get(currentListItme).getPath() != null) {
                    playMusic(songs.get(currentListItme).getPath());
                } else {
                    ToastUtil.show(context, "播放列表为空");
                }

                break;
        }


    }

    /* 上一首 */
    public void frontMusic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mode = prefs.getString("mode_list", "0");
        switch (mode) {
            case "0"://顺序
                if (--currentListItme < 0) {
                    currentListItme = songs.size() - 1;
                }
                if (songs.get(currentListItme).getPath() != null) {
                    playMusic(songs.get(currentListItme).getPath());
                } else {
                    ToastUtil.show(context, "播放列表为空");
                }
                break;
            case "2"://单曲
                if (songs.get(currentListItme).getPath() != null) {
                    playMusic(songs.get(currentListItme).getPath());
                } else {
                    ToastUtil.show(context, "播放列表为空");
                }
                break;
            case "1"://随机
                currentListItme = (int) (Math.random() * songs.size());

                if (songs.get(currentListItme).getPath() != null) {
                    playMusic(songs.get(currentListItme).getPath());
                } else {
                    ToastUtil.show(context, "播放列表为空");
                }

                break;
        }
        Log.v("itme", currentListItme + "hree");


    }


    /**
     * 歌曲是否真在播放
     */
    public boolean isPlay() {
        return mMediaPlayer.isPlaying();
    }

    /**
     * 暂停或开始播放歌曲
     */
    public void pausePlay() {

        if (mMediaPlayer.isPlaying()) {
            currentTime = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
        showNotification();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification() {
        MusicInfo m = null;
        CharSequence from = "";
        CharSequence message = "";
        if (getSongs().size() > 0 && getSongs() != null) {
            m = getSong();
            from = m.getName();
            message = m.getArtist();
        } else {
            from = "湖科音乐";
            message = "听喜欢的歌";
        }
        Intent nextIntent2 = new Intent();
        nextIntent2.setAction(NOTIFICATION_ACTION_NEXT); // 为Intent对象设置Action

        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(
                MusicPlayService.this, 0, nextIntent2, 0);

        Intent StartIntent2 = new Intent();
        StartIntent2.setAction(NOTIFICATION_ACTION_PLAY); // 为Intent对象设置Action

        PendingIntent nPendingIntent = PendingIntent.getBroadcast(
                MusicPlayService.this, 0, StartIntent2, 0);

        Intent intent = new Intent();
        intent.setClass(this, PlayerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        RemoteViews rv = new RemoteViews(getPackageName(),
                R.layout.notification);
        rv.setOnClickPendingIntent(R.id.notificationnext, nextPendingIntent);
        rv.setOnClickPendingIntent(R.id.notificationplay, nPendingIntent);
        rv.setTextViewText(R.id.noticationname, m.getName() == null ? "湖科音乐" : m.getName());
        rv.setTextViewText(R.id.noticationsinger,
                m.getArtist().equals("未知艺术家") ? "music" : m.getArtist());
        if (mMediaPlayer.isPlaying()) {
            rv.setImageViewResource(R.id.notificationplay,
                    R.drawable.notificapause);
        } else {
            rv.setImageViewResource(R.id.notificationplay,
                    R.drawable.notificaplay);
        }
        Bitmap bm = null;
        if (getSong().getAlbumPic() != null) {
            bm = BitmapFactory.decodeFile(getSong().getAlbumPic());
        } else {
            bm = null;
        }
        if (bm != null) {
            try {
                rv.setImageViewBitmap(R.id.gfdhstrdsga, RoundCorner
                        .toRoundCorner(bm, bm
                                .getHeight() / 6));
            } catch (Exception e) {
                rv.setImageViewResource(R.id.gfdhstrdsga,
                        R.drawable.ic_launcher);
            }
        } else {
            rv.setImageViewResource(R.id.gfdhstrdsga, R.mipmap.ic_launcher);
        }

        notif = new Notification.Builder(this)
                .setAutoCancel(false)
                .setTicker(from)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(from)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .build();
        notif.flags = Notification.FLAG_AUTO_CANCEL;
        notif.contentView = rv;

        startForeground(NOTIFICATION_ID, notif);
//
//        Log.e("notify", "notify");
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return mBinder;
    }

    public MusicInfo getSong() {
        return songs.get(currentListItme);
    }

    public String getSongName() {
        return songs.get(currentListItme).getName();
    }

    public String getPath() {
        return songs.get(currentListItme).getPath();
    }

    public String getSingerName() {
        return songs.get(currentListItme).getArtist();
    }


    /**
     * -，通过这里的getService得到Service，之后就可调用Service这里的方法了
     */
    public class LocalBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public void setmMediaPlayer(MediaPlayer mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    public int getCurrentListItme() {
        return currentListItme;
    }

    public void setCurrentListItme(int currentListItme) {
        this.currentListItme = currentListItme;
    }

    public int getDuration() {
        try {
            if (mMediaPlayer!=null){

                return mMediaPlayer.getDuration();
            }else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<MusicInfo> getSongs() {
        return songs;
    }

    public void setSongs(List<MusicInfo> songs) {
        this.songs = songs;
    }

//    // 兼容2.0以前版本
//    @Override
//    public void onStart(Intent intent, int startId) {
//    }


    // 在2.0以后的版本如果重写了onStartCommand，那onStart将不会被调用，注：在2.0以前是没有onStartCommand方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Service", "Received start id " + startId + ": " + intent);
        // 如果服务进程在它启动后(从onStartCommand()返回后)被kill掉, 那么让他呆在启动状态但不取传给它的intent.
        // 随后系统会重写创建service，因为在启动时，会在创建新的service时保证运行onStartCommand
        // 如果没有任何开始指令发送给service，那将得到null的intent，因此必须检查它.
        // 该方式可用在开始和在运行中任意时刻停止的情况，例如一个service执行音乐后台的重放

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if (mMediaPlayer != null) {
            stopForeground(true);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (serviceReceiver != null) {
            unregisterReceiver(serviceReceiver);
        }
    }


    private class MyReceiver extends BroadcastReceiver {
        int status = 0;//0未播放 1正在播放 2暂停 3下一首，上一首

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();

            int control = intent.getIntExtra("control", -1);
            if (str.equals(NOTIFICATION_ACTION_PLAY)) {
                control = 1;
            } else if (str.equals(NOTIFICATION_ACTION_NEXT)) {
                control = 3;
            }
            Log.e("Action", str);
            Log.e("ddd", control + "");
            switch (control) {
                case 0:
                    status = 1;
                    break;
                //播放或暂停
                case 1:
                    pausePlay();
                    if (mMediaPlayer.isPlaying()) {
                        status = 1;
                    } else {
                        status = 2;
                    }
                    break;
                //上一首歌
                case 2:
                    status = 3;
                    frontMusic();
                    break;
                //下一首歌
                case 3:
                    status = 3;
                    nextMusic();
                    break;

            }
            Intent it = new Intent(MyActivity.UPDATE_ACTION);
            it.putExtra("update", status);
            it.putExtra("current", currentListItme);
            it.putExtra("name", getSongName() + "");
            it.putExtra("artist", getSingerName() + "");
            it.putExtra("pic", getSong().getAlbumPic() + "");
            sendBroadcast(it);
        }
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

    private void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }


}
