package com.cyl.musiclake.ui.music.playlist.history

import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.data.AppRepository

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by yonglong on 2018/1/7.
 */

class RecentlyPresenter @Inject
constructor() : BasePresenter<RecentlyContract.View>(), RecentlyContract.Presenter {

    override fun loadSongs() {
        mView.showLoading()
        AppRepository.getPlayHistoryRepository()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(object : Observer<List<Music>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(songs: List<Music>) {
                        mView.showSongs(songs)
                        if (songs.isEmpty()) {
                            mView?.showEmptyView()
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView?.hideLoading()
                    }

                    override fun onComplete() {
                        mView?.hideLoading()
                    }
                })
    }

    override fun clearHistory() {

    }
}
