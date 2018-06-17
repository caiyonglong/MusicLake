package com.cyl.musiclake.ui.music.local.presenter;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.local.contract.FolderSongsContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/8.
 */

public class FolderSongPresenter extends BasePresenter<FolderSongsContract.View> implements FolderSongsContract.Presenter {

    @Inject
    public FolderSongPresenter() {
    }

    @Override
    public void loadSongs(String path) {
        AppRepository.INSTANCE.getFolderSongsRepository(mView.getContext(), path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        if (musicList.size() == 0)
                            mView.showEmptyView();
                        mView.showSongs(musicList);
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
