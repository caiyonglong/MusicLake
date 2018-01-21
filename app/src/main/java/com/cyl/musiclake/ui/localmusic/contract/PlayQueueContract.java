package com.cyl.musiclake.ui.localmusic.contract;

import android.content.Context;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

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
