package com.cyl.musiclake.api.kugou

import com.cyl.musicapi.kugou.Candidates
import com.cyl.musicapi.kugou.KuGouApiService
import com.cyl.musicapi.kugou.KugouLyric
import com.cyl.musicapi.kugou.KugouLyricInfo
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.utils.FileUtils
import com.cyl.musiclake.utils.LyricUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by master on 2018/4/30.
 */

object KuGouApiServiceImpl {

    private val TAG = "KuGouApiServiceImpl"

    val apiService by lazy { ApiManager.getInstance().create(KuGouApiService::class.java, Constants.BASE_KUGOU_URL) }

    /**
     * 搜索歌词
     */
    fun searchLyric(title: String, duration: Long): Observable<KugouLyric> {
        return apiService.searchLyric(title, duration.toString())
    }

    /**
     * 获取歌词
     */
    fun getKugouLyricInfo(candidates: Candidates?): Observable<KugouLyricInfo>? {
        if (candidates?.id == null) {
            return null
        }
        return apiService.getRawLyric(candidates.id, candidates.accesskey)
    }

    /**
     * 获取歌词
     */
    fun getKugouLyric(music: Music): Observable<String>? {
        val mLyricPath = FileUtils.getLrcDir() + music.title + "-" + music.artist + ".lrc"
        //网络歌词
        return if (FileUtils.exists(mLyricPath)) {
            MusicApi.getLocalLyricInfo(mLyricPath)
        } else {
            searchLyric(music.title!!, music.duration).flatMap {
                it.candidates?.get(0)?.let {
                    getKugouLyricInfo(it)?.flatMap {
                        Observable.create(ObservableOnSubscribe<String> { result ->
                            if (it.status == 200) {
                                val tt = LyricUtil.decryptBASE64(it.content)
                                result.onNext(tt)
                                result.onComplete()
                            } else {
                                result.onError(Throwable())
                            }
                        })


                    }
                }
            }
        }
    }
}
