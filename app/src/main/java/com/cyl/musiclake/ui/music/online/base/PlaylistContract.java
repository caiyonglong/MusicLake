package com.cyl.musiclake.ui.music.online.base;


import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;

public interface PlaylistContract {

    interface View extends BaseContract.BaseView {

        void showErrorInfo(String msg);

        void showPlayList(Playlist playlist);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getPlaylist(int idx, String type);

        void getMusicInfo(Music music);
    }
}
