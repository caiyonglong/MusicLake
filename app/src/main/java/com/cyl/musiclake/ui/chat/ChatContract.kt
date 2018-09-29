package com.cyl.musiclake.ui.chat

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.MessageInfoBean


interface ChatContract {

    interface View : BaseContract.BaseView {
        fun showMessages(msgList: MutableList<MessageInfoBean>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadMessages()
        fun deleteMessages()
    }
}
