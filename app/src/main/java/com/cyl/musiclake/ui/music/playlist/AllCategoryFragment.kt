package com.cyl.musiclake.ui.music.playlist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musicapi.netease.CatListBean
import com.cyl.musicapi.netease.SubItem
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
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


    private var mAdapter: AllCateAdapter? = null

    var isHighQuality: Boolean = false
    var curCateList = mutableListOf<String>()

    companion object {
        var categoryTags = mutableListOf<Any>()
    }

    var successListener: ((String) -> Unit?)? = null

    override fun onStart() {
        val lp = dialog.window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        lp?.height = WindowManager.LayoutParams.MATCH_PARENT
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

        ApiManager.request(NeteaseApiServiceImpl.getCatList(), object : RequestCallBack<CatListBean> {
            override fun success(result: CatListBean?) {
                /**
                 * 排序
                 */
                val map = mutableMapOf<String, MutableList<SubItem>>()
                result?.categories?.let {
                    map[result.categories.c0] = mutableListOf()
                    map[result.categories.c1] = mutableListOf()
                    map[result.categories.c2] = mutableListOf()
                    map[result.categories.c3] = mutableListOf()
                    map[result.categories.c4] = mutableListOf()
                }
                result?.sub?.forEach {
                    when (it.category) {
                        0 -> map[result.categories.c0]?.add(it)
                        1 -> map[result.categories.c1]?.add(it)
                        2 -> map[result.categories.c2]?.add(it)
                        3 -> map[result.categories.c3]?.add(it)
                        4 -> map[result.categories.c4]?.add(it)
                    }
                }
                categoryTags.clear()

                if (!isHighQuality) {
                    categoryTags.add("我的歌单广场")
                    curCateList.forEach {
                        categoryTags.add(SubItem(name = it))
                    }
                }
                map.forEach {
                    categoryTags.add(it.key)
                    categoryTags.addAll(it.value)
                }
                initRecyclerView(categoryTags)
            }

            override fun error(msg: String?) {
            }
        })
    }

    private fun initRecyclerView(list: MutableList<Any>) {
        if (mAdapter == null) {
            mAdapter = context?.let { AllCateAdapter(it, list) }
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            cateTagRcv?.layoutManager = layoutManager
            cateTagRcv?.adapter = mAdapter
//            mAdapter?.clickListener = {
//                this@AllCategoryFragment.cateTagRcv?.postDelayed({
//                    this@AllCategoryFragment.successListener?.invoke(categoryTags[it])
//                    if (!isHighQuality && !curCateList.contains(categoryTags[it])) {
//                        curCateList.add(categoryTags[it])
//                    }
//
//                    if (isHighQuality) {
//                        dismissAllowingStateLoss()
//                    }
//                }, 300)
//            }
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
                val item = list[position] as SubItem
                holder.tagTv.isSelectable = false
                tag?.let {
                    if (it == item.name) {
                        holder.tagTv.isSelectable = true
                    }
                }
                holder.tagTv.chipText = item.name
                holder.tagTv.setOnChipClickListener {
                    if (isEditMode) {
                        for (i in 1 until list.size - 1) {
                            if (list[i] is String) {
                                list.add(i, item)
                                notifyDataSetChanged()
                                return@setOnChipClickListener
                            }
                        }
                    }
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
            return when (list[position]) {
                is String -> TITLE_ITEM
                else -> TAG_ITEM
            }
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



