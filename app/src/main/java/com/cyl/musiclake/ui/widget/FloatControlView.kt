 package com.cyl.musiclake.ui.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import com.cyl.musiclake.Config
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import kotlinx.android.synthetic.main.float_control_view.view.*


/**
 * 悬浮View
 */
class FloatControlView(context: Context) : FrameLayout(context) {
    companion object {
        /**
         * 记录悬浮窗的宽度
         */
        var viewWidth: Int = 0
        /**
         * 记录悬浮窗的高度
         */
        var viewHeight: Int = 0
        var topHeight: Int = 0
    }

    //顶部是否选中
    private var topSelected: Boolean = false
    //底部是否选中
    private var bottomSelected: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.float_control_view, this)
        viewWidth = floatView.layoutParams.width
        viewHeight = floatView.layoutParams.height
        topHeight = context.resources.getDimensionPixelOffset(R.dimen.dp_56)
    }

    /**
     * 更新顶部按钮样式
     * topm
     *
     */
    fun updateViewStatus(params: WindowManager.LayoutParams) {
        //屏幕尺寸
        val size = Config.screenSize
        //播放悬浮窗的状态值
        val playerViewX = params.x
        val playerViewY = params.y
        val playerY = (size.y - playerViewY) / 2
        //上下两个按钮的状态值
        val topWidth = powerSavingIv.width

        //宽的范围[minRangeX,maxRangX]
        val minRangeX = size.x / 2 - topWidth / 2
        val maxRangeX = topWidth / 2 + size.x / 2


        //playerViewY 范围[0-topHeight] ,playerView的范围 [playerViewX,playerViewY+width] 与 [minRange,maxRangY] 有相同部分
//        if (playerViewY <= topHeight && playerViewX < maxRangeX && playerViewX + playerViewWidth > minRangeX) {
        if (playerViewY <= topHeight / 2) {
            if (!topSelected) {
                topSelected = true
                powerSavingIv.isPressed = true
            }
        } else if (playerY <= topHeight) {
//        } else if (playerY <= topHeight && playerViewX < maxRangeX && playerViewX + playerViewWidth > minRangeX) {
            //playerViewY 范围[0-topHeight] ,playerView的范围 [playerViewX,playerViewY+width] 与 [minRange,maxRangY] 有相同部分
            if (!bottomSelected) {
                bottomSelected = true
                clearIv.isPressed = true
            }
        } else {
            if (bottomSelected) {
                bottomSelected = false
                clearIv.isPressed = false
            }
            if (topSelected) {
                topSelected = false
                powerSavingIv.isPressed = false
            }
        }

    }

    /**
     * 获取状态码
     */
    fun getControlViewStatus(): Int {
        return when {
            topSelected -> 1
            bottomSelected -> 2
            else -> 0
        }
    }

}