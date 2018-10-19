package com.cyl.musiclake.ui.music.player

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.common.TransitionAnimationUtils
import com.cyl.musiclake.event.MetaChangedEvent
import com.cyl.musiclake.event.PlayModeEvent
import com.cyl.musiclake.event.StatusChangedEvent
import com.cyl.musiclake.player.FloatLyricViewManager
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.OnlinePlaylistUtils
import com.cyl.musiclake.ui.UIUtils
import com.cyl.musiclake.ui.music.comment.SongCommentActivity
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.dialog.MusicLyricDialog
import com.cyl.musiclake.ui.music.dialog.QualitySelectDialog
import com.cyl.musiclake.ui.music.local.adapter.MyPagerAdapter
import com.cyl.musiclake.ui.music.playqueue.PlayQueueDialog
import com.cyl.musiclake.utils.FormatUtil
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.Tools
import com.cyl.musiclake.view.DepthPageTransformer
import com.cyl.musiclake.view.LyricView
import com.cyl.musiclake.view.MultiTouchViewPager
import kotlinx.android.synthetic.main.activity_player.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity

class PlayerActivity : BaseActivity<PlayPresenter>(), PlayContract.View {

    private var playingMusic: Music? = null
    private var coverView: View? = null
    private var lyricView: View? = null
    private val viewPagerContent = mutableListOf<View>()
    private var mLyricView: LyricView? = null
    var mQualityTv: TextView? = null
    private var coverAnimator: ObjectAnimator? = null

    override fun showNowPlaying(music: Music?) {
        if (music == null) finish()

        playingMusic = music
        //更新标题
        titleIv.text = music?.title
        subTitleTv.text = music?.artist
        //更新图片
//        CoverLoader.loadBigImageView(this, music, coverView?.findViewById<ImageView>(R.id.civ_cover))
        //更新类型
        updateMusicType()
        //更新收藏状态
        music?.isLove?.let {
            collectIv.setImageResource(if (it) R.drawable.item_favorite_love else R.drawable.item_favorite)
        }
        //更新下载状态
//        music?.isDl?.let {
//            downloadIv.visibility = if (it) View.VISIBLE else View.GONE
//        }
        //隐藏显示歌曲评论
        songCommentTv.visibility = if (playingMusic?.type == Constants.XIAMI || playingMusic?.type == Constants.QQ || playingMusic?.type == Constants.NETEASE) View.VISIBLE else View.GONE

        coverAnimator?.cancel()
        coverAnimator?.start()
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
    }

    override fun updatePlayMode() {
        UIUtils.updatePlayMode(playModeIv, false)
    }

    override fun initData() {
        setupViewPager(viewPager)
        initAlbumPic(coverView?.findViewById(R.id.civ_cover))
        mPresenter?.updateNowPlaying(PlayManager.getPlayingMusic(), true)
        //初始加載歌詞
        //更新播放状态
        PlayManager.isPlaying().let {
            updatePlayStatus(it)
        }
        showLyric(FloatLyricViewManager.lyricInfo, true)
        updateMusicType()
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
                    mLyricView?.setCurrentTimeMillis(it.toLong())
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
        OnlinePlaylistUtils.addToPlaylist(this, playingMusic)
    }

    /**
     * 添加到歌單
     */
    fun showSongComment(view: View?) {
        startActivity<SongCommentActivity>(Extras.SONG to playingMusic)
    }

    /**
     * 分享歌曲
     */
    fun shareMusic(view: View?) {
        Tools.qqShare(this, PlayManager.getPlayingMusic())
    }

    fun downloadMusic(view: View?) {
        QualitySelectDialog.newInstance(playingMusic).apply {
            isDownload = true
        }.show(this)
    }

    override fun setPlayingBitmap(albumArt: Bitmap?) {
        coverView?.findViewById<ImageView>(R.id.civ_cover)?.setImageBitmap(albumArt)
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
        if (isPlaying) {
            playPauseIv.play()
            coverAnimator?.isStarted?.let {
                if (it) coverAnimator?.resume() else coverAnimator?.start()
            }
        } else {
            coverAnimator?.pause()
            playPauseIv.pause()
        }
    }

