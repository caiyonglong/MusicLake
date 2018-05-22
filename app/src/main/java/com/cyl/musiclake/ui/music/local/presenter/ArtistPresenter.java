package com.cyl.musiclake.ui.music.local.presenter;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.local.contract.ArtistContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/10.
 */

public class ArtistPresenter extends BasePresenter<ArtistContract.View> implements ArtistContract.Presenter {

    @Inject
    public ArtistPresenter() {
    }

    @Override
    public void loadArtists(String action) {
        mView.showLoading();
        AppRepository.getAllArtistsRepository(MusicApp.getAppContext())
                .subscribeOn(Schedulers.io())
                .compose(mView.bindToLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Artist> artistList) {
                        mView.showArtists(artistList);
                        if (artistList.size() == 0) {
                            mView.showEmptyView();
                        }
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
