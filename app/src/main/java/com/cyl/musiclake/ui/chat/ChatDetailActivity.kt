package com.cyl.musiclake.ui.chat

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseActivity
import kotlinx.android.synthetic.main.card_chat_detail.*


/**
 * 消息中心，收发消息
 */
class ChatDetailActivity : BaseActivity<ChatPresenter>() {


    private var mUserAdapter: OnlineUserListAdapter? = null

    override fun setToolbarTitle(): String {
        return getString(R.string.chat_detail)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_chat_detail
    }

    override fun initView() {
        //用户头像列表
        mUserAdapter = OnlineUserListAdapter(MusicApp.socketManager.onlineUsers)
        usersRsv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        usersRsv.adapter = mUserAdapter
        usersRsv.isNestedScrollingEnabled = false
        usersRsv.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        mUserAdapter?.bindToRecyclerView(usersRsv)
        onlineUserTv.text = getString(R.string.online_users, MusicApp.socketManager.onlineUsers.size)
    }

    override fun initData() {

    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

}
