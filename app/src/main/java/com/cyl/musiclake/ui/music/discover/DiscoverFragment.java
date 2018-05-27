package com.cyl.musiclake.ui.music.discover;

import android.os.Bundle;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.ui.music.online.fragment.NeteasePlaylistFragment;

import butterknife.OnClick;


/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class DiscoverFragment extends BaseLazyFragment<DiscoverPresenter> {

    private static final String TAG = "FoundFragment";

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
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void listener() {
    }

    @Override
    public void onLazyLoad() {

    }
}