package com.cyl.musiclake.ui.music.search;

import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.MusicApiServiceImpl;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/6.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    @Inject
    public SearchPresenter() {
    }

    @Override
    public void search(String key, int limit, int page) {
        mView.showLoading();
        ApiManager.request(MusicApiServiceImpl.INSTANCE.searchMusic(key, limit, page),
                new RequestCallBack<List<Music>>() {
                    @Override
                    public void success(List<Music> result) {
                        mView.showSearchResult(result);
                        mView.hideLoading();
                    }

                    @Override
                    public void error(String msg) {
                        mView.showEmptyView();
                        mView.hideLoading();
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
