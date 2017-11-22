package com.cyl.musiclake.mvp.contract;

import com.cyl.musiclake.mvp.model.music.Playlist;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

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
