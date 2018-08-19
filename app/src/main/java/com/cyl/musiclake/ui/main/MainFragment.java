package com.cyl.musiclake.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.ui.music.discover.DiscoverFragment;
import com.cyl.musiclake.ui.music.local.fragment.MyMusicFragment;
import com.cyl.musiclake.ui.music.mv.MvFragment;
import com.cyl.musiclake.ui.music.mv.MvListFragment;
import com.cyl.musiclake.view.custom.DisplayUtils;

import java.lang.reflect.Field;

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
            mToolbar.setTitle(R.string.app_name);
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            final ActionBar toggle = appCompatActivity.getSupportActionBar();
            if (toggle != null) {
                toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white);
                toggle.setDisplayHomeAsUpEnabled(true);
            }
        }
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void initInjector() {

    }

    private void setupViewPager(ViewPager mViewPager) {
        PageAdapter mAdapter = new PageAdapter(getChildFragmentManager());
        mAdapter.addFragment(MyMusicFragment.Companion.newInstance(), "我的");
        mAdapter.addFragment(DiscoverFragment.newInstance(), "发现");
        mAdapter.addFragment(MvListFragment.newInstance("rank"), "MV排行榜");
        mAdapter.addFragment(MvListFragment.newInstance("recently"), "最新MV");
        mViewPager.setAdapter(mAdapter);
    }

}

