package com.cyl.musiclake.ui.base;


public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void subscribe();

    void unsubscribe();
}
