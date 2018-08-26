package com.cyl.musiclake.bean

import com.cyl.musiclake.common.Constants
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by yonglong on 2016/11/23.
 */

class Artist : LitePalSupport, Serializable {
    var name: String? = null
    var id: Long = 0
    var artistId: Long = 0
    var count: Int = 0
    var type: String? = Constants.LOCAL
    var picUrl: String? = null
    var desc: String? = null
    var musicSize: Int = 0
    var score: Int = 0
    var albumSize: Int = 0

    var songs = mutableListOf<Music>()

    constructor() {}

    constructor(id: Long, name: String, count: Int) {
        this.name = name
        this.artistId = id
        this.musicSize = count
    }
}
