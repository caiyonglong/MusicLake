package com.cyl.musiclake.player.playback

import android.media.MediaPlayer

/**
 * Created by master on 2018/5/14.
 * 播放回调
 */
interface PlaybackListener {
    /**
     * 完成下一首
     */
    fun onCompletionNext()

    /**
     * 完成结束
     */
    fun onCompletionEnd()
    fun onBufferingUpdate(mp: MediaPlayer?, percent: Int)
    fun onPrepared()
    fun onError()


    //exoplayer
    fun onPlaybackProgress(
            position: Long,
            duration: Long,
            buffering: Long
    )

    fun onLoading(isLoading: Boolean)
    fun onPlayerStateChanged(isPlaying: Boolean)
}