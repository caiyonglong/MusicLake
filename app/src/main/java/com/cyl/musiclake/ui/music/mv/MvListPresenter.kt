package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.MvInfo
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.net.RequestCallBack
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class MvListPresenter @Inject
constructor() : BasePresenter<MvListContract.View>(), MvListContract.Presenter {
    private val mvModel = MvModel()
    override fun loadMv(offset: Int) {
        mvModel.loadMv(offset, object : RequestCallBack<MvInfo> {
            override fun success(result: MvInfo?) {
                result?.data?.let {
                    mView?.hideLoading()
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
                mView?.showError(msg, true)
            }

        })
    }

    override fun loadRecentMv(limit: Int) {
        mvModel.loadRecentMv(limit, object : RequestCallBack<MvInfo> {
            override fun success(result: MvInfo?) {
                result?.data?.let {
                    mView?.hideLoading()
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
                mView?.showError(msg, true)
            }

        })
    }

}
