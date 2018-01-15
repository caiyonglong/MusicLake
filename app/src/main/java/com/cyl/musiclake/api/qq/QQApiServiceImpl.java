package com.cyl.musiclake.api.qq;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.data.model.Music;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by D22434 on 2018/1/5.
 */

public class QQApiServiceImpl {
    public static Observable<List<Music>> searchByQQ(String baseUrl) {
        return ApiManager.getInstance().apiService.searchByQQ(baseUrl)
                .flatMap(qqApiModel -> {
                    List<Music> musicList = new ArrayList<>();
                    List<QQApiModel.DataBean.SongBean.ListBean> songList = qqApiModel.getData().getSong().getList();
                    for (int i = 0; i < songList.size(); i++) {
                        QQApiModel.DataBean.SongBean.ListBean song = songList.get(i);
                        Music music = new Music();
                        music.setType(Music.Type.QQ);
                        music.setId(song.getSongmid());
                        music.setTitle(song.getSongname());
                        music.setArtist(song.getSinger().get(0).getName());
                        music.setArtistId(song.getSinger().get(0).getId());
                        music.setAlbum(song.getAlbumname());
                        music.setAlbumId(song.getAlbumid());
                        music.setDuration(song.getPubtime());
                        //qq音乐播放地址前缀,代表音乐品质 M500一般,M800高
                        music.setPrefix(song.getSize128() != 0 ? "M500" : "M800");
                        String url = "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + song.getAlbummid() + ".jpg";
                        music.setCoverUri(url);
                        musicList.add(music);
                    }
                    return Observable.fromArray(musicList);
                });
    }
}
