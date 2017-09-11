package com.cyl.music_hnust.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;


/**
 * Created by Monkey on 2015/6/29.
 */
public class LocalFragment extends BaseFragment {

    ViewPager viewPager;
    public static TabLayout mTabLayout;

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
        adapter.addFragment(new SongsFragment().newInstance(0), "歌曲");
        adapter.addFragment(new SongsFragment().newInstance(1), "专辑");
        adapter.addFragment(new SongsFragment().newInstance(2), "艺术家");
        viewPager.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_music;
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) rootView.findViewById(R.id.m_viewpager);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }
}
