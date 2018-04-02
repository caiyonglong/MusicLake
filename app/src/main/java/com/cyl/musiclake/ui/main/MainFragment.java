package com.cyl.musiclake.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.PageAdapter;
import com.cyl.musiclake.ui.music.local.fragment.MyMusicFragment;
import com.cyl.musiclake.ui.music.online.fragment.BaiduPlaylistFragment;
import com.cyl.musiclake.ui.music.online.fragment.NeteasePlaylistFragment;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
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
    protected void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initViews() {
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            final ActionBar toggle = appCompatActivity.getSupportActionBar();
            if (toggle != null) {
                toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
                toggle.setDisplayHomeAsUpEnabled(true);
            }
        }
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
    }

    private void setupViewPager(ViewPager mViewPager) {
        mAdapter = new PageAdapter(getChildFragmentManager());
        mAdapter.addFragment(MyMusicFragment.newInstance(), "我的");
        mAdapter.addFragment(BaiduPlaylistFragment.newInstance(), "百度");
        mAdapter.addFragment(NeteasePlaylistFragment.newInstance(), "网易");
        mViewPager.setAdapter(mAdapter);
    }

}

