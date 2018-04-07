package com.cyl.musiclake.ui.music.online.presenter;

import android.content.Context;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.musicApi.MusicApiServiceImpl;
import com.cyl.musiclake.ui.music.online.contract.SearchContract;
import com.cyl.musiclake.utils.LogUtil;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/6.
 */

public class SearchPresenter implements SearchContract.Presenter {
    SearchContract.View mView;
    Context mContext;

    @Override
    public void attachView(SearchContract.View view) {
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
    public void search(String key, int limit, int page) {
        mView.showLoading();
        MusicApi.searchMusic(key, limit, page)
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> results) {
                        mView.showSearchResult(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showEmptyView();
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void collectMusic(String pid, Music music) {
        MusicApiServiceImpl.collectMusic(pid, music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(String status) {
                        LogUtil.e("add", status);
                        if (status.equals("true")) {
                            mView.showCollectStatus(true, "收藏成功");
                            RxBus.getInstance().post(new PlaylistEvent());
                        } else if (status.equals("false")) {
                            mView.showCollectStatus(false, "歌曲已存在");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void addPlaylist(Music music) {
        MusicApiServiceImpl.getPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Playlist>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(List<Playlist> playlists) {
                        mView.showAddPlaylistDialog(playlists, music);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showAddPlaylistDialog(null, null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getMusicInfo(int type, Music music) {
        if (music.getType() == Music.Type.QQ || music.getType() == Music.Type.NETEASE) {
            MusicApi.getMusicInfo(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Music>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Music music) {
                            mView.showMusicInfo(type, music);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mView.showMusicInfo(type, music);
        }
    }


}
