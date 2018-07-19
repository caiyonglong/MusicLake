package com.cyl.musiclake.ui.music.player

import android.os.Handler
import android.support.v7.graphics.Palette
import com.cyl.musiclake.RxBus
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.event.LyricChangedEvent
import com.cyl.musiclake.event.MetaChangedEvent
import com.cyl.musiclake.event.PlayModeEvent
import com.cyl.musiclake.event.StatusChangedEvent
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ImageUtils
import javax.inject.Inject


/**
 * Created by hefuyi on 2016/11/8.
 */

class PlayPresenter @Inject
constructor() : BasePresenter<PlayContract.View>(), PlayContract.Presenter {


    private var mProgress: Int = 0
    private var duration: Int = 0
    private var mHandler: Handler? = null

    private val updateProgress = object : Runnable {
        override fun run() {
            mProgress = PlayManager.getCurrentPosition()
            if (duration == 0) {
                duration = PlayManager.getDuration()
            }
            mView.updateProgress(mProgress, duration)
            mHandler?.postDelayed(this, 500)
        }
    }

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        mHandler = Handler()
        val disposable = RxBus.getInstance().register(MetaChangedEvent::class.java)
                .compose(mView.bindToLife())
                .subscribe { event -> updateNowPlaying(event.music) }
        val disposable2 = RxBus.getInstance().register(LyricChangedEvent::class.java)
                .compose(mView.bindToLife())
                .subscribe { event -> loadLyric(event.lyric, event.isStatus) }
        val disposable1 = RxBus.getInstance().register(PlayModeEvent::class.java)
                .subscribe { event -> mView?.updatePlayMode() }
        val disposable3 = RxBus.getInstance().register(StatusChangedEvent::class.java)
                .compose(mView.bindToLife())
                .subscribe { statusChangedEvent ->
                    mView?.updatePlayStatus(statusChangedEvent.isPlaying)
                }
        disposables.add(disposable)
        disposables.add(disposable1)
        disposables.add(disposable2)
        disposables.add(disposable3)
    }

    override fun detachView() {
        super.detachView()
        mHandler!!.removeCallbacks(updateProgress)
    }

    override fun loadLyric(result: String?, status: Boolean) {
        mView.showLyric(result, false)
    }

    override fun updateNowPlaying(music: Music) {
        mView?.showNowPlaying(music)

        CoverLoader.loadImageViewByMusic(mView.context, music) { bitmap ->
            mView.setPlayingBitmap(bitmap)
            mView.setPlayingBg(ImageUtils.createBlurredImageFromBitmap(bitmap, mView.context, 12))
            Palette.Builder(bitmap).generate { palette -> mView.setPalette(palette) }
        }
        mHandler?.post(updateProgress)
    }
}
