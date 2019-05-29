package com.cyl.musiclake.ui.widget

import android.os.CountDownTimer
import android.widget.TextView
import com.cyl.musiclake.utils.FormatUtil

/**
 * 若红楼梦空，亦初心不变
 * 作者：weijiale
 * 包名：com.aoe.music.ui.view
 * 时间：2018/6/22 12:51
 * 描述：
 */
object CountDown {
    private var countDown: CountDownTimer? = null
    private val textViewList = mutableListOf<TextView?>()
    var type = 0
    var isOpenSleepSwitch = true
    fun start(time: Long, type: Int) {
        this.type = type
        try {
            countDown?.cancel()
        } catch (e: Throwable) {


        } finally {
            countDown = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    textViewList.forEach {
                        it?.text = null
                    }
                    this@CountDown.type = -1
                    if (!isOpenSleepSwitch) {
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    textViewList.forEach {
                        it?.text = FormatUtil.formatTime(millisUntilFinished)
                    }
                }
            }.start()
        }
    }

    fun addTextView(tv: TextView) {
        textViewList.add(tv)
    }

    fun removeTextView(tv: TextView) {
        textViewList.remove(tv)
    }

    fun cancel() {
        type = 0
        try {
            textViewList.forEach {
                it?.text = null
            }
            countDown?.cancel()
        } catch (e: Throwable) {
        }
    }

}