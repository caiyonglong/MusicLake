package com.cyl.musiclake.mvp.contract;

import android.content.Context;

import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

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
