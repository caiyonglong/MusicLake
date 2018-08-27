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
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.OnlinePlaylistUtils
import com.cyl.musiclake.ui.downloadMusic
import com.cyl.musiclake.ui.music.edit.EditMusicActivity
import com.cyl.musiclake.utils.ConvertUtils
import org.jetbrains.anko.support.v4.startActivity

class BottomDialogFragment : BottomSheetDialogFragment() {
    lateinit var mContext: AppCompatActivity
    private val mRootView by lazy { LayoutInflater.from(context).inflate(R.layout.dialog_layout, null, false) }
    private val recyclerView by lazy { mRootView.findViewById<RecyclerView>(R.id.bottomSheetRv) }
    private val titleTv by lazy { mRootView.findViewById<TextView>(R.id.titleTv) }
    private val subTitleTv by lazy { mRootView.findViewById<TextView>(R.id.subTitleTv) }

    var mAdapter: ItemAdapter? = null
    var type: Int = 0

    var removeMusicListener: RemoveMusicListener? = null
    var position: Int = 0

    interface RemoveMusicListener {
        fun remove(position: Int, music: Music?)
    }

    companion object {
        var music: Music? = null

        fun newInstance(music: Music?): BottomDialogFragment {
            val args = Bundle()
            this.music = music
            val fragment = BottomDialogFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(music: Music?, type: Int): BottomDialogFragment {
            val args = Bundle()
            this.music = music
            val fragment = BottomDialogFragment()
            fragment.arguments = args
            args.putInt(Extras.PLAYLIST_TYPE, type)
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
        initItems()
        dialog.setContentView(mRootView)
        mBehavior = BottomSheetBehavior.from(mRootView.parent as View)
        return dialog
    }

    private fun initItems() {
        titleTv.text = music?.title
        subTitleTv.text = ConvertUtils.getArtistAndAlbum(music?.artist, music?.album)
        arguments?.getInt(Extras.PLAYLIST_TYPE, 0)?.let {
            type = it
        }
        mAdapter = ItemAdapter(type)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
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
                        NavigationHelper.navigateToPlaylist(mContext, it, null)
                    }
                }
            }
        }
    }

    private fun turnToEdit() {
        startActivity<EditMusicActivity>(Extras.SONG to music)
    }

    inner class ItemAdapter(type: Int = 0) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
        private var itemData = mutableMapOf(
                R.string.popup_play_next to R.drawable.ic_queue_play_next,
                R.string.popup_add_to_playlist to R.drawable.ic_playlist_add,
                R.string.popup_album to R.drawable.ic_album,
                R.string.popup_artist to R.drawable.ic_art_track,
                R.string.popup_detail_edit to R.drawable.ic_mode_edit,
                R.string.popup_download to R.drawable.item_download,
                R.string.popup_delete to R.drawable.ic_delete,
                R.string.popup_share to R.drawable.ic_share_black)
        val data = mutableListOf<PopupItemBean>()

        init {
            if (music?.type == Constants.LOCAL) {
                itemData.remove(R.string.popup_download)
                itemData.remove(R.string.popup_add_to_playlist)
                itemData.remove(R.string.popup_delete)
            } else {
                itemData.remove(R.string.popup_detail_edit)
            }

            if (type == Constants.OP_ONLINE) {
                itemData.remove(R.string.popup_delete)
            }

            itemData.forEach {
                data.add(PopupItemBean(getString(it.key), it.value))
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
            holder.textView.text = data[position].title
            holder.icon.setImageResource(data[position].icon)
            holder.icon.setColorFilter(Color.parseColor("#0091EA"))

            holder.itemView.setOnClickListener {
                when (data[position].icon) {
                    R.drawable.ic_queue_play_next -> PlayManager.nextPlay(music)
                    R.drawable.ic_playlist_add -> {
                        if (music?.type != Constants.LOCAL) {
                            OnlinePlaylistUtils.addToPlaylist(mContext, music)
                        }
                    }
                    R.drawable.ic_art_track -> {
                        turnToArtist()
                    }
                    R.drawable.ic_album -> {
                        turnToAlbum()
                    }
                    R.drawable.ic_mode_edit -> {
                        turnToEdit()
                    }
                    R.drawable.ic_delete -> {
                        removeMusicListener?.remove(this@BottomDialogFragment.position, music)
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

data class PopupItemBean(val title: String = "", val icon: Int = 0)
