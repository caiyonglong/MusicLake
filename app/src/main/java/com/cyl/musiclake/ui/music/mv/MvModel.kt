package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.*
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack

/**
 * Des    :
 * Author : master.
 * Date   : 2018/7/8 .
 */

class MvModel {
    /**
     * 加载mv详情
     */
    fun loadMvDetail(mvid: String?, result: RequestCallBack<MvDetailInfo>?) {
        val observable = mvid?.let { NeteaseApiServiceImpl.getMvDetailInfo(it) } ?: return
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
     * 加载推荐mv
     */
    fun loadMv(offset: Int, result: RequestCallBack<MvInfo>?) {
        val observable = NeteaseApiServiceImpl.getTopMv(50, offset)
        ApiManager.request(observable, result)
    }

    /**
     * 获取相似mv
     */
    fun loadSimilarMv(mvid: String?, result: RequestCallBack<SimilarMvInfo>?) {
        val observable = mvid?.let { NeteaseApiServiceImpl.getSimilarMv(it) } ?: return
        ApiManager.request(observable, result)
    }

    /**
     *获取mv数据
     */
    fun loadMvComment(mvid: String?, result: RequestCallBack<MvComment>?) {
        val observable = mvid?.let { NeteaseApiServiceImpl.getMvComment(it) } ?: return
        ApiManager.request(observable, result)
    }
}