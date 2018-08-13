package com.cyl.musiclake.ui.music.player

import android.support.v7.graphics.Palette
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.RxBus
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.event.MetaChangedEvent
import com.cyl.musiclake.event.PlayModeEvent
import com.cyl.musiclake.event.StatusChangedEvent
import com.cyl.musiclake.player.MusicPlayerService
import com.cyl.musiclake.player.playback.PlayProgressListener
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ImageUtils
import javax.inject.Inject


/**
 * Created by hefuyi on 2016/11/8.
 */

class PlayPresenter @Inject
constructor() : BasePresenter<PlayContract.View>(), PlayContract.Presenter, PlayProgressListener {
    override fun onProgressUpdate(position: Long, duration: Long) {
        mView.updateProgress(position, duration)
    }

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        MusicPlayerService.addProgressListener(this)
        val disposable = RxBus.getInstance().register(MetaChangedEvent::class.java)
                .compose(mView.bindToLife())
                .subscribe { event -> updateNowPlaying(event.music) }
        val disposable1 = RxBus.getInstance().register(PlayModeEvent::class.java)
                .subscribe { event -> mView?.updatePlayMode() }
        val disposable3 = RxBus.getInstance().register(StatusChangedEvent::class.java)
                .compose(mView.bindToLife())
                .subscribe { statusChangedEvent ->
                    mView?.updatePlayStatus(statusChangedEvent.isPlaying)
                }
        disposables.add(disposable)
        disposables.add(disposable1)
        disposables.add(disposable3)
    }

    override fun detachView() {
        super.detachView()
        MusicPlayerService.removeProgressListener(this)
    }

    override fun updateNowPlaying(music: Music?) {
        mView?.showNowPlaying(music)

        CoverLoader.loadBigImageView(mView?.context, music) { bitmap ->
            mView?.setPlayingBitmap(bitmap)
            mView?.setPlayingBg(ImageUtils.createBlurredImageFromBitmap(bitmap, MusicApp.getAppContext(), 12))
            Palette.Builder(bitmap).generate { palette -> mView?.setPalette(palette) }
        }
    }
}
