package com.cyl.musiclake.ui.music.download;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.download.TasksManager;
import com.cyl.musiclake.data.download.TasksManagerModel;

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
        TasksManager.INSTANCE.onCreate(new WeakReference<>(this));
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
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLazyLoad() {
        models = TasksManager.INSTANCE.getModelList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TasksManager.INSTANCE.onDestroy();
    }

    @Override
    public void showErrorInfo(String msg) {

    }

    @Override
    public void showSongs(List<Music> musicList) {

    }

    @Override
    public void showDownloadList(List<TasksManagerModel> modelList) {
        updateDownLoadList(modelList);
    }

    public void updateDownLoadList(List<TasksManagerModel> list) {
        hideLoading();
        if (mAdapter == null) {
            mAdapter = new TaskItemAdapter(getContext());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mAdapter);
        }
        if (models.size() == 0) {
            showEmptyState();
        }
        mAdapter.notifyDataSetChanged();
    }
}
