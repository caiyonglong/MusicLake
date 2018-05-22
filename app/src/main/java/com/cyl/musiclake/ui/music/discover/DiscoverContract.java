package com.cyl.musiclake.ui.music.discover;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;

import java.util.List;


public interface DiscoverContract {

    interface View extends BaseContract.BaseView {

        void showEmptyView();

    }

    interface Presenter extends BaseContract.BasePresenter<View>,BaseContract.BaseView {
        void loadAlbumSongs(String albumName);
    }
}
