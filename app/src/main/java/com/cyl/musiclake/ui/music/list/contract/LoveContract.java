package com.cyl.musiclake.ui.music.list.contract;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;

public interface LoveContract {

    interface View extends BaseView {

        void showSongs(List<Music> songs);

        void showEmptyView();

    }

    interface Presenter extends BasePresenter<View> {

        void loadSongs();

        void clearHistory();
    }
}
