package com.cyl.musiclake.mvp.contract;


import com.cyl.musiclake.mvp.model.music.Artist;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

import java.util.List;

public interface ArtistContract {

    interface View extends BaseView {

        void showArtists(List<Artist> artists);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View> {

        void loadArtists(String action);
    }
}
