package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.api.doupan.DoubanMusic;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.bean.Music;

public interface ArtistInfoContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showMusicInfo(DoubanMusic doubanMusic);

    }

    interface Presenter extends BasePresenter<View> {

        void loadArtistInfo(Music music);
    }

}
