package com.cyl.musiclake.ui.music.online.base;

import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.ui.music.online.contract.NeteaseListContract;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class PlaylistPresenter extends BasePresenter<PlaylistContract.View> implements PlaylistContract.Presenter {

    @Inject
    public PlaylistPresenter() {
    }

    @Override
    public void getPlaylist(int idx, String type) {
        MusicApi.getTopList(idx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Playlist>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Playlist result) {
                        mView.showPlayList(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                        mView.showErrorInfo(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getMusicInfo(Music music) {

    }
}
