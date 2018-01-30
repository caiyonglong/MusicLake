package com.cyl.musiclake.ui.base;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.data.model.HistoryChangedEvent;
import com.cyl.musiclake.data.model.MetaChangedEvent;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.data.model.StatusChangedEvent;
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
                switch (action) {
                    case MusicPlayerService.META_CHANGED:
                        RxBus.getInstance().post(new MetaChangedEvent());
                        break;
                    case MusicPlayerService.PLAY_QUEUE_CHANGE:
                        RxBus.getInstance().post(new HistoryChangedEvent());
                        break;
                    case MusicPlayerService.PLAY_STATE_CHANGED:
                        RxBus.getInstance().post(new StatusChangedEvent());
                        break;
                    case MusicPlayerService.PLAY_QUEUE_CLEAR:
                        break;
                    case MusicPlayerService.PLAYLIST_CHANGED:
                        RxBus.getInstance().post(new Playlist());
                        break;
                    case MusicPlayerService.TRACK_ERROR:
                        final String errorMsg = context.getString(R.string.error_playing_track);
                        Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
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
