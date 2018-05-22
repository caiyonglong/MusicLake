package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface MyMusicContract {

    interface View extends BaseContract.BaseView {

        void showSongs(List<Music> songList);

        void showEmptyView();

        void showPlaylist(List<Playlist> playlists);

        void showHistory(List<Music> musicList);

        void showLoveList(List<Music> musicList);

        void showDownloadList(List<Music> musicList);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadSongs();

        void loadPlaylist();
    }
}
