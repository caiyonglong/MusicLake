package com.cyl.musiclake.ui.music.mv

import android.content.Intent
import android.os.Bundle
import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.bean.VideoInfoBean
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.player.exoplayer.ExoPlayerManager
import com.cyl.musiclake.player.exoplayer.ExoPlayerManager.bindView
import com.cyl.musiclake.player.exoplayer.ExoPlayerManager.setDataSource
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.main.PageAdapter
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.frag_artist_detail.tabs
import kotlinx.android.synthetic.main.frag_artist_detail.viewPager
import java.util.*

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class VideoDetailActivity : BaseActivity<VideoDetailPresenter?>(), VideoDetailContract.View {
    private val mvInfoDetails: List<MvInfoDetail> = ArrayList()
    private var mVid: String = ""
    private var mType: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        mVid = intent.getStringExtra(Extras.VIDEO_VID)?:""
        mType = intent.getIntExtra(Extras.VIDEO_TYPE, 1)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_video_detail
    }

    override fun initView() {
        tabs?.setupWithViewPager(viewPager)
        setupViewPager()
        viewPager?.offscreenPageLimit = 2
    }

    override fun initData() {
        mPresenter?.loadMvUrl(mType, mVid)
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun retryLoading() {
        super.retryLoading()
        initData()
    }

    override fun showVideoInfoList(mvList: MutableList<VideoInfoBean>) {
    }

    override fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean?) {}
    override fun showMvDetailInfo(mvInfoDetailInfo: VideoInfoBean?) {
    }

    override fun showMvUrlInfo(mvUrl: String?) {
        setDataSource(mvUrl)
        bindView(videoView)
    }

    override fun showMvHotComment(mvHotCommentInfo: List<CommentsItemInfo>) {

    }

    override fun showMvComment(mvCommentInfo: List<CommentsItemInfo>) {
    }

    private fun setupViewPager() {
        val mAdapter = PageAdapter(supportFragmentManager)
        mAdapter.addFragment(VideoDetailFragment.newInstance(mVid, mType), "详情")
        mAdapter.addFragment(VideoCommentFragment.newInstance(mVid, mType), "评论")
        viewPager?.adapter = mAdapter
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getStringExtra(Extras.VIDEO_VID)?.let {
            mVid = it
        }
        intent?.getIntExtra(Extras.VIDEO_TYPE, 1)?.let {
            mType = it
        }
        setupViewPager()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerManager.stop()
    }

    companion object {
        private const val TAG = "MvDetailActivity"
    }
}