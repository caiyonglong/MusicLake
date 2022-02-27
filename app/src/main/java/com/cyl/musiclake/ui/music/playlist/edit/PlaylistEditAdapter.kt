package com.cyl.musiclake.ui.music.playlist.edit

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.utils.ConvertUtils

/**
 * 作者：yonglong
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class PlaylistEditAdapter(list: MutableList<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_playlist_edit, list), DraggableModule {
    /**
     * 选中列表
     */
    var checkedMap = mutableMapOf<String, Playlist>()

    override fun convert(holder: BaseViewHolder, item: Playlist) {
        holder.setText(R.id.tv_name, ConvertUtils.getTitle(item.name))
        holder.getView<CheckBox>(R.id.cb_playlist).isChecked = checkedMap.containsKey(item.pid.toString())

        holder.itemView.setOnClickListener {
            if (checkedMap.containsKey(item.pid.toString())) {
                checkedMap.remove(item.pid.toString())
            } else {
                checkedMap[item.pid.toString()] = item
            }
            notifyItemChanged(holder.adapterPosition)
        }
    }
}