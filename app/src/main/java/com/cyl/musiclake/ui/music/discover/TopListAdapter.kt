package com.cyl.musiclake.ui.music.discover

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class TopListAdapter(list: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_top_list, list) {

    override fun convert(helper: BaseViewHolder, neteaseList: Playlist) {
        if (neteaseList.name == null)
            return
        helper.setText(R.id.title, neteaseList.name)
        if (neteaseList.coverUrl == null)
            return
        CoverLoader.loadImageView(mContext, neteaseList.coverUrl, helper.getView(R.id.iv_cover))
    }
}

class PlaylistAdapter(playlists: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_playlist, playlists) {

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        helper.setText(R.id.tv_name, playlist.name)
        CoverLoader.loadImageView(mContext, playlist.coverUrl, helper.getView(R.id.iv_album))
        helper.setText(R.id.tv_num, playlist.musicList.size.toString() + "首")
    }
}