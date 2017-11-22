package com.cyl.musiclake.mvp.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

import java.util.List;

public interface PlaylistDetailContract {

    interface View extends BaseView {

        Context getContext();

        void showPlaylistSongs(List<Music> songList);

        void showPlaylistArt(Drawable playlistArt);

        void showPlaylistArt(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter<View> {

        void loadPlaylistSongs(long playlistID);

        void loadPlaylistArt(long firstAlbumID);
    }
}
