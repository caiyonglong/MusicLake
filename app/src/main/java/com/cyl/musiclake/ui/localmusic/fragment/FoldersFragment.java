package com.cyl.musiclake.ui.localmusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.FolderInfo;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.localmusic.adapter.FolderAdapter;
import com.cyl.musiclake.ui.localmusic.contract.FoldersContract;
import com.cyl.musiclake.ui.localmusic.presenter.FoldersPresenter;
import com.cyl.musiclake.view.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by D22434 on 2018/1/8.
 */

public class FoldersFragment extends BaseFragment implements FoldersContract.View {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private FolderAdapter mAdapter;
    private FoldersPresenter mPresenter;

    @Override
    protected void initDatas() {
        mPresenter = new FoldersPresenter(getActivity());
        mPresenter.attachView(this);
        mPresenter.loadFolders();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    @Override
    public void initViews() {

//        if (Build.VERSION.SDK_INT < 21 && view.findViewById(R.id.status_bar) != null) {
//            view.findViewById(R.id.status_bar).setVisibility(View.GONE);
//            if (Build.VERSION.SDK_INT >= 19) {
//                int statusBarHeight = DensityUtil.getStatusBarHeight(getContext());
//                view.findViewById(R.id.toolbar).setPadding(0, statusBarHeight, 0, 0);
//            }
//        }
        mAdapter = new FolderAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter.bindToRecyclerView(recyclerView);
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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

    }

    @Override
    public void hideLoading() {

    }
}
