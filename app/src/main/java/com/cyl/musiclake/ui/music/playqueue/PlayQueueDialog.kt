package com.cyl.musiclake.ui.music.playqueue

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.ui.UIUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.music.lake.musiclib.MusicPlayerManager
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.trello.rxlifecycle2.LifecycleTransformer
import org.greenrobot.eventbus.EventBus

class PlayQueueDialog : BottomSheetDialogFragment(), PlayQueueContract.View {
    private var rootView: View? = null

    private lateinit var playModeTv: TextView
    private lateinit var songSumTv: TextView
    private lateinit var playModeIv: ImageView
    private lateinit var clearAllIv: ImageView
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private var mPresenter: PlayQueuePresenter? = null
    private var baseMusicInfoInfoList = mutableListOf<BaseMusicInfo>()
    private var mAdapter: QueueAdapter? = null

    private var mBehavior: BottomSheetBehavior<*>? = null

    override fun onStart() {
        super.onStart()
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
        //默认全屏展开
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = PlayQueuePresenter()
        mPresenter?.attachView(this)
        mAdapter = QueueAdapter(baseMusicInfoInfoList)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)
        recyclerView.scrollToPosition(MusicPlayerManager.getInstance().getNowPlayingIndex())
        initListener()
        mPresenter?.loadSongs()
        mBehavior = BottomSheetBehavior.from(rootView?.parent as View)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_playqueue, container, false)
        }
        rootView?.let {
            recyclerView = it.findViewById(R.id.rcv_songs)
            playModeTv = it.findViewById(R.id.tv_play_mode)
            songSumTv = it.findViewById(R.id.tv_song_sum)
            playModeIv = it.findViewById(R.id.iv_play_mode)
            clearAllIv = it.findViewById(R.id.iv_clear_all)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    private fun initListener() {
        playModeTv.setOnClickListener { view ->
            UIUtils.updatePlayMode(playModeIv, playModeTv, true)
        }
        playModeIv.setOnClickListener { view ->
            UIUtils.updatePlayMode(view as ImageView, playModeTv, true)
        }
        clearAllIv.setOnClickListener { v ->
            context?.let {
                MaterialDialog(it).show {
                    title(R.string.playlist_queue_clear)
                    positiveButton(R.string.sure) {
                        clearQueue()
                    }
                    negativeButton(R.string.cancel)
                }
            }
        }
        mAdapter?.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_love && view.id != R.id.iv_more) {
                MusicPlayerManager.getInstance().playMusicById(position)
                mAdapter?.notifyDataSetChanged()
            }
        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_more -> {
                    MusicPlayerManager.getInstance().removeFromPlaylist(position)
                    songSumTv.text = "(${baseMusicInfoInfoList.size})"
                    if (baseMusicInfoInfoList.isEmpty()) {
                        dismiss()
                    } else {
                        mAdapter?.notifyItemRemoved(position)
                        mAdapter?.notifyItemChanged(position)
                    }
                }
            }
        }

    }

    private fun clearQueue() {
        mPresenter?.clearQueue()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun dismiss() {
        dialog?.dismiss()
    }

    private fun updatePlayMode() {
        UIUtils.updatePlayMode(playModeIv, playModeTv, false)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(message: String, showRetryButton: Boolean) {

    }

    override fun showEmptyState() {

    }

    override fun onDetach() {
        super.onDetach()
        mPresenter?.detachView()
    }

    override fun <T> bindToLife(): LifecycleTransformer<T>? {
        return null
    }

    override fun showSongs(songs: MutableList<BaseMusicInfo>) {
        baseMusicInfoInfoList = songs
        songSumTv.text = "(${songs.size})"
        updatePlayMode()
        mAdapter?.setNewData(songs)
        //滚动到正在播放的位置
        recyclerView.scrollToPosition(MusicPlayerManager.getInstance().nowPlayingIndex)

        if (songs.isEmpty()) {
            mAdapter?.setEmptyView(R.layout.view_queue_empty)
        }
    }

    companion object {
        fun newInstance(): PlayQueueDialog {
            val args = Bundle()
            val fragment = PlayQueueDialog()
            fragment.arguments = args
            return fragment
        }
    }

    fun showIt(context: AppCompatActivity) {
        val fm = context.supportFragmentManager
        show(fm, "dialog")
    }

}
