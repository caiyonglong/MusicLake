package com.cyl.musiclake.api.netease;

import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.db.Playlist;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by D22434 on 2018/1/5.
 */

public class NeteaseApiServiceImpl {
    private static final String TAG = "NeteaseApiServiceImpl";

    public static NeteaseApiService getApiService() {
        return ApiManager.getInstance().create(NeteaseApiService.class, Constants.BASE_NETEASE_URL);
    }

    public static Observable<Playlist> getTopList(int ids) {
        return getApiService().getTopList(ids)
                .flatMap(topList -> Observable.create((ObservableOnSubscribe<Playlist>) e -> {
                    try {
                        Playlist playlist = new Playlist();
                        playlist.setPid(String.valueOf(topList.getResult().getId()));
                        playlist.setName(topList.getResult().getName());
                        playlist.setDate(topList.getResult().getTrackUpdateTime());
                        playlist.setDes(topList.getResult().getDescription());
                        List<Music> musicList = new ArrayList<>();
                        for (NeteaseMusic neteaseMusic : topList.getResult().getTracks()) {
                        }
                        playlist.setCoverUrl(topList.getResult().getCoverImgUrl());
                        playlist.setMusicList(musicList);
                        e.onNext(playlist);
                        e.onComplete();
                    } catch (Exception ep) {
                        e.onError(ep);
                    }
                }));
    }

    public static Observable<List<NeteaseList>> getTopPlaylist() {
        return getApiService().getNeteasePlaylist()
                .flatMap(topList -> Observable.create((ObservableOnSubscribe<List<NeteaseList>>) e -> {
                    try {
                        e.onNext(topList.getPlaylists());
                        e.onComplete();
                    } catch (Exception ep) {
                        e.onError(ep);
                    }
                }));
    }

    public static Observable<Music> getMusicUrl(Music music) {
        return getApiService().getMusicUrl(music.getMid())
                .flatMap(neteaseMusicUrl -> Observable.create((ObservableOnSubscribe<Music>) e -> {
                    try {
                        if (neteaseMusicUrl.getCode() == 200) {
                            music.setUri(neteaseMusicUrl.getData().get(0).getUrl());
                        }
                        e.onNext(music);
                        e.onComplete();
                    } catch (Exception ep) {
                        e.onError(ep);
                    }
                }));
    }

    public static Observable<List<Music>> search(String key, int limit, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("offset", String.valueOf(page)); //page
        params.put("limit", String.valueOf(limit));//limit
        params.put("keywords", key);// key
        return getApiService().searchByNetease(params)
                .flatMap(netease -> {
                    List<Music> musicList = new ArrayList<>();
                    List<NeteaseMusic> songList = netease.getResult().getSongs();
                    for (int i = 0; i < songList.size(); i++) {
                    }
                    LogUtil.e("search", page + "--" + limit + "qq :" + musicList.size());
                    return Observable.fromArray(musicList);
                });
    }

    public static Observable<String> getNeteaseLyric(Music music) {
        return getApiService().getMusicLyric(music.getMid())
                .flatMap(netease -> {
                    String lyric = netease.getLrc().getLyric();
                    LogUtil.e("lyric =", lyric);
                    return Observable.fromArray(lyric);
                });
    }
}
