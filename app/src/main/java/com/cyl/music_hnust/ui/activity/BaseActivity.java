package com.cyl.music_hnust.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cyl.music_hnust.bean.music.Music;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.service.RxBus;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * 3
 * 基类
 *
 * @author yonglong
 * @date 2016/8/3
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected Handler mHandler;
    private PlayerReceiver mPlayStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        mHandler = new Handler();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayStatus = new PlayerReceiver(this);

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

        registerReceiver(mPlayStatus, filter);
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

    public class PlayerReceiver extends BroadcastReceiver {
        private final WeakReference<BaseActivity> mReference;


        public PlayerReceiver(final BaseActivity activity) {
            mReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                if (action.equals(MusicPlayService.META_CHANGED)) {
                    Music metaChangedEvent = PlayManager.getPlayingMusic();
                    RxBus.getInstance().post(metaChangedEvent);
                } else if (action.equals(MusicPlayService.TRACK_ERROR)) {
                    final String errorMsg = "play error";
                    Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        try {
            unregisterReceiver(mPlayStatus);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

    }

}
