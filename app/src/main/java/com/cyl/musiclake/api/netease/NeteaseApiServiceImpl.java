package com.cyl.musiclake.api.netease;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.bean.Music;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by D22434 on 2018/1/5.
 */

public class NeteaseApiServiceImpl {
    private static final String TAG = "NeteaseApiServiceImpl";

    public static Observable<List<Music>> getTopMusicList(int ids) {
        return ApiManager.getInstance().apiService.getTopList("http://musicapi.leanapp.cn/top/list?idx=" + ids)
                .flatMap(topList -> {
                    List<Music> musicList = new ArrayList<>();
                    for (NeteaseMusic songInfo : topList.getResult().getTracks()) {
                        Music music = new Music();
                        music.setType(Music.Type.NETEASE);
                        music.setOnline(true);
                        music.setId(String.valueOf(songInfo.getId()));
                        music.setAlbum(songInfo.getAlbum().getName());
                        music.setAlbumId(songInfo.getAlbum().getId());
                        music.setArtist(songInfo.getArtists().get(0).getName());
                        music.setArtistId(songInfo.getArtists().get(0).getId());
                        music.setTitle(songInfo.getName());
                        music.setCoverSmall(songInfo.getAlbum().getPicUrl());
                        music.setCoverUri(songInfo.getAlbum().getBlurPicUrl());
                        music.setCoverBig(songInfo.getAlbum().getPicUrl());
                        musicList.add(music);
                    }
                    return Observable.create((ObservableOnSubscribe<List<Music>>) e -> {
                        try {
                            e.onNext(musicList);
                            e.onComplete();
                        } catch (Exception ep) {
                            e.onError(ep);
                        }
                    });
                });
    }

    public static Observable<NeteaseList> getTopList(int ids) {
        return ApiManager.getInstance().apiService.getTopList("http://musicapi.leanapp.cn/top/list?idx=" + ids)
                .flatMap(topList -> {
                    return Observable.create((ObservableOnSubscribe<NeteaseList>) e -> {
                        try {
                            e.onNext(topList.getResult());
                            e.onComplete();
                        } catch (Exception ep) {
                            e.onError(ep);
                        }
                    });
                });
    }

    public static Observable<Music> getMusicUrl(NeteaseMusic neteaseMusic) {
        return ApiManager.getInstance().apiService.getMusicUrl("http://musicapi.leanapp.cn/music/url?id=" + neteaseMusic.getId())
                .flatMap(neteaseMusicUrl -> {
                    return Observable.create((ObservableOnSubscribe<Music>) e -> {
                        try {
                            Music music = new Music();
                            music.setType(Music.Type.NETEASE);
                            music.setOnline(true);
                            music.setId(neteaseMusic.getId() + "");
                            music.setAlbum(neteaseMusic.getAlbum().getName());
                            music.setAlbumId(neteaseMusic.getAlbum().getId());
                            String artist = neteaseMusic.getArtists().get(0).getName();
                            for (int i = 1; i < neteaseMusic.getArtists().size(); i++) {
                                artist += "-" + neteaseMusic.getArtists().get(i).getName();
                            }
                            music.setArtist(artist);
                            music.setArtistId(neteaseMusic.getArtists().get(0).getId());
                            music.setTitle(neteaseMusic.getName());
//                            music.setLrcPath(neteaseMusic.);
                            music.setCoverSmall(neteaseMusic.getAlbum().getPicUrl());
                            music.setCoverUri(neteaseMusic.getAlbum().getPicUrl());
                            music.setCoverBig(neteaseMusic.getAlbum().getPicUrl());
                            if (neteaseMusicUrl.getCode() == 200) {
                                music.setUri(neteaseMusicUrl.getData().get(0).getUrl());
                            }
                            e.onNext(music);
                            e.onComplete();
                        } catch (Exception ep) {
                            e.onError(ep);
                        }
                    });
                });
    }
}
