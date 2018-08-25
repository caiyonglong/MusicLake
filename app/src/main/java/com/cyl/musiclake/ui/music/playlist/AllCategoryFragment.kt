package com.cyl.musiclake.ui.music.playlist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatCheckedTextView
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musicapi.netease.CatListBean
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * Des    :
 * Author : master.
 * Date   : 2018/8/23 .
 */
class AllCategoryFragment : DialogFragment() {
    private var rootView: View? = null
    private val cateTagRcv by lazy { rootView?.findViewById<RecyclerView>(R.id.cateTagRcv) }
    private val cateAllTagTv by lazy { rootView?.findViewById<TextView>(R.id.cateAllTagTv) }
    private val backIv by lazy { rootView?.findViewById<ImageView>(R.id.backIv) }


    private var mAdapter: AllCateAdapter? = null

    var curCateName: String = "全部"

    companion object {
        var categoryTags = mutableListOf<String>()
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
        cateAllTagTv?.setOnClickListener {
            context?.let {
                cateAllTagTv?.setTextColor(ContextCompat.getColor(it, R.color.colorAccent))
            }
            it?.postDelayed({
                context?.getString(R.string.cate_all)?.let { it1 -> successListener?.invoke(it1) }
                dismissAllowingStateLoss()
            }, 300)
        }
        backIv?.setOnClickListener {
            dismissAllowingStateLoss()
        }
        if (curCateName == cateAllTagTv?.text.toString()) {
            context?.let {
                cateAllTagTv?.setTextColor(ContextCompat.getColor(it, R.color.colorAccent))
            }
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
                val map = mutableMapOf<String, MutableList<String>>()
                result?.categories?.let {
                    map[result.categories.c0] = mutableListOf()
                    map[result.categories.c1] = mutableListOf()
                    map[result.categories.c2] = mutableListOf()
                    map[result.categories.c3] = mutableListOf()
                    map[result.categories.c4] = mutableListOf()
                }
                result?.sub?.forEach {
                    when (it.category) {
                        0 -> map[result.categories.c0]?.add(it.name)
                        1 -> map[result.categories.c1]?.add(it.name)
                        2 -> map[result.categories.c2]?.add(it.name)
                        3 -> map[result.categories.c3]?.add(it.name)
                        4 -> map[result.categories.c4]?.add(it.name)
                    }
                }
                categoryTags.clear()
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

    private fun initRecyclerView(list: MutableList<String>) {
        if (mAdapter == null) {
            mAdapter = context?.let { AllCateAdapter(it, list) }
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            cateTagRcv?.layoutManager = layoutManager
            cateTagRcv?.adapter = mAdapter
            mAdapter?.clickListener = {
                this@AllCategoryFragment.cateTagRcv?.postDelayed({
                    this@AllCategoryFragment.successListener?.invoke(categoryTags[it])
                    dismissAllowingStateLoss()
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

    class AllCateAdapter(val context: Context, val list: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var clickListener: ((Int) -> Unit)? = null

        var tag: String? = null

        fun setSelectName(position: Int) {
            tag = list[position]
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
                holder.tagTv.setTextColor(Color.parseColor("#000000"))
                tag?.let {
                    if (it == list[position]) {
                        holder.tagTv.setTextColor(Color.parseColor("#DB4437"))
                    }
                }
                holder.tagTv.text = list[position]
                holder.tagTv.setOnClickListener {
                    setSelectName(position)
                    notifyDataSetChanged()
                    clickListener?.invoke(position)
                }
            } else if (holder is TitleViewHolder) {
                holder.titleTv.text = list[position]
            }

        }

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                "语种", "风格", "场景", "情感", "主题" -> TITLE_ITEM
                else -> TAG_ITEM
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tagTv = itemView.findViewById<Button>(R.id.tagTv)
        }

        inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var titleTv = itemView.findViewById<TextView>(R.id.titleTv)
        }

        private val TAG_ITEM = 1
        private val TITLE_ITEM = 2
    }

}



