package com.cyl.musiclake.ui.music.local.presenter;

import android.content.Context;

import com.cyl.musiclake.bean.FolderInfo;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.ui.music.local.contract.FoldersContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/8.
 */

public class FoldersPresenter implements FoldersContract.Presenter {

    private Context mContext;
    private FoldersContract.View mView;

    public FoldersPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(FoldersContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadFolders() {
        mView.showLoading();
        AppRepository.getFolderInfosRepository(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FolderInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<FolderInfo> folderInfos) {
                        mView.showFolders(folderInfos);
                        if (folderInfos.size() == 0) {
                            mView.showEmptyView();
                        }
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });

    }
}
