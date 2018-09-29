package com.cyl.musiclake.ui.chat

import com.cyl.musiclake.base.BasePresenter
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/9/27 .
 */
class ChatPresenter @Inject
constructor() : BasePresenter<ChatContract.View>(), ChatContract.Presenter {
    override fun deleteMessages() {
        val data = model.deleteAllMessages()
    }

    val model by lazy { ChatModel() }

    override fun loadMessages() {
        val data = model.loadHistoryMessages()
        mView?.showMessages(data)
    }

}