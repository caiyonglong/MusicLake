package com.cyl.musiclake.ui.music.discover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
public class DiscoverPresenter extends BasePresenter<DiscoverContract.Presenter> implements DiscoverContract.View {

    @Inject
    public DiscoverPresenter() {
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }
}
