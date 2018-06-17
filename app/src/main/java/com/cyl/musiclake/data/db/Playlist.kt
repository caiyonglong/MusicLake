package com.cyl.musiclake.data.db

import org.litepal.crud.LitePalSupport
import java.io.Serializable
import java.util.*

/**
 * 功能：本地歌单
 * 作者：yonglong on 2016/9/13 21:59
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class Playlist : LitePalSupport, Serializable {
    var id: Long = 0
    //歌单id
    var pid: String? = null
    //歌单名
    var name: String? = null
    //歌单名
    var count: Int = 0
    //更新日期
    var updateDate: Long = 0
    //创建日期
    var date: Long = 0
    //描述
    var des: String? = null
    //排列顺序
    var order: String? = null
    //封面
    var coverUrl: String? = null
    //类型 0：本地歌单 1：在线歌单
    var type: Int = 0

    //歌曲集合
    var musicList: List<Music> = ArrayList()

    constructor() {}

    constructor(pid: String?, name: String?) {
        this.pid = pid
        this.name = name
    }

    constructor(pid: String?, name: String?, order: String ="updateDate desc") {
        this.pid = pid
        this.name = name
    }

    override fun toString(): String {
        return "Playlist(id=$id, name=$name, count=$count, updateDate=$updateDate, date=$date, des=$des, order=$order, coverUrl=$coverUrl, type=$type, musicList=$musicList)"
    }

}

class MusicToPlaylist : LitePalSupport() {
    var id: Long = 0
    var pid: String? = null
    var mid: String? = null
    var count: Long = 0
    var updateDate: Long = 0
    var createDate: Long = 0
}