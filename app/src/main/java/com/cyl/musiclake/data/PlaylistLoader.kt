package com.cyl.musiclake.data

import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.common.Constants

/**
 * 作者：yonglong on 2016/11/6 17:02
 * 本地歌单操作类
 */
object PlaylistLoader {
    private val TAG = "PlaylistLoader"

    /**
     * 获取全部歌单
     *
     * @param context
     * @return
     */
    fun getAllPlaylist(): MutableList<Playlist> {
        return DaoLitepal.getAllPlaylist()
    }

    /**
     * 获取pid歌单
     */
    fun getPlaylist(pid: String): Playlist {
        return DaoLitepal.getPlaylist(pid)
    }


    /**
     * 获取播放历史
     */
    fun getHistoryPlaylist(): Playlist {
        return DaoLitepal.getPlaylist(Constants.PLAYLIST_HISTORY_ID)
    }

    /**
     * 获取收藏
     */
    fun getFavoritePlaylist(): Playlist {
        return DaoLitepal.getPlaylist(Constants.PLAYLIST_LOVE_ID)
    }

    /**
     * 创建默认的歌单
     *
     * @param name
     * @return
     */
    fun createDefaultPlaylist(type: String, name: String): Boolean {
        return createPlaylist(type, type, name)
    }

    /**
     * 新增歌单
     *
     * @param name
     * @return
     */
    fun createPlaylist(pid: String, type: String, name: String): Boolean {
        val playlist = Playlist()
        playlist.pid = pid
        playlist.date = System.currentTimeMillis()
        playlist.updateDate = System.currentTimeMillis()
        playlist.name = name
        playlist.type = type
        if (type != Constants.PLAYLIST_QUEUE_ID)
            playlist.order = "updateDate desc"
        return DaoLitepal.saveOrUpdatePlaylist(playlist)
    }


    /**
     * 重命名歌单
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun renamePlaylist(playlist: Playlist, name: String): Boolean {
        playlist.name = name
        return DaoLitepal.saveOrUpdatePlaylist(playlist)
    }


    /**
     * 扫描歌单歌曲
     */
    fun getMusicForPlaylist(pid: String, order: String? = null): MutableList<Music> {
        return if (order == null) {
            DaoLitepal.getMusicList(pid)
        } else {
            DaoLitepal.getMusicList(pid, order)
        }
    }

    fun addMusicList(pid: String, musicList: List<Music>): Boolean {
        for (music in musicList) {
            addToPlaylist(pid, music)
        }
        return true
    }

    /**
     * 添加歌曲到歌单
     */
    fun addToPlaylist(pid: String, music: Music): Boolean {
        try {
            return DaoLitepal.addToPlaylist(music, pid)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 移除歌曲
     */
    fun removeSong(pid: String, mid: String) {
        try {
            DaoLitepal.removeSong(pid, mid)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 删除歌单
     */
    fun deletePlaylist(playlist: Playlist) {
        try {
            DaoLitepal.deletePlaylist(playlist)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    /**
     * 清空播放列表
     */
    fun clearPlaylist(pid: String) {
        try {
            DaoLitepal.clearPlaylist(pid)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
