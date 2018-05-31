package com.cyl.musiclake.ui.music.local.presenter;

import android.content.Context;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.local.contract.AlbumDetailContract;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class AlbumDetailPresenter extends BasePresenter<AlbumDetailContract.View> implements AlbumDetailContract.Presenter {

    @Inject
    public AlbumDetailPresenter() {
    }

    @Override
    public void loadAlbumSongs(String albumName) {
        AppRepository.getAlbumSongsRepository(MusicApp.getAppContext(), albumName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> songs) {
                        mView.showAlbumSongs(songs);
                        if (songs.size() == 0) {
                            mView.showEmptyView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
        CoverLoader.loadImageViewByDouban(MusicApp.getAppContext(), albumName, null, resource -> {
            mView.showAlbumArt(resource);
        });
    }

}
