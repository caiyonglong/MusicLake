package com.cyl.musiclake.ui.music.charts;


import android.content.Context;

import com.cyl.musiclake.ui.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface PlaylistContract {

    interface View extends BaseContract.BaseView {

        void showPlayList(Playlist playlist);

        void showOnlineMusicList(List<Music> musicList);

        void showNeteaseCharts(List<Playlist> playlistList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadPlaylist(String idx, String type);

        void loadMorePlaylist(String tag, Context context);

        void loadOnlineMusicList(String type, int limit, int mOffset);
    }
}
