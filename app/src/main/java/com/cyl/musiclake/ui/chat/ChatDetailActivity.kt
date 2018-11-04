package com.cyl.musiclake.ui.chat

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
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
        usersRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        usersRsv.adapter = mUserAdapter
        usersRsv.isNestedScrollingEnabled = false
        usersRsv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mUserAdapter?.bindToRecyclerView(usersRsv)
        onlineUserTv.text = getString(R.string.online_users, MusicApp.socketManager.onlineUsers.size)
    }

    override fun initData() {

    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

}
