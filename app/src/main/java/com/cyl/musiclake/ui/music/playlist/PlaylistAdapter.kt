package com.cyl.musiclake.ui.music.playlist

import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class PlaylistAdapter(playlists: MutableList<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_playlist, playlists) {

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        helper.setText(R.id.tv_name, playlist.name)
        CoverLoader.loadImageView(context, playlist.coverUrl, helper.getView(R.id.iv_cover))
        helper.setText(R.id.tv_num, "${playlist.total}首")
        helper.setVisible(R.id.tv_num, true)

        helper.itemView.setOnClickListener {
            NavigationHelper.navigateToPlaylist(context as AppCompatActivity, playlist, null)
        }
    }
}

