package com.cyl.musiclake.ui.music.playlist.love;

import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.data.SongLoader;
import com.cyl.musiclake.bean.Music;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by yonglong on 2018/1/7.
 */

public class LovePresenter extends BasePresenter<LoveContract.View> implements LoveContract.Presenter {

    @Inject
    public LovePresenter() {
    }

    @Override
    public void loadSongs() {
        List<Music> songs = SongLoader.INSTANCE.getFavoriteSong();
        mView.showSongs(songs);
    }

    @Override
    public void clearHistory() {

    }
}
