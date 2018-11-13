package com.cyl.musiclake.bean

import android.os.Parcel
import android.os.Parcelable
import com.cyl.musicapi.playlist.QualityBean
import org.litepal.crud.LitePalSupport

/**
 * 作者：yonglong on 2016/8/9 10:50
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class Music() : LitePalSupport(), Parcelable {
    // 歌曲类型 本地/网络
    var type: String? = null
    //数据库存储id
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
    //在线歌曲是否付费歌曲，false 不能下载
    var isDl: Boolean = true
    //收藏id
    var collectId: String? = null
    //音乐品质，默认标准模式
    var quality: Int = 128000

    //音乐品质选择
    var hq: Boolean = false //192
    var sq: Boolean = false //320
    var high: Boolean = false //999
    //是否有mv 0代表无，1代表有
    var hasMv: Int = 0

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
        isDl = parcel.readByte() != 0.toByte()
        collectId = parcel.readString()
        quality = parcel.readInt()
        hq = parcel.readByte() != 0.toByte()
        sq = parcel.readByte() != 0.toByte()
        high = parcel.readByte() != 0.toByte()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(type)
        p0.writeLong(id)
        p0.writeString(mid)
        p0.writeString(title)
        p0.writeString(artist)
        p0.writeString(album)
        p0.writeString(artistId)
        p0.writeString(albumId)
        p0.writeInt(trackNumber)
        p0.writeLong(duration)
        p0.writeByte(if (isLove) 1 else 0)
        p0.writeByte(if (isOnline) 1 else 0)
        p0.writeString(uri)
        p0.writeString(lyric)
        p0.writeString(coverUri)
        p0.writeString(coverBig)
        p0.writeString(coverSmall)
        p0.writeString(fileName)
        p0.writeLong(fileSize)
        p0.writeString(year)
        p0.writeLong(date)
        p0.writeByte(if (isCp) 1 else 0)
        p0.writeByte(if (isDl) 1 else 0)
        p0.writeString(collectId)
        p0.writeInt(quality)
        p0.writeByte(if (hq) 1 else 0)
        p0.writeByte(if (sq) 1 else 0)
        p0.writeByte(if (high) 1 else 0)
    }

    override fun toString(): String {
        return "Music(type=$type, id=$id, mid=$mid, title=$title, " +
                "artist=$artist, album=$album, artistId=$artistId, " +
                "albumId=$albumId, trackNumber=$trackNumber," +
                " duration=$duration, isLove=$isLove, isOnline=$isOnline, " +
                "uri=$uri, lyric=$lyric, coverUri=$coverUri, coverBig=$coverBig, coverSmall=$coverSmall," +
                " fileName=$fileName, fileSize=$fileSize, year=$year, date=$date, isCp=$isCp, isDl=$isDl, " +
                "collectId=$collectId, quality=$quality," +
                "qualityList=$high $hq $sq)"
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

