package com.cyl.musiclake.ui.music.list.presenter;

import android.content.Context;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.list.contract.RecentlyContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/7.
 */

public class RecentlyPresenter implements RecentlyContract.Presenter {
    private RecentlyContract.View mView;
    private Context mContext;

    public RecentlyPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(RecentlyContract.View view) {
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
        mView.showLoading();
        AppRepository.getPlayHistoryRepository(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
    }

    @Override
    public void clearHistory() {

    }
}
