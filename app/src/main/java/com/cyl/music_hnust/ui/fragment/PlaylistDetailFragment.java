package com.cyl.music_hnust.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.ui.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.dataloaders.MusicLoader;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.utils.Extras;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailFragment extends BaseFragment{

    RecyclerView mRecyclerView;
    TextView tv_empty;
    Toolbar mToolbar;

    private static LocalMusicAdapter mAdapter;
    private static List<Music> musicInfos = new ArrayList<>();
    private String playlist_id;
    private boolean isAlbum;

    public static PlaylistDetailFragment newInstance(String id) {

        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putString(Extras.ALBUM_ID,id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        mToolbar.setTitle("湖科音乐");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playlist_id =getArguments().getString(Extras.ALBUM_ID);
        isAlbum = true;

        reloadAdapter();
        mAdapter = new LocalMusicAdapter((AppCompatActivity) getActivity(), musicInfos);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public int getLayoutId() {
        return R.layout.playlist_detail;
    }

    @Override
    public void initViews() {
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        mToolbar.setTitle("歌单列表");

        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_playlist,menu);
    }

    private void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                musicInfos.clear();
                if (playlist_id != null ) {
                    if (isAlbum){
                        Log.e("歌单id++++++", playlist_id + "");
                        musicInfos= MusicLoader.getAlbumSongs(getActivity(),playlist_id);
                    }else {
                        Log.e("歌单id++++++", playlist_id + "");
                        musicInfos= PlaylistLoader.getMusicForPlaylist(getActivity(), playlist_id);
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


}
