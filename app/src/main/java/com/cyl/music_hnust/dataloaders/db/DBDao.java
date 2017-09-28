package com.cyl.music_hnust.dataloaders.db;

import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.Playlist;

import java.util.List;

/**
 * Created by D22434 on 2017/9/28.
 */

public interface DBDao {

    void deletePlaylist(String pId);

    void newPlayList(String title);

    void insertSong(String pId, String mId);

    void removeSong(String pId, String mId);

    void insertSongs(List<Music> songs);

    void insertQueue(List<Music> songs);

    List<Playlist> getAllPlaylist();

    List<Music> getSongs(String pId);

    List<Music> getQueue();

    void closeDB();
}
