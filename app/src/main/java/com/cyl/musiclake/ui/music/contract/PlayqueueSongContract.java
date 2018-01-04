package com.cyl.musiclake.ui.music.contract;

import android.content.Context;

import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

public interface PlayqueueSongContract {

    interface View extends BaseView {

        Context getContext();

        void showSongs(List<Music> songs);

    }

    interface Presenter extends BasePresenter<View> {

        void loadSongs();
    }
}
