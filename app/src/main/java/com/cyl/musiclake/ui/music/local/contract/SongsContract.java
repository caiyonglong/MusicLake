package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface SongsContract {

    interface View extends BaseContract.BaseView {
        void showSongs(List<Music> songList);

        void setEmptyView();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadSongs(String action);

    }
}