    override fun showLyric(lyric: String?, init: Boolean) {
        if (init) {
            //初始化歌词配置
            mLyricView?.setTextSize(SPUtils.getFontSize())
            mLyricView?.setHighLightTextColor(SPUtils.getFontColor())
            mLyricView?.setTouchable(true)
            mLyricView?.setOnPlayerClickListener { progress, _ ->
                PlayManager.seekTo(progress.toInt())
                if (!PlayManager.isPlaying()) {
                    PlayManager.playPause()
                }
            }
        }
        mLyricView?.setLyricContent(lyric)


        searchLyricIv.setOnClickListener {
            MusicLyricDialog().apply {
                title = playingMusic?.title
                artist = playingMusic?.artist
                duration = PlayManager.getDuration().toLong()
                searchListener = {
                }
                textSizeListener = {
                    mLyricView?.setTextSize(it)
                }
                textColorListener = {
                    mLyricView?.setHighLightTextColor(it)
                }
                lyricListener = {
                    mLyricView?.setLyricContent(it)
                }
            }.show(this)

        }
    }

    override fun updateProgress(progress: Long, max: Long) {
        if (!isPause) {
            progressSb.progress = progress.toInt()
            progressSb.max = max.toInt()
            progressTv.text = FormatUtil.formatTime(progress)
            durationTv.text = FormatUtil.formatTime(max)

            mLyricView?.setCurrentTimeMillis(progress)
        }
    }

    private fun setupViewPager(viewPager: MultiTouchViewPager) {
        //初始化View
        coverView = LayoutInflater.from(this).inflate(R.layout.frag_player_coverview, viewPager, false)
        lyricView = LayoutInflater.from(this).inflate(R.layout.frag_player_lrcview, viewPager, false)
        mLyricView = lyricView?.findViewById(R.id.lyricShow)
        mQualityTv = coverView?.findViewById(R.id.tv_quality)
        coverView?.findViewById<TextView>(R.id.tv_sound_effect)?.setOnClickListener {
            NavigationHelper.navigateToSoundEffect(this)
        }
        coverView?.let {
            viewPagerContent.add(it)
        }
        lyricView?.let {
            viewPagerContent.add(it)
        }
        mQualityTv?.setOnClickListener {
            QualitySelectDialog.newInstance(playingMusic).apply {
                changeSuccessListener = {
                    this@PlayerActivity.mQualityTv?.text = it
                }
                isDownload = false
            }.show(this)
        }

        val mAdapter = MyPagerAdapter(viewPagerContent)
        viewPager.adapter = mAdapter
        viewPager.setPageTransformer(false, DepthPageTransformer())
        viewPager.offscreenPageLimit = 2
        viewPager.currentItem = 0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                LogUtil.d("PlayControlFragment", "--$position")
                if (position == 0) {
                    searchLyricIv.visibility = View.GONE
                    operateSongIv.visibility = View.VISIBLE
                    mLyricView?.setIndicatorShow(false)
                } else {
                    searchLyricIv.visibility = View.VISIBLE
                    operateSongIv.visibility = View.GONE
                }
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


    /**
     * 初始化旋转动画
     */
    private fun initAlbumPic(view: View?) {
        if (view == null) return
        coverAnimator = ObjectAnimator.ofFloat(view, "rotation", 0F, 359F).apply {
            duration = (20 * 1000).toLong()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
            interpolator = LinearInterpolator()
        }
    }


    /**
     * 更新歌曲類型
     * 更新音乐品质
     */
    private fun updateMusicType() {
        val value: String? = when (playingMusic?.type) {
            Constants.QQ -> {
                getString(R.string.res_qq)
            }
            Constants.BAIDU -> {
                getString(R.string.res_baidu)
            }
            Constants.NETEASE -> {
                getString(R.string.res_wangyi)
            }
            Constants.XIAMI -> {
                getString(R.string.res_xiami)
            }
            else -> {
                getString(R.string.res_local)
            }
        }
        val quality = when (playingMusic?.quality) {
            128000 -> "标准"
            192000 -> "较高品质"
            320000 -> "HQ高品质"
            999000 -> "SQ无损品质"
            else -> "标准"
        }
        mQualityTv?.text = quality
        value?.let {
            coverView?.findViewById<TextView>(R.id.tv_source)?.text = value
        }
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
    }

    override fun onBackPressed() {
        closeActivity()
    }


    private fun closeActivity() {
        overridePendingTransition(0, 0)
        ActivityCompat.finishAfterTransition(this)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        coverAnimator?.cancel()
        coverAnimator = null
    }


    public override fun onResume() {
        super.onResume()
        if (coverAnimator != null && coverAnimator?.isPaused!! && PlayManager.isPlaying()) {
            coverAnimator?.resume()
        }
    }

    public override fun onStop() {
        super.onStop()
        coverAnimator?.pause()
    }
}
