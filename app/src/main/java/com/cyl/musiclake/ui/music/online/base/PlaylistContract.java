package com.cyl.musiclake.ui.music.online.base;


import android.content.Context;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.db.Playlist;

public interface PlaylistContract {

    interface View extends BaseContract.BaseView {

        void showErrorInfo(String msg);

        void showPlayList(Playlist playlist);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getPlaylist(String idx, Context context);

        void getMusicInfo(Music music);
    }
}
