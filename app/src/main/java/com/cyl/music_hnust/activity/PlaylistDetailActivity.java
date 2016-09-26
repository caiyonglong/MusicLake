package com.cyl.music_hnust.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.MusicUtils;
import com.cyl.music_hnust.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/14 16:15
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailActivity extends BaseActivity implements LocalMusicAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    TextView tv_empty;
    Toolbar mToolbar;

    private static LocalMusicAdapter mAdapter;
    private static List<Music> musicInfos = new ArrayList<>();
    private String playlist_id;
    private boolean isAlbum;

    /**
     * 设置监听事件
     */
    @Override
    protected void listener() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_detail);
        SystemUtils.setSystemBarTransparent(this);

        initView();
        initData();
    }

    private void initData() {

        playlist_id = getIntent().getStringExtra(Extras.PLAYLIST_ID);
        isAlbum = getIntent().getBooleanExtra(Extras.ALBUM,false);
        reloadAdapter();
        mAdapter = new LocalMusicAdapter(this, musicInfos);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

    }


    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("歌单列表");
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (musicInfos.size() == 0) {
            tv_empty.setText("请稍后，本地音乐加载中...");
            tv_empty.setVisibility(View.VISIBLE);
        }else {
            tv_empty.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_playlist, menu);
        return super.onCreateOptionsMenu(menu);

    }


    private void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                musicInfos.clear();
                if (playlist_id != null ) {
                    if (isAlbum){
                        Log.e("歌单id++++++", playlist_id + "");
                        musicInfos= MusicUtils.getAlbumSongs(PlaylistDetailActivity.this,playlist_id);
                    }else {
                        Log.e("歌单id++++++", playlist_id + "");
                        MusicUtils.getMusicForPlaylist(PlaylistDetailActivity.this, playlist_id, musicInfos);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (musicInfos.size() == 0) {
                    tv_empty.setText("暂无音乐");
                    tv_empty.setVisibility(View.VISIBLE);
                }else {
                    tv_empty.setVisibility(View.GONE);
                }

                mAdapter.setMusicInfos(musicInfos);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onItemClick(View view, int position) {
        MainActivity.mPlayService.setMyMusicList(musicInfos);
        MainActivity.mPlayService.playMusic(position);
    }
}
