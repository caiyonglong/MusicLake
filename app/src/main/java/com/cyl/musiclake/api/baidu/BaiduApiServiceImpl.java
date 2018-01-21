package com.cyl.musiclake.api.baidu;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by yonglong on 2018/1/21.
 */

public class BaiduApiServiceImpl {
//    http://musicapi.qianqian.com/v1/restserver/ting?from=android&version=6.0.7.1&channel=huwei&operator=1&method=baidu.ting.billboard.billCategory&format=json&kflag=2

    public static Observable<OnlinePlaylists> getOnlinePlaylist() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_CATEGORY);// key
        params.put("operator", "1");
        params.put("kflag", "2");
        params.put("format", "json");
        return ApiManager.getInstance().apiService.getOnlinePlaylist(Constants.BASE_URL_BAIDU_MUSIC, params)
                .flatMap(Observable::fromArray);
    }

    public static Observable<List<Music>> getOnlineSongs(String type, int limit, int mOffset) {
        Map<String, String> params = new HashMap<>();

        params.put(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST);
        params.put(Constants.PARAM_TYPE, type);
        params.put(Constants.PARAM_SIZE, String.valueOf(limit));
        params.put(Constants.PARAM_OFFSET, String.valueOf(mOffset));

        return ApiManager.getInstance().apiService.getOnlineSongs(Constants.BASE_URL_BAIDU_MUSIC, params)
                .flatMap(bdMusicList -> {
                    List<Music> musicList = new ArrayList<>();
                    for (BaiduMusicInfo songInfo : bdMusicList.getSong_list()) {
                        Music music = new Music();
                        music.setType(Music.Type.BAIDU);
                        music.setId(songInfo.getSong_id());
                        music.setAlbum(songInfo.getAlbum_title());
                        music.setAlbumId(songInfo.getAlbum_id());
                        music.setArtist(songInfo.getArtist_name());
                        music.setArtistId(songInfo.getArtist_id());
                        music.setTitle(songInfo.getTitle());
                        music.setLrcPath(songInfo.getLrclink());
                        music.setCoverSmall(songInfo.getPic_small());
                        music.setCoverUri(songInfo.getPic_big());
                        music.setCoverBig(songInfo.getPic_radio());
                        musicList.add(music);
                    }
                    return Observable.fromArray(musicList);
                });
    }

    public static Observable<Music> getTingSongInfo(String mid) {
        Map<String, String> params = new HashMap<>();

        params.put(Constants.PARAM_METHOD, Constants.METHOD_MUSIC_INFO);
        params.put(Constants.PARAM_SONG_ID, mid);

        return ApiManager.getInstance().apiService.getTingSongInfo(Constants.BASE_URL_BAIDU_MUSIC, params)
                .flatMap(baiduSongInfo -> {
                    Music music = new Music();
                    BaiduSongInfo.SonginfoBean songInfo = baiduSongInfo.getSonginfo();
                    List<BaiduSongInfo.SongurlBean.UrlBean> songurlBeans = baiduSongInfo.getSongurl().getUrl();
                    for (int i = 0; i < songurlBeans.size(); i++) {
                        if (songurlBeans.get(i).getShow_link() != null) {
                            music.setUri(songurlBeans.get(i).getShow_link());
                            break;
                        }
                    }
                    music.setType(Music.Type.BAIDU);
                    music.setAlbum(songInfo.getAlbum_title());
                    music.setAlbumId(songInfo.getArtist_id());
                    music.setArtistId(songInfo.getAlbum_id());
                    music.setArtist(songInfo.getAuthor());
                    music.setTitle(songInfo.getTitle());
                    music.setLrcPath(songInfo.getLrclink());
                    music.setCoverSmall(songInfo.getPic_small());
                    music.setCoverUri(songInfo.getPic_big());
                    music.setCoverBig(songInfo.getPic_huge());

                    return Observable.create((ObservableOnSubscribe<Music>) e -> {
                        if (music.getUri() != null) {
                            e.onNext(music);
                            e.onComplete();
                        } else {
                            e.onError(new Throwable());
                        }
                    });
                });
    }

}
