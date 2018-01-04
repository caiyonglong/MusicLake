package com.cyl.musiclake.ui.music.contract;

import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

public interface SongsContract {

    interface View extends BaseView {

        void showSongs(List<Music> songList);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View> {

        void loadSongs(String action);
    }
}
