package com.cyl.musiclake.event

/**
 * Created by D22434 on 2018/1/10.
 */

class LyricChangedEvent {
    var lyric: String? = null
    var mid: String? = null
    var isStatus: Boolean = false

    constructor(lyric: String, mid: String, status: Boolean) {
        this.lyric = lyric
        this.mid = mid
        this.isStatus = status
    }

    constructor(lyric: String, status: Boolean) {
        this.lyric = lyric
        this.isStatus = status
    }
}
