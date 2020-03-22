package com.music.lake.musiclib.listener

import android.graphics.Bitmap

interface MusicRequestCallBack {

    fun onMusicBitmap(bitmap: Bitmap)

    fun onMusicValid(url: String)

    fun onActionDirect()
}