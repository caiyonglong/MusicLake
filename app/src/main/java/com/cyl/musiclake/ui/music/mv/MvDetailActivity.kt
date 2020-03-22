package com.cyl.musiclake.ui.music.mv

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper.navigateToArtist
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.utils.DisplayUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import com.music.lake.musiclib.playback.PlaybackListener
import com.music.lake.musiclib.player.MusicExoPlayer
import kotlinx.android.synthetic.main.activity_mv_detail.*
import kotlinx.android.synthetic.main.exo_player_control_view.*
import java.util.*

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class MvDetailActivity : BaseActivity<MvDetailPresenter?>(), MvDetailContract.View, PlaybackListener {
    private val mvInfoDetails: List<MvInfoDetail> = ArrayList()
    private var mAdapter: SimiMvListAdapter? = null
    private var mCommentAdapter: MvCommentAdapter? = null
    private var mHotCommentAdapter: MvCommentAdapter? = null
    private val brs: Map<String, String> = HashMap()
    //是否是横屏
    var isPortrait = true

    private var musicExoPlayer: MusicExoPlayer? = null

    fun fullscreen() {
        if (isPortrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val ll = video_view!!.layoutParams
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT
            ll.height = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = false
            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_exit)
//            brsTv.visibility = View.VISIBLE
            //设置全屏
            window.decorView.systemUiVisibility = fullscreenUiFlags
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val ll = video_view.layoutParams
            ll.height = DisplayUtils.dp2px(200f)
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = true
            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_white)
//            brsTv.visibility = View.GONE
            //设置全屏
            window.decorView.systemUiVisibility = stableUiFlags
        }
    }

    override fun onBackPressed() {
        if (!isPortrait) {
            fullscreen()
        } else {
            super.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_mv_detail
    }

    override fun initView() {
        mAdapter = SimiMvListAdapter(mvInfoDetails)
        mAdapter?.setEnableLoadMore(true)
        initUiFlags()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        similarMvRcv.layoutManager = layoutManager
        similarMvRcv.adapter = mAdapter
        similarMvRcv.isNestedScrollingEnabled = false
        mAdapter?.bindToRecyclerView(similarMvRcv)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                Log.e(TAG, "$scrollY---$oldScrollY")
                if (scrollY in 301..400) {
                    singerView.visibility = View.VISIBLE
                    singerView.alpha = (scrollY - 300) * 0.1f
                } else if (scrollY < 300 && singerView.visibility == View.VISIBLE) {
                    singerView.visibility = View.GONE
                }
            })
        }
    }

    override fun setToolbarTitle(): String {
        return intent.getStringExtra(Extras.MV_TITLE)
    }

    override fun initData() {
        val mVid = intent.getStringExtra(Extras.MV_ID)
        showLoading()
        initPlayer()
        mPresenter?.loadMvDetail(mVid)
        mPresenter?.loadSimilarMv(mVid)
        mPresenter?.loadMvComment(mVid)
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun listener() {
        fullscreenIv.setOnClickListener {
            fullscreen()
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
        mAdapter?.setNewData(mvList)
        mAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val intent = Intent(this, MvDetailActivity::class.java)
            intent.putExtra(Extras.MV_TITLE, mvList[position].name)
            intent.putExtra(Extras.MV_ID, mvList[position].id.toString())
            startActivity(intent)
            finish()
        }
    }

    override fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean) {}
    override fun showMvDetailInfo(mvInfoDetailInfo: MvInfoDetailInfo?) {
        hideLoading()
        //优先1080 720 480
        if (mvInfoDetailInfo != null) {
            var url: String? = null
            when {
                mvInfoDetailInfo.brs.p1080 != null -> {
                    url = mvInfoDetailInfo.brs.p1080
                }
                mvInfoDetailInfo.brs.p720 != null -> {
                    url = mvInfoDetailInfo.brs.p720
                }
                mvInfoDetailInfo.brs.p480 != null -> {
                    url = mvInfoDetailInfo.brs.p480
                }
                mvInfoDetailInfo.brs.p240 != null -> {
                    url = mvInfoDetailInfo.brs.p240
                }
            }
            nestedScrollView.visibility = View.VISIBLE
            LogUtil.d(TAG, "url = $url")
            if (url == null) {
                ToastUtils.show("播放地址异常，请稍后重试！")
            } else {
                playVideo(url)
                updateMvInfo(mvInfoDetailInfo)
            }
        }
    }

    private fun playVideo(url: String) {
        musicExoPlayer?.setDataSource(url)
    }

    override fun showMvHotComment(mvHotCommentInfo: List<CommentsItemInfo>) {
        if (mHotCommentAdapter == null) {
            mHotCommentAdapter = MvCommentAdapter(mvHotCommentInfo)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            //初始化评论adapter
            hotCommentRcv.layoutManager = layoutManager
            hotCommentRcv.adapter = mHotCommentAdapter
            hotCommentRcv.isNestedScrollingEnabled = false
            mHotCommentAdapter?.bindToRecyclerView(hotCommentRcv)
        } else {
            mHotCommentAdapter?.notifyDataSetChanged()
        }
    }

    override fun showMvComment(mvCommentInfo: List<CommentsItemInfo>) {
        if (mCommentAdapter == null) {
            mCommentAdapter = MvCommentAdapter(mvCommentInfo)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            //初始化评论adapter
            rv_comment.layoutManager = layoutManager
            rv_comment.adapter = mCommentAdapter
            rv_comment.isNestedScrollingEnabled = false
            mCommentAdapter?.bindToRecyclerView(rv_comment)
        } else {
            mCommentAdapter?.notifyDataSetChanged()
        }
    }

    fun updateMvInfo(info: MvInfoDetailInfo) {
        tv_play_count.text = getString(R.string.play_count, info.playCount)
        tv_like_count.text = info.likeCount.toString()
        tv_share_count.text = info.shareCount.toString()
        tv_collect_count.text = info.subCount.toString()
        tv_comment_count.text = info.commentCount.toString()
        titleTv.text = info.name
        artistTv.text = info.artistName
        singerTv.text = info.artistName
        tv_mv_detail.text = info.desc
        publishTimeTv.text = getString(R.string.publish_time, info.publishTime)
        updateTitle(info.name)
        singerView.setOnClickListener { v: View? ->
            val artist = Artist()
            artist.artistId = info.artistId.toString()
            artist.type = Constants.NETEASE
            artist.name = info.artistName
            navigateToArtist(this, artist, null)
        }
    }

    private fun initPlayer() {
        musicExoPlayer = MusicExoPlayer(this)
        video_view.visibility = View.VISIBLE
        musicExoPlayer?.bindView(video_view)
        musicExoPlayer?.setPlayBackListener(this)
    }


    /**
     * Listens to the system to determine when to show the default controls
     * for the [VideoView]
     */
    private inner class FullScreenListener : View.OnSystemUiVisibilityChangeListener {
        var lastVisibility = 0
        override fun onSystemUiVisibilityChange(visibility: Int) {
            lastVisibility = visibility
            if (visibility == 0 && View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                video_view.showController()
            }
        }
    }

    private val fullScreenListener = FullScreenListener()
    /**
     * Correctly sets up the fullscreen flags to avoid popping when we switch
     * between fullscreen and not
     */
    private fun initUiFlags() {
        window.decorView.systemUiVisibility = stableUiFlags
//        window.decorView.setOnSystemUiVisibilityChangeListener(fullScreenListener)
    }

    private val fullscreenUiFlags: Int
        private get() = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

    private val stableUiFlags: Int
        private get() = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

    override fun onDestroy() {
        super.onDestroy()
        musicExoPlayer?.setPlayBackListener(null)
        musicExoPlayer?.release()
    }

    override fun onPlayerStateChanged(isPlaying: Boolean) {
        exo_play.visibility = if (isPlaying) View.GONE else View.VISIBLE
        exo_pause.visibility = if (isPlaying) View.VISIBLE else View.GONE
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
    }

    override fun onPrepared() {
    }

    override fun onCompletionNext() {
    }

    override fun onLoading(isLoading: Boolean) {
    }

    override fun onError() {
    }

    override fun onPlaybackProgress(position: Long, duration: Long, buffering: Long) {
    }

    override fun onCompletionEnd() {
    }
}