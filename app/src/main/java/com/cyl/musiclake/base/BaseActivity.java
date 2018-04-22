package com.cyl.musiclake.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.event.HistoryChangedEvent;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.event.ScheduleTaskEvent;
import com.cyl.musiclake.event.StatusChangedEvent;
import com.cyl.musiclake.service.MusicPlayerService;
import com.cyl.musiclake.service.PlayManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

import static com.cyl.musiclake.service.PlayManager.mService;

/**
 * 基类
 *
 * @author yonglong
 * @date 2016/8/3
 */
public abstract class BaseActivity extends RxAppCompatActivity implements ServiceConnection {

    protected Handler mHandler;
    private PlayManager.ServiceToken mToken;
    private PlaybackStatus mPlaybackStatus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = PlayManager.bindToService(this, this);
        setContentView(getLayoutResID());
        mPlaybackStatus = new PlaybackStatus(this);
        mHandler = new Handler();
        init();
    }

    private void init() {
        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        initView();
        initData();
        listener();
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();

    protected void listener() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        // Play and pause changes
        filter.addAction(MusicPlayerService.PLAY_STATE_CHANGED);
        // Track changes
        filter.addAction(MusicPlayerService.META_CHANGED);
        // Update a list, probably the playlist fragment's
        filter.addAction(MusicPlayerService.PLAY_QUEUE_CHANGE);
        // If a queue has cleared, notify us
        filter.addAction(MusicPlayerService.PLAY_QUEUE_CLEAR);
        filter.addAction(MusicPlayerService.PLAYLIST_CHANGED);
        filter.addAction(MusicPlayerService.SCHEDULE_CHANGED);
        // If there is an error playing a track
        filter.addAction(MusicPlayerService.TRACK_ERROR);

        registerReceiver(mPlaybackStatus, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
        // Unbind from the service
        if (mToken != null) {
            PlayManager.unbindFromService(mToken);
            mToken = null;
        }
        try {
            unregisterReceiver(mPlaybackStatus);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    private final static class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<BaseActivity> mReferences;


        public PlaybackStatus(final BaseActivity activity) {
            mReferences = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            Log.e("PlaybackStatus", "接收到广播-------------" + action);
            BaseActivity baseActivity = (BaseActivity) mReferences.get();
            if (baseActivity != null && action != null) {
                switch (action) {
                    case MusicPlayerService.META_CHANGED:
                        RxBus.getInstance().post(new MetaChangedEvent());
                        RxBus.getInstance().post(new HistoryChangedEvent());
                        break;
                    case MusicPlayerService.PLAY_QUEUE_CHANGE:
                        RxBus.getInstance().post(new HistoryChangedEvent());
                        break;
                    case MusicPlayerService.PLAY_STATE_CHANGED:
                        StatusChangedEvent event = new StatusChangedEvent();
                        event.setPrepared(intent.getBooleanExtra("prepare", false));
                        RxBus.getInstance().post(event);
                        break;
                    case MusicPlayerService.PLAY_QUEUE_CLEAR:
                        RxBus.getInstance().post(new HistoryChangedEvent());
                        break;
                    case MusicPlayerService.PLAYLIST_CHANGED:
                        RxBus.getInstance().post(new PlaylistEvent());
                        break;
                    case MusicPlayerService.SCHEDULE_CHANGED:
                        RxBus.getInstance().post(new ScheduleTaskEvent());
                        break;
                    case MusicPlayerService.TRACK_ERROR:
                        final String errorMsg = context.getString(R.string.error_playing_track);
                        Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = IMusicService.Stub.asInterface(iBinder);
        RxBus.getInstance().post(new MetaChangedEvent());
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
    }
}
