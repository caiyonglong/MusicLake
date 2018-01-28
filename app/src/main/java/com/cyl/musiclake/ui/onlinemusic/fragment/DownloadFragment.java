package com.cyl.musiclake.ui.onlinemusic.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.localmusic.adapter.ViewPagerAdapter;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/26.
 */

public class DownloadFragment extends BaseFragment {
    //Toolbar
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.m_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    public static DownloadFragment newInstance() {

        Bundle args = new Bundle();

        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initDatas() {
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);

        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(3);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initViews() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(DownloadedFragment.newInstance(), "已下载");
        adapter.addFragment(DownloadManagerFragment.newInstance(), "正在下载");
        viewPager.setAdapter(adapter);
    }

}
