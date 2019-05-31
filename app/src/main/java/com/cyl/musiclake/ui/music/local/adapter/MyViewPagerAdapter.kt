package com.cyl.musiclake.ui.music.local.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by yonglong on 2015/6/29.
 */
class MyViewPagerAdapter(fm: FragmentManager, private var mFragments: MutableList<Fragment>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    fun setFragments(mFragments: MutableList<Fragment>) {
        this.mFragments = mFragments
    }
}
