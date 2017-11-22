package com.cyl.musiclake.mvp.contract;

import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

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
