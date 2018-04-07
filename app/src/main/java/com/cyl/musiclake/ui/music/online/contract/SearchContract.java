package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface SearchContract {

    interface View extends BaseView {

        void showSearchResult(List<Music> list);

        void showEmptyView();

        void showAddPlaylistDialog(List<Playlist> playlists, Music music);

        void showCollectStatus(boolean success, String msg);

        void showMusicInfo(int type, Music music);
    }

    interface Presenter extends BasePresenter<View> {

        void search(String key, int limit, int page);

        void collectMusic(String pid, Music music);

        void addPlaylist(Music music);

        void getMusicInfo(int type, Music music);
    }

}
