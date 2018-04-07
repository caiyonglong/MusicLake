package com.cyl.musiclake.ui.music.online.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.data.source.download.TasksManager;
import com.cyl.musiclake.data.source.download.TasksManagerModel;
import com.cyl.musiclake.ui.music.online.adapter.TaskItemAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/26.
 */

public class DownloadManagerFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

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
        if (models.size() == 0) {
            mAdapter.setEmptyView(R.layout.view_song_empty);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {
        models = TasksManager.getImpl().getModelList();
        mAdapter = new TaskItemAdapter(getContext(), models);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        TasksManager.getImpl().onCreate(new WeakReference<>(this));
    }

    public void postNotifyDataChanged() {
        if (mAdapter != null) {
            getActivity().runOnUiThread(() -> {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        TasksManager.getImpl().onDestroy();
        mAdapter = null;
        super.onDestroy();
    }
}
