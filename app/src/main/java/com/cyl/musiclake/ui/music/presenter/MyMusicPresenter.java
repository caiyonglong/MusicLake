package com.cyl.musiclake.ui.music.presenter;

import android.content.Context;

import com.cyl.musiclake.ui.music.contract.MyMusicContract;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.model.Playlist;
import com.cyl.musiclake.ui.music.model.data.MusicLoader;
import com.cyl.musiclake.ui.music.model.data.PlaylistLoader;

import java.util.List;

/**
 * Created by yonglong on 2018/1/6.
 */

public class MyMusicPresenter implements MyMusicContract.Presenter {
    private MyMusicContract.View mView;
    private Context mContext;
    private List<Playlist> playlists;
    private List<Music> musicList;

    public MyMusicPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(MyMusicContract.View view) {
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
        musicList = MusicLoader.getAllSongs(mContext);
        mView.showSongs(musicList);
    }

    @Override
    public void loadPlaylist() {
        playlists = PlaylistLoader.getPlaylist(mContext);
        mView.showPlaylist(playlists);
    }
}
