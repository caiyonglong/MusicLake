package com.cyl.musiclake.data.db

import com.cyl.musiclake.bean.*
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.download.TasksManagerModel
import com.cyl.musiclake.utils.FileUtils
import com.music.lake.musiclib.bean.BaseMusicInfo
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
    fun getAllSearchInfo(title: String? = null): MutableList<SearchHistoryBean> {
        return if (title == null) {
            LitePal.order("id desc").find(SearchHistoryBean::class.java)
        } else {
            LitePal.where("title like ?", "%$title%").order("id desc").find(SearchHistoryBean::class.java)
        }
    }

    /**
     * 增加搜索
     */
    fun addSearchInfo(info: String): Boolean {
        val id = System.currentTimeMillis()
        val queryInfo = SearchHistoryBean(id, info)
        return queryInfo.saveOrUpdate("title = ?", info)
    }


    /**
     * 删除搜索历史
     */
    fun deleteSearchInfo(info: String) {
        LitePal.deleteAllAsync(SearchHistoryBean::class.java, "title = ? ", info).listen {

        }
    }

    /**
     * 删除所有搜索历史
     */
    fun clearAllSearch() {
        LitePal.deleteAll(SearchHistoryBean::class.java)
    }

    /*
     **********************************
     * 播放歌单操作
     **********************************
     */
    fun saveOrUpdateMusic(baseMusicInfoInfo: BaseMusicInfo, isAsync: Boolean = false) {
//        if (isAsync) {
//            music.saveOrUpdateAsync("mid = ?", music.mid)
//        } else {
//            music.saveOrUpdate("mid = ?", music.mid)
//        }
    }


    /**
     * 扫描更新本地歌曲信息，如果
     */
