package com.cyl.musiclake.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 分类标题信息(视频分类)
 */
data class CategoryInfo(val id: Long, val title: String)

/**
 * 视频信息
 */
class VideoInfoBean() : Parcelable {

    //视频vid
    var vid: String = ""

    //视频名
    var title: String? = null

    //描述
    var description: String? = null

    //分享次数
    var shareCount: Long = 0

    //点赞
    var praisedCount: Long = 0

    //播放次数
    var playCount: Long = 0

    //评论次数
    var commentCount: Long = 0

    //视频高度
    var height: Int = 0

    //视频宽度
    var width: Int = 0

    //封面
    var coverUrl: String? = null

    //时长
    var durationms: Long = 0

    //类型:1:视频，2:mv
    var type: Int = 1


    // 播放地址
    var url: String? = null

    //发布者
    var artist = mutableListOf<Artist>()

    constructor(vid: String, title: String) : this() {
        this.vid = vid
        this.title = title
    }

    constructor(parcel: Parcel) : this() {
        vid = parcel.readString().toString()
        title = parcel.readString()
        description = parcel.readString()
        shareCount = parcel.readLong()
        praisedCount = parcel.readLong()
        playCount = parcel.readLong()
        commentCount = parcel.readLong()
        height = parcel.readInt()
        width = parcel.readInt()
        coverUrl = parcel.readString()
        durationms = parcel.readLong()
        type = parcel.readInt()
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(vid)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeLong(shareCount)
        parcel.writeLong(praisedCount)
        parcel.writeLong(playCount)
        parcel.writeLong(commentCount)
        parcel.writeInt(height)
        parcel.writeInt(width)
        parcel.writeString(coverUrl)
        parcel.writeLong(durationms)
        parcel.writeInt(type)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoInfoBean> {
        override fun createFromParcel(parcel: Parcel): VideoInfoBean {
            return VideoInfoBean(parcel)
        }

        override fun newArray(size: Int): Array<VideoInfoBean?> {
            return arrayOfNulls(size)
        }
    }

}