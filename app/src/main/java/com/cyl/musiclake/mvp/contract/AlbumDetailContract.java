package com.cyl.musiclake.mvp.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

import java.util.List;


public interface AlbumDetailContract {

    interface View extends BaseView {

        Context getContext();

        void showAlbumSongs(List<Music> songList);

        void showAlbumArt(Drawable albumArt);

        void showAlbumArt(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter<View> {

        void subscribe(long albumID);

        void loadAlbumSongs(long albumID);

        void loadAlbumArt(long albumID);
    }
}
