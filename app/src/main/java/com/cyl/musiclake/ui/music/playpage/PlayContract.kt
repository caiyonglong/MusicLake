package com.cyl.musiclake.ui.music.playpage

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.cyl.musiclake.ui.base.BaseContract
import com.music.lake.musiclib.bean.BaseMusicInfo


interface PlayContract {

    interface View : BaseContract.BaseView {

        fun setPlayingBitmap(albumArt: Bitmap?) {}

        fun setPlayingBg(albumArt: Drawable?, isInit: Boolean? = false) {}

        fun updatePlayStatus(isPlaying: Boolean)

        fun updateLoading(isLoading: Boolean)

        fun updatePlayMode() {}

        fun updateProgress(progress: Long, max: Long, bufferPercent: Int)

        fun showNowPlaying(baseMusic: BaseMusicInfo?)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun updateNowPlaying(baseMusic: BaseMusicInfo?, isInit: Boolean? = false)
    }
}
