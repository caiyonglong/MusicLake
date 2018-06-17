package com.cyl.musiclake.ui.music.playlist;

import android.content.Context;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.db.Playlist;

import java.util.List;

public interface PlaylistDetailContract {

    interface View extends BaseContract.BaseView {

        Context getContext();

        void showPlaylistSongs(List<Music> songList);

        void changePlayStatus(Boolean isPlaying);

        void removeMusic(int position);

        void success(int type);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadPlaylistSongs(Playlist playlist);

        void deletePlaylist(Playlist playlist);

        void renamePlaylist(Playlist playlist, String title);

        void disCollectMusic(String pid, int position, Music music);
    }
}
