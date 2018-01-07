package com.cyl.musiclake.ui.music.contract;

import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.model.Playlist;

import java.util.List;

public interface MyMusicContract {

    interface View extends BaseView {

        void showSongs(List<Music> songList);

        void showPlaylist(List<Playlist> playlists);
    }

    interface Presenter extends BasePresenter<View> {
        void loadSongs();

        void loadPlaylist();
    }
}
