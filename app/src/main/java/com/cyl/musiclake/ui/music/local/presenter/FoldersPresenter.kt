package com.cyl.musiclake.ui.music.local.presenter

import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.FolderInfo
import com.cyl.musiclake.data.AppRepository
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.ui.music.local.contract.FoldersContract
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/8.
 */

class FoldersPresenter @Inject
constructor() : BasePresenter<FoldersContract.View>(), FoldersContract.Presenter {
    override fun loadSongs(path: String) {
        mView?.showLoading()
        doAsync {
            val musicList = SongLoader.getSongListInFolder(mView.context, path)
            uiThread {
                mView?.hideLoading()
                mView?.showSongs(musicList)
            }

        }
    }

    override fun loadFolders() {
        mView.showLoading()
        AppRepository.getFolderInfosRepository(mView.context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(object : Observer<List<FolderInfo>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(folderInfos: List<FolderInfo>) {
                        mView.showFolders(folderInfos)
                        if (folderInfos.isEmpty()) {
                            mView.showEmptyView()
                        }
                        mView.hideLoading()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView.hideLoading()
                        mView.showFolders(mutableListOf())
                    }

                    override fun onComplete() {
                        mView.hideLoading()
                    }
                })

    }

}
