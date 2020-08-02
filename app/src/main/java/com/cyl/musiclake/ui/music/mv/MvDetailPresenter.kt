package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.MvComment
import com.cyl.musicapi.netease.MvDetailInfo
import com.cyl.musicapi.netease.MvInfo
import com.cyl.musicapi.netease.SimilarMvInfo
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.api.net.RequestCallBack
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class MvDetailPresenter @Inject
constructor() : BasePresenter<MvDetailContract.View>(), MvDetailContract.Presenter {
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

    private val mvModel = MvModel()


    fun loadMvUrl(type: Int, mvid: String?) {
        mvModel.loadMvUrl(type, mvid, object : RequestCallBack<String> {
            override fun success(result: String?) {
                mView?.showMvUrlInfo(result)
            }

            override fun error(msg: String?) {

            }
        })
    }

    override fun loadMvDetail(mvid: String?) {
        mvModel.loadMvDetail(mvid, object : RequestCallBack<MvDetailInfo> {
            override fun success(result: MvDetailInfo?) {
                mView?.hideLoading()
                result?.data?.let {
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
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
            }

        })
    }

    override fun loadSimilarMv(mvid: String?) {
        mvModel.loadSimilarMv(mvid, object : RequestCallBack<SimilarMvInfo> {
            override fun success(result: SimilarMvInfo?) {
                result?.data?.let {
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
            }

        })
    }

    override fun loadMvComment(mvid: String?, offset: Int) {
        mvModel.loadMvComment(mvid, offset, object : RequestCallBack<MvComment> {
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
