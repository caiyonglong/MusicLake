package com.cyl.musiclake.ui.music.local.presenter;

import android.content.Context;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.local.contract.SongsContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class SongsPresenter extends BasePresenter<SongsContract.View> implements SongsContract.Presenter {

    @Inject
    public SongsPresenter() {
    }

    @Override
    public void loadSongs(String action) {
        mView.showLoading();
        AppRepository.getAllSongsRepository(mView.getContext(), action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
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

    @Override
    public void playMusic(List<Music> playlist, int position) {
        PlayManager.setPlayList(playlist);
        PlayManager.play(position);
    }
}
