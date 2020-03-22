package com.music.lake.musiclib.listener

import com.music.lake.musiclib.bean.BaseMusicInfo

interface MusicUrlRequest {
    fun checkNonValid(baseMusicInfo: BaseMusicInfo, call: MusicRequestCallBack)
}