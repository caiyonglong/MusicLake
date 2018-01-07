package com.cyl.musiclake.ui.music.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.music.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.contract.SongsContract;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.presenter.SongsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SongsFragment extends BaseFragment implements SongsContract.View {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.loading)
    LinearLayout loading;
    private SongAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();

    private SongsPresenter mPresenter;

    public static SongsFragment newInstance() {

        Bundle args = new Bundle();
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initDatas() {
        mPresenter.loadSongs(null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview_songs;
    }

    @Override
    public void initViews() {
        mPresenter = new SongsPresenter(getActivity());
        mPresenter.attachView(this);

        mAdapter = new SongAdapter((AppCompatActivity) getActivity(), musicInfos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        tv_empty.setText("请稍后，努力加载中...");
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showSongs(List<Music> songList) {
        mAdapter.setMusicInfos(songList);
        mAdapter.notifyDataSetChanged();
    }

}
