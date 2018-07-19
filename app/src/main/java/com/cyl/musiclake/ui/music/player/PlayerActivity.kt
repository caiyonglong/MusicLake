package com.cyl.musiclake.ui.music.player

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.view.ViewPager
import android.support.v7.graphics.Palette
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.SeekBar
import com.cyl.musiclake.R
import com.cyl.musiclake.RxBus
import com.cyl.musiclake.api.AddPlaylistUtils
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.TransitionAnimationUtils
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.player.playqueue.PlayQueueManager
import com.cyl.musiclake.ui.main.PageAdapter
import com.cyl.musiclake.ui.music.dialog.downloadMusic
import com.cyl.musiclake.ui.music.playqueue.PlayQueueDialog
import com.cyl.musiclake.ui.music.playqueue.UIUtils
import com.cyl.musiclake.utils.FormatUtil
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.view.DepthPageTransformer
import com.cyl.musiclake.view.MultiTouchViewPager
import kotlinx.android.synthetic.main.activity_player.*
import java.text.ParsePosition

class PlayerActivity : BaseActivity<PlayPresenter>(), PlayContract.View {


    var playingMusic: Music? = null
    val coverFragment by lazy { CoverFragment.newInstance() }
    val lyricFragment by lazy { LyricFragment.newInstance() }

    override fun showNowPlaying(music: Music?) {
        playingMusic = music
        //更新标题
        titleIv.text = music?.title
        subTitleTv.text = music?.artist

        coverFragment?.updateMusicType(music)
        //更新收藏状态
        music?.isLove?.let {
            collectIv.setImageResource(if (it) R.drawable.item_favorite_love else R.drawable.item_favorite)
        }

    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_player
    }

    override fun setToolbarTitle(): String {
        return ""
    }

    override fun initView() {
        //更新播放状态
        PlayManager.isPlaying().let {
            if (it) playPauseIv.play() else playPauseIv.pause()
        }
        detailView.animation = moveToViewLocation()
        updatePlayMode()
    }

    override fun updatePlayMode() {
        UIUtils.updatePlayMode(playModeIv, false)
    }

    override fun initData() {
        setupViewPager(viewPager)
        val music = PlayManager.getPlayingMusic()
        mPresenter?.updateNowPlaying(music)
    }

    override fun listener() {
        super.listener()
        progressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let {
                    PlayManager.seekTo(it)
                    lyricFragment?.setCurrentTimeMillis(it.toLong())
                }
            }

        })
        playPauseIv.setOnClickListener {
            PlayManager.playPause()
        }

    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }


    fun nextPlay(view: View?) {
        PlayManager.next()
    }

    fun prevPlay(view: View?) {
        PlayManager.prev()
    }

    fun changePlayMode(view: View?) {
        UIUtils.updatePlayMode(view as ImageView, true)
    }

    /**
     * 打开播放队列
     */
    fun openPlayQueue(view: View?) {
        val fm = supportFragmentManager
        PlayQueueDialog.newInstance().show(fm, "fragment_bottom_dialog")
    }

    /**
     * 歌曲收藏
     */
    fun collectMusic(view: View?) {
        UIUtils.collectMusic(view as ImageView, playingMusic)
    }

    /**
     * 添加到歌單
     */
    fun addToPlaylist(view: View?) {
        AddPlaylistUtils.getPlaylist(this, playingMusic)
    }

    /**
     * 分享歌曲
     */
    fun shareMusic(view: View?) {
        MusicUtils.qqShare(this, PlayManager.getPlayingMusic())
    }

    fun downloadMusic(view: View?) {
        downloadMusic(playingMusic)
    }

    override fun setPlayingBitmap(albumArt: Bitmap?) {
        coverFragment.setCover(albumArt)
    }

    override fun setPlayingBg(albumArt: Drawable?) {
        //加载背景图过度
        TransitionAnimationUtils.startChangeAnimation(playingBgIv, albumArt)
    }

    override fun updatePlayStatus(isPlaying: Boolean) {
        if (isPlaying) playPauseIv.play() else playPauseIv.pause()

        if (coverFragment?.operatingAnim != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isPlaying) {
                    coverFragment?.operatingAnim?.resume()
                } else {
                    coverFragment?.operatingAnim?.pause()
                }
            }
        }
    }

    override fun setPalette(palette: Palette?) {
    }

    override fun showLyric(lyric: String?, isFilePath: Boolean) {
        lyricFragment.lyricInfo = lyric
        lyricFragment.showLyric(lyric, isFilePath)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun updateProgress(progress: Int, max: Int) {
        progressSb.progress = progress
        progressSb.max = max
        progressTv.text = FormatUtil.formatTime(progress.toLong())
        durationTv.text = FormatUtil.formatTime(max.toLong())
        lyricFragment.setCurrentTimeMillis(progress.toLong())
    }

    private fun setupViewPager(viewPager: MultiTouchViewPager) {
        val mAdapter = PageAdapter(supportFragmentManager)
        mAdapter.addFragment(coverFragment, "发现")
        mAdapter.addFragment(lyricFragment, "我的")
        viewPager.adapter = mAdapter
        viewPager.setPageTransformer(false, DepthPageTransformer())
        viewPager.offscreenPageLimit = 1
        viewPager.currentItem = 0

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                LogUtil.d("PlayControlFragment", "--$position")

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun moveToViewLocation(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        mHiddenAction.duration = 300
        return mHiddenAction
    }


}
