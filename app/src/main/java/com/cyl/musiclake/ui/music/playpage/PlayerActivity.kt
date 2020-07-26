package com.cyl.musiclake.ui.music.playpage

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.SeekBar
import androidx.viewpager2.widget.ViewPager2
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.TransitionAnimationUtils
import com.cyl.musiclake.event.MetaChangedEvent
import com.cyl.musiclake.event.PlayModeEvent
import com.cyl.musiclake.event.StatusChangedEvent
import com.cyl.musiclake.player.FloatLyricViewManager
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils
import com.cyl.musiclake.ui.UIUtils
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.music.comment.SongCommentActivity
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.dialog.MusicLyricDialog
import com.cyl.musiclake.ui.music.dialog.QualitySelectDialog
import com.cyl.musiclake.ui.music.local.adapter.PlayerPagerAdapter
import com.cyl.musiclake.ui.music.playpage.fragment.CoverFragment
import com.cyl.musiclake.ui.music.playpage.fragment.LyricFragment
import com.cyl.musiclake.ui.music.playqueue.PlayQueueDialog
import com.cyl.musiclake.ui.widget.DepthPageTransformer
import com.cyl.musiclake.utils.FormatUtil
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.Tools
import kotlinx.android.synthetic.main.activity_player.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity

class PlayerActivity : BaseActivity<PlayPresenter>(), PlayContract.View {
    private var playingMusic: Music? = null
    private var coverFragment: CoverFragment? = CoverFragment()
    private var lyricFragment: LyricFragment? = LyricFragment()

    private val fragments = mutableListOf<Fragment>()

    /***
     * 显示当前正在播放
     */
    override fun showNowPlaying(music: Music?) {
        if (music == null) finish()

        playingMusic = music
        //更新标题
        titleIv.text = music?.title
        subTitleTv.text = music?.artist
        //更新类型
        music?.let { coverFragment?.updateMusicType(it) }
        //更新收藏状态
        music?.isLove?.let {
            collectIv.setImageResource(if (it) R.drawable.item_favorite_love else R.drawable.item_favorite)
        }
        //更新下载状态
//        downloadIv.visibility = if (BuildConfig.HAS_DOWNLOAD && !music?.isDl!!) View.VISIBLE else View.GONE
        LogUtil.d("PlayerActivity", "showNowPlaying 开始旋转动画")
        //开始旋转动画
        coverFragment?.startRotateAnimation(PlayManager.isPlaying())
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_player
    }

    override fun hasToolbar(): Boolean {
        return false
    }

    override fun initView() {
        detailView.animation = moveToViewLocation()
        updatePlayMode()

        //歌词搜索
        searchLyricIv.setOnClickListener {
            MusicLyricDialog().apply {
                title = playingMusic?.title
                artist = playingMusic?.artist
                duration = PlayManager.getDuration().toLong()
                searchListener = {
                }
                textSizeListener = {
                    lyricFragment?.lyricTv?.setTextSize(it)
                }
                textColorListener = {
                    lyricFragment?.lyricTv?.setHighLightTextColor(it)
                }
                lyricListener = {
                    lyricFragment?.lyricTv?.setLyricContent(it)
                }
            }.show(this)

        }
    }

    override fun updatePlayMode() {
        UIUtils.updatePlayMode(playModeIv, false)
    }

    override fun initData() {
        setupViewPager(viewPager)
        coverFragment?.initAlbumPic()
        mPresenter?.updateNowPlaying(PlayManager.getPlayingMusic(), true)
        //更新播放状态
        PlayManager.isPlaying().let {
            updatePlayStatus(it)
        }
        lyricFragment?.showLyric(FloatLyricViewManager.lyricInfo, true)
        LogUtil.d("CoverFragment", "playingMusic =${playingMusic == null}")
        playingMusic?.let { coverFragment?.updateMusicType(it) }
    }

    override fun listener() {
        super.listener()
        backIv.setOnClickListener {
            closeActivity()
        }
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

        /**
         * 歌曲操作
         */
        operateSongIv.setOnClickListener {
            BottomDialogFragment.newInstance(playingMusic)
                    .show(this)
        }

        coverFragment?.setOnclickAlbumListener {
            LogUtil.d(TAG,"coverFragment click")
            viewPager.currentItem = 1
        }
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }


