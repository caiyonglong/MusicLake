package com.cyl.musiclake.ui.localmusic.contract;

import com.cyl.musiclake.data.model.Music;
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
