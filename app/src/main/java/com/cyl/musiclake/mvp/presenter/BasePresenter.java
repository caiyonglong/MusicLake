package com.cyl.musiclake.mvp.presenter;


import com.cyl.musiclake.mvp.view.BaseView;

public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void subscribe();

    void unsubscribe();
}
