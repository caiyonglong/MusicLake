package com.cyl.musiclake.ui.music.playlist

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.main.PageAdapter
import kotlinx.android.synthetic.main.activity_all_playlist.*
import java.util.*

class AllPlaylistActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    var mViewPager: ViewPager? = null
    var mTabLayout: TabLayout? = null

    private val cateList = ArrayList(Arrays.asList("推荐", "精品", "华语", "民谣", "摇滚", "流行", "古风", "日语"))

    override fun getLayoutResID(): Int {
        return R.layout.activity_all_playlist
    }

    override fun initView() {
        mViewPager = findViewById(R.id.m_viewpager)
        mTabLayout = findViewById(R.id.tabs)

        moreIv.setOnClickListener {
            toCatTagAll()
        }
    }

    override fun setToolbarTitle(): String {
        return getString(R.string.playlist_square)
    }

    override fun initData() {
        mTabLayout?.setupWithViewPager(mViewPager)
        mViewPager?.currentItem = 0
        mViewPager?.let {
            setupViewPager(it)
        }
        mViewPager?.offscreenPageLimit = cateList.size
    }

    override fun initInjector() {

    }


    /**
     * 显示所有分类
     */
    private fun toCatTagAll() {
        AllCategoryFragment().apply {
            curCateList = cateList
            successListener = { result ->
            }
        }.showIt(this)
    }

    private fun setupViewPager(mViewPager: ViewPager) {
        val mAdapter = PageAdapter(supportFragmentManager)
        for (i in cateList.indices) {
            when (i) {
                0 -> mAdapter.addFragment(PlaylistFragment.newInstance("全部"), cateList[i])
                1 -> mAdapter.addFragment(TopPlaylistFragment.newInstance(), cateList[i])
                else -> mAdapter.addFragment(PlaylistFragment.newInstance(cateList[i]), cateList[i])
            }
        }
        mViewPager.adapter = mAdapter
    }

}
