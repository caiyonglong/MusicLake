package com.cyl.musiclake.ui.onlinemusic.contract;


import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineArtistInfo;

public interface ArtistInfoContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showArtistInfo(OnlineArtistInfo artistInfo);

    }

    interface Presenter extends BasePresenter<View> {

        void loadArtistInfo(String artistID);
    }

}
