package com.cyl.musiclake.ui.music.playqueue

import android.support.v7.graphics.Palette
import android.view.View
import android.widget.ImageView

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.utils.ColorUtil
import com.cyl.musiclake.utils.ConvertUtils
import com.cyl.musiclake.utils.CoverLoader

/**
 * Created by D22434 on 2017/9/26.
 */

class PlayQueueAdapter : BaseItemDraggableAdapter<Music, BaseViewHolder> {

    private var mSwatch: Palette.Swatch? = null
    private var isDialog = true

    constructor(musicList: List<Music>, isDialog: Boolean) : super(R.layout.item_play_queue, musicList) {
        this.isDialog = isDialog
    }

    constructor(musicList: List<Music>) : super(R.layout.item_play_queue, musicList) {}

    override fun convert(holder: BaseViewHolder, item: Music) {

        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.title))
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.artist, item.album))

//        if (mSwatch != null) {
//            holder.setTextColor(R.id.tv_title, mSwatch!!.bodyTextColor)
//            holder.setTextColor(R.id.tv_artist, mSwatch!!.titleTextColor)
//            (holder.getView<View>(R.id.iv_clear) as ImageView).setColorFilter(ColorUtil.getBlackWhiteColor(mSwatch!!.rgb))
//        }
        if (PlayManager.getPlayingId() == item.mid) {
            holder.getView<View>(R.id.v_playing).visibility = View.VISIBLE
        } else {
            holder.getView<View>(R.id.v_playing).visibility = View.GONE
        }
//        if (isDialog) {
//            holder.setImageResource(R.id.iv_clear, R.drawable.ic_clear)
//        } else {
//            holder.setImageResource(R.id.iv_clear, R.drawable.ic_more)
//        }
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
        if (item.coverUri != null) {
            CoverLoader.loadImageView(mContext, item.coverUri, holder.getView(R.id.iv_cover))
        }
        holder.addOnClickListener(R.id.iv_more)
        holder.addOnClickListener(R.id.iv_love)
    }

    fun setPaletteSwatch(swatch: Palette.Swatch) {
        mSwatch = swatch
        notifyDataSetChanged()
    }

}
