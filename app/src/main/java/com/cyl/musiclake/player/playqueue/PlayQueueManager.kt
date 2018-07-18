package com.cyl.musiclake.player.playqueue

import com.cyl.musiclake.utils.SPUtils

/**
 * Created by master on 2018/5/14.
 */

object PlayQueueManager {
    /**
     * 播放模式 0：顺序播放，1：单曲循环，2：随机播放
     */
    val PLAY_MODE_LOOP = 0
    val PLAY_MODE_REPEAT = 1
    val PLAY_MODE_RANDOM = 2
    //播放模式
    var playingModeId = 0

    val playingMode = arrayOf("顺序播放", "单曲循环", "随机播放")

    /**
     * 更新播放模式
     */
    fun updatePlayMode(): Int {
        playingModeId = (playingModeId + 1) % 3
        SPUtils.savePlayMode(playingModeId)
        return playingModeId
    }

    /**
     * 获取播放模式
     */
    fun getPlayModeId(): Int {
        playingModeId = SPUtils.getPlayMode()
        return playingModeId
    }

    /**
     * 获取播放模式
     */
    fun getPlayMode(): String {
        return playingMode[playingModeId]
    }

}
