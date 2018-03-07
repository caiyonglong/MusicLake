package com.cyl.musiclake.base;


public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void subscribe();

    void unsubscribe();
}
