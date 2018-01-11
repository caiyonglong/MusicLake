package com.cyl.musiclake.ui.localmusic.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

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
