package com.cyl.musiclake.ui.music.list.presenter;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.list.contract.PlayQueueContract;

import java.util.List;


/**
 * Created by yonglong on 2018/1/7.
 */

public class PlayQueuePresenter extends BasePresenter<PlayQueueContract.View> implements PlayQueueContract.Presenter {


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
        List<Music> musicList = PlayManager.getPlayList();
        if (musicList.size() > 0) {
            mView.showSongs(musicList);
        } else {
            mView.showEmptyView();
        }
    }

    @Override
    public void clearQueue() {
        PlayManager.clearQueue();
        mView.showEmptyView();
    }
}
