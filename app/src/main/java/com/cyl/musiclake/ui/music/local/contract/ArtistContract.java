package com.cyl.musiclake.ui.music.local.contract;


import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;

public interface ArtistContract {

    interface View extends BaseContract.BaseView {

        void showArtists(List<Artist> artists);

        void showEmptyView();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadArtists(String action);
    }
}
