package com.cyl.musiclake.ui.music.online.base;

import android.content.Context;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musicapi.bean.ListItem;
import com.cyl.musiclake.api.MusicUtils;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.db.Playlist;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by D22434 on 2018/1/4.
 */

public class PlaylistPresenter extends BasePresenter<PlaylistContract.View> implements PlaylistContract.Presenter {

    @Inject
    public PlaylistPresenter() {
    }

    @Override
    public void getPlaylist(String idx, Context context) {
        BaseApiImpl.Companion.getInstance(context).getTopList(idx, topList -> {
            Playlist playlist = new Playlist();
            playlist.setPid(idx);
            playlist.setName(topList.getData().getName());
            playlist.setCount(topList.getData().getPlayCount());
            playlist.setCoverUrl(topList.getData().getCover());
            playlist.setDes(topList.getData().getDescription());
            List<Music> musicList = new ArrayList<>();
            if (topList.getData().getList().size() > 0) {
                for (ListItem item : topList.getData().getList()) {
                    Music music = MusicUtils.INSTANCE.getMusic(item);
                    musicList.add(music);
                }
            }
            playlist.setMusicList(musicList);
            mView.showPlayList(playlist);
            return null;
        });
    }

    @Override
    public void getMusicInfo(Music music) {

    }
}
