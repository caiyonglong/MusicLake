package com.cyl.musiclake.ui.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.R;
import com.cyl.musiclake.mvp.presenter.MusicStateListener;
import com.cyl.musiclake.service.MusicPlayService;
import com.cyl.musiclake.service.PlayManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;

import static com.cyl.musiclake.service.PlayManager.mService;

/**
 * 3
 * 基类
 *
 * @author yonglong
 * @date 2016/8/3
 */
public abstract class BaseActivity extends RxAppCompatActivity implements ServiceConnection,MusicStateListener {

    protected Handler mHandler;
    private PlayManager.ServiceToken mToken;
    private PlaybackStatus mPlaybackStatus;
    private final ArrayList<MusicStateListener> mMusicStateListener = new ArrayList<>();


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
        setSystemBarTransparent();
        initView();
        initData();
        listener();
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void listener();

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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
        }
        mMusicStateListener.clear();
    }

    @Override
    public void onMetaChanged() {
        // Let the listener know to the meta chnaged
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onMetaChanged();
            }
        }
    }

    @Override
    public void restartLoader() {
        // Let the listener know to update a list
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.restartLoader();
            }
        }
    }

    @Override
    public void onPlaylistChanged() {
        // Let the listener know to update a list
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onPlaylistChanged();
            }
        }
    }

    public void setMusicStateListenerListener(final MusicStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }

        if (status != null) {
            mMusicStateListener.add(status);
        }
    }

    public void removeMusicStateListenerListener(final MusicStateListener status) {
        if (status != null) {
            mMusicStateListener.remove(status);
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
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                if (action.equals(MusicPlayService.META_CHANGED)) {
                    baseActivity.onMetaChanged();
                } else if (action.equals(MusicPlayService.REFRESH)) {
                    baseActivity.restartLoader();
                } else if (action.equals(MusicPlayService.PLAYLIST_CHANGED)) {
                    baseActivity.onPlaylistChanged();
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
