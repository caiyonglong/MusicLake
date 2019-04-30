//package com.cyl.musiclake.ui.music.online.presenter;
//
//import com.cyl.musiclake.ui.base.BasePresenter;
//
//import com.cyl.musiclake.api.music.netease.NeteaseMusic;
//import com.cyl.musiclake.ui.music.online.contract.NeteaseListContract;
//
//import javax.inject.Inject;
//
///**
// * Created by D22434 on 2018/1/4.
// */
//
//public class NeteaseListPresenter extends BasePresenter<NeteaseListContract.View> implements NeteaseListContract.Presenter {
//
//    @Inject
//    public NeteaseListPresenter() {
//    }
//
//    @Override
//    public void loadNeteaseMusicList(int idx) {
////        MusicApi.getTopList(idx)
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Observer<NeteaseList>() {
////                    @Override
////                    public void onSubscribe(Disposable d) {
////                    }
////
////                    @Override
////                    public void onNext(NeteaseList result) {
////                        LogUtil.e("net_play", result.toString());
////                        mView.showTopList(result);
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////                        e.printStackTrace();
////                        mView.hideLoading();
////                        mView.showErrorInfo(e.getMessage());
////                    }
////
////                    @Override
////                    public void onComplete() {
////                        mView.hideLoading();
////                    }
////                });
//    }
//
//    @Override
//    public void getMusicInfo(NeteaseMusic neteaseMusic) {
//
//    }
//
//}
