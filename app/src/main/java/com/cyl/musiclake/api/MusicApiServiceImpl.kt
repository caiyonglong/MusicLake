package com.cyl.musiclake.api


import com.cyl.musicapi.BaseApiImpl
import com.cyl.musiclake.MusicApp
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
import com.cyl.musiclake.ui.music.search.SearchEngine
import com.cyl.musiclake.ui.music.search.SearchEngine.Filter.*
import io.reactivex.Observable
import io.reactivex.Observable.create
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Author   : D22434
 * version  : 2018/3/9
 * function : 接口数据集中到一个类中管理。
 */

object MusicApiServiceImpl {
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
    fun searchMusic(key: String, type: SearchEngine.Filter, limit: Int, page: Int): Observable<List<Music>> {
        return create({ result ->
            BaseApiImpl.getInstance(MusicApp.mContext)
                    .searchSong(key, limit, page, {
                        val musicList = mutableListOf<Music>()
                        if (it.status) {
                            if (type == ANY || type == NETEASE)
                                it.data.netease.songs?.forEach {
                                    musicList.add(MusicUtils.getSearchMusic(it, Constants.NETEASE))
                                }
                            if (type == ANY || type == QQ)
                                it.data.qq.songs?.forEach {
                                    musicList.add(MusicUtils.getSearchMusic(it, Constants.QQ))
                                }
                            if (type == ANY || type == XIAMI)
                                it.data.xiami.songs?.forEach {
                                    musicList.add(MusicUtils.getSearchMusic(it, Constants.XIAMI))
                                }
                            result.onNext(musicList)
                            result.onComplete()
                        } else {
                            result.onError(Throwable("service error"))
                        }
                    })
        })
    }


    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     */
    fun getMusicInfo(music: Music): Observable<Music>? {
        return when (music.type) {
            Constants.QQ -> QQApiServiceImpl.getMusicInfo(music)
            Constants.NETEASE -> NeteaseApiServiceImpl.getMusicUrl(music)
            Constants.XIAMI -> XiamiServiceImpl.getMusicInfo(music)
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


    /**
     * 批量獲取歌曲信息
     *
     */
    fun getBatchMusic(vendor: String, ids: Array<String>): Observable<List<Music>> {
        return create({ result ->
            BaseApiImpl.getInstance(MusicApp.mContext)
                    .getBatchSongDetail(vendor, ids, {
                        val musicList = mutableListOf<Music>()
                        if (it.status) {
                            val songList = it.data
                            songList.forEach {
                                musicList.add(MusicUtils.getSearchMusic(it, vendor))
                            }
                            result.onNext(musicList)
                            result.onComplete()
                        } else {
                            result.onError(Throwable(it.msg))
                        }
                    })
        })
    }

}
