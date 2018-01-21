package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.data.source.PlaylistLoader;
import com.cyl.musiclake.ui.localmusic.contract.MyMusicContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/6.
 */

public class MyMusicPresenter implements MyMusicContract.Presenter {
    private MyMusicContract.View mView;
    private Context mContext;
    private List<Playlist> playlists;
    private List<Music> musicList;

    public MyMusicPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(MyMusicContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadSongs() {
        AppRepository.getAllSongsRepository(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showSongs(musicList);
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

        AppRepository.getFavoriteSong(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showLoveList(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        AppRepository.getPlaylistSongsRepository(mContext, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showHistory(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadPlaylist() {
        playlists = PlaylistLoader.getPlaylist(mContext);
        mView.showPlaylist(playlists);
        if (playlists.size() == 0) {
            mView.showEmptyView();
        }
    }
}
