package com.cyl.musiclake.bean

import com.music.lake.musiclib.bean.BaseMusicInfo


/**
 * Created by cyl on 2018/5/10.
 */
class SearchResult {
    var songs: MutableList<BaseMusicInfo>? = null
    var nextPager: String? = ""
    var duration: Long = 0
}
