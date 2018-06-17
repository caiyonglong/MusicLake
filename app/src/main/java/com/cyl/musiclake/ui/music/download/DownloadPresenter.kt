package com.cyl.musiclake.ui.music.download

import com.cyl.musiclake.RxBus
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.DownloadLoader
import com.cyl.musiclake.data.download.TasksManagerModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Author   : D22434
 * version  : 2018/1/24
 * function :
 */

class DownloadPresenter @Inject
constructor() : BasePresenter<DownloadContract.View>(), DownloadContract.Presenter {

    override fun attachView(view: DownloadContract.View) {
        super.attachView(view)
        RxBus.getInstance().register(TasksManagerModel::class.java)
                .subscribe { taskChangedEvent ->
                    if (mView != null && mView.context != null) {
                        loadDownloadMusic()
                    }
                }
    }

    override fun loadDownloadMusic() {
        mView?.showLoading()
        doAsync {
            val data = DownloadLoader.getDownloadList()
            uiThread {
                mView?.showSongs(data)
            }
        }
    }

    override fun loadDownloading() {
        doAsync {
            val data = DownloadLoader.getDownloadingList()
            uiThread {
                mView?.showDownloadList(data)
            }
        }
    }

}
