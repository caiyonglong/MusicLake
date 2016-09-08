package com.cyl.music_hnust.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.model.Music;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/14 16:15
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetail extends BaseActivity implements LocalMusicAdapter.OnItemClickListener {

    XRecyclerView mRecyclerView ;
    TextView tv_empty;

    private static LocalMusicAdapter mAdapter;
    private static List<Music> musicInfos = new ArrayList<>();

    /**
     *   新建一个线程更新UI
     */

    final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mAdapter.musicInfos = musicInfos;
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 设置监听事件
     */
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
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


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
