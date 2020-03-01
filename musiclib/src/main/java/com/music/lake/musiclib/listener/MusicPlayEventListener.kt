package com.music.lake.musiclib.listener

interface MusicPlayEventListener {

    fun onLoading(isLoading: Boolean)
    fun onPlaybackProgress(curPosition: Long?, duration: Long?, bufferPercent: Long?)
    fun onAudioSessionId(audioSessionId: Int)
    fun onPlayCompletion()
    fun onPlayStart()
    fun onPlayPause()
    fun onPlayStop()
    fun onPlayerError(error: Throwable?)
}
