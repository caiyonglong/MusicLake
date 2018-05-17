package com.cyl.musiclake.ui.music.online.presenter;

import android.content.Context;

import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.api.baidu.BaiduMusicList;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class OnlinePlaylistPresenter implements OnlinePlaylistContract.Presenter {

    private OnlinePlaylistContract.View mView;
    private Context mContext;

    public OnlinePlaylistPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(OnlinePlaylistContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadOnlinePlaylist() {
        mView.showLoading();

        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_CATEGORY);
        BaiduApiServiceImpl.getOnlinePlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaiduMusicList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaiduMusicList result) {
                        List<BaiduMusicList.Billboard> mBillboards = result.getContent();
                        if (mBillboards != null && mBillboards.size() > 3) {
                            //移除T榜
                            mBillboards.remove(3);
                        }
                        mView.showOnlineSongs(mBillboards);
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
