package com.cyl.musiclake.ui.music.discover

import android.graphics.Color
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.FormatUtil

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
        val url = when (neteaseList.type) {
            Playlist.PT_NETEASE -> MusicUtils.getAlbumPic(neteaseList.coverUrl, Constants.NETEASE, 300)
            else -> neteaseList.coverUrl
        }
        CoverLoader.loadImageView(mContext, url, helper.getView(R.id.iv_cover))
    }
}

class TopPlaylistAdapter(list: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_play_list, list) {

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        val url = when (playlist.type) {
            Playlist.PT_NETEASE -> MusicUtils.getAlbumPic(playlist.coverUrl, Constants.NETEASE, 300)
            else -> playlist.coverUrl
        }
        CoverLoader.loadImageView(mContext, url, helper.getView(R.id.iv_cover))
        helper.setText(R.id.tv_title, playlist.name)
        helper.setText(R.id.tv_playCount, "播放次数：${playlist.playCount}")
    }
}


class PlaylistAdapter(playlists: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_playlist, playlists) {

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        helper.setText(R.id.tv_name, playlist.name)
        CoverLoader.loadImageView(mContext, playlist.coverUrl, helper.getView(R.id.ic_cover))
        helper.setText(R.id.tv_num, playlist.musicList.size.toString() + "首")
    }
}