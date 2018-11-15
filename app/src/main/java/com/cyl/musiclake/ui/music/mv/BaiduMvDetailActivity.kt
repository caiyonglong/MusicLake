package com.cyl.musiclake.ui.music.mv

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.view.custom.DisplayUtils
import com.devbrackets.android.exomedia.listener.OnPreparedListener
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.activity_mv_detail.*
import kotlinx.android.synthetic.main.exomedia_default_controls_mobile.*
import java.util.*

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class BaiduMvDetailActivity : BaseActivity<MvDetailPresenter>(), MvDetailContract.View, OnPreparedListener {

    private val mvInfoDetails = ArrayList<MvInfoDetail>()
    private var mAdapter: SimiMvListAdapter? = null
    private var mCommentAdapter: MvCommentAdapter? = null
    private var mHotCommentAdapter: MvCommentAdapter? = null

    //是否是横屏
    internal var isPortrait = true

    private val fullscreenUiFlags: Int
        get() = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

    private val stableUiFlags: Int
        get() = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

    private fun fullscreen() {
        if (isPortrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val ll = video_view.getLayoutParams()
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT
            ll.height = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = false
            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_exit_white_36dp)
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val ll = video_view.getLayoutParams()
            ll.height = DisplayUtils.dp2px(200f)
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = true
            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_white)
        }
    }

    override fun onBackPressed() {
        if (!isPortrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val ll = video_view.getLayoutParams()
            ll.height = DisplayUtils.dp2px(200f)
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = true
            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_white)
        } else {
            super.onBackPressed()
        }
    }


    override fun getLayoutResID(): Int {
        return R.layout.activity_mv_detail
    }

    override fun initView() {
    }

    override fun setToolbarTitle(): String? {
        return intent.getStringExtra(Extras.MV_TITLE)
    }

    override fun initData() {
        val mVid = intent.getStringExtra(Extras.MV_ID)
        showLoading()
        mPresenter?.loadBaiduMvInfo(mVid)
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun listener() {
        fullscreenIv.setOnClickListener {
            fullscreen()
        }

    }

    override fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean?) {
        if (mvInfoBean?.uri != null) {
            nestedScrollView.visibility = View.VISIBLE
            initPlayer()
            //For now we just picked an arbitrary item to play
            video_view.setPreviewImage(Uri.parse(mvInfoBean.picUrl))
            video_view.setVideoURI(Uri.parse(mvInfoBean.uri))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }


    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showError(message: String, showRetryButton: Boolean) {
        super.showError(message, showRetryButton)
    }

    override fun retryLoading() {
        super.retryLoading()
        initData()
    }

    override fun showMvList(mvList: List<MvInfoDetail>) {
        mAdapter!!.setNewData(mvList)
        mAdapter!!.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, BaiduMvDetailActivity::class.java)
            intent.putExtra(Extras.MV_TITLE, mvList[position.toInt()].name)
            intent.putExtra(Extras.MV_ID, mvList[position.toInt()].id.toString())
            startActivity(intent)
            finish()
        }
    }

    override fun showMvDetailInfo(mvInfoDetailInfo: MvInfoDetailInfo?) {
        hideLoading()
        if (mvInfoDetailInfo != null && mvInfoDetailInfo.brs.p720 != null) {
            nestedScrollView.visibility = View.VISIBLE
            val url = mvInfoDetailInfo.brs.p720
            initPlayer()
            //For now we just picked an arbitrary item to play
            video_view.setPreviewImage(Uri.parse(mvInfoDetailInfo.cover))
            video_view.setVideoURI(Uri.parse(url))
        }
    }

    override fun showMvHotComment(mvHotCommentInfo: List<CommentsItemInfo>) {

    }

    override fun showMvComment(mvCommentInfo: List<CommentsItemInfo>) {

    }

    private fun initPlayer() {
        video_view.visibility = View.VISIBLE
        video_view.setOnPreparedListener(this)
        video_view.setRepeatMode(Player.REPEAT_MODE_ONE)
    }

    override fun onPrepared() {
        video_view.start()
    }

    companion object {

        private val TAG = "MvDetailActivity"
    }
}
