package com.music.lake.musiclib.listener

interface MusicPlayEventListener {
    fun onLoading(isLoading: Boolean)
    fun onPlaybackProgress(curPosition: Long, duration: Long, bufferPercent: Int)
    fun onAudioSessionId(audioSessionId: Int)
    fun onPlayCompletion()
    fun onPlayStart()
    fun onPlayerStateChanged(isPlaying: Boolean)
    fun onPlayStop()
    fun onPlayerError(error: Throwable?)
}
