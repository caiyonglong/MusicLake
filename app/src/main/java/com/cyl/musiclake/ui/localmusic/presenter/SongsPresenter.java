package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.localmusic.contract.SongsContract;

import java.util.List;

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
        mView.showLoading();
        if (action.equals("love")) {
            AppRepository.getFavoriteSong(mContext)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Music>>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<Music> musicList) {
                            if (musicList.size() == 0) {
                                mView.setEmptyView();
                            }
                            mView.showSongs(musicList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.hideLoading();
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        } else {
            AppRepository.getAllSongsRepository(mContext)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Music>>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<Music> musicList) {
                            if (musicList.size() == 0) {
                                mView.setEmptyView();
                            }
                            mView.showSongs(musicList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.hideLoading();
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }

    }

    @Override
    public void playMusic(List<Music> playlist, int position) {
        PlayManager.setPlayList(playlist);
        PlayManager.play(position);
    }
}
