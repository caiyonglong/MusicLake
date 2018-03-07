package com.cyl.musiclake.ui.music.local.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;


public interface AlbumDetailContract {

    interface View extends BaseView {

        Context getContext();

        void showEmptyView();

        void showAlbumSongs(List<Music> songList);

        void showAlbumArt(Drawable albumArt);

        void showAlbumArt(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter<View> {

        void subscribe(long albumID);

        void loadAlbumSongs(String albumName);

        void loadAlbumArt(long albumID);

    }
}
