package com.cyl.musiclake.db

import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import org.litepal.crud.LitePalSupport
import java.io.Serializable
import java.util.*

/**
 * Created by yonglong on 2016/11/23.
 */

class Album : LitePalSupport, Serializable {

    var id: String? = null
    var name: String? = null
    var artistName: String? = null
    var cover: String? = null
    var type: String? = Constants.LOCAL
    var artistId: Long = 0
    var count: Int = 0

    var songs: List<Music> = ArrayList()

    constructor() {}

    constructor(id: String, name: String, artistName: String, artistId: Long, count: Int) {
        this.name = name
        this.id = id
        this.artistName = artistName
        this.artistId = artistId
        this.count = count
    }

    override fun toString(): String {
        return "Album{" +
                "name='" + name + '\''.toString() +
                ", id=" + id +
                ", artistName='" + artistName + '\''.toString() +
                ", artistId=" + artistId +
                ", count=" + count +
                '}'.toString()
    }
}
