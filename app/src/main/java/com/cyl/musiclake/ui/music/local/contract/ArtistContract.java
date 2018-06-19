package com.cyl.musiclake.ui.music.local.contract;


import com.cyl.musiclake.db.ArtistBean;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface ArtistContract {

    interface View extends BaseContract.BaseView {

        void showArtists(List<ArtistBean> artistBeans);

        void showEmptyView();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadArtists(String action);
    }
}
