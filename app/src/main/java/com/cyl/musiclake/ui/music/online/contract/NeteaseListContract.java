package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface NeteaseListContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showTopList(NeteaseList musicList);

        void showMusicInfo(Music music);

        void showAddPlaylistDialog(List<Playlist> playlists, Music music);

        void showCollectStatus(boolean success, String msg);
    }

    interface Presenter extends BasePresenter<View> {
        void loadNeteaseMusicList(int idx);

        void getMusicInfo(NeteaseMusic neteaseMusic);

        void collectMusic(String pid, Music music);

        void addPlaylist(Music music);

    }
}
