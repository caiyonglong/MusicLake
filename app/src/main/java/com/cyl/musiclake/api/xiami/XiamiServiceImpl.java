package com.cyl.musiclake.api.xiami;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.data.model.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by yonglong on 2018/1/15.
 */

public class XiamiServiceImpl {

    /**
     * 搜索虾米音乐
     * @param key 关键字
     * @param limit
     * @param page
     * @return
     */
    public static Observable<List<Music>> search(String key, int limit, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("v", "2.0"); //page
        params.put("page", page); //page
        params.put("limit", limit);//limit
        params.put("key", key);// key
        params.put("r", "search/songs");
        params.put("app_key", "1");
        params.put("format", "json");
        String url = "http://api.xiami.com/web?";
        return ApiManager.getInstance().apiService.searchByXiaMi(url, params)
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
                        musicList.add(music);
                    }
                    return Observable.fromArray(musicList);
                });
    }
}
