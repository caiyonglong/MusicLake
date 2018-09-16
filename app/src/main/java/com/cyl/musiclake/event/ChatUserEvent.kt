package com.cyl.musiclake.event

import com.cyl.musicapi.playlist.UserInfo

/**
 * Created by cyl on 2018/9/11.
 * 发送消息
 */
data class ChatUserEvent(val users: MutableList<UserInfo>)
