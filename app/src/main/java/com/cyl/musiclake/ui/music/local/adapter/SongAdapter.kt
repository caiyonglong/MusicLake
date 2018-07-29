package com.cyl.musiclake.ui.music.local.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
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
//            val info = item.title + "," + item.artist
//            ApiManager.request(MusicApi.getMusicAlbumInfo(info), object : RequestCallBack<DoubanMusic> {
//                override fun success(result: DoubanMusic?) {
//                    val data = result?.musics
//                    data?.let {
//                        if (it.size > 0) {
//                            url = result.musics?.first()?.image
//                            item.coverUri = url
//                            notifyItemChanged(holder.adapterPosition)
//                            SongLoader.updateMusic(item)
//                            CoverLoader.loadImageView(mContext, url, holder.getView<ImageView>(R.id.iv_cover))
//                        }
//                    }
//                }
//
//                override fun error(msg: String?) {
//                }
//            })
        CoverLoader.loadImageView(mContext, item.coverUri, holder.getView(R.id.iv_cover))
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.title))
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.artist, item.album))
        if (PlayManager.getPlayingId() == item.mid) {
            holder.getView<View>(R.id.v_playing).visibility = View.VISIBLE
        } else {
            holder.getView<View>(R.id.v_playing).visibility = View.GONE
        }
        holder.addOnClickListener(R.id.iv_more)

        if (item.isCp) {
            holder.getView<View>(R.id.isCpView).visibility = View.VISIBLE
        } else {
            holder.getView<View>(R.id.isCpView).visibility = View.GONE
        }

        if (item.type == Constants.LOCAL) {
            holder.getView<View>(R.id.iv_resource).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.iv_resource).visibility = View.VISIBLE
            when {
                item.type == Constants.BAIDU -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.baidu)
                }
                item.type == Constants.NETEASE -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.netease)
                }
                item.type == Constants.QQ -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.qq)
                }
                item.type == Constants.XIAMI -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.xiami)
                }
            }
        }
    }
}