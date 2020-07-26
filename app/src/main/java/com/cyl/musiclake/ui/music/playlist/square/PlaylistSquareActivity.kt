package com.cyl.musiclake.ui.music.playlist.square

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.main.PageAdapter
import com.cyl.musiclake.ui.music.playlist.PlaylistFragment
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.activity_all_playlist.*

class PlaylistSquareActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {
    var mViewPager: androidx.viewpager.widget.ViewPager? = null
    var mTabLayout: TabLayout? = null

    private var cateList = mutableListOf("推荐", "精品", "华语", "民谣", "摇滚", "流行", "古风", "日语")

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
        setupViewPager()
        mViewPager?.offscreenPageLimit = cateList.size
    }

    override fun initInjector() {

    }


    /**
     * 显示所有分类
     */
    private fun toCatTagAll() {
        AllPlaylistCatFragment().apply {
            curCateList = cateList
            successListener = { result ->
                //更新viewpager
                LogUtil.d(TAG,"更新list")
                this@PlaylistSquareActivity.cateList = curCateList
                this@PlaylistSquareActivity.setupViewPager()
            }
        }.showIt(this)
    }

    private fun setupViewPager() {
        val mAdapter = PageAdapter(supportFragmentManager)
        for (i in cateList.indices) {
            when (i) {
                0 -> mAdapter.addFragment(PlaylistFragment.newInstance("全部"), cateList[i])
                1 -> mAdapter.addFragment(TopPlaylistFragment.newInstance(), cateList[i])
                else -> mAdapter.addFragment(PlaylistFragment.newInstance(cateList[i]), cateList[i])
            }
        }
        mViewPager?.adapter = mAdapter
    }

}
