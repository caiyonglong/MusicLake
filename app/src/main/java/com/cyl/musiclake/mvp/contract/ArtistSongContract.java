package com.cyl.musiclake.mvp.contract;

import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

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
