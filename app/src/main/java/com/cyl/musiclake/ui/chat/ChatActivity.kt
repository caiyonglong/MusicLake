package com.cyl.musiclake.ui.chat

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.MessageInfoBean
import com.cyl.musiclake.bean.UserInfoBean
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.socket.SocketListener
import com.cyl.musiclake.socket.SocketManager
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.widget.NoticeView
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*
import org.jetbrains.anko.startActivity


/**
 * 消息中心，收发消息
 */
class ChatActivity : BaseActivity<ChatPresenter>(), ChatContract.View {

    private var messages = mutableListOf<MessageInfoBean>()
    private var nums = 0
    private var mAdapter: ChatListAdapter? = null

    override fun setToolbarTitle(): String {
        return getString(R.string.chat_title)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
        mAdapter = ChatListAdapter(messages)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        linearLayoutManager.stackFromEnd = true
        messageRsv.layoutManager = linearLayoutManager
        messageRsv.adapter = mAdapter
    }

    override fun initData() {
        showLoading()
        mPresenter?.loadMessages()
        if (Intent.ACTION_SEND == intent.action && intent.type != null) {
            if (Constants.TEXT_PLAIN == intent.type) {
                dealTextMessage(intent)
            }
        }
    }

    /**
     * 处理接收分享的信息
     */
    private fun dealTextMessage(intent: Intent?) {
        val title = intent?.getStringExtra(Intent.EXTRA_TEXT)
        messageInputView?.setText(title)
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }
    /**
     * socket监听事件
     */
    val listener = object : SocketListener {
        override fun onMessage(msgInfo: MessageInfoBean) {
            runOnUiThread {
                messages.add(msgInfo)
                mAdapter?.notifyItemInserted(messages.size)
                messageRsv?.smoothScrollToPosition(messages.size)
            }
        }

        override fun onOnlineUsers(users: MutableList<UserInfoBean>) {
            runOnUiThread {
                nums = users.size
//                mUserAdapter?.setNewData(users)
//                usersRsv.visibility = if (nums == 0) View.GONE else View.VISIBLE
//                onlineUserTv.text = getString(R.string.online_users, users.size)
                updateTitle(getString(R.string.chat_title))
            }
        }

        override fun onLeaveEvent(user: UserInfoBean) {
            runOnUiThread {
                updateUserStatus(user, true)
            }
        }

        override fun onJoinEvent(user: UserInfoBean) {
            runOnUiThread {
                updateUserStatus(user, false)
            }
        }

        override fun onError(msg: String) {
        }
    }

    /**
     * 更新用户状态（上下线）
     */
    fun updateUserStatus(userInfo: UserInfoBean, isLeave: Boolean) {
        val noticeView = NoticeView(this)
        noticeView.setNewData(userInfo, isLeave)
        noticeView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        userNoticeContainerView.addView(noticeView)
        noticeView.postDelayed({ userNoticeContainerView.removeView(noticeView) }, 3000)
        if (userNoticeContainerView.childCount > 5) {
            userNoticeContainerView.removeViewAt(0)
        }
    }

    /**
     * 设置监听
     */
    override fun listener() {
        super.listener()
        sendBtn.setOnClickListener {
            sendMessage()
        }
        messageInputView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    addIv.visibility = View.VISIBLE
                    sendBtn.visibility = View.GONE
                } else {
                    addIv.visibility = View.GONE
                    sendBtn.visibility = View.VISIBLE
                }
            }
        })
        addIv.setOnClickListener {
            //            updateUserStatus(MusicApp.socketManager.onlineUsers[0], true)
            MaterialDialog(this).show {
                title(R.string.share_playing_song)
                positiveButton(R.string.sure, null, object : DialogCallback {
                    override fun invoke(p1: MaterialDialog) {
                        sendMusicMessage()
                    }
                })
            }
        }
        messageInputView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
            }
            true
        }
        mSwipeRefreshLayout?.isEnabled = true
        SocketManager.addSocketListener(listener)
    }

    /**
     * 下拉加载更多
     */
    private fun startUpFetch() {
        /**
         * set fetching on when start network request.
         */
        /**
         * get data from internet.
         */
        mSwipeRefreshLayout?.isRefreshing = true
        messageRsv.postDelayed({
            if (messages.size > 0) {
                mPresenter?.loadMessages(messages[0].datetime)
            }
        }, 2000)
    }


    /**
     * 发送消息（普通）
     */
    private fun sendMessage() {
        val content = messageInputView?.text.toString()
        if (content.isNotEmpty()) {
            SocketManager.sendSocketMessage(content, SocketManager.MESSAGE_BROADCAST)
            messageInputView?.setText("")
        }
    }

    /**
     * 发送消息(当前正在播放的音乐)
     */
    private fun sendMusicMessage() {
        mPresenter?.sendMusicMessage()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.action_detail) {
            startActivity<ChatDetailActivity>()
        } else if (item?.itemId == R.id.action_about) {
            MaterialDialog(this).show {
                title(R.string.chat_about)
                message(R.string.about_music_lake)
                positiveButton(R.string.sure)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * 显示消息
     */
    override fun showMessages(msgList: MutableList<MessageInfoBean>) {
        messages = msgList
        mAdapter?.setNewInstance(messages)
        messageRsv?.smoothScrollToPosition(messages.size)
    }

    /**
     * 显示历史消息
     */
    override fun showHistortMessages(msgList: MutableList<MessageInfoBean>) {
        LogUtil.e("showHistortMessages =" + msgList.size)
        messages.addAll(0, msgList)
        mAdapter?.notifyDataSetChanged()
        hideLoading()
        messageRsv?.smoothScrollToPosition(msgList.size)
        mSwipeRefreshLayout?.isRefreshing = false
        if (msgList.size == 0) {
            mSwipeRefreshLayout?.isEnabled = false
        }
    }

    override fun deleteSuccessful() {
        messages.clear()
        mAdapter?.notifyDataSetChanged()
        showEmptyState()
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.removeSocketListener(listener)
    }

}
