package com.cyl.musiclake.bean

import com.cyl.musiclake.common.Constants
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by yonglong on 2016/11/23.
 */

class Album : LitePalSupport, Serializable, Comparable<Album> {


    var id: Int = 0
    var albumId: String? = null
    var name: String? = null
    var artistName: String? = null
    var cover: String? = null
    var type: String? = Constants.LOCAL
    var artistId: String? = null
    var info: String? = null
    var count: Int = 0

    var songs = mutableListOf<Music>()

    constructor() {}

    constructor(id: String, name: String, artistName: String, artistId: Long, count: Int) {
        this.name = name
        this.albumId = id
        this.artistName = artistName
        this.artistId = artistId.toString()
        this.count = count
    }

    override fun toString(): String {
        return "Album{" +
                "name='" + name + '\''.toString() +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\''.toString() +
                ", artistId=" + artistId +
                ", count=" + count +
                '}'.toString()
    }

    override fun compareTo(other: Album): Int {
        return (this.name ?: "").compareTo(other.name ?: "")
    }
}
