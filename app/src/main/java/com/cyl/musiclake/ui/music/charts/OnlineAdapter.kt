package com.cyl.musiclake.ui.music.charts

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class OnlineAdapter(playlist: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_online_large, playlist) {

    private val viewIds = arrayListOf(R.id.tv_music_1, R.id.tv_music_2, R.id.tv_music_3)
    private val stringIds = arrayListOf(R.string.song_list_item_title_1, R.string.song_list_item_title_2, R.string.song_list_item_title_3)

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        CoverLoader.loadImageView(mContext, playlist.coverUrl, helper.getView(R.id.iv_cover))
        helper.setText(R.id.title, playlist.name)
        for (i in 0 until viewIds.size) {
            if (playlist.musicList.size <= i) continue
            val music = playlist.musicList[i]
            helper.setText(viewIds[i], mContext.getString(stringIds[i],
                    music.title, music.artist))
        }
    }
}