package com.cyl.musiclake.ui.onlinemusic.presenter;

import android.content.Context;
import android.util.Log;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.api.qq.QQApiKey;
import com.cyl.musiclake.api.qq.QQApiServiceImpl;
import com.cyl.musiclake.api.xiami.XiamiServiceImpl;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.onlinemusic.contract.SearchContract;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/6.
 */

public class SearchPresenter implements SearchContract.Presenter {
    SearchContract.View mView;
    Context mContext;

    @Override
    public void attachView(SearchContract.View view) {
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
    public void search(String key, int limit, int page) {
        mView.showLoading();
        Map<String, String> params = new HashMap<>();
        params.put("p", page + ""); //page
        params.put("n", limit + "");//limit
        params.put("w", key);// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");
        StringBuilder tempParams = new StringBuilder();
        int pos = 0;
        for (String param : params.keySet()) {
            if (pos > 0) {
                tempParams.append("&");
            }
            try {
                tempParams.append(String.format("%s=%s", param, URLEncoder.encode(params.get(param), "utf-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            pos++;
        }
        String qqRequestUrl = "http://c.y.qq.com/soso/fcgi-bin/search_cp?" + tempParams.toString();
        String xiamiRequestUrl = "http://api.xiami.com/web?v='2.0'&key=" + key + "&limit=" + limit + "&page=" + page + "&r=search/songs&app_key=1";

        Observable.merge(QQApiServiceImpl.searchByQQ(qqRequestUrl), XiamiServiceImpl.search(xiamiRequestUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> results) {
                        mView.showSearchResult(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showEmptyView();
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void playQQMusic(Music music) {

        double guid = Math.floor(Math.random() * 1000000000);
        System.out.println(guid);
        String requestUrl = "https://c.y.qq.com/base/fcgi-bin/fcg_musicexpress.fcg?json=3&guid=" + guid + "&format=json";
        System.out.println(requestUrl);
        ApiManager.getInstance().apiService
                .getTokenKey(requestUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QQApiKey>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(QQApiKey qqApiKey) {
                        System.out.println(qqApiKey);
                        String perfix = "M500";
//                            if (sizekey.equals("size128")) {
//                                perfix = "M500";
//                            } else if (sizekey.equals("size320")) {
//                                perfix = "M800";
//                            }
                        String songUrl = "http://dl.stream.qqmusic.qq.com/" +
                                perfix +
                                music.getId() + ".mp3?vkey=" + qqApiKey.getKey() + "&guid=" + guid + "&fromtag=30";
                        Log.e("SongUri", songUrl);
                        music.setUri(songUrl);
                        PlayManager.playOnline(music);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
