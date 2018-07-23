package com.cyl.musiclake.ui.music.player

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.data.db.Music


interface PlayContract {

    interface View : BaseContract.BaseView {

        fun setPlayingBitmap(albumArt: Bitmap?)

        fun setPlayingBg(albumArt: Drawable?)

        fun setPalette(palette: Palette?)

        fun showLyric(lyric: String?, init: Boolean)

        fun updatePlayStatus(isPlaying: Boolean)

        fun updatePlayMode()

        fun updateProgress(progress: Long, max: Long)

        fun showNowPlaying(music: Music?)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun loadLyric(result: String?, status: Boolean)

        fun updateNowPlaying(music: Music?)
    }
}
