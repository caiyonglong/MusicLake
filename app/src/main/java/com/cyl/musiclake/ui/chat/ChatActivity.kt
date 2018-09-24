package com.cyl.musiclake.ui.chat

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
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
import com.cyl.musiclake.event.ChatUserEvent
import com.cyl.musiclake.player.PlayManager
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
        return getString(R.string.title_activity_chat)
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
        mUserAdapter = OnlineUserListAdapter(users)
        usersRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        usersRsv.adapter = mUserAdapter
        mUserAdapter?.bindToRecyclerView(usersRsv)
    }

    override fun initInjector() {
    }

    override fun listener() {
        super.listener()
        fab?.setOnClickListener {
            fab.hide()
            cardView.visibility = View.VISIBLE
            val animationShow = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show)
            cardView.animation = animationShow
            animationShow.start()
        }
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
    }

    /**
     * 发送消息
     */
    private fun sendMessage() {
        val content = messageInputView?.text.toString()
        if (content.isNotEmpty()) {
            MusicApp.socketManager.sendSocketMessage(content)
            messageInputView?.setText("")
        }
    }

    /**
     * 发送消息(当前正在播放的音乐)
     */
    private fun sendMusicMessage() {
        val music = PlayManager.getPlayingMusic()
        if (music?.mid != null) {
            val message = MusicApp.GSON.toJson(MusicUtils.getMusicInfo(music))
            MusicApp.socketManager.sendSocketMessage(message)
        } else {
            ToastUtils.show(getString(R.string.playing_empty))
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

        updateTitle(getString(R.string.chat_title, users.size))
    }
}