    fun nextPlay(view: View?) {
        if (UIUtils.isFastClick()) return
        PlayManager.next()
    }

    fun prevPlay(view: View?) {
        if (UIUtils.isFastClick()) return
        PlayManager.prev()
    }

    fun changePlayMode(view: View?) {
        UIUtils.updatePlayMode(view as ImageView, true)
    }

    /**
     * 打开播放队列
     */
    fun openPlayQueue(view: View?) {
        PlayQueueDialog.newInstance().showIt(this)
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
        PlaylistManagerUtils.addToPlaylist(this, playingMusic)
    }

    /**
     * 添加到歌單
     */
    fun showSongComment(view: View?) {
        startActivity<SongCommentActivity>(Extras.SONG to playingMusic)
    }

    /**
     * 分享歌曲
     * TODO 增加海报，截屏分享
     */
    fun shareMusic(view: View?) {
        Tools.qqShare(this, PlayManager.getPlayingMusic())
    }

    /**
     * 歌曲下载
     */
    fun downloadMusic(view: View?) {
        QualitySelectDialog.newInstance(playingMusic).apply {
            isDownload = true
        }.show(this)
    }

    override fun setPlayingBitmap(albumArt: Bitmap?) {
        coverFragment?.setImageBitmap(albumArt)
    }

    override fun setPlayingBg(albumArt: Drawable?, isInit: Boolean?) {
        if (isInit != null && isInit) {
            playingBgIv.setImageDrawable(albumArt)
        } else {
            //加载背景图过度
            TransitionAnimationUtils.startChangeAnimation(playingBgIv, albumArt)
        }
    }

    override fun updatePlayStatus(isPlaying: Boolean) {
        if (isPlaying && !playPauseIv.isPlaying) {
            playPauseIv.play()
            coverFragment?.resumeRotateAnimation()
        } else if (!isPlaying && playPauseIv.isPlaying) {
            coverFragment?.stopRotateAnimation()
            playPauseIv.pause()
        }
    }

    override fun updateProgress(progress: Long, max: Long) {
        if (!isPause) {
            progressSb.progress = progress.toInt()
            progressSb.max = max.toInt()
            progressTv.text = FormatUtil.formatTime(progress)
            durationTv.text = FormatUtil.formatTime(max)

            lyricFragment?.setCurrentTimeMillis(progress)
        }
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        fragments.clear()
        coverFragment?.let {
            fragments.add(it)
        }
        lyricFragment?.let {
            fragments.add(it)
        }
        val mAdapter = PlayerPagerAdapter(this, fragments)
        viewPager.adapter = mAdapter
        viewPager.setPageTransformer(DepthPageTransformer())
        viewPager.offscreenPageLimit = 2
        viewPager.currentItem = 0
        var height = 0
        bottomOpView?.post {
            height = bottomOpView.height;
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset <= 1 && position == 0) {
                    detailView.translationY = (height * positionOffset)
                } else {
                    detailView.translationY = (height * 1f)
                }
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    searchLyricIv.visibility = View.GONE
                    operateSongIv.visibility = View.VISIBLE
                    lyricFragment?.lyricTv?.setIndicatorShow(false)
                    rightTv.isChecked = false
                    leftTv.isChecked = true
                } else {
                    searchLyricIv.visibility = View.VISIBLE
                    operateSongIv.visibility = View.GONE
                    leftTv.isChecked = false
                    rightTv.isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    /**
     * 底部上移动画效果
     */
    private fun moveToViewLocation(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        mHiddenAction.duration = 300
        return mHiddenAction
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayModeChangedEvent(event: PlayModeEvent) {
        updatePlayMode()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMetaChangedEvent(event: MetaChangedEvent) {
        mPresenter?.updateNowPlaying(event.music)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updatePlayStatus(event: StatusChangedEvent) {
        playPauseIv.setLoading(!event.isPrepared)
        updatePlayStatus(event.isPlaying)
        progressSb?.secondaryProgress = event.percent.toInt()
    }

    override fun onBackPressed() {
        closeActivity()
    }

    /**
     * 关闭当前界面
     */
    private fun closeActivity() {
        super.onBackPressed()
//        finish()
//        overridePendingTransition(0, 0)
//        ActivityCompat.finishAfterTransition(this)
    }

}
