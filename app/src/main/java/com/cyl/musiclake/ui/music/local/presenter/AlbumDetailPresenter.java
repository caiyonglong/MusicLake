package com.cyl.musiclake.ui.music.local.presenter;

import android.content.Context;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.ui.music.local.contract.AlbumDetailContract;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class AlbumDetailPresenter implements AlbumDetailContract.Presenter {
    private AlbumDetailContract.View mView;
    private Context mContext;

    public AlbumDetailPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(AlbumDetailContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void subscribe(String albumID) {

    }

    @Override
    public void loadAlbumSongs(String albumName) {
        mView.showLoading();
        AppRepository.getAlbumSongsRepository(mContext, albumName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        CoverLoader.loadImageViewByDouban(mContext, albumName, null, resource -> {
            mView.showAlbumArt(resource);
        });
    }

    @Override
    public void loadAlbumArt(String albumID) {

    }
}
