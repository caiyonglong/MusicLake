package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.ui.localmusic.contract.AlbumDetailContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class ArtistDetailPresenter implements AlbumDetailContract.Presenter {
    private AlbumDetailContract.View mView;
    private Context mContext;

    public ArtistDetailPresenter(Context mContext) {
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
    public void subscribe(long albumID) {

    }

    @Override
    public void loadAlbumSongs(long albumID) {
        AppRepository.getArtistSongsRepository(mContext, albumID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> songs) {
                        mView.showAlbumSongs(songs);
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

    @Override
    public void loadAlbumArt(long albumID) {

    }

    @Override
    public void loadArtistSongs(long artistID) {

    }
}
