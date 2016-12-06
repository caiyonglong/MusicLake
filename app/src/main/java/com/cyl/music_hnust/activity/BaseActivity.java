package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        boolean on = Preferences.isNightMode();
        if (on) {
            setTheme(R.style.MyThemeDark);
        } else {
            setTheme(R.style.MyThemeBlue);
        }
        super.onCreate(savedInstanceState);
        mHandler = new Handler();

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        init();
    }

    private void init() {
        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        initView();
        initData();
        listener();
    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void listener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
