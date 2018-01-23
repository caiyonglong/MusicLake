package com.cyl.musiclake.ui.onlinemusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.download.DownloadListener;
import com.cyl.musiclake.ui.download.model.FileState;
import com.cyl.musiclake.ui.onlinemusic.adapter.DownloadAdapter;
import com.cyl.musiclake.ui.onlinemusic.contract.DownloadContract;
import com.cyl.musiclake.ui.onlinemusic.presenter.DownloadPresenter;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/14.
 */
public class DownloadingFragment extends BaseFragment implements DownloadContract.View, DownloadListener {

    private static final String TAG = "DownloadingFragment";
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading)
    LinearLayout loading;

    private DownloadAdapter mAdapter;
    private DownloadPresenter mPresenter;

    public static DownloadingFragment newInstance() {
        Bundle args = new Bundle();
        DownloadingFragment fragment = new DownloadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemChildClickListener((adapter, view, i) -> {
            mPresenter.updateDownloadStatus((FileState) adapter.getItem(i));
        });
    }

    @Override
    protected void initDatas() {
        mPresenter.updateProgress(null, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    @Override
    public void initViews() {
        mPresenter = new DownloadPresenter(getContext(), this);
        mPresenter.attachView(this);
        mAdapter = new DownloadAdapter(null, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.bindToRecyclerView(mRecyclerView);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void updateProgress(List<FileState> fileStates) {
        mAdapter.setNewData(fileStates);
    }


    @Override
    public void oDownloading(String url, int finished) {
        Log.e(TAG, "url" + url + "---finished " + finished);
        mPresenter.updateProgress(url, finished);
    }

    @Override
    public void onDownloadFinished(File downloadFile) {
    }
}
