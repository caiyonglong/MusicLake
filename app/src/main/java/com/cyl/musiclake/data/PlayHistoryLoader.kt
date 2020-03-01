package com.cyl.musiclake.data

import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.DaoLitepal
import com.music.lake.musiclib.bean.BaseMusicInfo

/**
 * 作者：yonglong on 2016/11/4 22:30
 */
object PlayHistoryLoader {

    private val TAG = "PlayQueueLoader"

    /**
     * 添加歌曲到播放历史
     */
    fun addSongToHistory(baseMusicInfoInfo: BaseMusicInfo) {
        try {
            DaoLitepal.addToPlaylist(baseMusicInfoInfo, Constants.PLAYLIST_HISTORY_ID)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 获取播放历史
     */
    fun getPlayHistory(): MutableList<BaseMusicInfo> {
        return DaoLitepal.getMusicList(Constants.PLAYLIST_HISTORY_ID, "updateDate desc")
    }

    /**
     * 清除播放历史
     */
    fun clearPlayHistory() {
        try {
            DaoLitepal.clearPlaylist(Constants.PLAYLIST_HISTORY_ID)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
