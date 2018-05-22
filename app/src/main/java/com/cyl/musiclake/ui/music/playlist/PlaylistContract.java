package com.cyl.musiclake.ui.music.playlist;

import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface PlaylistContract {

    interface View extends BaseContract.BaseView {

        void showPlaylist(List<Playlist> playlists);

        void showEmptyView();

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadPlaylist();
    }
}
