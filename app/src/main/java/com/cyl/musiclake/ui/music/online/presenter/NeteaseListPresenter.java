package com.cyl.musiclake.ui.music.online.presenter;

import android.content.Context;
import android.util.Log;

import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.music.online.contract.NeteaseListContract;
import com.cyl.musiclake.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class NeteaseListPresenter implements NeteaseListContract.Presenter {

    private NeteaseListContract.View mView;
    private Context mContext;

    public NeteaseListPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(NeteaseListContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void loadNeteaseMusicList(int idx) {
        NeteaseApiServiceImpl.getTopList(idx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeteaseList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NeteaseList result) {
                        Log.e("net_play", result.toString());
                        mView.showTopList(result);
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
    public void playCurrentMusic(NeteaseMusic neteaseMusic) {
        mView.showLoading();
        NeteaseApiServiceImpl.getMusicUrl(neteaseMusic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Music>() {

                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Music music) {
                        if (music.getUri() != null) {
                            mView.playNeteaseMusic(music);
                        } else {
                            ToastUtils.show("播放地址异常");
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
}
