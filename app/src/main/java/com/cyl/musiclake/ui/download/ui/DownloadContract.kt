package com.cyl.musiclake.ui.download.ui


import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.ui.download.TasksManagerModel

interface DownloadContract {

    interface View : BaseContract.BaseView {
        fun showErrorInfo(msg: String)

        fun showSongs(musicList: List<Music>)

        fun showDownloadList(modelList: List<TasksManagerModel>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadDownloadMusic(isCache: Boolean)

        fun loadDownloading()
    }
}
