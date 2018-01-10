package com.cyl.musiclake.ui.base;

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
import com.cyl.musiclake.data.model.MetaChangedEvent;
import com.cyl.musiclake.service.MusicPlayService;
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
        setContentView(getLayoutResID());

        mToken = PlayManager.bindToService(this, this);
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
        filter.addAction(MusicPlayService.PLAY_STATE_CHANGED);
        // Track changes
        filter.addAction(MusicPlayService.META_CHANGED);
        // Update a list, probably the playlist fragment's
        filter.addAction(MusicPlayService.REFRESH);
        // If a playlist has changed, notify us
        filter.addAction(MusicPlayService.PLAYLIST_CHANGED);
        // If there is an error playing a track
        filter.addAction(MusicPlayService.TRACK_ERROR);

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

        private final WeakReference<BaseActivity> mReference;


        public PlaybackStatus(final BaseActivity activity) {
            mReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            Log.e("PlaybackStatus", "接收到广播-------------" + action);
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null && action != null) {
                if (action.equals(MusicPlayService.META_CHANGED)) {
                    RxBus.getInstance().post(new MetaChangedEvent());
                } else if (action.equals(MusicPlayService.REFRESH)) {
                } else if (action.equals(MusicPlayService.PLAYLIST_CHANGED)) {
                } else if (action.equals(MusicPlayService.TRACK_ERROR)) {
                    final String errorMsg = context.getString(R.string.error_playing_track);
                    Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = IMusicService.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
    }
}
