package com.cyl.musiclake.ui.music.online.presenter;

import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.api.baidu.BaiduMusicList;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class OnlinePlaylistPresenter extends BasePresenter<OnlinePlaylistContract.View> implements OnlinePlaylistContract.Presenter {

    @Inject
    public OnlinePlaylistPresenter() {
    }

    @Override
    public void loadOnlinePlaylist() {
        mView.showLoading();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_CATEGORY);
        BaiduApiServiceImpl.getOnlinePlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
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
