package com.cyl.musiclake.ui.music.list.presenter;

import android.content.Context;

import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.list.contract.AlbumsContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class AlbumPresenter implements AlbumsContract.Presenter {
    private AlbumsContract.View mView;
    private Context mContext;

    public AlbumPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(AlbumsContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadAlbums(String action) {
        mView.showLoading();
        AppRepository.getAllAlbumsRepository(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
