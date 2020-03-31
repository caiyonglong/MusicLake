package com.music.lake.musiclib.listener

import com.music.lake.musiclib.bean.BaseMusicInfo

interface MusicPlayEventListener {
    fun onLoading(isLoading: Boolean)
    fun onPlaybackProgress(curPosition: Long, duration: Long, bufferPercent: Int)
    fun onAudioSessionId(audioSessionId: Int)
    fun onPlayCompletion()
    fun onPlayStart()
    fun onPlayerStateChanged(isPlaying: Boolean)
    fun onPlayStop()
    fun onPlayerError(error: Throwable?)
    /**
     * 更新播放队列
     */
    fun onUpdatePlayList(playlist: MutableList<BaseMusicInfo>)
}
