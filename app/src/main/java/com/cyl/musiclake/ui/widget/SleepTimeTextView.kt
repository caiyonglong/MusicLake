package com.cyl.musiclake.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * 若红楼梦空，亦初心不变
 * 作者：weijiale
 * 包名：com.aoe.music.ui.view
 * 时间：2018/6/22 14:37
 * 描述：
 */
class SleepTimeTextView :TextView{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        CountDown.addTextView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        CountDown.removeTextView(this)
    }
}