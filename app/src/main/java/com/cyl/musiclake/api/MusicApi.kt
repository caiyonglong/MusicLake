package com.cyl.musiclake.api


import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.doupan.DoubanApiServiceImpl
import com.cyl.musiclake.api.doupan.DoubanMusic
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.netease.NeteaseList
import com.cyl.musiclake.api.qq.QQApiServiceImpl
import com.cyl.musiclake.api.xiami.XiamiServiceImpl
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Author   : D22434
 * version  : 2018/3/9
 * function : 接口数据集中到一个类中管理。
 */

object MusicApi {
    private val TAG = "MusicApi"

    /**
     * 获取精品歌单
     *
     * @param
     * @return
     */
    val topPlaylist: Observable<List<NeteaseList>>
        get() = NeteaseApiServiceImpl.getTopPlaylist()

    /**
     * 获取歌词
     *
     * @param music
     * @return
     */
    fun getLyricInfo(music: Music): Observable<String>? {
        return when (music.type) {
            Constants.QQ -> QQApiServiceImpl.getQQLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            Constants.BAIDU -> BaiduApiServiceImpl.getBaiduLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            Constants.XIAMI -> XiamiServiceImpl.getXimaiLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            Constants.NETEASE -> NeteaseApiServiceImpl.getNeteaseLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            else -> null
        }
    }

    /**
     * 搜索音乐
     *
     * @param key
     * @param limit
     * @param page
     * @return
     */
    fun searchMusic(key: String, limit: Int, page: Int): Observable<List<Music>> {
        return Observable.merge(QQApiServiceImpl.search(key, limit, page),
                XiamiServiceImpl.search(key, limit, page),
                NeteaseApiServiceImpl.search(key, limit, page - 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     */
    fun getMusicInfo(music: Music): Observable<Music>? {
        return when (music.type) {
            Constants.QQ -> QQApiServiceImpl.getMusicInfo(music)
            Constants.NETEASE -> NeteaseApiServiceImpl.getMusicUrl(music)
            Constants.XIAMI -> XiamiServiceImpl.getMusicInfo(music)
            Constants.BAIDU -> music.mid?.let { BaiduApiServiceImpl.getTingSongInfo(it) }
            else -> null
        }
    }

    fun getTopList(id: Int): Observable<Playlist> {
        return NeteaseApiServiceImpl.getTopList(id)
    }

    /**
     * 加载图片
     */
    fun getMusicAlbumInfo(info: String): Observable<DoubanMusic> {
        return DoubanApiServiceImpl.getMusicInfo(info)
    }

}
