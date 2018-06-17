package com.cyl.musiclake.ui.music.local.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.utils.ConvertUtils
import com.cyl.musiclake.utils.CoverLoader

/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SongAdapter(musicList: List<Music>) : BaseQuickAdapter<Music, BaseViewHolder>(R.layout.item_music, musicList) {

    override fun convert(holder: BaseViewHolder, item: Music) {
        CoverLoader.loadImageView(mContext, item.coverUri, holder.getView<ImageView>(R.id.iv_cover))
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.title))
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.artist, item.album))
        if (PlayManager.getPlayingId() == item.mid) {
            holder.getView<View>(R.id.v_playing).visibility = View.VISIBLE
        } else {
            holder.getView<View>(R.id.v_playing).visibility = View.GONE
        }
        holder.addOnClickListener(R.id.iv_more)
    }


}