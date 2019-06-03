package com.cyl.musiclake.ui.music.playpage

import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.MusicPlayerService
import com.cyl.musiclake.player.playback.PlayProgressListener
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ImageUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by hefuyi on 2016/11/8.
 */

class PlayPresenter @Inject
constructor() : BasePresenter<PlayContract.View>(), PlayContract.Presenter, PlayProgressListener {
    override fun onProgressUpdate(position: Long, duration: Long) {
        mView?.updateProgress(position, duration)
    }

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        MusicPlayerService.addProgressListener(this)
    }

    override fun detachView() {
        super.detachView()
        MusicPlayerService.removeProgressListener(this)
    }

    override fun updateNowPlaying(music: Music?, isInit: Boolean?) {
        mView?.showNowPlaying(music)
        CoverLoader.loadBigImageView(mView?.context, music) { bitmap ->
            doAsync {
                val blur = ImageUtils.createBlurredImageFromBitmap(bitmap, 12)
                uiThread {
                    mView?.setPlayingBg(blur, isInit)
                    mView?.setPlayingBitmap(bitmap)
                }
            }
        }
    }
}
