package com.cyl.musiclake.ui.music.contract;

import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

public interface ArtistSongContract {

    interface View extends BaseView {

        void showSongs(List<Music> songList);
    }

    interface Presenter extends BasePresenter<View> {

        void subscribe(long artistID);

        void loadSongs(long artistID);
    }

}
