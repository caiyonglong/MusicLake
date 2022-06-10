package com.cyl.musiclake.ui.music.local.presenter

import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.ui.music.local.contract.SongsContract
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by yonglong on 2018/1/7.
 */

class SongsPresenter @Inject
constructor() : BasePresenter<SongsContract.View>(), SongsContract.Presenter {

    override fun loadSongs(isReload: Boolean) {
        mView?.showLoading()
        GlobalScope.launch {
            val data = async {
                SongLoader.getLocalMusic(MusicApp.getAppContext(), isReload)
            }.await()
            //主线程更新
            withContext(Dispatchers.Main) {
                mView?.hideLoading()
                mView?.showSongs(data)
                if (data.size == 0) {
                    mView?.setEmptyView()
                }
            }
        }
    }
}
