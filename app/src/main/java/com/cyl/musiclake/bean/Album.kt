package com.cyl.musiclake.bean

import com.cyl.musiclake.common.Constants
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by yonglong on 2016/11/23.
 */

class Album : LitePalSupport, Serializable {

    var id: Int = 0
    var albumId: String? = null
    var name: String? = null
    var artistName: String? = null
    var cover: String? = null
    var type: String? = Constants.LOCAL
    var artistId: Long = 0
    var count: Int = 0

    var songs = mutableListOf<Music>()

    constructor() {}

    constructor(id: String, name: String, artistName: String, artistId: Long, count: Int) {
        this.name = name
        this.albumId = id
        this.artistName = artistName
        this.artistId = artistId
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
}
