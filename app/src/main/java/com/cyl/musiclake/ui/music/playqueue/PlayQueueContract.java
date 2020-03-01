package com.cyl.musiclake.ui.music.playqueue;

import com.cyl.musiclake.ui.base.BaseContract;
import com.music.lake.musiclib.bean.BaseMusicInfo;

import java.util.List;

public interface PlayQueueContract {

    interface View extends BaseContract.BaseView {
        void showSongs(List<BaseMusicInfo> songs);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs();

        void clearQueue();
    }
}
