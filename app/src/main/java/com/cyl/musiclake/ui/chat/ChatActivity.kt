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
import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.ChatUserEvent
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.socket.SocketListener
import com.cyl.musiclake.socket.SocketManager
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 消息中心，收发消息
 */
class ChatActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {
    companion object {
        var messages = mutableListOf<MessageEvent>()
        var users = mutableListOf<UserInfo>()
    }

    private var curPosition = 0
    private var nums = 0
    var mAdapter: ChatListAdapter? = null
    private var mUserAdapter: OnlineUserListAdapter? = null

    override fun setToolbarTitle(): String {
        return getString(R.string.chat_title)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
        curPosition = messages.size
        mAdapter = ChatListAdapter(messages)
        messageRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        messageRsv.adapter = mAdapter
        mUserAdapter = OnlineUserListAdapter(users)
        usersRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        usersRsv.adapter = mUserAdapter
        mUserAdapter?.bindToRecyclerView(usersRsv)
        onlineUserTv.text = getString(R.string.online_users, users.size)
    }

    override fun initData() {
        if (Intent.ACTION_SEND == intent.action && intent.type != null) {
            if ("text/plain" == intent.type) {
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
    }

    /**
     * socket监听事件
     */
    val listener = object : SocketListener {
        override fun onLeaveEvent(user: UserInfo) {
            runOnUiThread {
                updateUserStatus(user, true)
            }
        }

        override fun onJoinEvent(user: UserInfo) {
            runOnUiThread {
                updateUserStatus(user, false)
            }
        }

        override fun onError(msg: String) {
        }
    }


    fun updateUserStatus(userInfo: UserInfo, isLeave: Boolean) {
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
     * 发送消息
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
        val music = PlayManager.getPlayingMusic()
        when {
            music?.type == Constants.LOCAL -> {
                val message = getString(R.string.share_local_song, music.artist, music.title)
                MusicApp.socketManager.sendSocketMessage(message, SocketManager.MESSAGE_BROADCAST)
            }
            music?.mid != null -> {
                val message = MusicApp.GSON.toJson(MusicUtils.getMusicInfo(music))
                MusicApp.socketManager.sendSocketMessage(message, SocketManager.MESSAGE_SHARE)
            }
            else -> ToastUtils.show(getString(R.string.playing_empty))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_clear_msg) {
            messages.clear()
            curPosition = 0
            mAdapter?.notifyDataSetChanged()
        } else if (item?.itemId == R.id.action_about) {
            MaterialDialog.Builder(this)
                    .title(R.string.prompt)
                    .content(R.string.about_music_lake)
                    .positiveText(R.string.sure)
                    .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicApp.socketManager.removeSocketListener(listener)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateMessage(msg: MessageEvent) {
        curPosition = messages.size
        mAdapter?.setNewData(messages)
        messageRsv?.smoothScrollToPosition(curPosition)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUserInfo(chatUsers: ChatUserEvent) {
        nums = chatUsers.users.size
        mUserAdapter?.setNewData(users)
        usersRsv.visibility = if (nums == 0) View.GONE else View.VISIBLE
        onlineUserTv.text = getString(R.string.online_users, users.size)
    }
}
