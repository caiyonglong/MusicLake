package com.cyl.musiclake.ui.music.list.presenter;

import android.content.Context;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.FolderInfo;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.ui.music.list.contract.FoldersContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/8.
 */

public class FoldersPresenter extends BasePresenter<FoldersContract.View> implements FoldersContract.Presenter {

    @Inject
    public FoldersPresenter() {
    }

    @Override
    public void loadFolders() {
        mView.showLoading();
        AppRepository.getFolderInfosRepository(mView.getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
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
