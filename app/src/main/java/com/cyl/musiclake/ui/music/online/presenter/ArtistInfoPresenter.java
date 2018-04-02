package com.cyl.musiclake.ui.music.online.presenter;

import android.content.Context;

import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.doupan.DoubanMusic;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.music.online.contract.ArtistInfoContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class ArtistInfoPresenter implements ArtistInfoContract.Presenter {

    private ArtistInfoContract.View mView;
    private Context mContext;

    @Override
    public void attachView(ArtistInfoContract.View view) {
        mView = view;
        mContext = (Context) view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadArtistInfo(Music music) {
        mView.showLoading();
        MusicApi.getMusicAlbumInfo(music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DoubanMusic>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DoubanMusic doubanMusic) {
                        if (doubanMusic.getCount() >= 1) {
                            mView.showMusicInfo(doubanMusic);
                        } else {
                            mView.showErrorInfo("暂无信息");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
