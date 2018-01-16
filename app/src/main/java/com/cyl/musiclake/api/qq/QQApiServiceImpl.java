package com.cyl.musiclake.api.qq;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.utils.Constants;

import java.io.UnsupportedEncodingException;
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
    @SuppressWarnings({"unchecked", "varargs"})
    public static Observable<List<Music>> search(String key, int limit, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("p", page + ""); //page
        params.put("n", limit + "");//limit
        params.put("w", key);// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");
        return ApiManager.getInstance().apiService.searchByQQ(Constants.BASE_URL_QQ_MUSIC_SEARCH, params)
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
                        String guid = "1.71787218E8";
                        String apiKey = "CB68B16BED816B3FE7118A3428510100741833CA95C2C780F65D7776B7101F327FFB1EFB33D89DB8A4039D08473E4D46DC9336978AFCACBC";
                        String songUrl = Constants.BASE_URL_QQ_MUSIC_URL +
                                music.getPrefix() + music.getId() + ".mp3?vkey=" + apiKey + "&guid=" + guid + "&fromtag=30";
                        music.setUri(songUrl);
                        musicList.add(music);
                    }
                    return Observable.fromArray(musicList);
                });
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public static Observable<Map<String, String>> getQQApiKey() {
        double guid = Math.floor(Math.random() * 1000000000);
        String requestUrl = Constants.BASE_URL_QQ_MUSIC_KEY + "json=3&guid=" + guid + "&format=json";
        return ApiManager.getInstance().apiService.getTokenKey(requestUrl)
                .flatMap(qqApiKey -> {
                    String key = qqApiKey.getKey();
                    Map<String, String> map = new HashMap<>();
                    map.put(String.valueOf(guid), key);
                    return Observable.fromArray(map);
                });
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public static Observable<String> getQQLyric(String mid) {
        String mLyricUrl = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?songmid=" + mid + "&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
        return ApiManager.getInstance().apiService.getQQLyric(mLyricUrl)
                .flatMap(qqLyricInfo -> {
                    System.out.println(mLyricUrl);
                    System.out.println(qqLyricInfo.toString());
                    String lyric = null;
                    byte[] asByte = Base64.decode(qqLyricInfo.getLyric(), Base64.DEFAULT);
//                  单元测试用  byte[] asByte =  java.util.Base64.getDecoder().decode(qqLyricInfo.getLyric());
                    try {
                        lyric = new String(asByte, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return Observable.fromArray(lyric);
                });
    }
}
