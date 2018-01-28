package com.cyl.musiclake.ui.localmusic.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

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
