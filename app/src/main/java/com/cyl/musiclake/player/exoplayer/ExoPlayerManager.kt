package com.cyl.musiclake.player.exoplayer

import com.cyl.musiclake.MusicApp
import com.google.android.exoplayer2.ui.PlayerView


object ExoPlayerManager {
    val musicExoPlayer = MusicExoPlayer(MusicApp.mContext)

    fun setDataSource(url: String?) {
        musicExoPlayer.setDataSource(url)
    }

    fun bindView(videoView: PlayerView?) {
        videoView?.let { musicExoPlayer.bindView(it) }
    }

    fun stop() {
        musicExoPlayer.stop()
    }

}