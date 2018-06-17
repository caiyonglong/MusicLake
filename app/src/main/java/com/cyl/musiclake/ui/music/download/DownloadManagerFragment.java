package com.cyl.musiclake.ui.music.download;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.data.DownloadLoader;
import com.cyl.musiclake.data.SongLoader;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.download.TasksManager;
import com.cyl.musiclake.data.download.TasksManagerModel;
import com.cyl.musiclake.player.PlayManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/26.
 */

public class DownloadManagerFragment extends BaseLazyFragment<DownloadPresenter> implements DownloadContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    TaskItemAdapter mAdapter;
    List<TasksManagerModel> models = new ArrayList<>();

    public static DownloadManagerFragment newInstance() {
        Bundle args = new Bundle();
        DownloadManagerFragment fragment = new DownloadManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData() {
        mPresenter.loadDownloading();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mAdapter = new TaskItemAdapter(models);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    public void postNotifyDataChanged() {
        if (mAdapter != null) {
            mFragmentComponent.getActivity().runOnUiThread(() -> {
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    protected void listener() {
        super.listener();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.loadDownloading();
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                Music music = SongLoader.INSTANCE.getMusicInfo(models.get(position).getMid());
                PlayManager.playOnline(music);
            }
        });
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLazyLoad() {
        models = TasksManager.INSTANCE.getModelList();
        mAdapter.setNewData(models);
    }

    @Override
    public void onStop() {
        super.onStop();
        TasksManager.INSTANCE.onDestroy();
        mAdapter = null;
    }

    @Override
    public void showErrorInfo(String msg) {

    }

    @Override
    public void showSongs(List<Music> musicList) {

    }

    @Override
    public void showDownloadList(List<TasksManagerModel> modelList) {
        TasksManager.INSTANCE.onCreate(new WeakReference<>(this));
        mAdapter.setNewData(models);
        hideLoading();
        if (models.size() == 0) {
            showEmptyState();
        }
    }
}
