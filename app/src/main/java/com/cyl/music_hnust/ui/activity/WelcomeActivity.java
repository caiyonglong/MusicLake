package com.cyl.music_hnust.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.SystemUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Bind;

/**
 * Created by 永龙 on 2016/3/19.
 */
public class WelcomeActivity extends BaseActivity {
    @Bind(R.id.wel_container)
    RelativeLayout container;
    RxPermissions rxPermissions;

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
        rxPermissions = new RxPermissions(this);
        if (SystemUtils.isMarshmallow()) {
            checkPermissionAndThenLoad();
        } else {
            initPlayQueue();
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
        rxPermissions
                .request(mPermissionList)
                .subscribe(granted -> {
                    if (granted) {
                        initPlayQueue();
                        // All requested permissions are granted
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
                });
    }

    //检查服务是否运行
    private void initPlayQueue() {
        startMainActivity();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 欢迎界面跳转到主界面
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
