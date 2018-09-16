package com.cyl.musiclake.utils

import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import com.cyl.musiclake.player.PlayManager

/**
 * Des    :倒计时工具类
 * Author : master.
 * Date   : 2018/9/15 .
 */
object CountDownUtils {
    private var countDown: CountDownTimer? = null
    private var text: String = ""
    private var startCountDown: Boolean = false

    private val countDownTextView = mutableListOf<TextView>()

    val times = intArrayOf(0, 15, 30, 45, 60)
    val selectItems = arrayListOf("不开启", "15分钟", "30分钟", "45分钟", "60分钟", "自定义")
    var selectID = 0
    /**
     * 总共时间
     */
    var totalTime = 0L
    var isCountDowning = false

    /**
     * 增加监听
     */
    fun setTextViewListener(view: TextView) {
        countDownTextView.add(view)
    }

    /**
     * 移除监听
     */
    fun removeTextViewListener(view: TextView) {
        countDownTextView.remove(view)
    }

    /**
     * 开始计时
     */
    fun start(time: Long) {
        try {
            countDown?.cancel()
        } catch (e: Throwable) {

        } finally {
            isCountDowning = true
            countDown = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    finishCountDown()
                }

                override fun onTick(millisUntilFinished: Long) {
                    text = FormatUtil.formatTime(millisUntilFinished)
                    countDownTextView.forEach {
                        it.text = text
                    }
                    if (millisUntilFinished == 10 * 1000L) {
                        ToastUtils.show("还剩10秒暂停播放")
                    }
                }
            }.start()
        }
    }

    /**
     * 开始倒计时
     */
    fun starCountDownById(id: Int) {
        totalTime = times[id] * 60 * 1000L
        start(totalTime)
    }

    /**
     * 开始倒计时
     */
    fun starCountDownByTime(time: Int) {
        totalTime = time * 60 * 1000L
        start(totalTime)
    }

    /**
     * 停止倒计时
     */
    fun stopCountDown() {
        isCountDowning = false
        countDown?.cancel()
    }

    /**
     * 计时结束
     */
    fun finishCountDown() {
        if (PlayManager.isPlaying()) {
            PlayManager.playPause()
        }
        countDownTextView.forEach {
            it.visibility = View.GONE
        }
        stopCountDown()
    }

}