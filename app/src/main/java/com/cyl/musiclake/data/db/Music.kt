package com.cyl.musiclake.data.db

import android.os.Parcel
import android.os.Parcelable

import org.litepal.crud.LitePalSupport

/**
 * 作者：yonglong on 2016/8/9 10:50
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class Music() : LitePalSupport(), Parcelable {
    // 歌曲类型 本地/网络
    var type: String? = null
    var id: Long = 0
    // 歌曲id
    var mid: String? = null
    // 音乐标题
    var title: String? = null
    // 艺术家
    var artist: String? = null//{123,123,13}
    // 专辑
    var album: String? = null
    // 专辑id
    var artistId: String? = null//{123,123,13}
    // 专辑id
    var albumId: String? = null
    // 专辑内歌曲个数
    var trackNumber: Int = 0
    // 持续时间
    var duration: Long = 0
    // 收藏
    var isLove: Boolean = false
    // [本地|网络]
    var isOnline: Boolean = true
    // 音乐路径
    var uri: String? = null
    // [本地|网络] 音乐歌词地址
    var lyric: String? = null
    // [本地|网络]专辑封面路径
    var coverUri: String? = null
    // [网络]专辑封面
    var coverBig: String? = null
    // [网络]small封面
    var coverSmall: String? = null
    // 文件名
    var fileName: String? = null
    // 文件大小
    var fileSize: Long = 0
    // 发行日期
    var year: String? = null
    //更新日期
    var date: Long = 0
    //在线歌曲是否限制播放，false 可以播放
    var isCp: Boolean = false
    //歌曲评论Id
    var commentId: String? = null
    //收藏id
    var collectId: String? = null

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()
        id = parcel.readLong()
        mid = parcel.readString()
        title = parcel.readString()
        artist = parcel.readString()
        album = parcel.readString()
        artistId = parcel.readString()
        albumId = parcel.readString()
        trackNumber = parcel.readInt()
        duration = parcel.readLong()
        isLove = parcel.readByte() != 0.toByte()
        isOnline = parcel.readByte() != 0.toByte()
        uri = parcel.readString()
        lyric = parcel.readString()
        coverUri = parcel.readString()
        coverBig = parcel.readString()
        coverSmall = parcel.readString()
        fileName = parcel.readString()
        fileSize = parcel.readLong()
        year = parcel.readString()
        date = parcel.readLong()
        isCp = parcel.readByte() != 0.toByte()
        commentId = parcel.readString()
        collectId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeValue(id)
        parcel.writeString(mid)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeString(artistId)
        parcel.writeString(albumId)
        parcel.writeInt(trackNumber)
        parcel.writeLong(duration)
        parcel.writeByte(if (isLove) 1 else 0)
        parcel.writeByte(if (isOnline) 1 else 0)
        parcel.writeString(uri)
        parcel.writeString(lyric)
        parcel.writeString(coverUri)
        parcel.writeString(coverBig)
        parcel.writeString(coverSmall)
        parcel.writeString(fileName)
        parcel.writeLong(fileSize)
        parcel.writeString(year)
        parcel.writeLong(date)
        parcel.writeByte(if (isCp) 1 else 0)
        parcel.writeString(commentId)
        parcel.writeString(collectId)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Music(type=$type, id=$id, mid=$mid, title=$title, artist=$artist, album=$album, artistId=$artistId, albumId=$albumId, trackNumber=$trackNumber, duration=$duration, isLove=$isLove, isOnline=$isOnline, uri=$uri, lyric=$lyric, coverUri=$coverUri, coverBig=$coverBig, coverSmall=$coverSmall, fileName=$fileName, fileSize=$fileSize, year=$year, date=$date, isCp=$isCp, commentId=$commentId, collectId=$collectId)"
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }

}
