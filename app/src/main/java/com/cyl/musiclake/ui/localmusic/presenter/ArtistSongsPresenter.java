package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.ui.localmusic.contract.ArtistSongContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class ArtistSongsPresenter implements ArtistSongContract.Presenter {
    private ArtistSongContract.View mView;
    private Context mContext;

    public ArtistSongsPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(ArtistSongContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void loadSongs(String artistName) {
        mView.showLoading();
        AppRepository.getArtistSongsRepository(mContext, artistName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> songs) {
                        mView.showSongs(songs);
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
    }

}
