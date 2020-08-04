package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.*
import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.bean.VideoInfoBean
import io.reactivex.Observable

/**
 * Des    :
 * Author : master.
 * Date   : 2018/7/8 .
 */

class VideoLoadModel {
    /**
     * 加载mv Url
     */
    fun loadMvUrl(type: Int, mvid: String?, result: RequestCallBack<String>?) {
        val observable = mvid?.let { NeteaseApiServiceImpl.getVideoUrlInfo(type, mvid) } ?: return
        ApiManager.request(observable, result)
    }

    /**
     * 加载mv详情
     */
    fun loadMvDetail(mvid: String?, type: Int, result: RequestCallBack<VideoInfoBean>?) {
        val observable = if (type == 2) {
            mvid?.let { NeteaseApiServiceImpl.getMvDetailInfo(it) }
        } else {
            mvid?.let { NeteaseApiServiceImpl.getVideoDetailInfo(it) }
        }
        ApiManager.request(observable, result)
    }

    /**
     *
     * 加载最新mv
     */
    fun loadRecentMv(limit: Int = 30, result: RequestCallBack<MvInfo>?) {
        val observable = NeteaseApiServiceImpl.getNewestMv(limit)
        ApiManager.request(observable, result)
    }

    /**
     *
     * 加载排行榜mv
     */
    fun loadMv(offset: Int, result: RequestCallBack<MvInfo>?) {
        val observable = NeteaseApiServiceImpl.getTopMv(50, offset)
        ApiManager.request(observable, result)
    }

    /**
     *
     * 加载推荐mv
     */
    fun loadPersonalizedMv(result: RequestCallBack<MvInfo>?) {
        val observable = NeteaseApiServiceImpl.personalizedMv()
        ApiManager.request(observable, result)
    }

    /**
     * 获取相似mv
     */
    fun loadSimilarMv(mvid: String?, type: Int, result: RequestCallBack<MutableList<VideoInfoBean>>?) {
        val observable = if (type == 2) {
            mvid?.let { NeteaseApiServiceImpl.getSimilarMv(it) }
        } else {
            mvid?.let { NeteaseApiServiceImpl.getRelatedVideoList(it) }
        }
        observable?.let {
            ApiManager.request(it, result)
        }
    }

    /**
     *获取mv数据
     */
    fun loadMvComment(mvid: String?, type: String, offset: Int = 0, result: RequestCallBack<MvComment>?) {
        val observable = mvid?.let { NeteaseApiServiceImpl.getMvComment(it, type, offset) }
                ?: return
        ApiManager.request(observable, result)
    }

    /**
     *搜索mv数据
     */
    fun searchMv(key: String, offset: Int, result: RequestCallBack<SearchInfo>?) {
        val observable = NeteaseApiServiceImpl.searchMoreInfo(key, 30, offset, 1004)
        ApiManager.request(observable, result)
    }

    fun loadBaiduMv(songId: String?, result: RequestCallBack<MvInfoBean>?) {
        val observable = BaiduApiServiceImpl.getMvInfo(songId)
        ApiManager.request(observable, result)
    }
}