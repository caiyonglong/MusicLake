package com.cyl.musiclake.ui.music.presenter;

import android.content.Context;

import com.cyl.musiclake.ui.music.contract.SongsContract;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.model.data.MusicLoader;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class SongsPresenter implements SongsContract.Presenter {
    private SongsContract.View mView;
    private Context mContext;

    public SongsPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(SongsContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadSongs(String action) {
//        mView.showLoading();
//        List<Music> data = MusicLoader.getAllSongs(mContext);
//        mView.showSongs(data);
//        mView.hideLoading();
        mView.showLoading();
        Observable.create(e -> {
            List<Music> data = MusicLoader.getAllSongs(mContext);
            e.onNext(data);
            e.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        mView.showSongs((List<Music>) o);
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
