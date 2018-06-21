package com.cyl.musiclake.api


import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.doupan.DoubanApiServiceImpl
import com.cyl.musiclake.api.doupan.DoubanMusic
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.qq.QQApiServiceImpl
import com.cyl.musiclake.api.xiami.XiamiServiceImpl
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.utils.FileUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess
import io.reactivex.schedulers.Schedulers

/**
 * Author   : D22434
 * version  : 2018/3/9
 * function : 接口数据集中到一个类中管理。
 */

object MusicApi {
    private val TAG = "MusicApi"

    /**
     * 获取歌词
     *
     * @param music
     * @return
     */
    fun getLyricInfo(music: Music): Observable<String>? {
        return when (music.type) {
            Constants.BAIDU -> BaiduApiServiceImpl.getBaiduLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            Constants.LOCAL ->{null}
            else -> {
                MusicApiServiceImpl.getLyricInfo(music)
            }
        }
    }


    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     */
    fun getMusicInfo(music: Music): Observable<String>? {
        return when (music.type) {
            Constants.BAIDU -> BaiduApiServiceImpl.getTingSongInfo(music.mid!!).flatMap { result ->
                Observable.create(ObservableOnSubscribe<String> {
                    val url = result.uri
                    if (url != null && url.isNotEmpty()) {
                        it.onNext(url)
                        it.onComplete()
                    } else {
                        it.onError(Throwable(""))
                    }
                })
            }
            else -> {
                MusicApiServiceImpl.getMusicUrl(music.type!!, music.mid!!)
            }
        }
    }


    /**
     * 加载图片
     */
    fun getMusicAlbumInfo(info: String): Observable<DoubanMusic> {
        return DoubanApiServiceImpl.getMusicInfo(info)
    }

}
