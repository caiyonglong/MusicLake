package com.cyl.music_hnust.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.StatusBarCompat;

/**
 * Created by 永龙 on 2016/3/19.
 */
public class WelcomeActivity extends AppCompatActivity {

    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        container = (RelativeLayout) findViewById(R.id.container);
        initSystemBar();
        new Handler().postDelayed(new splashhandler(), 1000);


    }

    private void init() {
        String[] mPermissionList = new String[]{Manifest.permission.CHANGE_CONFIGURATION,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.WAKE_LOCK,Manifest.permission.WRITE_SETTINGS,Manifest.permission.VIBRATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE};
        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(mPermissionList,100);

            Intent intent =new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }
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

    class splashhandler implements Runnable{

        public void run() {
            init();
        }

    }


}
