package com.cyl.musiclake.ui.music.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.ui.music.online.activity.BaiduMusicListActivity;
import com.cyl.musiclake.ui.music.online.activity.NeteasePlaylistActivity;
import com.cyl.musiclake.ui.music.online.adapter.OnlineAdapter;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;
import com.cyl.musiclake.ui.music.online.fragment.NeteasePlaylistFragment;
import com.cyl.musiclake.ui.music.online.presenter.OnlinePlaylistPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class DiscoverFragment extends BaseLazyFragment<DiscoverPresenter> implements DiscoverContract.View {

    private static final String TAG = "FoundFragment";

    private OnlineAdapter mBaiduAdapter;
    private TopListAdapter mNeteaseAdapter;
    private List<Playlist> playlist = new ArrayList<>();

    @BindView(R.id.baiChartsRv)
    RecyclerView mBaiChartsRv;

    @BindView(R.id.wangChartsRv)
    RecyclerView mWangChartsRv;


    @OnClick(R.id.baiChartsTv)
    void toBaidu() {
        NavigationHelper.navigateFragment(getActivity(), NeteasePlaylistFragment.newInstance());
    }

    @OnClick(R.id.wanChartsTv)
    void toNetease() {
        NavigationHelper.navigateFragment(getActivity(), NeteasePlaylistFragment.newInstance());
    }

    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();

        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_discover;
    }

    @Override
    public void initViews() {
        //初始化列表
        mBaiChartsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //适配器
        mBaiduAdapter = new OnlineAdapter(playlist);
        mBaiChartsRv.setAdapter(mBaiduAdapter);
        mBaiChartsRv.setFocusable(false);
        mBaiChartsRv.setNestedScrollingEnabled(false);
        mBaiduAdapter.bindToRecyclerView(mBaiChartsRv);

        mWangChartsRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //适配器
        mNeteaseAdapter = new TopListAdapter(playlist);
        mWangChartsRv.setAdapter(mNeteaseAdapter);
        mWangChartsRv.setFocusable(false);
        mWangChartsRv.setNestedScrollingEnabled(false);
        mNeteaseAdapter.bindToRecyclerView(mWangChartsRv);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
        mPresenter.loadBaidu();
        mPresenter.loadNetease();
    }

    @Override
    protected void listener() {
        mBaiduAdapter.setOnItemClickListener((adapter, view, position) -> {
            Playlist playlist = (Playlist) adapter.getItem(position);
            Intent intent = new Intent(getActivity(), BaiduMusicListActivity.class);
            intent.putExtra(Extras.BILLBOARD_TITLE, playlist.getName());
            intent.putExtra(Extras.BILLBOARD_DESC, playlist.getDes());
            intent.putExtra(Extras.BILLBOARD_ALBUM, playlist.getCoverUrl());
            intent.putExtra(Extras.BILLBOARD_TYPE, playlist.getId());
            startActivity(intent);
        });

        mNeteaseAdapter.setOnItemClickListener((adapter, view, position) -> {
            Playlist playlist = (Playlist) adapter.getData().get(position);
            Intent intent = new Intent(getActivity(), NeteasePlaylistActivity.class);
            intent.putExtra("title", playlist.getName());
            intent.putExtra("id", playlist.getId());
            startActivity(intent);
        });
    }

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showBaiduCharts(List<Playlist> charts) {
        mBaiduAdapter.setNewData(charts);
    }

    @Override
    public void showNeteaseCharts(List<Playlist> charts) {
        mNeteaseAdapter.setNewData(charts);
    }
}