//package com.cyl.musiclake.ui.music.playlist
//
//import android.graphics.Color
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.BaseViewHolder
//import com.cyl.musicapi.netease.SubItem
//import com.cyl.musiclake.R
//
//class AllCateAdapter(val subs: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_cate_tag, subs) {
//
//    var tag: String? = null
//
//    fun setSelectName(position: Int) {
//        tag = subs[position]
//    }
//
//    override fun convert(holder: BaseViewHolder, item: String) {
//        holder.setTextColor(R.id.titleTv, Color.parseColor("#000000"))
//        tag?.let {
//            if (it == item) {
//                holder.setTextColor(R.id.titleTv, Color.parseColor("#DB4437"))
//            }
//        }
//        holder.setText(R.id.titleTv, item)
//        holder.addOnClickListener(R.id.titleTv)
//    }
//}
