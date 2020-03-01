package com.cyl.musiclake.api.music

import com.cyl.musicapi.netease.RecommendItem
import com.cyl.musicapi.netease.TracksItem
import com.cyl.musicapi.playlist.Album
import com.cyl.musicapi.playlist.ArtistsItem
import com.cyl.musicapi.playlist.MusicInfo
import com.cyl.musicapi.playlist.QualityBean
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.utils.LogUtil
import com.music.lake.musiclib.bean.BaseMusicInfo

/**
 * Created by master on 2018/4/7.
 */

object MusicUtils {
    /**
     * 在线歌单歌曲歌曲实体类转化成本地歌曲实体
     */
    fun getMusic(musicInfo: MusicInfo): BaseMusicInfo {
        val music = BaseMusicInfo()
        if (musicInfo.songId != null) {
            music.mid = musicInfo.songId
        } else if (musicInfo.id != null) {
            music.mid = musicInfo.id
        }
        music.collectId = musicInfo.id
        music.title = musicInfo.name
        music.isOnline = true
        music.type = musicInfo.vendor
        music.album = musicInfo.album.name
        music.albumId = musicInfo.album.id
        music.isCp = musicInfo.cp
        music.isDl = musicInfo.dl
        musicInfo.quality?.let {
            music.sq = it.sq
            music.hq = it.hq
            music.high = it.high
        }
        if (musicInfo.artists != null) {
            var artistIds = musicInfo.artists?.get(0)?.id
            var artistNames = musicInfo.artists?.get(0)?.name
            for (j in 1 until musicInfo.artists?.size!! - 1) {
                artistIds += ",${musicInfo.artists?.get(j)?.id}"
                artistNames += ",${musicInfo.artists?.get(j)?.name}"
            }
            music.artist = artistNames
            music.artistId = artistIds
        }
        music.coverUri = getAlbumPic(musicInfo.album.cover, musicInfo.vendor, PIC_SIZE_NORMAL)
        music.coverBig = getAlbumPic(musicInfo.album.cover, musicInfo.vendor, PIC_SIZE_NORMAL)
        music.coverSmall = getAlbumPic(musicInfo.album.cover, musicInfo.vendor, PIC_SIZE_SMALL)
        return music
    }

    /**
     * 在线歌单歌曲歌曲实体类转化成本地歌曲实体(即可)
     * (网易云歌曲)
     */
    fun getMusicList(musicInfo: MutableList<MusicInfo>?, type: String): MutableList<BaseMusicInfo> {
        val musicList = mutableListOf<BaseMusicInfo>()
        musicInfo?.forEach {
            val music = BaseMusicInfo()
            it.id?.let { id ->
                music.mid = id
            }
            music.title = it.name
            music.type = type
            music.isOnline = true
            music.album = it.album.name
            music.albumId = it.album.id
            music.isCp = it.cp
            music.isDl = it.dl
            it.quality?.let { quality ->
                music.sq = quality.sq
                music.hq = quality.hq
                music.high = quality.high
            }
            if (it.artists != null) {
                var artistIds = it.artists?.get(0)?.id
                var artistNames = it.artists?.get(0)?.name
                for (j in 1 until it.artists?.size!! - 1) {
                    artistIds += ",${it.artists?.get(j)?.id}"
                    artistNames += ",${it.artists?.get(j)?.name}"
                }
                music.artist = artistNames
                music.artistId = artistIds
            }
            music.coverUri = getAlbumPic(it.album.cover, type, PIC_SIZE_NORMAL)
            music.coverBig = getAlbumPic(it.album.cover, type, PIC_SIZE_BIG)
            music.coverSmall = getAlbumPic(it.album.cover, type, PIC_SIZE_SMALL)
            if (!it.cp) {
                musicList.add(music)
            }
        }
        return musicList
    }


    fun getNeteaseMusicList(tracks: MutableList<TracksItem>?): MutableList<BaseMusicInfo> {
        val musicList = mutableListOf<BaseMusicInfo>()
        tracks?.forEach {
            val music = BaseMusicInfo()
            music.mid = it.id.toString()
            music.title = it.name
            music.type = Constants.NETEASE
            music.album = it.al?.name
            music.isOnline = true
            music.albumId = it.al?.id.toString()
            if (it.ar != null) {
                var artistIds = it.ar?.get(0)?.id.toString()
                var artistNames = it.ar?.get(0)?.name
                for (j in 1 until it.ar?.size!! - 1) {
                    artistIds += ",${it.ar?.get(j)?.id}"
                    artistNames += ",${it.ar?.get(j)?.name}"
                }
                music.artist = artistNames
                music.artistId = artistIds
            }
            music.coverUri = getAlbumPic(it.al?.picUrl, Constants.NETEASE, PIC_SIZE_NORMAL)
            music.coverBig = getAlbumPic(it.al?.picUrl, Constants.NETEASE, PIC_SIZE_BIG)
            music.coverSmall = getAlbumPic(it.al?.picUrl, Constants.NETEASE, PIC_SIZE_SMALL)
//            if (it.cp != 0) {
            musicList.add(music)
//            }
        }
        return musicList
    }

