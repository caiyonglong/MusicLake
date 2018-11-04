package com.cyl.musiclake.ui.base;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * des : MVP模式 基类P层
 * author   : master
 * update     : 2018/5/19
 */

public class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;
    protected List<Disposable> disposables;

    @Override
    public void attachView(T view) {
        this.mView = view;
        disposables = new ArrayList<>();
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
        for (Disposable dis : disposables) {
            dis.dispose();
        }
        disposables.clear();
    }

}
