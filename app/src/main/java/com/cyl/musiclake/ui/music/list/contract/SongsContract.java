package com.cyl.musiclake.ui.music.list.contract;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface SongsContract {

    interface View extends BaseContract.BaseView {
        void showSongs(List<Music> songList);

        void setEmptyView();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadSongs(String action);

        void playMusic(List<Music> playlist, int position);
    }
}
