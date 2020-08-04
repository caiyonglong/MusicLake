package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.MvComment
import com.cyl.musicapi.netease.MvDetailInfo
import com.cyl.musicapi.netease.MvInfo
import com.cyl.musicapi.netease.SimilarMvInfo
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.bean.VideoInfoBean
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class VideoDetailPresenter @Inject
constructor() : BasePresenter<VideoDetailContract.View>(), VideoDetailContract.Presenter {

    private val mvModel = VideoLoadModel()

    override fun loadBaiduMvInfo(songId: String?) {
        mvModel.loadBaiduMv(songId, object : RequestCallBack<MvInfoBean> {
            override fun success(result: MvInfoBean?) {
                mView?.hideLoading()
                mView?.showBaiduMvDetailInfo(result)
            }

            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }

        })
    }

    fun loadMvUrl(type: Int, mvid: String?) {
        mvModel.loadMvUrl(type, mvid, object : RequestCallBack<String> {
            override fun success(result: String?) {
                mView?.showMvUrlInfo(result)
            }

            override fun error(msg: String?) {

            }
        })
    }

    override fun loadMvDetail(mvid: String?, type: Int) {
        mvModel.loadMvDetail(mvid, type,object : RequestCallBack<VideoInfoBean> {
            override fun success(result: VideoInfoBean?) {
                mView?.hideLoading()
                result?.let {
                    mView?.showMvDetailInfo(it)
                }
            }

            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }
        })
    }

    override fun loadMv(offset: Int) {
        mvModel.loadMv(offset, object : RequestCallBack<MvInfo> {
            override fun success(result: MvInfo?) {
                result?.data?.let {
//                    mView?.showVideoInfoList(it)
                }
            }

            override fun error(msg: String?) {
            }

        })
    }

    override fun loadSimilarMv(mvid: String?, type: Int) {
        mvModel.loadSimilarMv(mvid, type, object : RequestCallBack<MutableList<VideoInfoBean>> {
            override fun success(result: MutableList<VideoInfoBean>) {
                mView?.showVideoInfoList(result)
            }

            override fun error(msg: String?) {
            }

        })
    }

    override fun loadMvComment(mvid: String?, type: Int, offset: Int) {
        val videoType = if (type == 2) "mv" else "video"
        mvModel.loadMvComment(mvid, videoType, offset, object : RequestCallBack<MvComment> {
            override fun success(result: MvComment?) {
                result?.hotComments?.let {
                    if (it.size > 0) {
                        mView?.showMvHotComment(it)
                    }
                }
                result?.comments?.let {
                    mView?.showMvComment(it)
                }
            }

            override fun error(msg: String?) {
            }

        })
    }
}
