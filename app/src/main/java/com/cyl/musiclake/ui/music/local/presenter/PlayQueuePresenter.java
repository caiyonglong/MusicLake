package com.cyl.musiclake.ui.music.local.presenter;

import android.content.Context;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.music.local.contract.PlayQueueContract;

import java.util.List;


/**
 * Created by yonglong on 2018/1/7.
 */

public class PlayQueuePresenter implements PlayQueueContract.Presenter {
    private PlayQueueContract.View mView;
    private Context mContext;

    public PlayQueuePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(PlayQueueContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

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
