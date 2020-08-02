package com.cyl.musiclake.player.exoplayer

import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.playback.PlaybackListener


open class BasePlayer {
    @JvmField
    var mNowPlayingMusic: Music? = null
    @JvmField
    var listener: PlaybackListener? = null
    @JvmField
    var playWhenReady = true

    open fun stop() {}
    open fun release() {}
    open fun start() {}
    open fun pause() {}
    open fun isPlaying(): Boolean {
        return false
    }

    open fun position(): Long {
        return 0
    }

    open fun duration(): Long {
        return 0
    }

    /**
     * 获取标题
     */
    val title: String?
        get() = if (mNowPlayingMusic != null) {
            mNowPlayingMusic!!.title
        } else ""

    /**
     * 获取歌手专辑
     *
     * @return
     */
    val artistName: String?
        get() = if (mNowPlayingMusic != null) {
            mNowPlayingMusic!!.artist
        } else null

    open fun isInitialized(): Boolean {
        return true
    }

    open fun isPrepared(): Boolean {
        return false
    }

    open fun seekTo(ms: Long) {}
    open fun bufferedPercentage(): Int {
        return 0
    }

    open fun getAudioSessionId(): Int {
        return 0
    }

    open fun setDataSource(uri: String?) {}

    open fun setMusicInfo(mNowPlayingMusic: Music?) {
        this.mNowPlayingMusic = mNowPlayingMusic
    }

    open fun setPlayBackListener(listener: PlaybackListener?) {
        this.listener = listener
    }

    open fun setVolume(mCurrentVolume: Float) {}
}