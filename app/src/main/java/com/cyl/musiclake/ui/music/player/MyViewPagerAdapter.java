package com.cyl.musiclake.ui.music.player;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void setFragments(List<Fragment> mFragments) {
        this.mFragments = mFragments;
    }
}
