package com.cyl.musiclake.api.qq;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.data.model.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by D22434 on 2018/1/5.
 */

public class QQApiServiceImpl {
    /**
     * @param
     * @return
     */
    public static Observable<List<Music>> search(String key, int limit, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("p", page + ""); //page
        params.put("n", limit + "");//limit
        params.put("w", key);// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");
        String qqRequestUrl = "http://c.y.qq.com/soso/fcgi-bin/search_cp?";
        return ApiManager.getInstance().apiService.searchByQQ(qqRequestUrl, params)
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

    public static Observable<Map<String, String>> getQQApiKey() {
        double guid = Math.floor(Math.random() * 1000000000);
        String requestUrl = "https://c.y.qq.com/base/fcgi-bin/fcg_musicexpress.fcg?json=3&guid=" + guid + "&format=json";
        return ApiManager.getInstance().apiService.getTokenKey(requestUrl)
                .flatMap(qqApiKey -> {
                    String key = qqApiKey.getKey();
                    Map<String, String> map = new HashMap<>();
                    map.put(String.valueOf(guid), key);
                    return Observable.fromArray(map);
                });
    }
}
