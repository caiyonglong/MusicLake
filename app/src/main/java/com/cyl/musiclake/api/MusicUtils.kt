package com.cyl.musiclake.api

import android.app.Activity
import android.content.Intent
import com.cyl.musicapi.bean.ListItem
import com.cyl.musicapi.bean.SongsItem
import com.cyl.musicapi.netease.TracksItem
import com.cyl.musicapi.playlist.Album
import com.cyl.musicapi.playlist.ArtistsItem
import com.cyl.musicapi.playlist.MusicInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.utils.ToastUtils
import java.lang.StringBuilder

/**
 * Created by master on 2018/4/7.
 */

object MusicUtils {

    /**
     * 分享到QQ
     */
    fun qqShare(activity: Activity, music: Music?) {
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext(), "暂无音乐播放!")
            return
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append(activity.getString(R.string.share_content))
        stringBuilder.append(activity.getString(R.string.share_song_content, music.artist, music.title))
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
//        if (music.type == Constants.LOCAL) {
//            textIntent.type = "video/mp3"
//            textIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(music.uri))
//        }
        activity.startActivity(Intent.createChooser(textIntent, "歌曲分享"))
    }

    /**
     * 在线歌曲实体类转化成本地歌曲实体
     */
    fun getMusic(item: ListItem): Music {
        val music = Music()
        music.mid = item.id.toString()
        music.title = item.name
        music.type = Constants.NETEASE
        music.album = item.album.name
        music.isOnline = true
        music.albumId = item.album.id
        music.commentId = item.commentId.toString()
        music.isCp = item.cp

        if (item.artists != null) {
            var artistIds = item.artists?.get(0)?.id
            var artistNames = item.artists?.get(0)?.name
            for (j in 1 until item.artists?.size!! - 1) {
                artistIds += ",${item.artists?.get(j)?.id}"
                artistNames += ",${item.artists?.get(j)?.name}"
            }
            music.artist = artistNames
            music.artistId = artistIds
        }
        music.coverUri = item.album.cover
        music.coverBig = item.album.cover
        music.coverSmall = item.album.cover
        return music
    }

    /**
     * 在线歌单歌曲歌曲实体类转化成本地歌曲实体
     */
    fun getMusic(musicInfo: MusicInfo): Music {
        val music = Music()
        if (musicInfo.songId != null) {
            music.mid = musicInfo.songId
            music.commentId = musicInfo.songId
        } else if (musicInfo.id != null) {
            music.mid = musicInfo.id
            music.commentId = musicInfo.id
        }
        music.collectId = musicInfo.id
        music.title = musicInfo.name
        music.isOnline = true
        music.type = musicInfo.vendor
        music.album = musicInfo.album.name
        music.albumId = musicInfo.album.id
        music.commentId = musicInfo.commentId
        music.isCp = musicInfo.cp
        music.isDl = musicInfo.dl

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
    fun getMusicList(musicInfo: MutableList<MusicInfo>?, type: String): MutableList<Music> {
        val musicList = mutableListOf<Music>()
        musicInfo?.forEach {
            val music = Music()
            it.id?.let { id ->
                music.mid = id
                music.commentId = id
            }
            music.title = it.name
            music.type = type
            music.isOnline = true
            music.album = it.album.name
            music.albumId = it.album.id
            music.isCp = it.cp
            music.isDl = it.dl
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


    fun getNeteaseMusicList(tracks: MutableList<TracksItem>?): MutableList<Music> {
        val musicList = mutableListOf<Music>()
        tracks?.forEach {
            val music = Music()
            it.id?.let { id ->
                music.mid = id
                music.commentId = id
            }
            music.title = it.name
            music.type = Constants.NETEASE
            music.album = it.album.name
            music.isOnline = true
            music.albumId = it.album.id.toString()
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
            if (it.cp != 0) {
                musicList.add(music)
            }
        }
        return musicList
    }


    /**
     * 搜索歌曲实体类转化成本地歌曲实体
     */
    fun getSearchMusic(song: SongsItem, type: String): Music {
        val music = Music()
        music.mid = song.id
        music.title = song.name
        music.type = type
        music.isOnline = true
        music.album = song.album.name
        music.albumId = song.album.id
        music.commentId = song.commentId
        music.isCp = song.cp
        music.isDl = song.dl

        if (song.artists != null) {
            var artistIds = song.artists?.get(0)?.id
            var artistNames = song.artists?.get(0)?.name
            for (j in 1 until song.artists?.size!! - 1) {
                artistIds += ",${song.artists?.get(j)?.id}"
                artistNames += ",${song.artists?.get(j)?.name}"
            }
            music.artist = artistNames
            music.artistId = artistIds
        }
        music.coverUri = getAlbumPic(song.album.cover, type, PIC_SIZE_NORMAL)
        music.coverBig = getAlbumPic(song.album.cover, type, PIC_SIZE_BIG)
        music.coverSmall = getAlbumPic(song.album.cover, type, PIC_SIZE_SMALL)
        return music
    }

    val PIC_SIZE_SMALL = 0
    val PIC_SIZE_NORMAL = 1
    val PIC_SIZE_BIG = 2

    /**
     * 根据不同的歌曲类型生成不同的图片
     * @param size
     */
    fun getAlbumPic(url: String?, type: String?, size: Int): String? {
        return when (type) {
            Constants.QQ -> {
                when (size) {
                    PIC_SIZE_SMALL -> {
                        url?.replace("300x300", "90x90")
                    }
                    PIC_SIZE_NORMAL -> {
                        url?.replace("300x300", "150x150")
                    }
                    else -> {
                        url
                    }
                }
            }
            Constants.XIAMI -> {
                when (size) {
                    PIC_SIZE_SMALL -> "$url@1e_1c_100Q_90w_90h"
                    PIC_SIZE_NORMAL -> "$url@1e_1c_100Q_150w_150h"
                    else -> "$url@1e_1c_100Q_300w_300h"
                }
            }
            Constants.NETEASE -> {
                when (size) {
                    PIC_SIZE_SMALL -> "$url?param=90y90"
                    PIC_SIZE_NORMAL -> "$url?param=150y150"
                    else -> "$url?param=450y450"
                }
            }
            Constants.BAIDU -> {
                when (size) {
                    PIC_SIZE_SMALL -> "$url?@s_1,w_90,h_90"
                    PIC_SIZE_NORMAL -> "$url?@s_1,w_150,h_150"
                    else -> "$url?@s_1,w_300,h_300"
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
    fun getMusicInfo(music: Music): MusicInfo {
        val artistIds = music.artistId?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artists = music.artist?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artistsBeans = mutableListOf<ArtistsItem>()
        if (artists != null) {
            for (i in artists.indices) {
                val artistsBean = artistIds?.get(i)?.let { ArtistsItem(it, artists[i]) }
                artistsBean?.let { artistsBeans.add(it) }
            }
        }
        val album = Album(music.albumId, music.album, music.coverUri)
        return MusicInfo(music.mid, music.mid, music.title, artistsBeans, album, music.type, music.mid, music.isCp, music.isDl)
    }


    /**
     * 获取歌手名
     */
    fun getArtistInfo(music: Music): Artist? {
        val artistIds = music.artistId?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artistNames = music.artist?.let { it.split(",").dropLastWhile { it.isEmpty() }.toTypedArray() }
        val artists = mutableListOf<Artist>()
        if (artistNames != null && artistIds?.size ?: 0 == artistNames.size) {
            for (i in artistNames.indices) {
                val artist = Artist()
                artistIds?.get(i)?.let {
                    artist.artistId = it
                    artist.name = artistNames[i]
                    artist.type = music.type
                    artists.add(artist)
                }
            }
        }
        if (artists.size > 0) {
            return artists[0]
        }
        return null
    }

}
