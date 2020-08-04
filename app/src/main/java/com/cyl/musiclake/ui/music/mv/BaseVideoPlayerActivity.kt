package com.cyl.musiclake.ui.music.mv

import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.bean.VideoInfoBean
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.utils.DisplayUtils
import com.cyl.musiclake.utils.LogUtil
import com.google.android.exoplayer2.ui.PlayerView

/**
 * 作者：yonglong
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 描述：视频播放基础类，负责视频播放相关操作
 */
abstract class BaseVideoPlayerActivity : BaseActivity<VideoDetailPresenter>(), VideoDetailContract.View {

    private val fullScreenListener = FullScreenListener()

    private var playerView: PlayerView? = null

    //是否是横屏
    internal var isPortrait = true

    private val fullscreenUiFlags = (View.SYSTEM_UI_FLAG_LOW_PROFILE
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

    private val stableUiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

    private fun fullscreen() {
        if (isPortrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val ll = playerView?.layoutParams
            ll?.width = ViewGroup.LayoutParams.MATCH_PARENT
            ll?.height = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = false
//            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_exit)
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val ll = playerView?.layoutParams
            ll?.height = DisplayUtils.dp2px(200f)
            ll?.width = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = true
//            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_white)
        }
    }

    override fun onBackPressed() {
        if (!isPortrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val ll = playerView?.layoutParams
            ll?.height = DisplayUtils.dp2px(200f)
            ll?.width = ViewGroup.LayoutParams.MATCH_PARENT
            isPortrait = true
//            fullscreenIv.setImageResource(R.drawable.ic_fullscreen_white)
            exitFullscreen()
        } else {
            super.onBackPressed()
        }
    }

    fun setPlayerView(playerView: PlayerView) {
        this.playerView = playerView
    }

    override fun initView() {

    }

    override fun setToolbarTitle(): String? {
        return intent.getStringExtra(Extras.MV_TITLE)
    }

    override fun initData() {
        val mVid = intent.getStringExtra(Extras.VIDEO_VID)
        showLoading()
        mVid?.let {
            mPresenter?.loadBaiduMvInfo(mVid)
        }

        //加载本地视频
        intent.getStringExtra(Extras.VIDEO_PATH)?.let {
            initPlayer()
            LogUtil.d(TAG, "url = $it")
        }
    }

    override fun retryLoading() {
        super.retryLoading()
        initData()
    }

    override fun showMvDetailInfo(mvInfoDetailInfo: VideoInfoBean?) {
        hideLoading()
    }

    private fun initPlayer() {
    }

    /**
     * Correctly sets up the fullscreen flags to avoid popping when we switch
     * between fullscreen and not
     */
    private fun initUiFlags() {
        window.decorView.systemUiVisibility = stableUiFlags
        window.decorView.setOnSystemUiVisibilityChangeListener(fullScreenListener)
    }

    /**
     * Applies the correct flags to the windows decor view to enter
     * or exit fullscreen mode
     *
     * @param fullscreen True if entering fullscreen mode
     */
    private fun setUiFlags(fullscreen: Boolean) {
        window.decorView.systemUiVisibility = if (fullscreen) fullscreenUiFlags else stableUiFlags
    }


    private fun goFullscreen() {
        setUiFlags(true)
    }

    private fun exitFullscreen() {
        setUiFlags(false)
    }

    /**
     * Listens to the system to determine when to show the default controls
     * for the [VideoView]
     */
    private inner class FullScreenListener : View.OnSystemUiVisibilityChangeListener {
        var lastVisibility = 0
            private set

        override fun onSystemUiVisibilityChange(visibility: Int) {
            lastVisibility = visibility
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        // Resets the flags
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

}
