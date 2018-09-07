package com.cyl.musiclake.ui.music.player

import android.support.v7.graphics.Palette
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
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
    }

    override fun detachView() {
        super.detachView()
        MusicPlayerService.removeProgressListener(this)
    }

    override fun updateNowPlaying(music: Music?) {
        mView?.showNowPlaying(music)
        CoverLoader.loadImageViewByMusic(mView?.context, music) { bitmap ->
            Palette.Builder(bitmap).generate { palette -> mView?.setPalette(palette) }
        }
        CoverLoader.loadBigImageView(mView?.context, music) { bitmap ->
            mView?.setPlayingBg(ImageUtils.createBlurredImageFromBitmap(bitmap, 12))
            mView?.setPlayingBitmap(bitmap)
        }
    }
}
