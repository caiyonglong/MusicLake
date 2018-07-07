package com.cyl.musiclake.ui.music.playlist

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class PlaylistAdapter(playlists: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_playlist, playlists) {

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        helper.setText(R.id.tv_name, playlist.name)
        helper.setText(R.id.tv_num, playlist.count.toString() + "首")


        ApiManager.request(playlist.pid?.let { PlaylistApiServiceImpl.getMusicList(it) }, object : RequestCallBack<MutableList<Music>> {
            override fun error(msg: String?) {

            }

            override fun success(result: MutableList<Music>) {
                if (result.size > 0) {
                    playlist.coverUrl = result[0].coverUri
                    CoverLoader.loadImageView(mContext, playlist.coverUrl, helper.getView(R.id.iv_album))
                }
            }
        })
    }
}