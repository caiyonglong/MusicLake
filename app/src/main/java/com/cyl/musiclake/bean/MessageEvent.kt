package com.cyl.musiclake.bean

import com.cyl.musicapi.playlist.MusicInfo
import com.cyl.musicapi.playlist.UserInfo
import org.litepal.crud.LitePalSupport

/**
 * Created by cyl on 2018/9/11.
 * 发送消息
 */
class MessageEvent : LitePalSupport() {
    val userInfo: UserInfo? = null
    val datetime: String = ""
    val message: String = ""
    var music: MusicInfo? = null
    var type: String = ""
    override fun toString(): String {
        return "MessageEvent(userInfo=$userInfo, datetime='$datetime', message='$message')"
    }
}
