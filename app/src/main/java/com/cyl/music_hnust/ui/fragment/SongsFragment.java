package com.cyl.music_hnust.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.ui.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.ui.adapter.MyStaggeredViewAdapter;
import com.cyl.music_hnust.dataloaders.MusicLoader;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.music.Album;
import com.cyl.music_hnust.model.music.Artist;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SongsFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    TextView tv_empty;
    LinearLayout loading;
    private LocalMusicAdapter mAdapter;
    private MyStaggeredViewAdapter adapter;
    private List<Music> musicInfos = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();

    int type = 0;

    public static SongsFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(Extras.SONG_TYPE, type);
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 设置监听事件
     */
    @Override
    protected void listener() {
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        type = getArguments().getInt(Extras.SONG_TYPE);
        Log.e("tppppp", type + "===");
        new loadSongs().execute("");
    }

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        type = getArguments().getInt(Extras.SONG_TYPE);
        if (type == 0) {
            return R.layout.frag_recyclerview_songs;
        } else {
            return R.layout.frag_recyclerview;
        }
    }

    /**
     * 初始化控件
     */
    @Override
    public void initViews() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        loading = (LinearLayout) rootView.findViewById(R.id.loading);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mRecyclerView.scrollToPosition(PlayManager.position());
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void reloadAdapter(int type) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                try {
                    musicInfos = MusicUtils.getAllSongs(getActivity());
                    albums = MusicLoader.getAllAlbums(getActivity());
                    artists = MusicLoader.getAllArtists(getActivity());
                } catch (Exception e) {

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

    private class loadSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null) {
                if (type == 0) {
                    musicInfos = MusicUtils.getAllSongs(getActivity());
                    mAdapter = new LocalMusicAdapter((AppCompatActivity) getActivity(), musicInfos);
                } else if (type == 1) {
                    albums = MusicLoader.getAllAlbums(getActivity());
                    adapter = new MyStaggeredViewAdapter(getActivity(), albums, artists, true);
                } else {
                    artists = MusicLoader.getAllArtists(getActivity());
                    adapter = new MyStaggeredViewAdapter(getActivity(), albums, artists, false);
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            loading.setVisibility(View.GONE);
            if (type == 0) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
            tv_empty.setText("请稍后，努力加载中...");
        }
    }
}