    fun getNeteaseRecommendMusic(tracks: MutableList<RecommendItem>?): MutableList<BaseMusicInfo> {
        val musicList = mutableListOf<BaseMusicInfo>()
        tracks?.forEach {
            val music = BaseMusicInfo()
            music.mid = it.id
            music.title = it.name
            music.type = Constants.NETEASE
            music.album = it.album.name
            music.isOnline = true
            music.albumId = it.album.id
            if (it.artists != null) {
                var artistIds = it.artists?.get(0)?.id.toString()
                var artistNames = it.artists?.get(0)?.name
                for (j in 1 until it.artists?.size!! - 1) {
                    artistIds += ",${it.artists?.get(j)?.id}"
                    artistNames += ",${it.artists?.get(j)?.name}"
                }
                music.artist = artistNames
                music.artistId = artistIds
            }
            music.coverUri = getAlbumPic(it.album.picUrl, Constants.NETEASE, PIC_SIZE_NORMAL)
            music.coverBig = getAlbumPic(it.album.picUrl, Constants.NETEASE, PIC_SIZE_BIG)
            music.coverSmall = getAlbumPic(it.album.picUrl, Constants.NETEASE, PIC_SIZE_SMALL)
            musicList.add(music)
        }
        return musicList
    }

    val PIC_SIZE_SMALL = 0
    val PIC_SIZE_NORMAL = 1
    val PIC_SIZE_BIG = 2

    /**
     * 根据不同的歌曲类型生成不同的图片
     * @param size
     */
    /**
     * 根据不同的歌曲类型生成不同的图片
     * @param size
     */
    fun getAlbumPic(url: String?, type: String?, size: Int): String? {
        println(url)
        return when (type) {
            Constants.QQ -> {
                when (size) {
                    PIC_SIZE_SMALL -> {
                        url?.replace("150x150", "90x90")
                    }
                    PIC_SIZE_NORMAL -> {
                        url?.replace("150x150", "150x150")
                    }
                    else -> {
                        url?.replace("150x150", "300x300")
                    }
                }
            }
            Constants.XIAMI -> {
                val tmp = url?.split("@")?.get(0) ?: url
                when (size) {
                    PIC_SIZE_SMALL -> "$tmp@1e_1c_100Q_90w_90h"
                    PIC_SIZE_NORMAL -> "$tmp@1e_1c_100Q_150w_150h"
                    else -> "$tmp@1e_1c_100Q_450w_450h"
                }
            }
            Constants.NETEASE -> {
                val temp = url?.split("?")?.get(0) ?: url
                when (size) {
                    PIC_SIZE_SMALL -> "$temp?param=90y90"
                    PIC_SIZE_NORMAL -> "$temp?param=150y150"
                    else -> "$temp?param=450y450"
                }
            }
            Constants.BAIDU -> {
                val tmp = url?.split("@")?.get(0) ?: url
                when (size) {
                    PIC_SIZE_SMALL -> "$tmp@s_1,w_90,h_90"
                    PIC_SIZE_NORMAL -> "$tmp@s_1,w_150,h_150"
                    else -> "$tmp@s_1,w_450,h_450"
                }
            }
            else -> {
                url
            }
        }
    }

    /**
     * 本地歌曲实体转化成在线歌单歌曲实体
     */
    fun getMusicInfo(baseMusicInfo: BaseMusicInfo): MusicInfo {
        val artistIds = baseMusicInfo.artistId?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artists = baseMusicInfo.artist?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artistsBeans = mutableListOf<ArtistsItem>()
        if (artists != null) {
            for (i in artists.indices) {
                val artistsBean = artistIds?.get(i)?.let { ArtistsItem(it, artists[i]) }
                artistsBean?.let { artistsBeans.add(it) }
            }
        }
        val album = Album(baseMusicInfo.albumId, baseMusicInfo.album, baseMusicInfo.coverUri)
        if (baseMusicInfo.type == Constants.BAIDU) baseMusicInfo.isCp = false
        return MusicInfo(baseMusicInfo.mid, baseMusicInfo.mid, baseMusicInfo.title, artistsBeans, album, baseMusicInfo.type, cp = baseMusicInfo.isCp, dl = baseMusicInfo.isDl, quality = QualityBean(hq = baseMusicInfo.hq, sq = baseMusicInfo.sq, high = baseMusicInfo.high))
    }

    /**
     * 获取所有的歌手
     */
    fun getArtistInfo(baseMusicInfoInfo: BaseMusicInfo?): MutableList<Artist> {
        LogUtil.d("getArtistInfo","music?.artistId = " + baseMusicInfoInfo?.artistId + ": artistNames =" + baseMusicInfoInfo?.artist)
        val artistIds = baseMusicInfoInfo?.artistId?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artistNames = baseMusicInfoInfo?.artist?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artists = mutableListOf<Artist>()
        LogUtil.d("getArtistInfo","music?.artistId = " + artistIds.toString() + ": artistNames =" + artists.toString())
        if (artistNames != null && artistIds?.size ?: 0 == artistNames.size) {
            for (i in artistNames.indices) {
                val artist = Artist()
                artistIds?.get(i)?.let {
                    artist.artistId = it
                    artist.name = artistNames[i]
                    artist.type = baseMusicInfoInfo.type
                    artists.add(artist)
                }
            }
        }
        return artists
    }

}
