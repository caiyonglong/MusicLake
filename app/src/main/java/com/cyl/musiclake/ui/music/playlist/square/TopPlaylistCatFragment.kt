package com.cyl.musiclake.ui.music.playlist.square

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.cyl.musiclake.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.all_category_dialog.*

/**
 * Des    : 精品歌单分类选择列表
 * Author : master.
 * Date   : 2018/8/23 .
 */
class TopPlaylistCatFragment : BottomSheetDialogFragment() {
    private var rootView: View? = null
    private val allChipTv by lazy { rootView?.findViewById<TextView>(R.id.allTagTv) }
    private val categoryTags = mutableListOf("华语", "欧美", "韩语", "日语", "粤语", "小语种", "运动", "ACG", "影视原声", "流行", "摇滚", "后摇", "古风", "民谣", "轻音乐", "电子", "器乐", "说唱", "古典", "爵士")

    private var mAdapter: AllCateAdapter? = null
    var isHighQuality: Boolean = false
    var successListener: ((String) -> Unit?)? = null

    /**
     *显示出对话框
     */
    fun showIt(context: androidx.fragment.app.FragmentActivity?) {
        dialog?.dismiss()
        val transaction = context?.supportFragmentManager?.beginTransaction()
        transaction?.add(this, tag)?.commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.all_category_dialog, container, false)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView(categoryTags)
        allChipTv?.setOnClickListener {
            successListener?.invoke("全部")
            dismissAllowingStateLoss()
        }
    }

    private fun initRecyclerView(list: MutableList<String>) {
        if (mAdapter == null) {
            mAdapter = context?.let { AllCateAdapter(it, list.toMutableList()) }
            val layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
            cateTagRcv?.layoutManager = layoutManager
            cateTagRcv?.adapter = mAdapter
            mAdapter?.clickListener = {
                //延时点击
                this@TopPlaylistCatFragment.cateTagRcv?.postDelayed({
                    this@TopPlaylistCatFragment.successListener?.invoke(categoryTags[it])

                    if (isHighQuality) {
                        dismissAllowingStateLoss()
                    }
                }, 300)
            }
            mAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * 标签适配器
     */
    class AllCateAdapter(val context: Context, val list: MutableList<Any>) : androidx.recyclerview.widget.RecyclerView.Adapter<AllCateAdapter.TagViewHolder>() {

        var clickListener: ((Int) -> Unit)? = null
        var tag: String? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
            val mInflater = LayoutInflater.from(context)
            val v = mInflater.inflate(R.layout.item_cate_tag, parent, false)
            return TagViewHolder(v)
        }

        override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
            val item = list[position]
            holder.tagTv.text = item.toString()
            holder.tagTv.setOnClickListener {
                clickListener?.invoke(position)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return TAG_ITEM
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class TagViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
            var tagTv = itemView.findViewById<TextView>(R.id.tagTv)
        }

        private val TAG_ITEM = 1
        private val TITLE_ITEM = 2
    }

}



