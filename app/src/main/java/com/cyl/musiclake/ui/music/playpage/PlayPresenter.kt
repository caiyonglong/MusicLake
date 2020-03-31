package com.cyl.musiclake.ui.music.playpage

import android.annotation.SuppressLint
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ImageUtils
import com.music.lake.musiclib.MusicPlayerManager
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.music.lake.musiclib.listener.MusicPlayEventListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by hefuyi on 2016/11/8.
 */

class PlayPresenter @Inject
constructor() : BasePresenter<PlayContract.View>(), PlayContract.Presenter, MusicPlayEventListener {
    private var disposable: Disposable? = null

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        MusicPlayerManager.getInstance().addMusicPlayerEventListener(this)
    }

    override fun detachView() {
        super.detachView()
        disposable?.dispose()
        MusicPlayerManager.getInstance().removeMusicPlayerEventListener(this)
    }

    override fun updateNowPlaying(baseMusic: BaseMusicInfo?, isInit: Boolean?) {
        mView?.showNowPlaying(baseMusic)
        CoverLoader.loadBigImageView(mView?.context, baseMusic) { bitmap ->
            doAsync {
                val blur = ImageUtils.createBlurredImageFromBitmap(bitmap, 10)
                uiThread {
                    mView?.setPlayingBg(blur, isInit)
                    mView?.setPlayingBitmap(bitmap)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onLoading(isLoading: Boolean) {
        Observable.create<Boolean> { sub -> sub.onNext(isLoading) }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView?.updateLoading(it)
                }
    }

    override fun onPlaybackProgress(curPosition: Long, duration: Long, bufferPercent: Int) {
        mView?.updateProgress(MusicPlayerManager.getInstance().getPlayingPosition(), MusicPlayerManager.getInstance().getDuration(), bufferPercent)
    }

    override fun onAudioSessionId(audioSessionId: Int) {
    }

    override fun onPlayCompletion() {
    }

    override fun onPlayStart() {
    }

    @SuppressLint("CheckResult")
    override fun onPlayerStateChanged(isPlaying: Boolean) {
        Observable.create<Boolean> { sub -> sub.onNext(true) }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView?.updatePlayStatus(isPlaying)
                }
    }

    override fun onPlayStop() {
    }

    override fun onPlayerError(error: Throwable?) {
    }

    override fun onUpdatePlayList(playlist: MutableList<BaseMusicInfo>) {

    }

}
