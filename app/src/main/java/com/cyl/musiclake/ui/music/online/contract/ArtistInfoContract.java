package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.api.doupan.DoubanMusic;
import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;

public interface ArtistInfoContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showMusicInfo(DoubanMusic doubanMusic);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadArtistInfo(Music music);
    }

}
