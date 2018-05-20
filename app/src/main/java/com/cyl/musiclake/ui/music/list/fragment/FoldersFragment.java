package com.cyl.musiclake.ui.music.list.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.bean.FolderInfo;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.ui.music.list.adapter.FolderAdapter;
import com.cyl.musiclake.ui.music.list.contract.FoldersContract;
import com.cyl.musiclake.ui.music.list.presenter.FoldersPresenter;
import com.cyl.musiclake.view.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by D22434 on 2018/1/8.
 */

public class FoldersFragment extends BaseLazyFragment<FoldersPresenter> implements FoldersContract.View {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FolderAdapter mAdapter;

    public static FoldersFragment newInstance() {

        Bundle args = new Bundle();

        FoldersFragment fragment = new FoldersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {
        mAdapter = new FolderAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void listener() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.loadFolders();
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            FolderInfo folderInfo = (FolderInfo) adapter.getItem(position);
            NavigationHelper.navigateToFolderSongs(getActivity(), folderInfo.folderPath);
        });
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onLazyLoad() {
        mPresenter.loadFolders();
    }


    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }

    @Override
    public void showFolders(List<FolderInfo> folderInfos) {
        mAdapter.setNewData(folderInfos);
    }

    @Override
    public void showLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }
}
