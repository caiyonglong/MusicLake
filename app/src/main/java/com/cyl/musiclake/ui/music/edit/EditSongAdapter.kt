package com.cyl.musiclake.ui.music.edit

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class EditSongAdapter(list: MutableList<Music>) : BaseItemDraggableAdapter<Music, BaseViewHolder>(R.layout.item_music_edit, list) {
    var checkedMap = mutableMapOf<String, Music>()

    override fun convert(holder: BaseViewHolder, item: Music) {
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_artist, item.artist)
        CoverLoader.loadImageView(mContext, item.coverUri, holder.getView(R.id.iv_cover))
        holder.getView<CheckBox>(R.id.cb_select).isChecked = checkedMap.containsKey(item.mid.toString())
        holder.itemView.setOnClickListener {
            if (checkedMap.containsKey(item.mid.toString())) {
                checkedMap.remove(item.mid.toString())
            } else {
                checkedMap[item.mid.toString()] = item
            }
            notifyItemChanged(holder.adapterPosition)
        }
        holder.getView<View>(R.id.iv_more).setOnClickListener {
            BottomDialogFragment.newInstance(item).show(mContext as AppCompatActivity)
        }

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