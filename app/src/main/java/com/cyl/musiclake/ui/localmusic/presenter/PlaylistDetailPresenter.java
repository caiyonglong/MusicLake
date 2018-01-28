package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.ui.localmusic.contract.PlaylistDetailContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class PlaylistDetailPresenter implements PlaylistDetailContract.Presenter {
    private PlaylistDetailContract.View mView;
    private Context mContext;

    public PlaylistDetailPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(PlaylistDetailContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadPlaylistSongs(String playlistID) {
        AppRepository.getPlaylistSongsRepository(mContext, playlistID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        if (musicList.size() == 0) {
                            mView.showEmptyView();
                        }
                        mView.showPlaylistSongs(musicList);
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
    public void loadPlaylistArt(String playlistID) {
        AppRepository.getPlaylistSongsRepository(mContext, playlistID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        String url = null;
                        for (int i = 0; i < musicList.size(); i++) {
                            url = musicList.get(i).getCoverUri();
                            if (url != null)
                                break;
                        }
                        GlideApp.with(mContext)
                                .load(url)
                                .placeholder(R.drawable.default_cover)
                                .error(R.drawable.default_cover)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        mView.showPlaylistArt(resource);
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
