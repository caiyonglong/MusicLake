package com.cyl.musiclake.bean

import org.litepal.crud.LitePalSupport

/**
 * Created by cyl on 2018/9/11.
 * 发送消息
 */
class MessageInfoBean : LitePalSupport() {
    var id: Long = 0
    val userInfo: UserInfoBean? = null
    val datetime: String = ""
    val message: String = ""
    var type: String = ""
    override fun toString(): String {
        return "MessageEvent(userInfo=$userInfo, datetime='$datetime', message='$message')"
    }
}
