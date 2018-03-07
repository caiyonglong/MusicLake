package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.api.baidu.OnlineArtistInfo;

public interface ArtistInfoContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showArtistInfo(OnlineArtistInfo artistInfo);

    }

    interface Presenter extends BasePresenter<View> {

        void loadArtistInfo(String artistID);
    }

}
