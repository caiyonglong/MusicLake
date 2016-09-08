package com.cyl.music_hnust.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.StatusBarCompat;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by 永龙 on 2016/3/19.
 */
public class WelcomeActivity extends BaseActivity {

    RelativeLayout container;
    private PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            loadEverything();

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
    protected void initDatas() {
        Nammu.init(this);
        new Handler().postDelayed(new splashhandler(), 1000);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        container = (RelativeLayout) findViewById(R.id.container);
        initSystemBar();
    }

    private void loadEverything() {
       init();
    }

    private void init() {
            Intent intent =new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            WelcomeActivity.this.finish();
    }
    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = StatusBarCompat.getStatusBarHeight(this);
            container.setPadding(0, top, 0, 0);
        }
    }

    private void checkPermissionAndThenLoad() {

        //check for permission
        if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadEverything();
        } else {
            if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(container, "软件必须获取相关权限",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Nammu.askForPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
                            }
                        }).show();
            } else {
                Nammu.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
            }
        }
    }


    private class splashhandler implements Runnable{
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissionAndThenLoad();
            } else {
                loadEverything();
            }
        }
    }
}
