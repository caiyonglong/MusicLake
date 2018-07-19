package com.cyl.musiclake.ui.music.mv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musicapi.netease.MvInfoDetail;
import com.cyl.musicapi.netease.MvInfoDetailInfo;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.music.discover.MvListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class MvListFragment extends BaseFragment<MvListPresenter> implements MvListContract.View {

    private static final String TAG = "BaiduPlaylistFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private int mOffset = 0;
    private String mvType = "rank";

    //适配器
    private TopMvListAdapter mAdapter;
    private List<MvInfoDetail> mvList = new ArrayList<>();

    public static MvListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        MvListFragment fragment = new MvListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //适配器
        mAdapter = new TopMvListAdapter(mvList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.bindToRecyclerView(mRecyclerView);

        mAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            //成功获取更多数据
            mPresenter.loadMv(mvList.size());
        }, 1000), mRecyclerView);

    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
        if (getArguments() != null) {
            mvType = getArguments().getString("type");
        }
        mvList.clear();
        if (mvType.equals("rank")) {
            mPresenter.loadMv(0);
        } else {
            mAdapter.setEnableLoadMore(false);
            mPresenter.loadRecentMv(30);
        }
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mFragmentComponent.getActivity(), MvDetailActivity.class);
            intent.putExtra(Extras.MV_TITLE, mvList.get(position).getName());
            intent.putExtra(Extras.MV_ID, String.valueOf(mvList.get(position).getId()));
            startActivity(intent);
        });
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }


    @Override
    public void showMvList(List<MvInfoDetail> mvList) {
        this.mvList.addAll(mvList);
        mAdapter.setNewData(this.mvList);
    }

}