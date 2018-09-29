package com.cyl.musiclake.ui.chat

import android.animation.Animator
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.bean.MessageInfoBean
import com.cyl.musiclake.bean.UserInfoBean
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.socket.SocketListener
import com.cyl.musiclake.socket.SocketManager
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*


/**
 * 消息中心，收发消息
 */
class ChatActivity : BaseActivity<ChatPresenter>(), ChatContract.View {

    private var messages = mutableListOf<MessageInfoBean>()
    private var nums = 0
    private var mAdapter: ChatListAdapter? = null
    private var mUserAdapter: OnlineUserListAdapter? = null

    override fun setToolbarTitle(): String {
        return getString(R.string.chat_title)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
        mAdapter = ChatListAdapter(messages)
        messageRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        messageRsv.adapter = mAdapter
//        mAdapter?.isUpFetchEnable = true
//        mAdapter?.setUpFetchListener {
//        }
        //用户头像列表
        mUserAdapter = OnlineUserListAdapter(MusicApp.socketManager.onlineUsers)
        usersRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        usersRsv.adapter = mUserAdapter
        mUserAdapter?.bindToRecyclerView(usersRsv)
        onlineUserTv.text = getString(R.string.online_users, MusicApp.socketManager.onlineUsers.size)
    }

    override fun initData() {
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
//                mAdapter?.setNewData(messages)
                messageRsv?.smoothScrollToPosition(messages.size)
            }
        }

        override fun onOnlineUsers(users: MutableList<UserInfoBean>) {
            runOnUiThread {
                nums = users.size
                mUserAdapter?.setNewData(users)
                usersRsv.visibility = if (nums == 0) View.GONE else View.VISIBLE
                onlineUserTv.text = getString(R.string.online_users, users.size)
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
        userNameTv?.text = userInfo.nickname
        userStatusTv?.text = if (isLeave) getString(R.string.user_join) else getString(R.string.user_leave)
        CoverLoader.loadImageView(this, userInfo.avatar, userCoverIv)
        promptView?.animate()?.cancel()
        promptView?.visibility = View.VISIBLE
        promptView?.animate()?.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                promptView?.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })?.setDuration(3000)?.start()
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
            MaterialDialog.Builder(this)
                    .items("分享当前正在播放歌曲")
                    .itemsCallback { _, _, _, _ ->
                        sendMusicMessage()
                    }
                    .show()
        }
        messageInputView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
            }
            true
        }
        MusicApp.socketManager.addSocketListener(listener)
    }


    /**
     * 发送消息（普通）
     */
    private fun sendMessage() {
        val content = messageInputView?.text.toString()
        if (content.isNotEmpty()) {
            MusicApp.socketManager.sendSocketMessage(content, SocketManager.MESSAGE_BROADCAST)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_clear_msg) {
            mPresenter?.deleteMessages()
        } else if (item?.itemId == R.id.action_about) {
            MaterialDialog.Builder(this)
                    .title(R.string.prompt)
                    .content(R.string.about_music_lake)
                    .positiveText(R.string.sure)
                    .show()
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * 显示消息
     */
    override fun showMessages(msgList: MutableList<MessageInfoBean>) {
        messages = msgList
        mAdapter?.setNewData(messages)
        messageRsv?.smoothScrollToPosition(messages.size)
    }

    override fun showHistortMessages(msgList: MutableList<MessageInfoBean>) {
        LogUtil.e("showHistortMessages")
        messages.addAll(0, msgList)
        mAdapter?.notifyDataSetChanged()
        messageRsv?.smoothScrollToPosition(msgList.size)
    }

    override fun deleteSuccessful() {
        messages.clear()
        mAdapter?.notifyDataSetChanged()
        showEmptyState()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicApp.socketManager.removeSocketListener(listener)
    }

}
