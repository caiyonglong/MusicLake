package com.cyl.music_hnust.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.transition.Slide;
import android.view.Gravity;

import com.cyl.music_hnust.R;

/**
 * 作者：yonglong on 2016/8/14 16:15
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetail extends BaseActivity {


    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.playlist_detail;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initViews(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.playlist_detail);
            slide.addTarget(R.id.playlist_img);
            slide.addTarget(R.id.view_separator);
            getWindow().setEnterTransition(slide);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
