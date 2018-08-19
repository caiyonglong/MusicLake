package com.cyl.musiclake.ui.music.playqueue;

import android.content.Context;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface PlayQueueContract {

    interface View extends BaseContract.BaseView {

        Context getContext();

        void showSongs(List<Music> songs);

        void showEmptyView();

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs();

        void clearQueue();
    }
}
