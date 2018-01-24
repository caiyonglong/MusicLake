package com.cyl.musiclake.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.PageAdapter;
import com.cyl.musiclake.ui.localmusic.fragment.MyMusicFragment;
import com.cyl.musiclake.ui.onlinemusic.fragment.BaiduPlaylistFragment;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.m_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private PageAdapter mAdapter;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initDatas() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        final ActionBar toggle = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toggle != null) {
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
            toggle.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initViews() {
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);

        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
    }

    private void setupViewPager(ViewPager mViewPager) {
        mAdapter = new PageAdapter(getChildFragmentManager());
        mAdapter.addFragment(MyMusicFragment.newInstance(), "我的音乐");
        mAdapter.addFragment(BaiduPlaylistFragment.newInstance(), "发现");
//        adapter.addFragment(CommunityFragment.newInstance(1), "音乐湖");
        mViewPager.setAdapter(mAdapter);
    }

}

