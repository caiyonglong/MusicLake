package com.cyl.music_hnust.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.SystemUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import rx.functions.Action1;

/**
 * Created by 永龙 on 2016/3/19.
 */
public class WelcomeActivity extends BaseActivity {
    @Bind(R.id.wel_container)
    RelativeLayout container;

    private ServiceConnection mPlayServiceConnection;

    @Override
    protected void listener() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        if (SystemUtils.isMarshmallow()) {
            checkPermissionAndThenLoad();
        } else {
            checkService();
        }
    }

    //检查权限
    private void checkPermissionAndThenLoad() {

        //需要检查的权限
        final String[] mPermissionList = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                //获取电话状态
                Manifest.permission.READ_PHONE_STATE,
                //传感器
                Manifest.permission.VIBRATE,
                //定位权限
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        RxPermissions.getInstance(this)
                .request(mPermissionList)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            checkService();
                        } else {
                            Snackbar.make(container, "软件必须获取相关权限",
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkPermissionAndThenLoad();
                                        }
                                    }).show();
                        }
                    }
                });
    }

    //检查服务是否运行
    private void checkService() {
        if (SystemUtils.isServiceRunning(this, PlayService.class)) {
            startMainActivity();
            finish();
        } else {
            startService();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }
            }, 100);
        }
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection);
        }
        super.onDestroy();
    }

    /**
     * 启动服务
     */
    private void startService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        startService(intent);
    }

    /**
     * 欢迎界面跳转到主界面
     */
    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
    }


    /**
     * 服务连接
     */
    private class PlayServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService playService = ((PlayService.MyBinder) service).getService();
            playService.updateMusicList();
            startMainActivity();
            finish();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        mPlayServiceConnection = new PlayServiceConnection();
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
