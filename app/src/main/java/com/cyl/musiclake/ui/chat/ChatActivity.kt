package com.cyl.musiclake.ui.chat

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AnimationUtils
import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChatActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {
    companion object {
        var messages = mutableListOf<MessageEvent>()
        var users = mutableListOf<UserInfo>()
    }

    var curPosition = 0
    var mAdapter: ChatListAdapter? = null

    override fun setToolbarTitle(): String {
        return getString(R.string.message_center)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
    }

    override fun initData() {
        curPosition = messages.size
        mAdapter = ChatListAdapter(messages)
        messageRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        messageRsv.adapter = mAdapter
        mAdapter?.bindToRecyclerView(messageRsv)
    }

    override fun initInjector() {
    }

    override fun listener() {
        super.listener()
        fab?.setOnClickListener {
            fab.visibility = View.GONE
            inputView.visibility = View.VISIBLE
            val animationShow = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show)
            inputView.animation = animationShow
            animationShow.start()
        }
        sendBtn.setOnClickListener {
            val content = messageInputView?.text.toString()
            MainActivity.socketManager.sendSocketMessage(content)
            messageInputView?.setText("")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateMessage(msg: MessageEvent) {
        mAdapter?.notifyItemRangeInserted(curPosition, messages.size - curPosition)
        curPosition = messages.size
        messageRsv?.smoothScrollToPosition(messages.size)
    }
}
