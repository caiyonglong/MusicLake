package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.Preferences;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * 基类
 *
 * @author yonglong
 * @date 2016/8/3
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected Handler mHandler;
    boolean  on = Preferences.isNightMode();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (on){
            this.setTheme(R.style.MyThemeDark);
        }else {
            this.setTheme(R.style.MyThemeBlue);
        }
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        listener();
    }


    protected abstract void listener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
