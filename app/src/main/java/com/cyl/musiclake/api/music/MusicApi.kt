package com.cyl.musiclake.api.music


import com.cyl.musicapi.bean.SongComment
import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.common.Constants
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
     * @param baseMusicInfoInfo
     * @return
     */
    fun getLyricInfo(baseMusicInfoInfo: BaseMusicInfo): Observable<String>? {
        return when (baseMusicInfoInfo.type) {
            Constants.BAIDU -> {
                if (baseMusicInfoInfo.lyric != null) {
                    BaiduApiServiceImpl.getBaiduLyric(baseMusicInfoInfo)
                } else {
                    BaiduApiServiceImpl.getTingSongInfo(baseMusicInfoInfo).flatMap { result ->
                        baseMusicInfoInfo.lyric = result.lyric
                        BaiduApiServiceImpl.getBaiduLyric(baseMusicInfoInfo)
                    }
                }
            }
            Constants.LOCAL -> {
                MusicApiServiceImpl.getLocalLyricInfo(baseMusicInfoInfo)
            }
            else -> {
                MusicApiServiceImpl.getLyricInfo(baseMusicInfoInfo)
            }
        }
    }


    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     *
     */
    fun getMusicInfo(baseMusicInfo: BaseMusicInfo): Observable<BaseMusicInfo> {
        //获取默认音质
        var quality = SPUtils.getAnyByKey(Constants.SP_KEY_SONG_QUALITY, baseMusicInfo.quality)
        //判断是否当前音质
        if (baseMusicInfo.quality != quality) {
            quality = when {
                quality >= 999000 && baseMusicInfo.sq -> 999000
                quality >= 320000 && baseMusicInfo.hq -> 320000
                quality >= 192000 && baseMusicInfo.high -> 192000
                quality >= 128000 -> 128000
                else -> 128000
            }
        }

        val cachePath = FileUtils.getMusicCacheDir() + baseMusicInfo.artist + " - " + baseMusicInfo.title + "(" + quality + ")"
        val downloadPath = FileUtils.getMusicDir() + baseMusicInfo.artist + " - " + baseMusicInfo.title + ".mp3"
        if (FileUtils.exists(cachePath)) {
            return Observable.create {
                baseMusicInfo.uri = cachePath
                if (baseMusicInfo.uri != null) {
                    it.onNext(baseMusicInfo)
                    it.onComplete()
                } else {
                    it.onError(Throwable(""))
                }
            }
        } else if (FileUtils.exists(downloadPath)) {
            return Observable.create {
                baseMusicInfo.uri = downloadPath
                if (baseMusicInfo.uri != null) {
                    it.onNext(baseMusicInfo)
                    it.onComplete()
                } else {
                    it.onError(Throwable(""))
                }
            }
        }
        return when (baseMusicInfo.type) {
            Constants.BAIDU -> BaiduApiServiceImpl.getTingSongInfo(baseMusicInfo).flatMap { result ->
                Observable.create(ObservableOnSubscribe<BaseMusicInfo> {
                    baseMusicInfo.uri = result.uri
                    baseMusicInfo.lyric = result.lyric
                    if (baseMusicInfo.uri != null) {
                        it.onNext(baseMusicInfo)
                        it.onComplete()
                    } else {
                        it.onError(Throwable(""))
                    }
                })
            }
            else -> {
                MusicApiServiceImpl.getMusicUrl(baseMusicInfo.type
                        ?: Constants.LOCAL, baseMusicInfo.mid
                        ?: "", quality).flatMap { result ->
                    Observable.create(ObservableOnSubscribe<BaseMusicInfo> {
                        baseMusicInfo.uri = result
                        if (baseMusicInfo.uri != null) {
                            it.onNext(baseMusicInfo)
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
    fun getMusicDownloadUrl(baseMusicInfoInfo: BaseMusicInfo, isCache: Boolean = false): Observable<String>? {
        return when (baseMusicInfoInfo.type) {
            Constants.BAIDU ->
                BaiduApiServiceImpl.getTingSongInfo(baseMusicInfoInfo).flatMap { result ->
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
                val br = if (isCache) baseMusicInfoInfo.quality else 128000
                MusicApiServiceImpl.getMusicUrl(baseMusicInfoInfo.type!!, baseMusicInfoInfo.mid!!, br).flatMap { result ->
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
    fun getMusicCommentInfo(baseMusicInfoInfo: BaseMusicInfo, success: (MutableList<SongComment>?) -> Unit, fail: (() -> Unit?)? = null) {
        if (baseMusicInfoInfo.type == null || baseMusicInfoInfo.mid == null) {
            fail?.invoke()
            return
        }
        val observable = MusicApiServiceImpl.getMusicComment(baseMusicInfoInfo.type!!, baseMusicInfoInfo.mid!!)
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
    fun loadSongDetailInfo(vendor: String, mid: String, success: ((BaseMusicInfo?) -> Unit)?) {
        val observable = MusicApiServiceImpl.getSongDetail(vendor, mid)
        ApiManager.request(observable, object : RequestCallBack<BaseMusicInfo> {
            override fun success(result: BaseMusicInfo) {
                success?.invoke(result)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }
}