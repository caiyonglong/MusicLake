package com.cyl.musiclake.ui.onlinemusic.presenter;

import android.app.Activity;
import android.content.Context;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.SongLoader;
import com.cyl.musiclake.data.source.download.TasksManager;
import com.cyl.musiclake.data.source.download.TasksManagerModel;
import com.cyl.musiclake.ui.onlinemusic.contract.DownloadContract;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author   : D22434
 * version  : 2018/1/24
 * function :
 */

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View mView;
    private Context mContext;
    private Activity activity;

    public DownloadPresenter(Context mContext) {
        this.mContext = mContext;
        this.activity = (Activity) mContext;
    }

    @Override
    public void attachView(DownloadContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        RxBus.getInstance().register(TasksManagerModel.class)
                .subscribe(taskChangedEvent -> {
//                    if (!activity.isFinishing()) {
                    loadDownloadMusic();
//                    }
                });
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadDownloadMusic() {
        LogUtil.e("loadDownloadMusic");
        Observable.create((ObservableOnSubscribe<List<Music>>) emitter -> {
            try {

                List<TasksManagerModel> managers = TasksManager.getImpl().getModelList();
                List<Music> musicList = new ArrayList<>();
                LogUtil.e("loadDownloadMusic" + managers.size());
                for (TasksManagerModel model : managers) {
                    Music music = SongLoader.getMusicInfo(mContext, model.getMid());
                    musicList.add(music);
                }
                emitter.onNext(musicList);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
