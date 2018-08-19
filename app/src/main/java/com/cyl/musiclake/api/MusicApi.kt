package com.cyl.musiclake.api


import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.utils.FileUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

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
            Constants.BAIDU -> {
                if (music.lyric != null) {
                    BaiduApiServiceImpl.getBaiduLyric(music)
                } else {
                    BaiduApiServiceImpl.getTingSongInfo(music).flatMap { result ->
                        music.lyric = result.lyric
                        BaiduApiServiceImpl.getBaiduLyric(music)
                    }
                }
            }
            Constants.LOCAL -> {
                MusicApiServiceImpl.getLocalLyricInfo(music)
            }
            else -> {
                MusicApiServiceImpl.getLyricInfo(music)
            }
        }
    }


    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     */
    fun getMusicInfo(music: Music): Observable<Music>? {
        return when (music.type) {
            Constants.BAIDU -> BaiduApiServiceImpl.getTingSongInfo(music).flatMap { result ->
                Observable.create(ObservableOnSubscribe<Music> {
                    music.lyric = result.lyric
                    music.uri = result.uri
                    if (music.uri != null) {
                        it.onNext(music)
                        it.onComplete()
                    } else {
                        it.onError(Throwable(""))
                    }
                })
            }
            else -> {
                MusicApiServiceImpl.getMusicUrl(music.type!!, music.mid!!).flatMap { result ->
                    Observable.create(ObservableOnSubscribe<Music> {
                        music.uri = result
                        if (music.uri != null) {
                            it.onNext(music)
                            it.onComplete()
                        } else {
                            it.onError(Throwable(""))
                        }
                    })
                }
            }
        }
    }

    /**
     * 加载图片
     */
//    fun getMusicAlbumInfo(info: String): Observable<String> {
//        return BaiduApiServiceImpl.getSearchPicInfo(info)
//    }

    /**
     * 加载图片
     */
//    fun getMusicAlbumPic(info: String, success: (String?) -> Unit, fail: (() -> Unit?)? = null) {
//        ApiManager.request(MusicApi.getMusicAlbumInfo(info), object : RequestCallBack<String> {
//            override fun success(result: String?) {
//                if (result == null) {
//                    fail?.invoke()
//                } else {
//                    success.invoke(result)
//                }
//            }
//
//            override fun error(msg: String?) {
//                LogUtil.e("getMusicAlbumPic", msg)
//                fail?.invoke()
//            }
//        })
//    }

    /**
     * 根据路径获取本地歌词
     */
    fun getLocalLyricInfo(path: String?): Observable<String> {
        return Observable.create { emitter ->
            try {
                val lyric = FileUtils.readFile(path)
                emitter.onNext(lyric)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}