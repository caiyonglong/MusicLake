package com.cyl.musiclake.ui.music.playqueue;

import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.music.lake.musiclib.MusicPlayerManager;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by yonglong on 2018/1/7.
 */

public class PlayQueuePresenter extends BasePresenter<PlayQueueContract.View> implements PlayQueueContract.Presenter {

    @Inject
    public PlayQueuePresenter() {
    }

    @Override
    public void attachView(PlayQueueContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadSongs() {
        List<BaseMusicInfo> baseMusicInfoList = MusicPlayerManager.getInstance().getPlayList();
        mView.showSongs(baseMusicInfoList);
    }

    @Override
    public void clearQueue() {
        MusicPlayerManager.getInstance().clearPlaylist();
    }
}
