package com.cyl.musiclake.ui.music.local.contract;

import android.content.Context;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;

public interface PlayQueueContract {

    interface View extends BaseView {

        Context getContext();

        void showSongs(List<Music> songs);

        void showEmptyView();

    }

    interface Presenter extends BasePresenter<View> {

        void loadSongs();

        void clearQueue();
    }
}
