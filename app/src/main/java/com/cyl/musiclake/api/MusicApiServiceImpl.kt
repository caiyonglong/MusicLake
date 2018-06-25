package com.cyl.musiclake.api


import com.cyl.musicapi.BaseApiImpl
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.api.doupan.DoubanApiServiceImpl
import com.cyl.musiclake.api.doupan.DoubanMusic
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.ui.music.search.SearchEngine
import com.cyl.musiclake.ui.music.search.SearchEngine.Filter.*
import com.cyl.musiclake.utils.FileUtils
import com.cyl.musiclake.utils.LogUtil
import io.reactivex.Observable
import io.reactivex.Observable.create

/**
 * Author   : D22434
 * version  : 2018/3/9
 * function : 接口数据集中到一个类中管理。
 */

object MusicApiServiceImpl {
    private val TAG = "MusicApi"

    /**
     * 搜索音乐
     *
     * @param key
     * @param limit
     * @param page
     * @return
     */
    fun searchMusic(key: String, type: SearchEngine.Filter, limit: Int, page: Int): Observable<List<Music>> {
        return create { result ->
            BaseApiImpl.getInstance(MusicApp.mContext)
                    .searchSong(key, limit, page) {
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
                    }
        }
    }


    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     */
    fun getMusicInfo(music: Music, url: String): Observable<Music>? {
        return create { result ->

        }
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
        return create { result ->
            BaseApiImpl.getInstance(MusicApp.mContext)
                    .getBatchSongDetail(vendor, ids) {
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
                    }
        }
    }

    /**
     * 获取歌曲url信息
     *
     */
    fun getMusicUrl(vendor: String, mid: String): Observable<String> {
        return create { result ->
            BaseApiImpl.getInstance(MusicApp.mContext)
                    .getSongUrl(vendor, mid, {
                        if (it.status) {
                            val url = it.data.url
                            result.onNext(url)
                            result.onComplete()
                        } else {
                            result.onError(Throwable(""))
                        }
                    }, {})
        }
    }


    /**
     * 获取歌词
     *
     */
    fun getLyricInfo(music: Music): Observable<String> {
        val mLyricPath = FileUtils.getLrcDir() + music.title + "-" + music.artist + ".lrc"
        val vendor = music.type!!
        val mid = music.mid!!
        //网络歌词
        return if (FileUtils.exists(mLyricPath)) {
            MusicApi.getLocalLyricInfo(mLyricPath)
        } else create { result ->
            BaseApiImpl.getInstance(MusicApp.mContext)
                    .getLyricInfo(vendor, mid) {
                        if (it.status) {
                            val url = it.data
                            val lyric = StringBuilder()
                            url.forEach {
                                lyric.append(it)
                                lyric.append("\n")
                            }
                            //保存文件
                            val save = FileUtils.writeText(mLyricPath, lyric.toString())
                            LogUtil.e("保存网络歌词：$save")
                            Observable.fromArray(lyric)

                            result.onNext(lyric.toString())
                            result.onComplete()
                        } else {
                            result.onError(Throwable(""))
                        }
                    }
        }
    }
}
