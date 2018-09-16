package com.cyl.musiclake.ui.chat

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class ChatListAdapter(list: List<MessageEvent>) : BaseQuickAdapter<MessageEvent, BaseViewHolder>(R.layout.item_chat, list) {

    override fun convert(helper: BaseViewHolder, item: MessageEvent) {
        helper.setText(R.id.tv_comment_user, item.userInfo?.nickname ?: "")
        helper.setText(R.id.tv_comment_time, item.datetime)
        helper.setText(R.id.tv_comment_content, item.message)
        CoverLoader.loadImageView(mContext, item.userInfo?.avatar, helper.getView(R.id.civ_cover))
    }

}

class OnlineUserListAdapter(list: List<UserInfo>) : BaseQuickAdapter<UserInfo, BaseViewHolder>(R.layout.item_user, list) {

    override fun convert(helper: BaseViewHolder, item: UserInfo) {
        CoverLoader.loadImageView(mContext, item.avatar, helper.getView(R.id.user_avatar))
    }

}
