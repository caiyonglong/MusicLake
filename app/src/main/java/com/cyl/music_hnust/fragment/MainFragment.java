package com.cyl.music_hnust.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class MainFragment extends BaseFragment {

    ViewPager viewPager;
    public static TabLayout mTabLayout;
    Toolbar mToolbar;

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
//        mToolbar.setTitle("湖科音乐");
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

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        viewPager = (ViewPager) rootView.findViewById(R.id.m_viewpager);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }
    public static void initTab(int color){
        mTabLayout.setBackgroundResource(color);
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