//    fun saveOrUpdateLocalMusic(music: Music, isAsync: Boolean = false) {
//        val downloadInfo = LitePal.where("path = ?", music.uri).find(TasksManagerModel::class.java)
//        downloadInfo?.size?.let {
//            if (it > 0) {
//            } else {
//                music.saveOrUpdate("mid = ?", music.mid)
//            }
//        }
//    }

    /**
     * 添加歌曲到歌单
     */
    fun addToPlaylist(baseMusicInfoInfo: BaseMusicInfo, pid: String): Boolean {
        saveOrUpdateMusic(baseMusicInfoInfo)
        val count = LitePal.where("mid = ? and pid = ?", baseMusicInfoInfo.mid, pid)
                .count(MusicToPlaylist::class.java)
        return if (count == 0) {
            val mtp = MusicToPlaylist()
            mtp.mid = baseMusicInfoInfo.mid
            mtp.pid = pid
            mtp.total = 1
            mtp.createDate = System.currentTimeMillis()
            mtp.updateDate = System.currentTimeMillis()
            mtp.save()
        } else {
            val mtp = MusicToPlaylist()
            mtp.total++
            mtp.updateDate = System.currentTimeMillis()
            mtp.saveOrUpdate("mid = ? and pid =?", baseMusicInfoInfo.mid, pid)
        }
    }

    fun saveOrUpdatePlaylist(playlist: Playlist): Boolean {
        playlist.updateDate = System.currentTimeMillis()
        return playlist.saveOrUpdate("pid = ?", playlist.pid)
    }

    /**
     * 删除本地歌曲（Music、MusicToPlaylist）
     */
    fun deleteMusic(baseMusicInfoInfo: BaseMusicInfo) {
        val cachePath = FileUtils.getMusicCacheDir() + baseMusicInfoInfo.artist + " - " + baseMusicInfoInfo.title + "(" + baseMusicInfoInfo.quality + ")"
        val downloadPath = FileUtils.getMusicDir() + baseMusicInfoInfo.artist + " - " + baseMusicInfoInfo.title + ".mp3"
        if (FileUtils.exists(cachePath)) {
            FileUtils.delFile(cachePath)
        }
        if (FileUtils.exists(downloadPath)) {
            FileUtils.delFile(downloadPath)
        }
        if (FileUtils.exists(baseMusicInfoInfo.uri)) {
            FileUtils.delFile(baseMusicInfoInfo.uri)
        }
        LitePal.deleteAll(BaseMusicInfo::class.java, "mid=?", baseMusicInfoInfo.mid.toString())
        LitePal.deleteAll(TasksManagerModel::class.java, "mid=?", baseMusicInfoInfo.mid.toString())
        LitePal.deleteAll(MusicToPlaylist::class.java, "mid=?", baseMusicInfoInfo.mid.toString())
    }

    /**
     * 删除歌单
     * 先删除歌单歌曲，然后删除歌单
     */
    fun deletePlaylist(playlist: Playlist): Int {
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=?", playlist.pid)
        return LitePal.deleteAll(Playlist::class.java, "pid=?", playlist.pid)
    }

    /**
     * 清空歌单歌曲
     */
    fun clearPlaylist(pid: String): Int {
        return LitePal.deleteAll(MusicToPlaylist::class.java, "pid=?", pid)
    }

    /**
     * 根据pid获取本地歌单所有歌曲
     * @param pid
     */
    fun getMusicList(pid: String, order: String = ""): MutableList<BaseMusicInfo> {
        val musicLists = mutableListOf<BaseMusicInfo>()
        when (pid) {
            Constants.PLAYLIST_LOVE_ID -> {
                val data = LitePal.where("isLove = ? ", "1").find(BaseMusicInfo::class.java)
                musicLists.addAll(data)
            }
            Constants.PLAYLIST_LOCAL_ID -> {
                val data = LitePal.where("isOnline = ? ", "0").find(BaseMusicInfo::class.java)
                musicLists.addAll(data)
            }
            else -> {
                val data = LitePal.where("pid = ?", pid).order(order).find(MusicToPlaylist::class.java)
                for (it in data) {
                    val musicList = LitePal.where("mid = ?", it.mid).find(BaseMusicInfo::class.java)
                    musicLists.addAll(musicList)
                }
            }
        }
        return musicLists
    }

    /**
     * 获取本地新建的所有歌单
     */
    fun getAllPlaylist(): MutableList<Playlist> {
        return LitePal.where("type = ?", Constants.PLAYLIST_LOCAL_ID).find(Playlist::class.java)
    }


    /**
     * 根据pid获取本地歌单
     * @param pid
     */
    fun getPlaylist(pid: String): Playlist {
        return LitePal.where("pid = ?", pid).findFirst(Playlist::class.java)
    }

    fun getMusicInfo(mid: String): BaseMusicInfo? {
        return LitePal.where("mid = ? ", mid).findFirst(BaseMusicInfo::class.java)
    }

    fun removeSong(pid: String, mid: String) {
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=? and mid=?", pid, mid)
    }

    fun searchLocalMusic(info: String): MutableList<BaseMusicInfo> {
        return LitePal.where("title LIKE ? or artist LIKE ? or album LIKE ?", "%$info%", "%$info%", "%$info%").find(BaseMusicInfo::class.java)
    }

    fun getAllAlbum(): MutableList<Album> {
        return LitePal.findAll(Album::class.java)
    }

    fun getAllArtist(): MutableList<Artist> {
        return LitePal.findAll(Artist::class.java)
    }


    fun updateArtistList(): MutableList<Artist> {
        val sql = "SELECT music.artistid,music.artist,count(music.title) as num FROM music where music.isonline=0 and music.type=\"local\" GROUP BY music.artist"
        val cursor = LitePal.findBySQL(sql)
        val results = mutableListOf<Artist>()
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val artist = MusicCursorWrapper(cursor).artists
//                artist.saveOrUpdate("artistId = ?", artist.artistId.toString())
                results.add(artist)
            }
        }
        // 记得关闭游标
        cursor?.close()
        return results
    }


    fun updateAlbumList(): MutableList<Album> {
        val sql = "SELECT music.albumid,music.album,music.artistid,music.artist,count(music.title) as num FROM music WHERE music.isonline=0 and music.type=\"local\" GROUP BY music.album"
        val cursor = LitePal.findBySQL(sql)
        val results = mutableListOf<Album>()
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val album = MusicCursorWrapper(cursor).album
//                album.saveOrUpdate("albumId = ?", album.albumId)
                results.add(album)
            }
        }
        // 记得关闭游标
        cursor?.close()
        return results
    }

}
