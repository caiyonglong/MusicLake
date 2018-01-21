package com.cyl.musiclake.ui.onlinemusic.presenter;

import android.content.Context;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.api.ApiModel;
import com.cyl.musiclake.ui.onlinemusic.contract.ArtistInfoContract;
import com.cyl.musiclake.api.baidu.OnlineArtistInfo;
import com.cyl.musiclake.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class ArtistInfoPresenter implements ArtistInfoContract.Presenter {

    private ArtistInfoContract.View mView;
    private Context mContext;

    @Override
    public void attachView(ArtistInfoContract.View view) {
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
    public void loadArtistInfo(String artistID) {
        mView.showLoading();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_ARTIST_INFO);
        params.put(Constants.PARAM_TING_UID, artistID);

        ApiManager.getInstance().apiService
                .getArtistInfo(Constants.BASE_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiModel<OnlineArtistInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ApiModel<OnlineArtistInfo> result) {
                        if (result.getStatus().equals("success")) {
                            mView.showArtistInfo(result.getData());
                        } else {
                            mView.showErrorInfo(result.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.showErrorInfo("请求数据失败");
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
}
