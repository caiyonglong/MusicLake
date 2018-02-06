package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

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
