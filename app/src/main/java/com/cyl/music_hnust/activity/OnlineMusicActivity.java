package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.OnlineMusicAdapter;
import com.cyl.music_hnust.model.OnlineMusicInfo;
import com.cyl.music_hnust.view.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineMusicActivity extends BaseActivity {


    private List<OnlineMusicInfo> onlineMusicInfos = new ArrayList<>();
    private OnlineMusicAdapter mAdapter;
    RecyclerView recyclerview;
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_online;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        for (int i =0; i<5;i++)
        {
            OnlineMusicInfo onlineMusicInfo = new OnlineMusicInfo();
            onlineMusicInfo.setTitle("111111");
            onlineMusicInfo.setAlbum_title("111111");
            onlineMusicInfo.setArtist_name("111111");
            onlineMusicInfo.setSong_id("111111");

            onlineMusicInfos.add(onlineMusicInfo);
        }
        recyclerview.setLayoutManager(new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mAdapter = new OnlineMusicAdapter(this, recyclerview, onlineMusicInfos);
        recyclerview.setAdapter(mAdapter);
//        mAdapter = new OnlineMusicAdapter();
    }
}
