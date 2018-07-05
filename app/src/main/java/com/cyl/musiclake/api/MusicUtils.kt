package com.cyl.musiclake.api

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.cyl.musicapi.bean.ListItem
import com.cyl.musicapi.bean.SongsItem
import com.cyl.musicapi.playlist.Album
import com.cyl.musicapi.playlist.ArtistsItem
import com.cyl.musicapi.playlist.MusicInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.utils.ToastUtils

/**
 * Created by master on 2018/4/7.
 */

object MusicUtils {
    fun checkDownload(activity: AppCompatActivity, music: Music?) {
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext(), "暂无音乐播放!")
            return
        }
        if (music.type == Constants.LOCAL) {
            ToastUtils.show(MusicApp.getAppContext(), "已经本地音乐!")
            return
        }
    }

    /**
     * 分享到QQ
     */
    fun qqShare(activity: Activity, music: Music?) {
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext(), "暂无音乐播放!")
            return
        }

        val stringBuilder = activity.getString(R.string.share_content)

        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder)
        activity.startActivity(Intent.createChooser(textIntent, "分享"))
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
        music.mid = musicInfo.songId
        music.collectId = musicInfo.id
        music.title = musicInfo.name
        music.type = musicInfo.vendor
        music.album = musicInfo.album.name
        music.albumId = musicInfo.album.id
        music.commentId = musicInfo.commentId
        music.isCp = musicInfo.cp

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
        music.coverUri = musicInfo.album.cover
        music.coverBig = musicInfo.album.cover
        music.coverSmall = musicInfo.album.cover
        return music
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
        music.coverUri = song.album.cover
        music.coverBig = song.album.cover
        music.coverSmall = song.album.cover
        return music
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
        return MusicInfo(music.mid, music.mid, music.title, artistsBeans, album, music.type, music.commentId, music.isCp)
    }


}
