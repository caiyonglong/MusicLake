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
        val songInfo = SongInfo()
        songInfo.parseByMusicInfo(baseMusicInfoInfo)
        if (isAsync) {
            songInfo.saveOrUpdateAsync("mid = ?", songInfo.mid)
        } else {
            songInfo.saveOrUpdate("mid = ?", songInfo.mid)
        }
    }


    /**
     * 扫描更新本地歌曲信息，如果
     */
//    fun saveOrUpdateLocalMusic(music: Music, isAsync: Boolean = false) {
//        val downloadInfo = LitePal.where("path = ?", songinfo.uri).find(TasksManagerModel::class.java)
//        downloadInfo?.size?.let {
//            if (it > 0) {
//            } else {
//                songinfo.saveOrUpdate("mid = ?", songinfo.mid)
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
    fun deleteMusic(music: BaseMusicInfo) {
        val songInfo = SongInfo()
        songInfo.parseByMusicInfo(music)
        val cachePath = FileUtils.getMusicCacheDir() + songInfo.artist + " - " + songInfo.title + "(" + songInfo.quality + ")"
        val downloadPath = FileUtils.getMusicDir() + songInfo.artist + " - " + songInfo.title + ".mp3"
        if (FileUtils.exists(cachePath)) {
            FileUtils.delFile(cachePath)
        }
        if (FileUtils.exists(downloadPath)) {
            FileUtils.delFile(downloadPath)
        }
        if (FileUtils.exists(songInfo.uri)) {
            FileUtils.delFile(songInfo.uri)
        }
        LitePal.deleteAll(SongInfo::class.java, "mid=?", songInfo.mid.toString())
        LitePal.deleteAll(TasksManagerModel::class.java, "mid=?", songInfo.mid.toString())
        LitePal.deleteAll(MusicToPlaylist::class.java, "mid=?", songInfo.mid.toString())
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
                val data = LitePal.where("isLove = ? ", "1").find(SongInfo::class.java)
                data.forEach {
                    musicLists.add(it.convertToMusicInfo())
                }
            }
            Constants.PLAYLIST_LOCAL_ID -> {
                val data = LitePal.where("isOnline = ? ", "0").find(SongInfo::class.java)
                data.forEach {
                    musicLists.add(it.convertToMusicInfo())
                }
            }
            else -> {
                val data = LitePal.where("pid = ?", pid).order(order).find(MusicToPlaylist::class.java)
                for (it in data) {
                    val data1 = LitePal.where("mid = ?", it.mid).find(SongInfo::class.java)
                    data1.forEach {
                        musicLists.add(it.convertToMusicInfo())
                    }
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
        return LitePal.where("mid = ? ", mid).findFirst(SongInfo::class.java).convertToMusicInfo()
    }

    fun removeSong(pid: String, mid: String) {
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=? and mid=?", pid, mid)
    }

    fun searchLocalMusic(info: String): MutableList<BaseMusicInfo> {
        val data = LitePal.where("title LIKE ? or artist LIKE ? or album LIKE ?", "%$info%", "%$info%", "%$info%").find(SongInfo::class.java)
        val musicList = mutableListOf<BaseMusicInfo>()
        data.forEach {
            musicList.add(it.convertToMusicInfo())
        }
        return musicList
    }

    fun getAllAlbum(): MutableList<Album> {
        return LitePal.findAll(Album::class.java)
    }

    fun getAllArtist(): MutableList<Artist> {
        return LitePal.findAll(Artist::class.java)
    }


    fun updateArtistList(): MutableList<Artist> {
        val sql = "SELECT songinfo.artistid,songinfo.artist,count(songinfo.title) as num FROM songinfo where songinfo.isonline=0 and songinfo.type=\"local\" GROUP BY songinfo.artist"
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
        val sql = "SELECT songinfo.albumid,songinfo.album,songinfo.artistid,songinfo.artist,count(songinfo.title) as num FROM songinfo WHERE songinfo.isonline=0 and songinfo.type=\"local\" GROUP BY songinfo.album"
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
