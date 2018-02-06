package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

public interface PlaylistContract {

    interface View extends BaseView {

        void showPlaylist(List<Playlist> playlists);

        void showEmptyView();

    }

    interface Presenter extends BasePresenter<View> {

        void loadPlaylist();
    }
}
