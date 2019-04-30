package com.cyl.musiclake.api.music


import com.cyl.musicapi.bean.SongComment
import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.utils.FileUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
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
     *
     */
    fun getMusicInfo(music: Music): Observable<Music> {
        //获取默认音质
        var quality = SPUtils.getAnyByKey(Constants.SP_KEY_SONG_QUALITY, music.quality)
        //判断是否当前音质
        if (music.quality != quality) {
            quality = when {
                quality >= 999000 && music.sq -> 999000
                quality >= 320000 && music.hq -> 320000
                quality >= 192000 && music.high -> 192000
                quality >= 128000 -> 128000
                else -> 128000
            }
        }

        val cachePath = FileUtils.getMusicCacheDir() + music.artist + " - " + music.title + "(" + quality + ")"
        val downloadPath = FileUtils.getMusicDir() + music.artist + " - " + music.title + ".mp3"
        if (FileUtils.exists(cachePath)) {
            return Observable.create {
                music.uri = cachePath
                if (music.uri != null) {
                    it.onNext(music)
                    it.onComplete()
                } else {
                    it.onError(Throwable(""))
                }
            }
        } else if (FileUtils.exists(downloadPath)) {
            return Observable.create {
                music.uri = downloadPath
                if (music.uri != null) {
                    it.onNext(music)
                    it.onComplete()
                } else {
                    it.onError(Throwable(""))
                }
            }
        }
        return when (music.type) {
            Constants.BAIDU -> BaiduApiServiceImpl.getTingSongInfo(music).flatMap { result ->
                Observable.create(ObservableOnSubscribe<Music> {
                    music.uri = result.uri
                    music.lyric = result.lyric
                    if (music.uri != null) {
                        it.onNext(music)
                        it.onComplete()
                    } else {
                        it.onError(Throwable(""))
                    }
                })
            }
            else -> {
                MusicApiServiceImpl.getMusicUrl(music.type
                        ?: Constants.LOCAL, music.mid
                        ?: "", quality).flatMap { result ->
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
     * 获取播放地址（下载）
     */
    fun getMusicDownloadUrl(music: Music, isCache: Boolean = false): Observable<String>? {
        return when (music.type) {
            Constants.BAIDU -> BaiduApiServiceImpl.getTingSongInfo(music).flatMap { result ->
                Observable.create(ObservableOnSubscribe<String> {
                    if (result.uri != null) {
                        it.onNext(result.uri!!)
                        it.onComplete()
                    } else {
                        it.onError(Throwable(""))
                    }
                })
            }
            else -> {
                val br = if (isCache) music.quality else 128000
                MusicApiServiceImpl.getMusicUrl(music.type!!, music.mid!!, br).flatMap { result ->
                    Observable.create(ObservableOnSubscribe<String> {
                        if (result.isNotEmpty()) {
                            it.onNext(result)
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
     * 搜索歌曲评论
     */
    fun getMusicCommentInfo(music: Music, success: (MutableList<SongComment>?) -> Unit, fail: (() -> Unit?)? = null) {
        if (music.type == null || music.mid == null) {
            fail?.invoke()
            return
        }
        val observable = MusicApiServiceImpl.getMusicComment(music.type!!, music.mid!!)
        if (observable == null) {
            fail?.invoke()
            return
        }
        ApiManager.request(observable, object : RequestCallBack<MutableList<SongComment>> {
            override fun success(result: MutableList<SongComment>?) {
                success.invoke(result)
            }

            override fun error(msg: String?) {
                LogUtil.e("getMusicAlbumPic", msg)
                fail?.invoke()
            }
        })
    }


    /**
     * 加载图片
     */
    fun getMusicAlbumPic(info: String, success: (String?) -> Unit, fail: (() -> Unit?)? = null) {
        ApiManager.request(MusicApiServiceImpl.getAlbumUrl(info), object : RequestCallBack<String> {
            override fun success(result: String?) {
                if (result == null) {
                    fail?.invoke()
                } else {
                    success.invoke(result)
                }
            }

            override fun error(msg: String?) {
                LogUtil.e("getMusicAlbumPic", msg)
                fail?.invoke()
            }
        })
    }

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

    /**
     * 根据id获取歌曲信息
     */
    fun loadSongDetailInfo(vendor: String, mid: String, success: ((Music?) -> Unit)?) {
        val observable = MusicApiServiceImpl.getSongDetail(vendor, mid)
        ApiManager.request(observable, object : RequestCallBack<Music> {
            override fun success(result: Music) {
                success?.invoke(result)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }
}