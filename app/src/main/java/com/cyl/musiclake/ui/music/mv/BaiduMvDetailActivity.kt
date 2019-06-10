package com.cyl.musiclake.ui.music.mv

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
import com.cyl.musiclake.utils.DisplayUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import com.devbrackets.android.exomedia.listener.OnPreparedListener
import com.devbrackets.android.exomedia.listener.VideoControlsVisibilityListener
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_video.video_view
import kotlinx.android.synthetic.main.exomedia_default_controls_mobile.*

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class BaiduMvDetailActivity : BaseActivity<MvDetailPresenter>(), MvDetailContract.View, OnPreparedListener {


    private val fullScreenListener = FullScreenListener()

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
            exitFullscreen()
        } else {
            super.onBackPressed()
        }
    }


    override fun getLayoutResID(): Int {
        return R.layout.activity_video
    }

    override fun initView() {

        video_view.videoControls?.setVisibilityListener(ControlsVisibilityListener())
    }

    override fun setToolbarTitle(): String? {
        return intent.getStringExtra(Extras.MV_TITLE)
    }

    override fun initData() {
        val mVid = intent.getStringExtra(Extras.MV_ID)
        showLoading()
        mVid?.let {
            mPresenter?.loadBaiduMvInfo(mVid)
        }

        //加载本地视频
        intent.getStringExtra(Extras.VIDEO_PATH)?.let {
            initPlayer()
            LogUtil.d(TAG, "url = $it")
            video_view.setVideoURI(Uri.parse(it))
            video_view.setOnPreparedListener {
                video_view.start()
                hideLoading()
            }
        }
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
            LogUtil.d(TAG, "url = ${mvInfoBean.uri}")
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
    }

    override fun showMvDetailInfo(mvInfoDetailInfo: MvInfoDetailInfo?) {
        hideLoading()
        if (mvInfoDetailInfo != null) {
            val url = when {
                mvInfoDetailInfo.brs.p1080 != null -> mvInfoDetailInfo.brs.p1080
                mvInfoDetailInfo.brs.p720 != null -> mvInfoDetailInfo.brs.p720
                mvInfoDetailInfo.brs.p480 != null -> mvInfoDetailInfo.brs.p480
                mvInfoDetailInfo.brs.p240 != null -> mvInfoDetailInfo.brs.p240
                else -> {
                    ""
                }
            }
            if (url == "") {
                ToastUtils.show(getString(R.string.mv_path_error))
                return
            }
            LogUtil.d(TAG, "url = $url")
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
//        window.decorView.systemUiVisibility = if (fullscreen) fullscreenUiFlags else stableUiFlags
    }


    private fun goFullscreen() {
        setUiFlags(true)
    }

    private fun exitFullscreen() {
        setUiFlags(false)
    }

    override fun onPrepared() {
        video_view.start()
    }


    /**
     * Listens to the system to determine when to show the default controls
     * for the [VideoView]
     */
    private inner class FullScreenListener : View.OnSystemUiVisibilityChangeListener {
        var lastVisibility = 0
            private set

        override fun onSystemUiVisibilityChange(visibility: Int) {
            // NOTE: if the screen is double tapped in just the right way (or wrong way)
            // the SYSTEM_UI_FLAG_HIDE_NAVIGATION flag is dropped. Because of this we
            // no longer get notified of the temporary change when the screen is tapped
            // (i.e. the VideoControls get the touch event instead of the OS). So we store
            // the visibility off for use in the ControlsVisibilityListener for verification
            lastVisibility = visibility
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                video_view.showControls()
            }
        }
    }

    /**
     * A Listener for the [VideoControls]
     * so that we can re-enter fullscreen mode when the controls are hidden.
     */
    private inner class ControlsVisibilityListener : VideoControlsVisibilityListener {
        override fun onControlsShown() {
            if (fullScreenListener.lastVisibility != View.SYSTEM_UI_FLAG_VISIBLE) {
                exitFullscreen()
            }
        }

        override fun onControlsHidden() {
            goFullscreen()
        }
    }


    public override fun onDestroy() {
        super.onDestroy()

        // Resets the flags
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }


    companion object {

        private val TAG = "MvDetailActivity"
    }
}
