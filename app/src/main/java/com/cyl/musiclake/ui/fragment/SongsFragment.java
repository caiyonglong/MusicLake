package com.cyl.musiclake.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.dataloaders.MusicLoader;
import com.cyl.musiclake.ui.adapter.SongAdapter;
import com.cyl.musiclake.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SongsFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.loading)
    LinearLayout loading;
    private SongAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();

    public static SongsFragment newInstance() {

        Bundle args = new Bundle();
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {
    }

    @Override
    protected void initDatas() {
        new loadSongs().execute();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview_songs;
    }

    @Override
    public void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadSongs().execute("");
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class loadSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null) {
                musicInfos = MusicLoader.getAllSongs(getActivity());
                mAdapter = new SongAdapter((AppCompatActivity) getActivity(), musicInfos);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            loading.setVisibility(View.GONE);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
            tv_empty.setText("请稍后，努力加载中...");
        }
    }
}
