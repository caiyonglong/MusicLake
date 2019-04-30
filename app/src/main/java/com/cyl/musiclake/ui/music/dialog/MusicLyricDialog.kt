package com.cyl.musiclake.ui.music.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import com.cyl.musicapi.kugou.Candidates
import com.cyl.musicapi.kugou.KugouLyric
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.kugou.KuGouApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.player.FloatLyricViewManager
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
import com.rtugeek.android.colorseekbar.ColorSeekBar
import org.jetbrains.anko.support.v4.toast

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 歌词搜索
 */
class MusicLyricDialog : DialogFragment() {

    private val rootView by lazy { LayoutInflater.from(context).inflate(R.layout.dialog_lyric_manager, null) }
    private val controlsView by lazy { rootView.findViewById<View>(R.id.controlsView) }
    private val searchLyricView by lazy { rootView.findViewById<View>(R.id.searchLyricView) }
    private val lyricFormatView by lazy { rootView.findViewById<View>(R.id.lyricFormatView) }
    private val formatView by lazy { rootView.findViewById<View>(R.id.formatView) }
    private val closeIv by lazy { rootView.findViewById<View>(R.id.closeIv) }
    private val formatColorTv by lazy { rootView.findViewById<TextView>(R.id.formatColorTv) }
    private val formatColorSb by lazy { rootView.findViewById<ColorSeekBar>(R.id.formatColorSb) }
    private val formatSizeSb by lazy { rootView.findViewById<SeekBar>(R.id.formatSizeSb) }
    private val loadingView by lazy { rootView.findViewById<ProgressBar>(R.id.loadingView) }
    private val lyricRecyclerView by lazy { rootView.findViewById<RecyclerView>(R.id.lyricRecyclerView) }
    private val lyricResultView by lazy { rootView.findViewById<View>(R.id.lyricResultView) }

    var searchListener: (() -> Unit)? = null
    var textSizeListener: ((Int) -> Unit)? = null
    var textColorListener: ((Int) -> Unit)? = null
    var lyricListener: ((String) -> Unit)? = null
    var title: String? = ""
    var artist: String? = ""
    var duration: Long = 0

    override fun onStart() {
        super.onStart()
        val lp = dialog.window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window.attributes = lp
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawableResource(R.color.translucent)
        dialog.setCanceledOnTouchOutside(true)
        initListener()
        return rootView
    }

    /**
     * 初始化歌词列表
     */
    private fun initLyricList(candidates: MutableList<Candidates>?) {
        lyricRecyclerView.adapter = candidates?.let { ItemAdapter(it) }
        lyricRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        lyricResultView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        controlsView.visibility = View.GONE
    }

    /**
     *初始化监听事件
     */
    private fun initListener() {
        closeIv.setOnClickListener {
            dismiss()
        }
        lyricFormatView.setOnClickListener {
            controlsView.visibility = View.GONE
            formatView.visibility = View.VISIBLE
        }
        searchLyricView.setOnClickListener {
            loadingView.visibility = View.VISIBLE
            searchLyric(title.toString(), duration)
        }
        formatColorSb.setOnColorChangeListener { _, _, color ->
            formatColorTv.setTextColor(color)
            textColorListener?.invoke(color)
            SPUtils.saveFontColor(color)
        }
        formatSizeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textSizeListener?.invoke(progress)
                //初始化歌词配置
                SPUtils.saveFontSize(progress)
            }
        })
    }

    /**
     * 搜索歌词
     */
    private fun searchLyric(title: String, duration: Long) {
        ApiManager.request(KuGouApiServiceImpl.searchLyric(title, duration), object : RequestCallBack<KugouLyric> {
            override fun success(result: KugouLyric?) {
                if (result?.status == 200 || result?.candidates?.size != 0) {
                    initLyricList(result?.candidates)
                } else {
                    ToastUtils.show(getString(R.string.lyric_search_error, title))
                    loadingView.visibility = View.GONE
                    dismiss()
                }
            }

            override fun error(msg: String?) {
                ToastUtils.show(getString(R.string.lyric_search_error, title))
                loadingView.visibility = View.GONE
                dismiss()
            }
        })
    }

    fun show(context: AppCompatActivity) {
        val ft = context.supportFragmentManager
        show(ft, javaClass.simpleName)
    }


    inner class ItemAdapter(private val candidates: MutableList<Candidates>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        var selectId = -1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lyric_list, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
            holder.titleTv.text = candidates[position].song
            holder.subTitleTv.text = candidates[position].singer
            holder.itemView.setOnClickListener {
                loadingView.visibility = View.VISIBLE
                ApiManager.request(KuGouApiServiceImpl.getKugouLyricInfo(candidates[position]),
                        object : RequestCallBack<String> {
                            override fun success(result: String?) {
                                result?.let { it1 -> lyricListener?.invoke(it1) }
                                loadingView.visibility = View.GONE
                            }

                            override fun error(msg: String?) {
                                msg?.let { it1 -> toast(it1) }
                                loadingView.visibility = View.GONE
                            }

                        })
            }
            holder.selectTv.isChecked = (selectId == position)

            holder.selectTv.setOnClickListener {
                loadingView.visibility = View.VISIBLE
                ApiManager.request(KuGouApiServiceImpl.getKugouLyricInfo(candidates[position]),
                        object : RequestCallBack<String> {
                            override fun success(result: String?) {
                                result?.let { it1 ->
                                    loadingView.visibility = View.GONE
                                    lyricListener?.invoke(it1)
                                    selectId = position
                                    FloatLyricViewManager.saveLyricInfo(title, artist, it1)
                                    ToastUtils.show(getString(R.string.lyric_search_apply))
                                    dismiss()
                                }
                            }

                            override fun error(msg: String?) {
                                msg?.let { it1 -> toast(msg) }
                                loadingView.visibility = View.GONE
                            }

                        })
            }
        }


        override fun getItemCount(): Int {
            return candidates.size
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var titleTv: TextView = itemView.findViewById(R.id.titleTv)
            var subTitleTv: TextView = itemView.findViewById(R.id.subTitleTv)
            var selectTv: CheckBox = itemView.findViewById(R.id.selectTv)
        }
    }

}
