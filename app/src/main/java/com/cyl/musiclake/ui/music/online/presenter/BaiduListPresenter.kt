package com.cyl.musiclake.ui.music.online.presenter

import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.music.online.contract.BaiduListContract

import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class BaiduListPresenter @Inject
constructor() : BasePresenter<BaiduListContract.View>(), BaiduListContract.Presenter {

    override fun loadOnlineMusicList(type: String, limit: Int, mOffset: Int) {
        ApiManager.request(BaiduApiServiceImpl.getOnlineSongs(type, limit, mOffset), object : RequestCallBack<MutableList<Music>> {
            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showErrorInfo(msg)
            }

            override fun success(result: MutableList<Music>?) {
                result?.forEach {
                    if (it.isCp)
                        result.remove(it)
                }
                mView?.showOnlineMusicList(result)
                mView?.hideLoading()
            }

        })
    }
}
