package com.cyl.musiclake.ui.map;


import android.content.Context;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.api.ApiModel;
import com.cyl.musiclake.ui.login.UserModel;
import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.ui.map.location.Location;
import com.cyl.musiclake.ui.common.Constants;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/3.
 */

public class NearPresenter implements NearContract.Presenter {
    private NearContract.View mView;
    private Context mContext;
    private UserModel userModel;

    @Override
    public void attachView(NearContract.View view) {
        mView = view;
        mContext = (Context) view;
        userModel = new UserModel(mContext);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getNearPeopleInfo(Map<String, String> params) {
        User user = userModel.getUserInfo();
        if (user != null) {

            ApiManager.getInstance().apiService
                    .getNearPeopleInfo(Constants.DEFAULT_URL, params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiModel<List<Location>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ApiModel<List<Location>> info) {
                            if (info.getStatus().equals("success")) {
                                //保存用户信息
                                mView.showLocations(info.getData());
                            } else {
                                mView.showErrorInfo(info.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.hideLoading();
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                            mView.updateView();
                        }
                    });


//            String song = "";
//            if (PlayManager.isPlaying()) {
//                Music music = PlayManager.getPlayingMusic();
//                song = ConvertUtils.getArtistAndAlbum(music.getArtist(), music.getTitle());
//            }
//            OkHttpUtils.post()//
//                    .url(Constants.DEFAULT_URL)//
//                    .addParams(Constants.FUNC, Constants.SONG_ADD)//
//                    .addParams(Constants.USER_ID, user_id)//
//                    .addParams(Constants.SONG, song)//
//                    .build()//
//                    .execute(new NearCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            Log.e("ccc", ":aa");
//                            isRequest = false;
//                            dissmissProgressDialog();
//                        }
//
//                        @Override
//                        public void onResponse(LocationInfo response) {
//                            dissmissProgressDialog();
//                            isRequest = false;
//                            mydatas = response.getData();
//                            Log.e("ccc", ":bb" + mydatas.size());
//
//                            adapter = new ShakeActivity.MyLocationAdapter(getApplicationContext(), mydatas);
//                            shake_result.setAdapter(adapter);
//                        }
//                    });


        }
    }

}
