package com.cyl.musiclake.ui.music.playlist.history;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;

public interface RecentlyContract {

    interface View extends BaseContract.BaseView {

        void showSongs(List<Music> songs);

        void showEmptyView();

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs();

        void clearHistory();
    }
}
