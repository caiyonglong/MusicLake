package com.cyl.musiclake.ui.music.playlist

import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter

class PlaylistActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {
    private val TAG = javaClass.simpleName
    private var curTag = "全部"

    private val navigateLibrary = Runnable {
        val fragment = PlaylistFragment.newInstance(curTag)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss()
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_playlist
    }

    override fun initView() {

    }

    override fun setToolbarTitle(): String {
        return intent.getStringExtra("curTag")
    }

    override fun initData() {
        curTag = intent.getStringExtra("curTag")
        //开始加载
        navigateLibrary.run()
    }

    override fun initInjector() {

    }

}
