package com.music.lake.musiclib.manager

import com.music.lake.musiclib.utils.LogUtil

/**
 * Created by master on 2018/5/14.
 */

object PlayListManager {
    /**
     * 播放模式 0：顺序播放，1：单曲循环，2：随机播放
     */
    const val PLAY_MODE_LOOP = 0
    const val PLAY_MODE_REPEAT = 1
    const val PLAY_MODE_RANDOM = 2
    //播放模式
    private var playingModeId = 0

    /**
     * 总共多少首歌曲
     */
    private var total = 0
    private var orderList = mutableListOf<Int>()
    private var saveList = mutableListOf<Int>()
    private var randomPosition = 0

    /**
     * 更新播放模式
     */
    fun setLoopMode(loopMode: Int) {
        playingModeId = loopMode
    }

    /**
     * 获取播放模式id
     */
    fun getLoopMode(): Int {
        return playingModeId
    }

    private fun initOrderList(total: Int) {
        PlayListManager.total = total
        orderList.clear()
        for (i in 0 until total) {
            orderList.add(i)
        }

        /**
         * 更新
         */
        if (getLoopMode() == PLAY_MODE_RANDOM) {
            orderList.shuffle()
            randomPosition = 0
            printOrderList(-1)
        }
    }

    /**
     * 获取下一首位置
     *
     * @return isAuto 是否自动下一曲
     */
    fun getNextPosition(isAuto: Boolean?, total: Int, cuePosition: Int): Int {
        if (total == 1) {
            return 0
        }
        initOrderList(total)
        if (playingModeId == PLAY_MODE_REPEAT && isAuto!!) {
            return if (cuePosition < 0) {
                0
            } else {
                cuePosition
            }
        } else if (playingModeId == PLAY_MODE_RANDOM) {
            printOrderList(orderList[randomPosition])
            saveList.add(orderList[randomPosition])
            return orderList[randomPosition]
        } else {
            if (cuePosition == total - 1) {
                return 0
            } else if (cuePosition < total - 1) {
                return cuePosition + 1
            }
        }
        return cuePosition
    }

    /**
     * 获取下一首位置
     *
     * @return isAuto 是否自动下一曲
     */
    fun getPreviousPosition(total: Int, cuePosition: Int): Int {
        if (total == 1) {
            return 0
        }
        getLoopMode()
        if (playingModeId == PLAY_MODE_REPEAT) {
            return if (cuePosition < 0) {
                0
            } else {
                cuePosition
            }
        } else if (playingModeId == PLAY_MODE_RANDOM) {
            randomPosition = if (saveList.size > 0) {
                saveList.last()
                saveList.removeAt(saveList.lastIndex)
            } else {
                randomPosition--
                if (randomPosition < 0) {
                    randomPosition = total - 1
                }
                orderList[randomPosition]
            }
            printOrderList(randomPosition)
            return randomPosition
        } else {
            if (cuePosition == 0) {
                return total - 1
            } else if (cuePosition > 0) {
                return cuePosition - 1
            }
        }
        return cuePosition
    }

    /**
     * 打印当前顺序
     */
    private fun printOrderList(cur: Int) {
        LogUtil.e("PlayQueueManager", orderList.toString() + " --- $cur")
    }
}
