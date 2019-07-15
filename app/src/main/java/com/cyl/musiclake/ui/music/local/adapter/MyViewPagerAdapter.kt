package com.cyl.musiclake.ui.music.local.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by yonglong on 2015/6/29.
 */
class MyViewPagerAdapter(fm: androidx.fragment.app.FragmentManager, private var mFragments: MutableList<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    fun setFragments(mFragments: MutableList<androidx.fragment.app.Fragment>) {
        this.mFragments = mFragments
    }
}
