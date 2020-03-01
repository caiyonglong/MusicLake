package com.cyl.musiclake.ui.music.artist.contract;


import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.cyl.musiclake.ui.base.BaseContract;

public interface ArtistInfoContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadArtistInfo(BaseMusicInfo baseMusicInfo);
    }

}
