package com.cyl.musiclake.ui.music.presenter;

import android.content.Context;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.api.QQApiModel;
import com.cyl.musiclake.api.QQApiModel.DataBean.SongBean.ListBean;
import com.cyl.musiclake.ui.music.contract.SearchContract;
import com.cyl.musiclake.ui.music.model.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> params = new HashMap<>();
        params.put("p", page); //page
        params.put("n", limit);//limit
        params.put("w", key);// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");

        ApiManager.getInstance().apiService
                .searchByQQ("http://c.y.qq.com/soso/fcgi-bin/search_cp?", params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QQApiModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(QQApiModel qqApiModel) {
                        List<Music> results = new ArrayList<>();
                        List<ListBean> data = qqApiModel.getData().getSong().getList();
                        for (int i = 0; i < data.size(); i++) {
                            ListBean item = data.get(i);
                            Music music = new Music();
//                            music.setId(item.getSongmid());
                            music.setAlbum(item.getAlbumname());
                            music.setAlbumId(item.getAlbumid());
                            String url = "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + item.getAlbummid() + ".jpg";
                            music.setCoverUri(url);
                            music.setArtist(item.getSinger().get(0).getName());
                            results.add(music);
                        }

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
}
