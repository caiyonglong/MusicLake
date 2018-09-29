package com.cyl.musiclake.ui.chat

import com.cyl.musiclake.bean.MessageInfoBean
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport


/**
 * Des    :
 * Author : master.
 * Date   : 2018/9/27 .
 */
class ChatModel() {

    //获取MessageInfoBean
    fun loadHistoryMessages(): MutableList<MessageInfoBean> {
        return LitePal.order("datetime asc").find(MessageInfoBean::class.java, true)
    }

    //获取MessageInfoBean
    fun deleteAllMessages(): Int {
        return LitePal.deleteAll(MessageInfoBean::class.java)
    }
}