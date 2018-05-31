package com.cyl.musiclake.ui.music.search;

import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.MusicApiServiceImpl;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.SearchHistoryBean;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;
import com.cyl.musiclake.utils.LogUtil;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/6.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    @Inject
    public SearchPresenter() {
    }

    @Override
    public void search(String key, SearchEngine.Filter type, int limit, int page) {
        ApiManager.request(MusicApiServiceImpl.INSTANCE
                        .searchMusic(key, type, limit, page)
                        .compose(mView.bindToLife()),
                new RequestCallBack<List<Music>>() {
                    @Override
                    public void success(List<Music> result) {
                        mView.showSearchResult(result);
                        mView.hideLoading();
                    }

                    @Override
                    public void error(String msg) {
                        mView.showEmptyView();
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getSuggestions(String query) {
        DataSupport.select("title").where("title like ?", "%" + query + "%")
                .order("id desc").findAsync(SearchHistoryBean.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                mView.showSearchSuggestion((List<SearchHistoryBean>) t);
            }
        });
    }

    @Override
    public void saveQueryInfo(String query) {
        List<SearchHistoryBean> data = DataSupport.where("title = ?", query).find(SearchHistoryBean.class);
        if (data.size() == 0) {
            SearchHistoryBean historyBean = new SearchHistoryBean(System.currentTimeMillis(), query);
            if (historyBean.save()) {
                LogUtil.d("存储成功");
            } else {
                LogUtil.d("失败");
            }
        }
    }

    @Override
    public void getMusicInfo(int type, Music music) {
        if (music.getType() == Music.Type.QQ || music.getType() == Music.Type.NETEASE) {
            MusicApi.getMusicInfo(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Music>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Music music) {
                            mView.showMusicInfo(type, music);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mView.showMusicInfo(type, music);
        }
    }


}
