package com.cyl.musiclake.ui.music.playlist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.robertlevonyan.views.chip.Chip

/**
 * Des    :
 * Author : master.
 * Date   : 2018/8/23 .
 */
class AllCategoryFragment : DialogFragment() {
    private var rootView: View? = null
    private val cateTagRcv by lazy { rootView?.findViewById<RecyclerView>(R.id.cateTagRcv) }
    private val backIv by lazy { rootView?.findViewById<ImageView>(R.id.backIv) }
    private val allChipTv by lazy { rootView?.findViewById<Chip>(R.id.chip2) }
    private val categoryTags = mutableListOf("华语", "欧美", "韩语", "日语", "粤语", "小语种", "运动", "ACG", "影视原声", "流行", "摇滚", "后摇", "古风", "民谣", "轻音乐", "电子", "器乐", "说唱", "古典", "爵士")


    private var mAdapter: AllCateAdapter? = null

    var isHighQuality: Boolean = false
    var curCateList = mutableListOf<String>()

    var successListener: ((String) -> Unit?)? = null

    override fun onStart() {
        val lp = dialog.window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        lp?.height = MusicApp.screenSize.y / 2
        lp?.windowAnimations = R.style.dialogAnim
        lp?.gravity = Gravity.BOTTOM
        dialog.window.attributes = lp
        dialog.setCanceledOnTouchOutside(true)
        super.onStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.all_category_dialog, container, false)
        }
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backIv?.setOnClickListener {
            dismissAllowingStateLoss()
        }
        if (categoryTags.size > 0) {
            initRecyclerView(categoryTags)
            return
        }

        allChipTv?.setOnChipClickListener {
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
                this@AllCategoryFragment.cateTagRcv?.postDelayed({
                    this@AllCategoryFragment.successListener?.invoke(categoryTags[it])

                    if (isHighQuality) {
                        dismissAllowingStateLoss()
                    }
                }, 300)
            }
            mAdapter?.notifyDataSetChanged()
        }
    }

    /**
     *显示出对话框
     */
    fun showIt(context: FragmentActivity?) {
        if (dialog != null) {
            dialog.dismiss()
        }
        val transaction = context?.supportFragmentManager?.beginTransaction()
        transaction?.add(this, tag)?.commitAllowingStateLoss()
    }

    class AllCateAdapter(val context: Context, val list: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var clickListener: ((Int) -> Unit)? = null

        var tag: String? = null

        //是否是编辑模式
        private var isEditMode = false


        private fun setSelectName(tag: String) {
            this.tag = tag
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val mInflater = LayoutInflater.from(context)
            return if (TAG_ITEM == viewType) {
                val v = mInflater.inflate(R.layout.item_cate_tag, parent, false)
                TagViewHolder(v)
            } else {
                val v = mInflater.inflate(R.layout.item_cate, parent, false)
                TitleViewHolder(v)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is TagViewHolder) {
                val item = list[position]
                holder.tagTv.isSelectable = false
                tag?.let {
                    if (it == item) {
                        holder.tagTv.isSelectable = true
                    }
                }
                holder.tagTv.chipText = item.toString()
                holder.tagTv.setOnChipClickListener {
                    clickListener?.invoke(position)
                }
            } else if (holder is TitleViewHolder) {
                holder.titleTv.text = list[position].toString()
                if (position == 0) {
                    holder.editTv.visibility = View.VISIBLE
                    holder.editTv.chipText = if (isEditMode) "完成" else "编辑"
                } else {
                    holder.editTv.visibility = View.GONE
                }

                holder.editTv.setOnChipClickListener {
                    isEditMode = !isEditMode
                    //更新列表
                    notifyDataSetChanged()
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return TAG_ITEM
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tagTv = itemView.findViewById<Chip>(R.id.tagTv)
        }

        inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var titleTv = itemView.findViewById<TextView>(R.id.titleTv)
            var editTv = itemView.findViewById<Chip>(R.id.editTv)
        }

        private val TAG_ITEM = 1
        private val TITLE_ITEM = 2
    }

}



