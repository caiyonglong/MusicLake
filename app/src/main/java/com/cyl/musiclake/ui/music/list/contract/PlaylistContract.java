package com.cyl.musiclake.ui.music.list.contract;

import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

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
