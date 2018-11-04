package com.cyl.musiclake.ui.base;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * des : MVP模式 契约基类
 * author   : master
 * date     : 2018/5/19
 */
public class BaseContract {
    public interface BasePresenter<T extends BaseContract.BaseView> {

        void attachView(T view);

        void detachView();
    }

    public interface BaseView {
        Context getContext();

        //显示进度中
        void showLoading();

        //隐藏进度
        void hideLoading();

        //隐藏进度
        void showError(String message, boolean showRetryButton);

        //显示空状态
        void showEmptyState();

        /**
         * 绑定生命周期
         *
         * @param <T>
         * @return
         */
        <T> LifecycleTransformer<T> bindToLife();
    }
}
