package com.cyl.musiclake.ui.localmusic.presenter;

import android.content.Context;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.data.source.MusicLoader;
import com.cyl.musiclake.data.source.PlaylistLoader;
import com.cyl.musiclake.ui.localmusic.contract.MyMusicContract;

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
        RxBus.getInstance().register(Playlist.class).subscribe(integer -> {
            playlists = PlaylistLoader.getPlaylist(mContext);
            mView.showPlaylist(playlists);
        });
    }

    @Override
    public void unsubscribe() {
        RxBus.getInstance().unregisterAll();
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
