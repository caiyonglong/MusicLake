package com.cyl.musiclake.ui.onlinemusic.presenter;

import android.content.Context;
import android.util.Log;

import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.onlinemusic.contract.OnlineMusicListContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class OnlineMusicListPresenter implements OnlineMusicListContract.Presenter {

    private OnlineMusicListContract.View mView;
    private Context mContext;

    public OnlineMusicListPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(OnlineMusicListContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadOnlineMusicList(String type, int limit, int mOffset) {
        mView.showLoading();

        BaiduApiServiceImpl.getOnlineSongs(type, limit, mOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showOnlineMusicList(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                        mView.showErrorInfo(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void playCurrentMusic(Music music) {
        mView.showLoading();
        BaiduApiServiceImpl.getTingSongInfo(music.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Music>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Music result) {
                        Log.e("baidu_play", result.toString());
                        PlayManager.playOnline(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                        mView.showErrorInfo(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
}
