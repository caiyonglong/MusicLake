package com.cyl.musiclake.ui.download.ui


import com.cyl.musiclake.ui.base.BaseContract
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.ui.download.TasksManagerModel

interface DownloadContract {

    interface View : BaseContract.BaseView {
        fun showErrorInfo(msg: String)

        fun showSongs(baseMusicInfoInfoList: List<BaseMusicInfo>)

        fun showDownloadList(modelList: List<TasksManagerModel>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadDownloadMusic(isCache: Boolean)

        fun loadDownloading()
    }
}
