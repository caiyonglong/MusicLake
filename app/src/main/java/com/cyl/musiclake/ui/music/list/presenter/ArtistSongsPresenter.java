package com.cyl.musiclake.ui.music.list.presenter;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.list.contract.ArtistSongContract;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class ArtistSongsPresenter extends BasePresenter<ArtistSongContract.View> implements ArtistSongContract.Presenter {

    @Inject
    public ArtistSongsPresenter() {
    }
    @Override
    public void loadSongs(String artistName) {
        mView.showLoading();
        AppRepository.getArtistSongsRepository(MusicApp.getAppContext(), artistName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> songs) {
                        mView.showSongs(songs);
                        if (songs.size() == 0) {
                            mView.showEmptyView();
                        }
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

        CoverLoader.loadImageViewByDouban(MusicApp.getAppContext(), artistName, null, bitmap -> {
            mView.showAlbumArt(bitmap);
        });
    }

}
