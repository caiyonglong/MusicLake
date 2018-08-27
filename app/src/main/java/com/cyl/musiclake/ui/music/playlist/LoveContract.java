package com.cyl.musiclake.ui.music.playlist;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface LoveContract {

    interface View extends BaseContract.BaseView {

        void showSongs(List<Music> songs);

        void showEmptyView();

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs();

        void clearHistory();
    }
}
