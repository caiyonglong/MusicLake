package com.cyl.musiclake.ui.music.playpage

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.ui.base.BaseContract


interface PlayContract {

    interface View : BaseContract.BaseView {

        fun setPlayingBitmap(albumArt: Bitmap?)

        fun setPlayingBg(albumArt: Drawable?, isInit: Boolean? = false)

//        fun setPalette(palette: Palette?)

        fun updatePlayStatus(isPlaying: Boolean)

        fun updatePlayMode() {}

        fun updateProgress(progress: Long, max: Long)

        fun showNowPlaying(music: Music?)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun updateNowPlaying(music: Music?, isInit: Boolean? = false)
    }
}
