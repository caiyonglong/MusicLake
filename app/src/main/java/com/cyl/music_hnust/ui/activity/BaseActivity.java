package com.cyl.music_hnust.ui.activity;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.cyl.music_hnust.IMusicService;
import com.cyl.music_hnust.service.PlayManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import static com.cyl.music_hnust.service.PlayManager.mService;

/**
 * 3
 * 基类
 *
 * @author yonglong
 * @date 2016/8/3
 */
public abstract class BaseActivity extends RxAppCompatActivity implements ServiceConnection {

    protected Handler mHandler;
    private PlayManager.ServiceToken mToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        mToken = PlayManager.bindToService(this, this);

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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
        // Unbind from the service
        if (mToken != null) {
            PlayManager.unbindFromService(mToken);
            mToken = null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = IMusicService.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService =null;
    }
}
