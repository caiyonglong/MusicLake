package com.cyl.musiclake.ui.onlinemusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.localmusic.adapter.SongAdapter;
import com.cyl.musiclake.ui.onlinemusic.contract.DownloadContract;
import com.cyl.musiclake.ui.onlinemusic.presenter.DownloadPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/26.
 */

public class DownloadedFragment extends BaseFragment implements DownloadContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    SongAdapter mAdapter;
    DownloadPresenter mPresenter;

    public static DownloadedFragment newInstance() {
        Bundle args = new Bundle();
        DownloadedFragment fragment = new DownloadedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initDatas() {
        mPresenter = new DownloadPresenter(getActivity());
        mPresenter.attachView(this);
        mPresenter.loadDownloadMusic();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {
        mAdapter = new SongAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorInfo(String msg) {

    }

    @Override
    public void showSongs(List<Music> musicList) {
        mAdapter.setNewData(musicList);
        if (musicList.size() == 0) {
            mAdapter.setEmptyView(R.layout.view_song_empty);
        }
    }
}
