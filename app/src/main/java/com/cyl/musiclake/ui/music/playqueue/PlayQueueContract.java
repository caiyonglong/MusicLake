package com.cyl.musiclake.ui.music.playqueue;

import com.cyl.musiclake.ui.base.BaseContract;
import com.cyl.musiclake.bean.Music;

import java.util.List;

public interface PlayQueueContract {

    interface View extends BaseContract.BaseView {
        void showSongs(List<Music> songs);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs();

        void clearQueue();
    }
}
