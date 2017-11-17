package com.cyl.music_hnust.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;

import butterknife.BindView;


/**
 * Created by Monkey on 2015/6/29.
 */
public class LocalFragment extends BaseFragment {
    @BindView(R.id.m_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    public static LocalFragment newInstance() {

        Bundle args = new Bundle();

        LocalFragment fragment = new LocalFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(3);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        MainFragment.Adapter adapter = new MainFragment.Adapter(getChildFragmentManager());
        adapter.addFragment( SongsFragment.newInstance(), "歌曲");
        adapter.addFragment( AlbumFragment.newInstance(), "专辑");
        adapter.addFragment( ArtistFragment.newInstance(), "艺术家");
        viewPager.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_music;
    }

    @Override
    public void initViews() {
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }
}
