package com.cyl.musiclake.ui.music.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.downloadMusic

/**
 * 音质选择器
 */
class QualitySelectDialog : BaseBottomSheetDialogFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.dialog_quality
    }

    private var mBehavior: BottomSheetBehavior<*>? = null
    var mAdapter: QualityDAdapter? = null
    private val recyclerView by lazy { mRootView?.findViewById<RecyclerView>(R.id.bottomSheetRv) }
    private val downloadTv by lazy { mRootView?.findViewById<TextView>(R.id.downloadTv) }
    private val cacheTv by lazy { mRootView?.findViewById<TextView>(R.id.cacheTv) }
    private val downloadView by lazy { mRootView?.findViewById<View>(R.id.downloadView) }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBehavior = BottomSheetBehavior.from(mRootView?.parent as View)
        initItems()
    }

    private fun initItems() {
        val qualities = mutableListOf(
                QualityItem(name = "标准品质", quality = 128000))
        music?.let {
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
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView?.adapter = mAdapter

        if (isDownload) {
            downloadView?.visibility = View.VISIBLE
        }
        if (music?.isDl == false) {
            downloadTv?.isClickable = false
            downloadTv?.setTextColor(Color.GRAY)
        } else {
            downloadTv?.isClickable = true
            downloadTv?.setTextColor(Color.WHITE)
        }

        downloadTv?.setOnClickListener {
            mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            (activity as AppCompatActivity).downloadMusic(music)
        }
        cacheTv?.setOnClickListener {
            mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            (activity as AppCompatActivity).downloadMusic(music, true)
        }
    }

    inner class QualityDAdapter(qualities: MutableList<QualityItem>) : BaseQuickAdapter<QualityItem, BaseViewHolder>(R.layout.item_quality_select, qualities) {

        override fun convert(helper: BaseViewHolder, item: QualityItem) {
            helper.setText(R.id.tv_title, item.name)
            helper.getView<ImageView>(R.id.iv_check).visibility = if (item.quality == music?.quality) View.VISIBLE else View.GONE
            helper.itemView.setOnClickListener {
                music?.quality = item.quality
                notifyDataSetChanged()
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


