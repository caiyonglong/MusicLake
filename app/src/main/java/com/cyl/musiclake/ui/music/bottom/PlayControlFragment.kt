package com.cyl.musiclake.ui.music.bottom

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.event.MetaChangedEvent
import com.cyl.musiclake.event.PlayModeEvent
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.event.StatusChangedEvent
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.playpage.PlayContract
import com.cyl.musiclake.ui.music.playpage.PlayPresenter
import com.cyl.musiclake.ui.music.playqueue.PlayQueueDialog
import com.cyl.musiclake.utils.LogUtil
import com.music.lake.musiclib.MusicPlayerManager
import com.music.lake.musiclib.bean.BaseMusicInfo
import kotlinx.android.synthetic.main.play_control_menu.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.*
import kotlin.math.max
import kotlin.math.min

class PlayControlFragment : BaseFragment<PlayPresenter>(), SeekBar.OnSeekBarChangeListener, PlayContract.View {

    private var coverAnimator: ObjectAnimator? = null
    private var mAdapter: BottomMusicAdapter? = null
    private val musicList = ArrayList<BaseMusicInfo>()

    override fun getLayoutId(): Int {
        return R.layout.play_control_menu
    }

    public override fun initViews() {
        //初始化控件
        updatePlayStatus(MusicPlayerManager.getInstance().isPlaying)
        musicList.clear()
        if (MusicPlayerManager.getInstance().playList != null) {
            musicList.addAll(MusicPlayerManager.getInstance().playList)
        }
        initSongList()
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        bottomPlayRcv.setOnClickListener { v -> NavigationHelper.navigateToPlaying(mFragmentComponent.activity) }
        playQueueIv.setOnClickListener {
            PlayQueueDialog.newInstance().showIt((activity as AppCompatActivity))
        }
        playPauseView.setOnClickListener {
            MusicPlayerManager.getInstance().pausePlay()
        }
    }

    override fun loadData() {
        val music = MusicPlayerManager.getInstance().getNowPlayingMusic()
        music?.let {
            mPresenter?.updateNowPlaying(it, true)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (MusicPlayerManager.getInstance().isPlaying()) {
            val progress = seekBar.progress
            MusicPlayerManager.getInstance().seekTo(progress.toLong())
        } else {
            seekBar.progress = 0
        }
    }


    override fun updatePlayStatus(isPlaying: Boolean) {
        if (isPlaying && !playPauseView.isPlaying) {
            playPauseView.play()
        } else if (!isPlaying && playPauseView.isPlaying) {
            playPauseView.pause()
        }
    }

    override fun updateLoading(isLoading: Boolean) {
        playPauseView.setLoading(isLoading)
    }

    override fun updateProgress(progress: Long, max: Long, bufferPercent: Int) {
        runOnUiThread {
            progressBar.progress = if (max <= 0) 0 else max(0, min((progress * 100 / max).toInt(), 100))
            progressBar.secondaryProgress = bufferPercent
            progressBar.max = 100
            playPauseView.setProgress(1.0f * progress / max)
        }
    }

    override fun showNowPlaying(baseMusicInfo: BaseMusicInfo?) {
        if (baseMusicInfo != null) {
            rootView.visibility = View.VISIBLE
        } else {
            rootView.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayModeChangedEvent(event: PlayModeEvent) {
        updatePlayMode()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMetaChangedEvent(event: MetaChangedEvent) {
        mPresenter?.updateNowPlaying(event.baseMusicInfoInfo, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(MusicPlayerManager.getInstance().getPlayingPosition().toInt(), true)
        } else {
            progressBar.progress = MusicPlayerManager.getInstance().getPlayingPosition().toInt()
        }
        progressBar.max = MusicPlayerManager.getInstance().getDuration().toInt()
        bottomPlayRcv.scrollToPosition(MusicPlayerManager.getInstance().getNowPlayingIndex())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStatusChangedEvent(event: StatusChangedEvent) {
        updatePlayStatus(event.isPlaying)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayListChange(event: PlaylistEvent) {
        if (event.type == Constants.PLAYLIST_QUEUE_ID) {
            val playlist = MusicPlayerManager.getInstance().playList
            val index = MusicPlayerManager.getInstance().nowPlayingIndex
            LogUtil.d(TAG, "播放列表已改变" + playlist.size + " - " + index)
            musicList.clear()
            musicList.addAll(playlist)
            mAdapter?.notifyItemRangeChanged(0, playlist.size)
            bottomPlayRcv.scrollToPosition(index)
            if (musicList.size == 0) {
                emptyView.visibility = View.VISIBLE
            } else {
                emptyView.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        coverAnimator = null
        EventBus.getDefault().unregister(this)
    }

    /**
     * 初始化歌曲列表
     */
    private fun initSongList() {
        if (mAdapter == null) {
            bottomPlayRcv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mAdapter = BottomMusicAdapter(musicList)
            val snap = PagerSnapHelper()
            snap.attachToRecyclerView(bottomPlayRcv)
            bottomPlayRcv.adapter = mAdapter
            bottomPlayRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val manager = recyclerView?.layoutManager as LinearLayoutManager
                        val first = manager.findFirstVisibleItemPosition()
                        val last = manager.findLastVisibleItemPosition()
                        LogUtil.e("Scroll", "$first-$last")
                        if (first == last && first != MusicPlayerManager.getInstance().getNowPlayingIndex()) {
                            MusicPlayerManager.getInstance().playMusicById(first)
                        }
                    }
                }
            })
            mAdapter?.bindToRecyclerView(bottomPlayRcv)
        } else {
            mAdapter?.notifyDataSetChanged()
        }
        bottomPlayRcv.scrollToPosition(MusicPlayerManager.getInstance().getNowPlayingIndex())
        if (musicList.size == 0) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
    }

    companion object {

        private val TAG = "PlayControlFragment"

        fun newInstance(): PlayControlFragment {
            val args = Bundle()
            val fragment = PlayControlFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
