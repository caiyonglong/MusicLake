package com.cyl.musiclake.ui.music.playlist.love;

import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;

public interface LoveContract {

    interface View extends BaseContract.BaseView {

        void showSongs(List<BaseMusicInfo> songs);

        void showEmptyView();

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs();

        void clearHistory();
    }
}
