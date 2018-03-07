package com.cyl.musiclake.ui.music.local.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;

public interface PlaylistDetailContract {

    interface View extends BaseView {

        Context getContext();

        void showPlaylistSongs(List<Music> songList);

        void showPlaylistArt(Drawable playlistArt);

        void showPlaylistArt(Bitmap bitmap);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View> {

        void loadPlaylistSongs(String playlistID);

        void loadPlaylistArt(String playlistID);
    }
}
