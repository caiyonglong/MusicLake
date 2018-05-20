package com.cyl.musiclake.ui.music.online.presenter;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.data.download.TasksManagerModel;
import com.cyl.musiclake.ui.music.online.contract.DownloadContract;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.LogUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author   : D22434
 * version  : 2018/1/24
 * function :
 */

public class DownloadPresenter extends BasePresenter<DownloadContract.View> implements DownloadContract.Presenter {
    @Inject
    public DownloadPresenter() {

    }

    @Override
    public void attachView(DownloadContract.View view) {
        super.attachView(view);

        RxBus.getInstance().register(TasksManagerModel.class)
                .subscribe(taskChangedEvent -> {
//                    if (!activity.isFinishing()) {
                    loadDownloadMusic();
//                    }
                });
    }

    @Override
    public void loadDownloadMusic() {
        mView.showLoading();
        LogUtil.e("loadDownloadMusic");
        AppRepository.getFolderSongsRepository(mView.getContext(), FileUtils.getMusicDir()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showSongs(musicList);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

}
