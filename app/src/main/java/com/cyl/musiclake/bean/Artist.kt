package com.cyl.musiclake.bean

import android.os.Parcel
import android.os.Parcelable
import com.cyl.musiclake.common.Constants
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by yonglong on 2016/11/23.
 */

class Artist() : LitePalSupport(), Parcelable {
    var name: String? = null
    var id: Long = 0
    var artistId: String? = null
    var count: Int = 0
    var type: String? = Constants.LOCAL
    var picUrl: String? = null
    var desc: String? = null
    var musicSize: Int = 0
    var score: Int = 0
    var albumSize: Int = 0
    var followed: Boolean = false

    var songs = mutableListOf<Music>()

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        id = parcel.readLong()
        artistId = parcel.readString()
        count = parcel.readInt()
        type = parcel.readString()
        picUrl = parcel.readString()
        desc = parcel.readString()
        musicSize = parcel.readInt()
        score = parcel.readInt()
        albumSize = parcel.readInt()
    }

    constructor(id: Long, name: String, count: Int) : this() {
        this.name = name
        this.artistId = id.toString()
        this.musicSize = count
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeLong(id)
        parcel.writeString(artistId)
        parcel.writeInt(count)
        parcel.writeString(type)
        parcel.writeString(picUrl)
        parcel.writeString(desc)
        parcel.writeInt(musicSize)
        parcel.writeInt(score)
        parcel.writeInt(albumSize)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(parcel)
        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }
    }
}
