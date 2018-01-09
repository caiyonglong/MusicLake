package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.ui.localmusic.contract.FoldersContract;

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

    }
}
