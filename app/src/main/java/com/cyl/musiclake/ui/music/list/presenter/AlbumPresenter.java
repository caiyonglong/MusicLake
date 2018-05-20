package com.cyl.musiclake.ui.music.list.presenter;

import android.content.Context;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.list.contract.AlbumsContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class AlbumPresenter extends BasePresenter<AlbumsContract.View> implements AlbumsContract.Presenter {

    @Inject
    public AlbumPresenter() {
    }

    @Override
    public void loadAlbums(String action) {
        mView.showLoading();
        AppRepository.getAllAlbumsRepository(MusicApp.getAppContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(new Observer<List<Album>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Album> albums) {
                        mView.showAlbums(albums);
                        if (albums.size() == 0) {
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
    }
}
