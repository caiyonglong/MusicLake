package com.cyl.music_hnust.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.MainActivity;
import com.cyl.music_hnust.adapter.ViewPagerAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;

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
        mToolbar.setTitle("湖科音乐");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        DrawerLayout mDrawerLayout = MainActivity.getmDrawerLayout();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);

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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SongsFragment(), "本地音乐");
        adapter.addFragment(new OnlineFragment(), "在线音乐");
        adapter.addFragment(new PlaylistFragment(), "我的歌单");
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

}

