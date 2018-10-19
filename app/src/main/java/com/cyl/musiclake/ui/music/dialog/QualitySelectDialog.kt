package com.cyl.musiclake.ui.music.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.PlayManager

class QualitySelectDialog : BottomSheetDialogFragment() {
    lateinit var mContext: AppCompatActivity
    var mAdapter: QualityDAdapter? = null
    private val mRootView by lazy { LayoutInflater.from(context).inflate(R.layout.dialog_quality, null, false) }
    private val recyclerView by lazy { mRootView.findViewById<RecyclerView>(R.id.bottomSheetRv) }
    private val downloadTv by lazy { mRootView.findViewById<TextView>(R.id.downloadTv) }
    private val cacheTv by lazy { mRootView.findViewById<TextView>(R.id.cacheTv) }

    var isDownload = false
    var changeSuccessListener: ((String) -> Unit)? = null

    companion object {
        var music: Music? = null
        fun newInstance(music: Music?): QualitySelectDialog {
            val args = Bundle()
            this.music = music
            val fragment = QualitySelectDialog()
            fragment.arguments = args
            return fragment
        }
    }

    fun show(context: AppCompatActivity) {
        mContext = context
        val ft = context.supportFragmentManager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    private var mBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        initItems()
        dialog.setContentView(mRootView)
        mBehavior = BottomSheetBehavior.from(mRootView.parent as View)
        return dialog
    }

    private fun initItems() {
        val qualities = mutableListOf(
                QualityItem(name = "标准品质", quality = 128000))
        music?.qualityList?.let {
            if (it.high) {
                qualities.add(QualityItem(name = "较高品质", quality = 192000))
            }
            if (it.hq) {
                qualities.add(QualityItem(name = "HQ品质", quality = 320000))
            }
            if (it.sq) {
                qualities.add(QualityItem(name = "无损品质", quality = 999000))
            }
        }

        mAdapter = QualityDAdapter(qualities)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        if (isDownload){
            downloadTv.visibility = View.VISIBLE
            cacheTv.visibility = View.VISIBLE
        }
        downloadTv.setOnClickListener {

        }
        cacheTv.setOnClickListener {

        }
    }

    /**
     * 下拉列表适配器
     */
//    inner class QualityAdapter(qualityList: QualityBean?) : RecyclerView.Adapter<QualityAdapter.ItemViewHolder>() {
//
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quality_select, parent, false)
//            return ItemViewHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: QualityAdapter.ItemViewHolder, position: Int) {
//            holder.checkIv.visibility = if (music?.quality == qualities[position].first) View.VISIBLE else View.GONE
//
//            holder.titleTv.text = qualities[position].second
//
//            holder.itemView.setOnClickListener {
//                music?.quality = qualities[position].first
//                changeSuccessListener?.invoke(qualities[position].second)
//                mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
//                PlayManager.play(PlayManager.position())
//            }
//        }
//
//        override fun getItemCount(): Int {
//            return qualities.size
//        }
//
//        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            var titleTv: TextView = itemView.findViewById(R.id.tv_title)
//            var checkIv: ImageView = itemView.findViewById(R.id.iv_check)
//        }
//    }


    inner  class QualityDAdapter(private val qualities: List<QualityItem>) : BaseQuickAdapter<QualityItem, BaseViewHolder>(R.layout.item_quality_select, qualities) {

        override fun convert(helper: BaseViewHolder, item: QualityItem) {
            helper.setText(R.id.tv_title, item.name)
            helper.getView<ImageView>(R.id.iv_check).visibility = if (item.quality == music?.quality) View.VISIBLE else View.GONE
            helper.itemView.setOnClickListener {
                QualitySelectDialog.music?.quality = item.quality
                changeSuccessListener?.invoke(item.name)
                if (!isDownload) {
                    mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    PlayManager.play(PlayManager.position())
                }
            }
        }
    }


}

data class QualityItem(val name: String, val quality: Int)


