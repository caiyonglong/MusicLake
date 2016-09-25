package com.cyl.music_hnust.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.StatusBarCompat;
import com.cyl.music_hnust.utils.SystemUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by 永龙 on 2016/3/19.
 */
public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.container)
    RelativeLayout container;

    private ServiceConnection mPlayServiceConnection;

    private PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            checkService();
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };

    @Override
    protected void listener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        SystemUtils.setSystemBarTransparent(this);
        initSystemBar();
        checkPermissionAndThenLoad();

    }


    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (SystemUtils.isKITKAT()) {
            int top = StatusBarCompat.getStatusBarHeight(this);
            container.setPadding(0, top, 0, 0);
        }
    }

    //检查权限
    private void checkPermissionAndThenLoad() {

//        RxPermissions.getInstance(this)
//                .request(
//                        Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//
//                        //网络
//                        Manifest.permission.INTERNET,
//                        Manifest.permission.ACCESS_NETWORK_STATE,
//                        Manifest.permission.ACCESS_WIFI_STATE,
//                        Manifest.permission.CHANGE_WIFI_STATE,
//                        //获取电话状态
//                        Manifest.permission.READ_PHONE_STATE,
//                        //传感器
//                        Manifest.permission.VIBRATE,
//                        Manifest.permission.LOCATION_HARDWARE,
//                        //定位权限
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//
//                        )//这里申请了两组权限
//                .subscribe(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean granted) {
//                        if (granted) {
//                            //同意后跳转
//                            checkService();
//                        } else {
//                            //不同意，给提示
//                            Toast.makeText(WelcomeActivity.this, "请同意软件的权限，才能继续提供服务", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

        Nammu.init(this);
        //check for permission
        final String[] mPermissionList = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,

                //网络
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                //获取电话状态
                Manifest.permission.READ_PHONE_STATE,
                //传感器
                Manifest.permission.VIBRATE,
                Manifest.permission.LOCATION_HARDWARE,
                //定位权限
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            checkService();
        } else {
            if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(container, "软件必须获取相关权限",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Nammu.askForPermission(WelcomeActivity.this, mPermissionList, permissionReadstorageCallback);
                            }
                        }).show();
            } else {
                Nammu.askForPermission(this, mPermissionList, permissionReadstorageCallback);
            }
        }
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
//                    startMainActivity();
                    bindService();
                }
            }, 1000);
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
     * 跳转
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

    @Override
    public void onBackPressed() {
    }
}
