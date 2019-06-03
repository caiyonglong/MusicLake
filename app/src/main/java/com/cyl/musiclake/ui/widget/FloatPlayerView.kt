package com.cyl.musiclake.ui.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.FrameLayout
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.MetaChangedEvent
import com.cyl.musiclake.event.StatusChangedEvent
import com.cyl.musiclake.player.FloatWindowManager
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.playpage.PlayerActivity
import com.cyl.musiclake.utils.SPUtils
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.android.synthetic.main.activity_lock_screen.view.nextIv
import kotlinx.android.synthetic.main.activity_lock_screen.view.playPauseIv
import kotlinx.android.synthetic.main.activity_lock_screen.view.prevIv
import kotlinx.android.synthetic.main.float_player_view.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


@SuppressLint("ViewConstructor")
/**
 * 悬浮View
 */
class FloatPlayerView(context: Context) : FrameLayout(context), View.OnTouchListener {

    companion object {
        private var statusBarHeight: Int = 0
        /**
         * 记录悬浮窗的宽度
         */
        var viewWidth: Int = 0
        /**
         * 记录悬浮窗的高度
         */
        var viewHeight: Int = 0
        var isFirstLoading: Boolean = true
    }

    private var playerViewParams: WindowManager.LayoutParams? = null
    private var xInScreen: Float = 0f
    private var yInScreen: Float = 0f
    private var xDownInScreen: Float = 0f
    private var yDownInScreen: Float = 0f
    private var xInView: Float = 0f
    private var yInView: Float = 0f
    private var mMovement: Boolean = false
    private var isHiddenSettings: Boolean = false
    private var isAnimating: Boolean = false

    private var gestureDetector: GestureDetector? = null

    init {
        mMovement = true
        isHiddenSettings = true
        context.setTheme(R.style.AppTheme)
        LayoutInflater.from(context).inflate(R.layout.float_player_view, this)
        viewHeight = playerView.layoutParams.height
        viewWidth = playerView.layoutParams.width
        statusBarHeight = FloatWindowManager.getStatusBarHeight(context)

        gestureDetector = GestureDetector(context, onGestureListener())
//        exoPlayerView.hideController()
        exoPlayerView.setOnTouchListener(this)

        EventBus.getDefault().register(this)

        playPauseIv.setOnClickListener {
            PlayManager.playPause()
        }
        nextIv.setOnClickListener {
            PlayManager.next()
        }
        prevIv.setOnClickListener {
            PlayManager.prev()
        }
        fullScreenIv.setOnClickListener {
            //            val intent = Intent(context, FullScreenPlayerActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intent)
        }
        initData()
    }


    private fun initData() {
        val songInfo = PlayManager.getPlayingMusic()
        songInfo?.let {
            isFirstLoading = true
//            coverPicIv.visibility = View.VISIBLE
//            loading_panel.visibility = View.VISIBLE
//            CoverLoader.loadImageView2(context, it.coverPic, coverPicIv)
        }
        val state = PlayManager.isPlaying()
        if (state) {
            isFirstLoading = false
//            coverPicIv.visibility = View.GONE
//            loading_panel.visibility = View.GONE
            playPauseIv.setImageResource(R.drawable.ic_pause)
        } else {
            playPauseIv.setImageResource(R.drawable.ic_play)
//            loading_panel.visibility = if (isFirstLoading) View.VISIBLE else View.GONE
//            PlayService.instance?.getLoadingStatus()?.let {
//                if (!it) {
//                    progressBarLoadingPanel.visibility = View.GONE
//                } else {
//                    progressBarLoadingPanel.visibility = View.VISIBLE
//                }
//            }
        }
    }


    fun setParams(params: WindowManager.LayoutParams) {
        playerViewParams = params
        if (params.width == MusicApp.screenSize.x) {
            fullScreenIv.visibility = View.VISIBLE
        } else {
            fullScreenIv.visibility = View.GONE
        }
    }

