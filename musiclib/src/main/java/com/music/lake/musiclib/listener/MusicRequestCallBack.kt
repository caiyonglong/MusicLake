package com.music.lake.musiclib.listener

interface MusicRequestCallBack {

    fun onMusicValid(url: String)

    fun onActionDirect()
}