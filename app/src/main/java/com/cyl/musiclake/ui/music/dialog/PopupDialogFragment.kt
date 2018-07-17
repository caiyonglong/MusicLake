package com.cyl.musiclake.ui.music.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musiclake.R
import com.cyl.musiclake.api.AddPlaylistUtils
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.utils.ConvertUtils

class PopupDialogFragment : BottomSheetDialogFragment() {
    lateinit var mContext: AppCompatActivity
    var mAdapter = ItemAdapter()

    companion object {
        var music: Music? = null

        fun newInstance(music: Music): PopupDialogFragment {
            val args = Bundle()
            this.music = music
            val fragment = PopupDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun show(context: AppCompatActivity) {
        mContext = context
        val ft = context.supportFragmentManager
        show(ft, javaClass.simpleName)
    }

    private var mBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.bottomSheetRv)
        val titleTv = view.findViewById<TextView>(R.id.titleTv)
        val subTitleTv = view.findViewById<TextView>(R.id.subTitleTv)
        titleTv.text = music?.title
        subTitleTv.text = ConvertUtils.getArtistAndAlbum(music?.artist, music?.album)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    private fun turnToAlbum() {
        activity?.let { it1 ->
            if (music != null && music?.albumId != null && music?.album != null) {
                NavigationHelper.navigateToAlbum(it1,
                        music?.albumId!!,
                        music?.album!!, null)
            }
        }
    }

    private fun turnToArtist() {
        activity?.let { it1 ->
            if (music != null && music?.artistId != null && music?.artist != null) {
                if (music!!.type == Constants.LOCAL) {
                    NavigationHelper.navigateToArtist(it1,
                            music?.artistId!!,
                            music?.artist!!, null)
                } else {
                    val artist = music?.let { it1 -> MusicUtils.getArtistInfo(it1) }
                    artist?.let {
                        NavigationHelper.navigateToPlaylist(mContext, it)
                    }
                }
            }
        }
    }

    inner class ItemAdapter(val type: Int = 0) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        private val local = arrayOf("下一首播放", "查看歌手", "查看专辑", "修改歌曲信息", "分享")
        private val iconsLocal = arrayOf(R.drawable.ic_queue_play_next, R.drawable.ic_art_track,
                R.drawable.ic_album, R.drawable.ic_mode_edit, R.drawable.ic_share_black)
        private val online = arrayOf("下一首播放", "加到歌单", "查看歌手", "查看专辑", "下载", "分享")
        private val iconsOnline = arrayOf(R.drawable.ic_queue_play_next, R.drawable.ic_playlist_add,
                R.drawable.ic_art_track, R.drawable.ic_album,
                R.drawable.item_download, R.drawable.ic_share_black)
        var data: Array<String>
        var icons: Array<Int>

        init {
            if (music?.type == Constants.LOCAL) {
                data = local
                icons = iconsLocal
            } else {
                data = online
                icons = iconsOnline
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
            holder.textView.text = data[position]
            holder.icon.setImageResource(icons[position])
            holder.icon.setColorFilter(Color.parseColor("#0091EA"))

            holder.itemView.setOnClickListener {
                when (icons[position]) {
                    R.drawable.ic_queue_play_next -> PlayManager.nextPlay(music)
                    R.drawable.ic_playlist_add -> {
                        if (music?.type != Constants.LOCAL) {
                            AddPlaylistUtils.getPlaylist(mContext, music)
                        }
                    }
                    R.drawable.ic_art_track -> {
                        turnToArtist()
                    }
                    R.drawable.ic_album -> {
                        turnToAlbum()
                    }
                    R.drawable.item_download -> {
                        if (music?.type != Constants.LOCAL) {
                            mContext.downloadMusic(music)
                        }
                    }
                    R.drawable.ic_share_black -> {
                        MusicUtils.qqShare(mContext, PlayManager.getPlayingMusic())
                    }
                }
                mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }


        override fun getItemCount(): Int {
            return data.size
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView: TextView = itemView.findViewById(R.id.tv_title)
            var icon: ImageView = itemView.findViewById(R.id.iv_icon)
        }
    }
}


