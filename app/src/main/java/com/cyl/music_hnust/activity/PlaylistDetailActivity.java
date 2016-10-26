package com.cyl.music_hnust.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
    FloatingActionButton fab;

    private static LocalMusicAdapter mAdapter;
    private static List<Music> musicInfos = new ArrayList<>();
    private String playlist_id, album_id;
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

        playlist_id = getIntent().getStringExtra(Extras.PLAYLIST_ID);
        isAlbum = getIntent().getBooleanExtra(Extras.ALBUM, false);
        album_id = getIntent().getStringExtra(Extras.ALBUM_ID);
        Log.e("playlist_id", playlist_id + "===" + isAlbum + "+++" + album_id + "00");


        initView();
        initData();
    }

    private void initData() {


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
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (musicInfos.size() == 0) {
            tv_empty.setText("请稍后，本地音乐加载中...");
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaylistDetailActivity.this, EditActivity.class);
                String content = "";
                if (musicInfos.size() > 0) {
                    content = "分享歌单\n";
                }
                for (int i = 0; i < musicInfos.size(); i++) {
                    content += musicInfos.get(i).getTitle() + "---" + musicInfos.get(i).getArtist();
                    content += "\n";
                }
                intent.putExtra("content", content);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                final Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            case R.id.action_settings:
                final Intent intent1 = new Intent(this, SettingsActivity.class);
                startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return super.onCreateOptionsMenu(menu);

    }


    private void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                musicInfos.clear();
                if (isAlbum) {
                    Log.e("歌单id++++++", album_id + "");
                    musicInfos = MusicUtils.getAlbumSongs(PlaylistDetailActivity.this, album_id);
                } else {
                    Log.e("歌单id++++++", playlist_id + "");
                    MusicUtils.getMusicForPlaylist(PlaylistDetailActivity.this, playlist_id, musicInfos);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (musicInfos.size() == 0) {
                    tv_empty.setText("暂无音乐");
                    tv_empty.setVisibility(View.VISIBLE);
                } else {
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
