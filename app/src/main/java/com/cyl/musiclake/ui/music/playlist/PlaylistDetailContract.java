package com.cyl.musiclake.ui.music.playlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface PlaylistDetailContract {

    interface View extends BaseContract.BaseView {

        Context getContext();

        void showPlaylistSongs(List<Music> songList);

        void showPlaylistArt(Drawable playlistArt);

        void showPlaylistArt(Bitmap bitmap);

        void removeMusic(int position);

        void success(int type);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadPlaylistSongs(String playlistID);

        void loadPlaylistArt(String playlistID);

        void deletePlaylist(Playlist playlist);

        void renamePlaylist(Playlist playlist, String title);

        void uncollectMusic(String pid, int position, Music music);
    }
}
