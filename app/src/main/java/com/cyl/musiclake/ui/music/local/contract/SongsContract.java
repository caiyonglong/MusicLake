package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;

public interface SongsContract {

    interface View extends BaseView {
        void showSongs(List<Music> songList);

        void setEmptyView();
    }

    interface Presenter extends BasePresenter<View> {
        void loadSongs(String action);

        void playMusic(List<Music> playlist, int position);
    }
}
