package com.cyl.musiclake.ui.music.playpage

import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ImageUtils
import com.cyl.musiclake.utils.LogUtil
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.music.lake.musiclib.playback.PlayProgressListener
import com.music.lake.musiclib.player.MusicPlayerService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by hefuyi on 2016/11/8.
 */

class PlayPresenter @Inject
constructor() : BasePresenter<PlayContract.View>(), PlayContract.Presenter, PlayProgressListener {

    private var disposable: Disposable? = null
    override fun onProgressUpdate(position: Long, duration: Long) {
    }

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        if (disposable == null) {
            disposable = Observable
                    .interval(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        LogUtil.d("PlayerActivity ", "disposable");
                        mView?.updateProgress(MusicPlayerService.getInstance().getPlayingPosition(),
                                MusicPlayerService.getInstance().getDuration())
                    };
        }
    }

    override fun detachView() {
        super.detachView()
        disposable?.dispose()
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
}
