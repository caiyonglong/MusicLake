package com.cyl.musiclake.player

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.widget.FloatControlView
import com.cyl.musiclake.ui.widget.FloatPlayerView
import com.cyl.musiclake.utils.SPUtils
import com.google.android.exoplayer2.ExoPlayer

/**
 * 悬浮窗管理类
 */
object FloatWindowManager {


    //悬浮窗控制
    private var windowManager: WindowManager? = null
    //播放View参数
    private var playerViewParams: WindowManager.LayoutParams? = null
    //上下两个View的参数
    private var controlViewParams: WindowManager.LayoutParams? = null

    //播放悬浮窗
    private var playerView: FloatPlayerView? = null
    //上省电、下退出悬浮窗
    private var controlView: FloatControlView? = null
    private var statusBarHeight: Int = 0
    private var position: IntArray? = null

    /**
     * 创建播放悬浮窗/更新悬浮窗位置
     */
    fun createFloatPlayerWindow(context: Context, startView: View? = null, enableTouch: Boolean): Boolean {
        try {
            removeFloatView(context)
            MusicApp.isShowingFloatView = true
            if (windowManager == null) {
                windowManager = getWindowManager(context)
            }
            if (playerView == null) {
                playerView = FloatPlayerView(context)
            }
            playerView = FloatPlayerView(context)
            playerViewParams = getPlayerParams(context, startView)
            playerViewParams?.let {
                if (position != null) {
                    it.x = position!![0]
                    it.y = position!![1] - statusBarHeight
                    it.width = position!![2]
                    it.height = position!![3]
                }
                statusBarHeight = getStatusBarHeight(context)
                playerView?.setParams(it)
                playerView?.setTouchEnable(enableTouch)
                windowManager?.addView(playerView, playerViewParams)
                return true
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 移动到View的固定位置，且不可点击
     */
    fun moveToViewPosition(context: Context, view: View?, enableTouch: Boolean, position: IntArray? = null) {
        if (playerView == null || playerViewParams == null) return
        when {
            view != null -> {
                //更新悬浮窗位置
                val location = IntArray(2)
                view.getLocationInWindow(location)
                playerViewParams!!.x = location[0]
                playerViewParams!!.y = location[1] - getStatusBarHeight(context)
                playerViewParams!!.width = view.width
                playerViewParams!!.height = view.height
                playerView?.setTouchEnable(enableTouch)
                playerView?.setParams(playerViewParams!!)
                windowManager?.updateViewLayout(playerView, playerViewParams)
            }
            (position != null && position[3] > 0) -> {
                //更新悬浮窗位置
                playerViewParams!!.x = position[0]
                playerViewParams!!.y = position[1] - getStatusBarHeight(context)
                playerViewParams!!.width = position[2]
                playerViewParams!!.height = position[3]
                playerView?.setTouchEnable(enableTouch)
                playerView?.setParams(playerViewParams!!)
                windowManager?.updateViewLayout(playerView, playerViewParams)
            }
            else -> {
                val size = getWindowSize(context)
                //没有view，默认位置，默认宽高
                playerViewParams?.x = SPUtils.getAnyByKey(Constants.FLOAT_VIEW_X, (size.x - 500))
                playerViewParams?.y = SPUtils.getAnyByKey(Constants.FLOAT_VIEW_Y, 300)
                playerViewParams?.width = context.resources.getDimensionPixelOffset(R.dimen.dp_160)
                playerViewParams?.height = context.resources.getDimensionPixelOffset(R.dimen.dp_90)
                playerView?.setParams(playerViewParams!!)
                playerView?.setTouchEnable(enableTouch)
                windowManager?.updateViewLayout(playerView, playerViewParams)
            }
        }
    }

    /**
     * 设置播放exoPlayer
     */
    fun setPlayerView(exoPlayer: ExoPlayer? = null) {
        playerView?.setPlayerView(exoPlayer)
    }

    /**
     * 获取悬浮窗大小位置参数
     */
    private fun getPlayerParams(context: Context, startView: View? = null): WindowManager.LayoutParams? {
        if (playerViewParams == null) {
            playerViewParams = initParams()
            playerViewParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        }
        if (startView != null) {
            //更新悬浮窗位置
            val location = IntArray(2)
            startView.getLocationInWindow(location)
            playerViewParams!!.x = location[0]
            playerViewParams!!.y = location[1] - getStatusBarHeight(context)
            playerViewParams!!.width = startView.width
            playerViewParams!!.height = startView.height
            playerView?.setParams(playerViewParams!!)
        } else {
            val size = getWindowSize(context)
            //没有view，默认位置，默认宽高
            playerViewParams?.x = SPUtils.getAnyByKey(Constants.FLOAT_VIEW_X, size.x - 500)
            playerViewParams?.y = SPUtils.getAnyByKey(Constants.FLOAT_VIEW_Y, 300)
            playerViewParams?.width = context.resources.getDimensionPixelOffset(R.dimen.dp_160)
            playerViewParams!!.height = context.resources.getDimensionPixelOffset(R.dimen.dp_90)
            playerView?.setParams(playerViewParams!!)
        }
        return playerViewParams
    }

    /**
     * 创建上下两个按钮悬浮窗
     */
    fun createFloatControlWindow(context: Context) {
        if (controlView == null) {
            controlView = FloatControlView(context)
            if (controlViewParams == null) {
                controlViewParams = initParams()
                controlViewParams?.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                controlViewParams?.width = FloatControlView.viewWidth
                controlViewParams?.height = FloatControlView.viewHeight
            }
            windowManager?.addView(controlView, controlViewParams)
        }
    }

    private fun initParams(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        params.format = PixelFormat.RGBA_8888
        params.gravity = Gravity.START or Gravity.TOP
        return params
    }

    /**
     * 移除播放悬浮窗
     * @param view
     * *
     * @return
     */
    fun removeFloatView(context: Context): Boolean {
        if (playerView != null) {
            MusicApp.isShowingFloatView = false
            val windowManager = getWindowManager(context)
            windowManager?.removeView(playerView)
            playerView?.removeEvents()
            playerView?.setPlayerView(null)
            playerView = null
            playerViewParams = null
            controlViewParams = null
            removeFloatControlView(context)
            return true
        }
        return false
    }

    /**
     * 移除上下按钮悬浮窗
     * @param view
     * *
     * @return
     */
    fun removeFloatControlView(context: Context) {
        if (controlView != null) {
            val windowManager = getWindowManager(context)
            windowManager?.removeView(controlView)
            controlView = null
        }
    }


    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context
     * 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private fun getWindowManager(context: Context): WindowManager? {
        if (windowManager == null) {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        return windowManager
    }

    /**
     * 更新悬浮窗的位置
     */
    fun updateViewPosition(context: Context, x: Int, y: Int) {
        if (playerView == null || playerViewParams == null) return
        if (windowManager == null) windowManager = getWindowManager(context)
        playerViewParams?.x = x
        playerViewParams?.y = y
        windowManager?.updateViewLayout(playerView, playerViewParams)
        playerViewParams?.let {
            playerView?.setParams(it)
        }
    }


    fun getFloatViewStatus(): Boolean {
        if (windowManager == null) return false
        if (playerViewParams == null) return false
        if (playerView == null) return false
        return true
    }

    /**
     * 更新上下View的状态
     */
    fun updateControlViewStatus(params: WindowManager.LayoutParams) {
        if (controlView != null) {
            controlView?.updateViewStatus(params)
        }
    }

    /**
     * 返回状态值 0 :正常，1: 滑动到顶部触发省电播放，2: 滑动到底部触发关闭
     */
    fun getControlViewStatus(): Int {
        if (controlView != null) {
            return controlView?.getControlViewStatus() ?: 0
        }
        return 0
    }


    /**
     * 获取屏幕尺寸高度
     */
    fun getWindowSize(context: Context): Point {
        val size = Point()
        windowManager = getWindowManager(context)
        //获取屏幕宽高
        windowManager?.defaultDisplay?.getSize(size)
        return size
    }


    /**
     * 获取状态栏的高度
     */
    fun getStatusBarHeight(context: Context): Int {
        if (statusBarHeight == 0) {
            try {
                val h = context.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (h > 0)
                    statusBarHeight = context.resources.getDimensionPixelSize(h)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return statusBarHeight
    }
}