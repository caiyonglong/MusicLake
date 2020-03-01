package com.music.lake.musiclib.listener

import com.music.lake.musiclib.bean.BaseMusicInfo

interface MusicRequest {
    fun checkNonValid(baseMusicInfo: BaseMusicInfo, call: MusicRequestCallBack)
}