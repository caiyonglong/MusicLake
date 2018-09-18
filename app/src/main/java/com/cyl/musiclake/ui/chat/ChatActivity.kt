package com.cyl.musiclake.ui.chat

import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.event.ChatUserEvent
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

    var curPosition = 0
    var curUserNums = 0
    var mAdapter: ChatListAdapter? = null
    var mUserAdapter: OnlineUserListAdapter? = null

    override fun setToolbarTitle(): String {
        return getString(R.string.message_center)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
        toggleSocket.isChecked = MusicApp.isOpenSocket
    }

    override fun initData() {
        curPosition = messages.size
        mAdapter = ChatListAdapter(messages)
        messageRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        messageRsv.adapter = mAdapter
        mAdapter?.bindToRecyclerView(messageRsv)
        0
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
            fab.visibility = View.GONE
            cardView.visibility = View.VISIBLE
            val animationShow = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show)
            cardView.animation = animationShow
            animationShow.start()
        }
        sendBtn.setOnClickListener {
            val content = messageInputView?.text.toString()
            MusicApp.socketManager.sendSocketMessage(content)
            messageInputView?.setText("")
        }
        toggleSocket.setOnCheckedChangeListener { _, isChecked ->
            MusicApp.isOpenSocket = isChecked
            MusicApp.socketManager.toggleSocket(isChecked)
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
            mAdapter?.setEmptyView(R.layout.view_song_empty)
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
        if (curPosition == 0) {
            mAdapter?.setEmptyView(R.layout.view_song_empty)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUserInfo(chatUsers: ChatUserEvent) {
        curUserNums = chatUsers.users.size
        mUserAdapter?.setNewData(users)
        usersRsv.visibility = if (curUserNums == 0) View.GONE else View.VISIBLE
    }
}
