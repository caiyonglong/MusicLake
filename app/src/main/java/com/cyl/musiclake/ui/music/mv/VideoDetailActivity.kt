package com.cyl.musiclake.ui.music.mv

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.common.Extras
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
class VideoDetailActivity : BaseActivity<MvDetailPresenter?>(), MvDetailContract.View {
    private val mvInfoDetails: List<MvInfoDetail> = ArrayList()
    private var mAdapter: SimiMvListAdapter? = null
    private var mCommentAdapter: MvCommentAdapter? = null
    private var mHotCommentAdapter: MvCommentAdapter? = null
    private val brs: Map<String, String> = HashMap()
    private var mVid: String = ""
    private var mType: Int = 1

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

    override fun showMvList(mvList: List<MvInfoDetail>) {
        mAdapter?.setNewData(mvList)
        mAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val intent = Intent(this, VideoDetailActivity::class.java)
            intent.putExtra(Extras.MV_TITLE, mvList[position].name)
            intent.putExtra(Extras.VIDEO_VID, mvList[position].id.toString())
            startActivity(intent)
            finish()
        }
    }

    override fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean?) {}
    override fun showMvDetailInfo(mvInfoDetailInfo: MvInfoDetailInfo?) {
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
        mVid = intent.getStringExtra(Extras.VIDEO_VID)
        mType = intent.getIntExtra(Extras.VIDEO_TYPE, 1)

        val mAdapter = PageAdapter(supportFragmentManager)
        mAdapter.addFragment(VideoDetailFragment.newInstance(mVid, mType), "详情")
        mAdapter.addFragment(VideoCommentFragment.newInstance(mVid, mType), "评论")
        viewPager?.adapter = mAdapter
    }


    companion object {
        private const val TAG = "MvDetailActivity"
    }
}