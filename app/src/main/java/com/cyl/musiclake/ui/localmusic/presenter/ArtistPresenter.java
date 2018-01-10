package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.data.model.Artist;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.ui.localmusic.contract.ArtistContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/10.
 */

public class ArtistPresenter implements ArtistContract.Presenter {
    private ArtistContract.View mView;
    private Context mContext;

    public ArtistPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(ArtistContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadArtists(String action) {
        mView.showLoading();
        AppRepository.getAllArtistsRepository(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Artist> artistList) {
                        mView.showArtists(artistList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showEmptyView();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
}
