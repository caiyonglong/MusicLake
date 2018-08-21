package com.cyl.musiclake.ui.music.discover;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.bean.Artist;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class AllListFragment extends BaseFragment {

    private static final String TAG = "NeteasePlaylistFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    //适配器
    private ArtistListAdapter mArtistAdapter;
    private PlaylistAdapter mPlaylistAdapter;

    private String type;

    @Override
    protected String getToolBarTitle() {
        type = getArguments().getString(Extras.PLAYLIST, Constants.NETEASE_ARITIST_LIST);
        if (type.equals(Constants.NETEASE_ARITIST_LIST))
            return getString(R.string.hot_artist);
        else
            return getString(R.string.radio);
    }

    public static AllListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Extras.PLAYLIST, type);
        AllListFragment fragment = new AllListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void initViews() {
        type = getArguments().getString(Extras.PLAYLIST, Constants.NETEASE_ARITIST_LIST);
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void loadData() {
        if (type.equals(Constants.NETEASE_ARITIST_LIST)) {
            //适配器
            mArtistAdapter = new ArtistListAdapter(DiscoverPresenter.Companion.getArtistList());
            mRecyclerView.setAdapter(mArtistAdapter);
            mArtistAdapter.bindToRecyclerView(mRecyclerView);

            mArtistAdapter.setOnItemClickListener((adapter, view, position) -> {
                Artist artist = (Artist) adapter.getData().get(position);
                NavigationHelper.INSTANCE.navigateToPlaylist(mFragmentComponent.getActivity(), artist);
            });
        } else if (type.equals(Constants.BAIDU_RADIO_LIST)) {
            //适配器
            mPlaylistAdapter = new PlaylistAdapter(DiscoverPresenter.Companion.getRadioList());
            mRecyclerView.setAdapter(mArtistAdapter);
            mPlaylistAdapter.bindToRecyclerView(mRecyclerView);

            mPlaylistAdapter.setOnItemClickListener((adapter, view, position) -> {
                NavigationHelper.INSTANCE.navigateToPlaylist(mFragmentComponent.getActivity(), DiscoverPresenter.Companion.getRadioList().get(position), null);
            });
        }
    }

    @Override
    protected void listener() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));
    }
}