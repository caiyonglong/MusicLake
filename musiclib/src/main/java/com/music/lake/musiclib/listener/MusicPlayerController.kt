package com.music.lake.musiclib.listener

import com.music.lake.musiclib.bean.BaseMusicInfo

interface MusicPlayerController {

    /**
     * 根据id 播放歌曲
     */
    fun playMusicById(index: Int)

    /**
     * 播放歌曲，新增播放
     */
    fun playMusic(song: BaseMusicInfo)

    /**
     * 播放歌曲列表
     */
    fun playMusic(songs: List<BaseMusicInfo>, index: Int)

    /**
     * 更新歌曲列表
     */
    fun updatePlaylist(songs: List<BaseMusicInfo>, index: Int)

    /**
     * 播放下一首
     */
    fun playNextMusic()

    /**
     * 播放上一首
     */
    fun playPrevMusic()

    /**
     * 恢复播放
     */
    fun restorePlay()

    /**
     * 暂停
     */
    fun pausePlay()

    /**
     * 停止
     */
    fun stopPlay()

    /**
     * 设置循环模式
     */
    fun setLoopMode(mode: Int)

    /**
     * 获取循环模式
     */
    fun getLoopMode(): Int

    /**
     * 移动到媒体流中的新位置,以毫秒为单位。
     */
    fun seekTo(ms: Long)

    /**
     * 获取现在正在播放的歌曲
     */
    fun getNowPlayingMusic(): BaseMusicInfo?

    /**
     *  获取现在正在播放的歌曲的下标
     */
    fun getNowPlayingIndex(): Int

    /**
     * 获取现在正在播放的歌曲列表
     */
    fun getPlayList(): List<BaseMusicInfo>

    /**
     * 更新播放列表
     */
//    fun updatePlayList(songs: List<BaseMusicInfo>, index: Int)

    /**
     * 移除播放列表的某条歌曲
     */
    fun removeFromPlaylist(position: Int)

    /**
     * 清空播放列表
     */
    fun clearPlaylist()

    /**
     * 判断是否正在播放
     */
    fun isPlaying(): Boolean

    /**
     * 返回正在播放的音频总时长，ms为单位
     */
    fun getDuration(): Long

    /**
     * 获取正在播放的位置，ms为单位
     */
    fun getPlayingPosition(): Long

    /**
     * 增加播放监听事件
     */
    fun addMusicPlayerEventListener(listener: MusicPlayEventListener)

    /**
     * 移除播放监听事件
     */
    fun removeMusicPlayerEventListener(listener: MusicPlayEventListener)

    /**
     * 移除播放监听事件
     */
    fun setMusicRequestListener(urlRequest: MusicUrlRequest)


    fun showDesktopLyric(show: Boolean)

    fun AudioSessionId(): Int

}