//package com.cyl.musiclake.ui.music.player
//
//import android.annotation.SuppressLint
//import android.graphics.BitmapFactory
//import android.support.v7.graphics.Palette
//import android.text.TextUtils
//import com.cyl.musiclake.MusicApp
//import com.cyl.musiclake.R
//import com.cyl.musiclake.RxBus
//import com.cyl.musiclake.base.BasePresenter
//import com.cyl.musiclake.bean.Music
//import com.cyl.musiclake.event.MetaChangedEvent
//import com.cyl.musiclake.event.StatusChangedEvent
//import com.cyl.musiclake.player.MusicPlayerService
//import com.cyl.musiclake.player.PlayManager
//import com.cyl.musiclake.player.playback.PlayProgressListener
//import com.cyl.musiclake.utils.CoverLoader
//import com.cyl.musiclake.utils.ImageUtils
//import com.cyl.musiclake.utils.LogUtil
//import com.cyl.musiclake.utils.ToastUtils
//import org.jetbrains.anko.doAsync
//import org.jetbrains.anko.uiThread
//import javax.inject.Inject
//
//
//
//class PlayControlsPresenter @Inject
//constructor() : BasePresenter<PlayControlsContract.View>(), PlayControlsContract.Presenter, PlayProgressListener {
//    override fun onProgressUpdate(position: Long, duration: Long) {
//        mView.updateProgress(position.toInt())
//        mView.setProgressMax(PlayManager.getDuration())
//    }
//
//    private var isPlayPauseClick = false
//
//
//    override fun attachView(view: PlayControlsContract.View) {
//        super.attachView(view)
//        MusicPlayerService.addProgressListener(this)
//        var disposable = RxBus.getInstance().register(MetaChangedEvent::class.java)
//                .compose(mView.bindToLife())
//                .subscribe { event -> updateNowPlayingCard(event.music) }
//        disposables.add(disposable)
//        disposable = RxBus.getInstance().register(StatusChangedEvent::class.java)
//                .compose(mView.bindToLife())
//                .subscribe { statusChangedEvent ->
//                    updatePlayStatus(statusChangedEvent.isPlaying)
//                    if (!statusChangedEvent.isPrepared) {
//                        mView.showLoading()
//                    } else {
//                        mView.hideLoading()
//                    }
//                }
//        disposables.add(disposable)
//    }
//
//    override fun detachView() {
//        super.detachView()
//        MusicPlayerService.removeProgressListener(this)
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    override fun onPlayPauseClick() {
//        isPlayPauseClick = true
//        PlayManager.playPause()
//        if (PlayManager.getPlayingMusic() == null) {
//            ToastUtils.show(MusicApp.getAppContext(), "请选择需要播放的音乐")
//        } else {
//            val isPlaying = PlayManager.isPlaying()
//            mView.setPlayPauseButton(isPlaying)
//        }
//    }
//
//    override fun onPreviousClick() {
//        PlayManager.prev()
//    }
//
//    override fun loadLyric(result: String?, status: Boolean) {
//        mView.showLyric(result, false)
//    }
//
//    override fun onNextClick() {
//        PlayManager.next()
//    }
//
//
//    override fun updateNowPlayingCard(music: Music?) {
//        LogUtil.d(TAG, "updateNowPlayingCard")
//        if (mView == null) return
//        if (music == null || PlayManager.getPlayList().size == 0) {
//            val bitmap = BitmapFactory.decodeResource(mView.context.resources, R.drawable.header_material)
//            mView.setAlbumArt(bitmap)
//            mView.setAlbumArt(ImageUtils.createBlurredImageFromBitmap(bitmap, mView.context, 6))
//            Palette.Builder(bitmap).generate { palette -> mView.setPalette(palette) }
//            mView.setTitle(mView.context.resources.getString(R.string.app_name))
//            mView.setArtist("")
//            return
//        } else {
//            mView.updatePanelLayout(true)
//        }
//
//
//        val title = PlayManager.getSongName()
//        val artist = PlayManager.getSongArtist()
//        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(artist)) {
//            mView.setTitle(mView.context.resources.getString(R.string.app_name))
//            mView.setArtist("")
//        } else {
//            mView.setTitle(title)
//            mView.setArtist(artist)
//        }
//        //设置音乐来源
//        mView.setOtherInfo(music.type)
//
//        //获取当前歌曲状态
//        mView.updateFavorite(music.isLove)
//
//        if (!isPlayPauseClick) {
//            CoverLoader.loadImageViewByMusic(mView.context, music) { bitmap ->
//                mView.setAlbumArt(bitmap)
//                doAsync {
//                    val drawable = ImageUtils.createBlurredImageFromBitmap(bitmap, mView.context, 6)
//                    uiThread {
//                        mView.setAlbumArt(drawable)
//                    }
//                }
//                Palette.Builder(bitmap).generate { palette -> mView.setPalette(palette) }
//            }
//        }
//        isPlayPauseClick = false
//    }
//
//    override fun updatePlayStatus(isPlaying: Boolean) {
//        if (PlayManager.isPlaying()) {
//            if (!mView.playPauseStatus) {//true表示按钮为待暂停状态
//                mView.setPlayPauseButton(true)
//            }
//        } else {
//            if (mView.playPauseStatus) {
//                mView.setPlayPauseButton(false)
//            }
//        }
//    }
//
//    companion object {
//
//        private val TAG = "PlayControlsPresenter"
//    }
//
//}
