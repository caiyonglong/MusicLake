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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.di.component.ActivityComponent;
import com.cyl.musiclake.di.component.DaggerActivityComponent;
import com.cyl.musiclake.di.module.ActivityModule;
import com.cyl.musiclake.event.HistoryChangedEvent;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlayQueueEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.event.ScheduleTaskEvent;
import com.cyl.musiclake.event.StatusChangedEvent;
import com.cyl.musiclake.player.MusicPlayerService;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.utils.LogUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

import static com.cyl.musiclake.player.PlayManager.mService;

/**
 * 基类
 *
 * @author yonglong
 * @date 2016/8/3
 */
public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends RxAppCompatActivity implements ServiceConnection, BaseContract.BaseView {

    @Nullable
    @Inject
    protected T mPresenter;
    protected ActivityComponent mActivityComponent;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    protected Handler mHandler;
    private Unbinder unbinder;
    private PlayManager.ServiceToken mToken;
    private PlaybackStatus mPlaybackStatus;

    private List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = PlayManager.bindToService(this, this);
        setContentView(getLayoutResID());
        mPlaybackStatus = new PlaybackStatus(this);
        mHandler = new Handler();
        initActivityComponent();
        initInjector();
        //初始化黄油刀控件绑定框架
        unbinder = ButterKnife.bind(this);
        initToolBar();
        attachView();
        initView();
        initData();
        listener();
    }

    /**
     * 初始化Dagger
     */
    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((MusicApp) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    private void initToolBar() {
        if (hasToolbar() && mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initInjector();

    protected void listener() {
    }

    protected boolean hasToolbar() {
        return true;
    }

    protected void setToolbarTitle(String name) {
        if (hasToolbar() && mToolbar != null) {
            mToolbar.setTitle(name);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayerService.PLAY_STATE_CHANGED);
        filter.addAction(MusicPlayerService.META_CHANGED);
        filter.addAction(MusicPlayerService.PLAY_QUEUE_CHANGE);
        filter.addAction(MusicPlayerService.PLAY_QUEUE_CLEAR);
        filter.addAction(MusicPlayerService.PLAYLIST_CHANGED);
        filter.addAction(MusicPlayerService.SCHEDULE_CHANGED);
        filter.addAction(MusicPlayerService.TRACK_ERROR);
        registerReceiver(mPlaybackStatus, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mToken != null) {
            PlayManager.unbindFromService(mToken);
            mToken = null;
        }
        try {
            unregisterReceiver(mPlaybackStatus);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        detachView();
    }


    /**
     * 贴上view
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private final static class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<BaseActivity> mReferences;


        public PlaybackStatus(final BaseActivity activity) {
            mReferences = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            LogUtil.e("PlaybackStatus", "接收到广播-------------" + action);
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
                        RxBus.getInstance().post(new PlayQueueEvent());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
