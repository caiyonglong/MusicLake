package com.cyl.musiclake.ui.onlinemusic.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cyl.musiclake.ui.download.DownloadListener;
import com.cyl.musiclake.ui.download.DownloadService;
import com.cyl.musiclake.ui.download.IDownloadService;
import com.cyl.musiclake.ui.download.db.DBDao;
import com.cyl.musiclake.ui.download.model.FileState;
import com.cyl.musiclake.ui.onlinemusic.contract.DownloadContract;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yonglong on 2018/1/7.
 */

public class DownloadPresenter implements DownloadContract.Presenter {
    private static final String TAG = "DownloadPresenter";
    private DownloadContract.View mView;
    private Context mContext;
    DownloadListener downloadListener;
    IDownloadService mService;

    public DownloadPresenter(Context mContext, DownloadListener downloadListener) {
        this.mContext = mContext;
        this.downloadListener = downloadListener;
        Intent intent = new Intent(mContext, DownloadService.class);
        ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = (IDownloadService) service;
                mService.addProgressCallBack(downloadListener);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ToastUtils.show(mContext, "下载服务停止");
            }
        };
        mContext.bindService(intent, mConnection, 0);
    }

    @Override
    public void attachView(DownloadContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void updateDownloadStatus(FileState status) {
        if (mService != null)
            mService.updateStatus(status.getMid(), status.getName(), status.getUrl());
    }

    @Override
    public void updateProgress(String url, int finished) {
        Observable.create((ObservableOnSubscribe<List<FileState>>) emitter -> {
            try {
                DBDao dbDao = new DBDao(mContext);
                List<FileState> fileStates = dbDao.getFileStates();
                emitter.onNext(fileStates);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FileState>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(List<FileState> fileStates) {
                        mView.updateProgress(fileStates);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtils.show(mContext, throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
