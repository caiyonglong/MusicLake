package com.cyl.musiclake.data.db

import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.db.SearchHistoryBean
import org.litepal.LitePal

/**
 * 数据库操作类
 * Created by master on 2018/4/26.
 */

object DaoLitepal {

/*
 **********************************
 * 播放历史操作
 **********************************
 */
    /**
     * 获取搜索历史
     */
    fun getAllSearchInfo(title: String): List<SearchHistoryBean> {
        return LitePal.where("title like ?", "%$title%").find(SearchHistoryBean::class.java)
    }


    /**
     * 增加搜索
     */
    fun addSearchInfo(info: String) {
        val id = System.currentTimeMillis()
        val queryInfo = SearchHistoryBean(id, info)
        queryInfo.saveOrUpdate("title = ?", info)
    }


    /**
     * 删除搜索历史
     */
    fun deleteSearchInfo(info: String) {
        LitePal.deleteAll(info, "title = ? ", info)
    }

    /*
     **********************************
     * 播放歌单操作
     **********************************
     */
    fun saveOrUpdateMusic(music: Music, isAsync: Boolean = false) {
        if (isAsync) {
            music.saveOrUpdateAsync("mid = ?", music.mid)
        } else {
            music.saveOrUpdate("mid = ?", music.mid)
        }
    }

    fun addToPlaylist(music: Music, pid: String): Boolean {
        saveOrUpdateMusic(music)
        val count = LitePal.where("mid = ? and pid = ?", music.mid, pid)
                .count(MusicToPlaylist::class.java)
        return if (count == 0) {
            val mtp = MusicToPlaylist()
            mtp.mid = music.mid
            mtp.pid = pid
            mtp.count = 1
            mtp.createDate = System.currentTimeMillis()
            mtp.updateDate = System.currentTimeMillis()
            mtp.save()
        } else {
            val mtp = MusicToPlaylist()
            mtp.count++
            mtp.updateDate = System.currentTimeMillis()
            mtp.saveOrUpdate("mid = ? and pid =?", music.mid, pid)
        }
    }

    fun saveOrUpdatePlaylist(playlist: Playlist): Boolean {
        playlist.updateDate = System.currentTimeMillis()
        return playlist.saveOrUpdate("pid = ?", playlist.pid)
    }

    fun deleteMusic(music: Music) {
        LitePal.delete(Music::class.java, music.id)
        LitePal.deleteAll(MusicToPlaylist::class.java, "mid=?", music.mid)
    }

    fun deletePlaylist(playlist: Playlist) {
        LitePal.delete(Music::class.java, playlist.id)
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=?", playlist.pid)
    }

    fun clearPlaylist(pid: String) {
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=?", pid)
    }

    fun getMusicList(pid: String, order: String = ""): MutableList<Music> {
        val musicLists = mutableListOf<Music>()
        when (pid) {
            Constants.PLAYLIST_LOVE_ID -> {
                val data = LitePal.where("isLove = ? ", "1").find(Music::class.java)
                musicLists.addAll(data)
            }
            Constants.PLAYLIST_LOCAL_ID -> {
                val data = LitePal.where("isOnline = ? ", "0").find(Music::class.java)
                musicLists.addAll(data)
            }
            else -> {
                val data = LitePal.where("pid = ?", pid).order(order).find(MusicToPlaylist::class.java)
                for (it in data) {
                    val musicList = LitePal.where("mid = ?", it.mid).find(Music::class.java)
                    musicLists.addAll(musicList)
                }
            }
        }
        return musicLists
    }

    fun getAllPlaylist(): List<Playlist> {
        return LitePal.where("pid != ? and pid !=?", Constants.PLAYLIST_QUEUE_ID, Constants.PLAYLIST_HISTORY_ID).find(Playlist::class.java)
    }


    fun getPlaylist(pid: String): Playlist {
        return LitePal.where("pid = ?", pid).find(Playlist::class.java).first()
    }

    fun getMusicInfo(mid: String): MutableList<Music>? {
        return LitePal.where("mid =? ", mid).find(Music::class.java)
    }

    fun removeSong(pid: String, mid: String) {
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=? and mid=?", pid, mid)
    }

}
