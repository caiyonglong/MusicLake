package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
