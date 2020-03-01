package com.cyl.musiclake.ui.music.charts;


import com.cyl.musiclake.ui.base.BaseContract;
import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface PlaylistContract {

    interface View extends BaseContract.BaseView {

        void showPlayList(Playlist playlist);

        void showOnlineMusicList(List<BaseMusicInfo> baseMusicInfoInfoList);

        void showNeteaseCharts(List<Playlist> playlistList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadPlaylist(String idx, String type);

        void loadNeteasePlaylist(String id);

        void loadOnlineMusicList(String type, int limit, int mOffset);
    }
}