    fun setPlayerView(player: ExoPlayer?) {
//        player?.bindView(exoPlayerView)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        gestureDetector?.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.x
                yInView = event.y
                xDownInScreen = event.rawX
                yDownInScreen = event.rawY - statusBarHeight
                xInScreen = event.rawX
                yInScreen = event.rawY - statusBarHeight

            }
            MotionEvent.ACTION_MOVE -> {
                xInScreen = event.rawX
                yInScreen = event.rawY - statusBarHeight
                // 手指移动的时候更新小悬浮窗的位置
                FloatWindowManager.updateViewPosition(context, (xInScreen - xInView).toInt(), (yInScreen - yInView).toInt())
                //创建上下悬浮窗
                FloatWindowManager.createFloatControlWindow(MusicApp.getAppContext())
                //更新上下view状态
                playerViewParams?.let { FloatWindowManager.updateControlViewStatus(it) }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                //处理事件
                responseEvent()
                //移除上下悬浮窗
                FloatWindowManager.removeFloatControlView(context)
            }
            else -> {
                //移除上下悬浮窗
                FloatWindowManager.removeFloatControlView(context)
            }
        }
        return true
    }

    private fun doubleClick() {
        //移除上下悬浮窗
        FloatWindowManager.removeFloatControlView(context)
        val intent = Intent(context, PlayerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        try {
            pendingIntent.send() // 监听到Home键按下后立即调用startActivity启动Activity会有5s延迟
        } catch (e: Throwable) {
            e.printStackTrace()
            context.startActivity(intent)
        }
    }

    /**
     * 延时隐藏控制View
     */
    private fun delayHide() {
        if (isAnimating) return
        controllerView.animate()
                .setStartDelay(1000)
                .withStartAction {
                    isAnimating = true
                }
                .setDuration(200)
                .withEndAction {
                    isAnimating = false
                    controllerView.visibility = View.INVISIBLE
                }
                .start()

    }

    private fun showControlView() {
        controllerView.visibility = View.VISIBLE
        //延时隐藏
        delayHide()
    }

    /**
     * 更新界面歌曲信息
     */
    @Subscribe
    fun updateSongInfo(event: MetaChangedEvent) {
        event.music?.let {
            isFirstLoading = true
//            coverPicIv.visibility = View.VISIBLE
//            loading_panel.visibility = View.VISIBLE
//            PlayService.instance?.setPlayView(exoPlayerView)
//            CoverLoader.loadImageView2(context, it.coverPic, coverPicIv)
        }
    }

    /**
     * 更新播放状态
     */
    @Subscribe
    fun updatePlayStatus(event: StatusChangedEvent) {
        if (event.isPlaying) {
            isFirstLoading = false
//            coverPicIv.visibility = View.GONE
//            loading_panel.visibility = View.GONE
            playPauseIv.setImageResource(R.drawable.ic_pause)
        } else {
            playPauseIv.setImageResource(R.drawable.ic_play)
//            loading_panel.visibility = if (isFirstLoading) View.VISIBLE else View.GONE
//            PlayService.instance?.getLoadingStatus()?.let {
//                if (!it) {
//                    progressBarLoadingPanel.visibility = View.GONE
//                } else {
//                    progressBarLoadingPanel.visibility = View.VISIBLE
//                }
//            }
        }
    }

    /**
     * 自动靠边
     */
    private fun moveToEdge() {
        //获取屏幕宽度
        val size = FloatWindowManager.getWindowSize(context).x / 2
        val positionX = playerViewParams!!.x + playerView.width / 2
        var newX = 0
        if (size < positionX) {
            newX = size * 2 - playerView.width
        }
        //保存移动的位置
        SPUtils.putAnyCommit(Constants.FLOAT_VIEW_X, newX)
        SPUtils.putAnyCommit(Constants.FLOAT_VIEW_Y, playerViewParams!!.y + statusBarHeight)
        val moveAnimator = ValueAnimator.ofInt(playerViewParams!!.x, newX)
        moveAnimator.duration = 500
        moveAnimator.addUpdateListener {
            val x = it.animatedValue as Int
            // 手指移动的时候更新小悬浮窗的位置
            FloatWindowManager.updateViewPosition(context, x, playerViewParams!!.y)
        }
        moveAnimator.start()
    }

    /**
     * 响应自定义事件
    //     */
    private fun responseEvent() {
        val state = FloatWindowManager.getControlViewStatus()
        when (state) {
            1 -> {
//                val intent = Intent(context, PowerSavingActivity::class.java)
//                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                try {
//                    pendingIntent.send() // 监听到Home键按下后立即调用startActivity启动Activity会有5s延迟
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                    context.startActivity(intent)
//                }
            }
            2 -> {
                PlayManager.playPause()
                FloatWindowManager.removeFloatView(context)
            }
            else -> moveToEdge()
        }
    }

    /**
     * 设置是否可触摸，固定位置，不能移动
     */
    fun setTouchEnable(enableTouch: Boolean) {
        exoPlayerView.isEnabled = enableTouch
        if (enableTouch) {
            showControlView()
        } else {
            controllerView.visibility = View.INVISIBLE
        }
    }

    fun removeEvents() {
        EventBus.getDefault().unregister(this)
    }

    private fun onGestureListener() = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            doubleClick()
            return super.onDoubleTap(e)
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            showControlView()
            return super.onSingleTapUp(e)
        }
    }

}