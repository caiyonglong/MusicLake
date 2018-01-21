package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.ui.localmusic.contract.FolderSongsContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/8.
 */

public class FolderSongPresenter implements FolderSongsContract.Presenter {

    private Context mContext;
    private FolderSongsContract.View mView;

    public FolderSongPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(FolderSongsContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void loadSongs(String path) {
        AppRepository.getFolderSongsRepository(mContext,path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Exception {
                        if (musicList.size()==0)
                            mView.showEmptyView();
                        mView.showSongs(musicList);
                    }
                });
    }
}
