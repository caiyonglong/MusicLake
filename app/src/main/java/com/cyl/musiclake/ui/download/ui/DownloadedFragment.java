package com.cyl.musiclake.ui.download.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.ui.download.TasksManagerModel;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/26.
 */

public class DownloadedFragment extends BaseFragment<DownloadPresenter> implements DownloadContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private SongAdapter mAdapter;
    private Boolean isCache;
    private List<Music> musicList = new ArrayList<>();

    public static DownloadedFragment newInstance(Boolean isCache) {
        Bundle args = new Bundle();
        args.putBoolean(Constants.KEY_IS_CACHE, isCache);
        DownloadedFragment fragment = new DownloadedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.play(position, musicList, Constants.PLAYLIST_DOWNLOAD_ID);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getItem(position);
            BottomDialogFragment.Companion.newInstance(music).show((AppCompatActivity) mFragmentComponent.getActivity());
        });
    }

    @Override
    protected void loadData() {
        if (getArguments() != null) {
            isCache = getArguments().getBoolean(Constants.KEY_IS_CACHE);
        }
        mPresenter.loadDownloadMusic(isCache);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {
        mAdapter = new SongAdapter(musicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showErrorInfo(String msg) {

    }

    @Override
    public void showSongs(List<Music> musicList) {
        this.musicList = musicList;
        mAdapter.setNewData(musicList);
        if (musicList.size() == 0) {
            showEmptyState();
        }
    }

    @Override
    public void showDownloadList(List<TasksManagerModel> modelList) {

    }
}
