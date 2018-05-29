package com.cyl.musiclake.ui.music.online.presenter;

import android.content.Context;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musiclake.api.MusicApiServiceImpl;
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.utils.LogUtil;

import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.music.online.contract.NeteaseListContract;
import com.cyl.musiclake.utils.ToastUtils;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class NeteaseListPresenter extends BasePresenter<NeteaseListContract.View> implements NeteaseListContract.Presenter {

    @Inject
    public NeteaseListPresenter() {
    }

    @Override
    public void loadNeteaseMusicList(int idx) {
//        MusicApi.getTopList(idx)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<NeteaseList>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(NeteaseList result) {
//                        LogUtil.e("net_play", result.toString());
//                        mView.showTopList(result);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        mView.hideLoading();
//                        mView.showErrorInfo(e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mView.hideLoading();
//                    }
//                });
    }

    @Override
    public void getMusicInfo(NeteaseMusic neteaseMusic) {

    }

}
