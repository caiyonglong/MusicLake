package com.cyl.musiclake.ui.download.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
            BottomDialogFragment.Companion.newInstance(music, Constants.PLAYLIST_DOWNLOAD_ID).show((AppCompatActivity) mFragmentComponent.getActivity());
        });
        mAdapter.setOnItemLongClickListener((adapter, view, position) ->
                false
        );
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (getActivity() != null) {
            getActivity().getMenuInflater().inflate(R.menu.menu_download, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            if (mPresenter != null) {
                mPresenter.deleteAll();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSongs(List<Music> musicList) {
        this.musicList = musicList;
        mAdapter.setNewData(musicList);
        if (musicList.size() == 0) {
            mAdapter.setEmptyView(R.layout.view_song_empty, mRecyclerView);
        }
    }

    @Override
    public void showDownloadList(List<TasksManagerModel> modelList) {

    }
}
