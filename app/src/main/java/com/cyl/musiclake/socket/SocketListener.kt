package com.cyl.musiclake.socket

import com.cyl.musicapi.playlist.UserInfo

/**
 * Des    :
 * Author : master.
 * Date   : 2018/9/26 .
 */
interface SocketListener {
    /**
     * 用户下线
     */
    fun onLeaveEvent(user: UserInfo)

    /**
     * 用户上线
     */
    fun onJoinEvent(user: UserInfo)

    /**
     * 错误
     */
    fun onError(msg: String)
}