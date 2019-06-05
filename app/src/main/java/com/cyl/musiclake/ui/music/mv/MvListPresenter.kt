package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.MvInfo
import com.cyl.musicapi.netease.SearchInfo
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.api.net.RequestCallBack
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class MvListPresenter @Inject
constructor() : BasePresenter<MvListContract.View>(), MvListContract.Presenter {
    private val mvModel = MvModel()
    override fun loadPersonalizedMv() {
        mView?.showLoading()
        mvModel.loadPersonalizedMv(object : RequestCallBack<MvInfo> {
            override fun success(result: MvInfo?) {
                result?.data?.let {
                    mView?.hideLoading()
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }

        })
    }

    override fun loadMv(offset: Int) {
        mView?.showLoading()
        mvModel.loadMv(offset, object : RequestCallBack<MvInfo> {
            override fun success(result: MvInfo?) {
                result?.data?.let {
                    mView?.hideLoading()
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }

        })
    }

    override fun loadRecentMv(limit: Int) {
        mView?.showLoading()
        mvModel.loadRecentMv(limit, object : RequestCallBack<MvInfo> {
            override fun success(result: MvInfo?) {
                result?.data?.let {
                    mView?.hideLoading()
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }

        })
    }

    override fun searchMv(key: String, offset: Int) {
        mView?.showLoading()
        mvModel.searchMv(key, offset, object : RequestCallBack<SearchInfo> {
            override fun success(result: SearchInfo?) {
                result?.result?.mvs?.let {
                    mView?.hideLoading()
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }

        })
    }

}
