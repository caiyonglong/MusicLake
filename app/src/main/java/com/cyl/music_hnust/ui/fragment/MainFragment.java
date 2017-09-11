package com.cyl.music_hnust.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.m_viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        final ActionBar toggle = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        toggle.setDisplayHomeAsUpEnabled(true);


        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(3);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initViews() {
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new LocalFragment(), "本地音乐");
        adapter.addFragment(new OnlineFragment(), "在线音乐");
        adapter.addFragment(new CommunityFragment().newInstance(1), "音乐湖");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

