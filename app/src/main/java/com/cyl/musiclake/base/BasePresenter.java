package com.cyl.musiclake.base;


/**
 * des : MVP模式 基类P层
 * author   : master
 * update     : 2018/5/19
 */

public class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }

}
