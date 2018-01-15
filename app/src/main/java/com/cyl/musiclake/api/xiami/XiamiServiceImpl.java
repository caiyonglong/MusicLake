package com.cyl.musiclake.api.xiami;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.data.model.Music;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by yonglong on 2018/1/15.
 */

public class XiamiServiceImpl {

    public static Observable<List<Music>> search(String baseUrl) {
        return ApiManager.getInstance().apiService.searchByXiaMi(baseUrl)
                .flatMap(xiaMiModel -> {
                    List<Music> musicList = new ArrayList<>();
                    List<XiamiModel.DataBean.SongsBean> songs = xiaMiModel.getData().getSongs();
                    for (int i = 0; i < songs.size(); i++) {
                        XiamiModel.DataBean.SongsBean song = songs.get(i);
                        Music music = new Music();
                        music.setType(Music.Type.XIAMI);
                        music.setId(String.valueOf(song.getSong_id()));
                        music.setTitle(song.getSong_name());
                        music.setArtist(song.getArtist_name());
                        music.setArtistId(song.getArtist_id());
                        music.setAlbum(song.getAlbum_name());
                        music.setAlbumId(song.getAlbum_id());
                        music.setUri(song.getListen_file());
                        music.setCoverUri(song.getArtist_logo());
                        music.setLrcPath(song.getLyric());
                        //qq音乐播放地址前缀,代表音乐品质 M500一般,M800高
                        musicList.add(music);
                    }
                    return Observable.fromArray(musicList);
                });
    }
}
