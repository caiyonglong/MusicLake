package com.cyl.musiclake.ui.music.local.contract;


import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

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
