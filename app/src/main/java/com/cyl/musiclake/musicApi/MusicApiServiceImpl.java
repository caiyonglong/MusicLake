package com.cyl.musiclake.musicApi;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.common.Constants;
import com.cyl.musicapi.playlist.CollectionInfo;
import com.cyl.musicapi.playlist.PlaylistInfo;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.ui.my.user.UserStatus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by master on 2018/4/5.
 */

public class MusicApiServiceImpl {
    private static final String TAG = "MusicApiServiceImpl";

    private static MusicApiService getMusicApiService() {
        return ApiManager.getInstance().create(MusicApiService.class, Constants.BASE_PLAYER_URL);
    }

    private static String getToken() {
        return UserStatus.getUserInfo(MusicApp.getAppContext()).getToken();
    }

    public static Observable<List<Playlist>> getPlaylist() {
        return getMusicApiService()
                .getOnlinePlaylist(getToken())
                .flatMap(myPlaylist -> Observable.fromArray(myPlaylist.getData()));
    }

    public static Observable<List<Music>> getMusicList(String pid) {
        return getMusicApiService().getMusicList(getToken(), pid)
                .flatMap(myMusicInfo -> {
                    List<Music> musicList = new ArrayList<>();
                    for (int i = 0; i < myMusicInfo.getData().size(); i++) {
                        MusicInfo musicInfo = myMusicInfo.getData().get(i);
                        Music music = new Music();
                        music.setId(musicInfo.getSong_id());
                        music.setTitle(musicInfo.getSourceData().getName());
                        music.setType(musicInfo.getSourceData().getSource());
                        music.setAlbum(musicInfo.getSourceData().getAlbum().getName());
                        music.setAlbumId(musicInfo.getSourceData().getAlbum().getId());

                        String artists = musicInfo.getSourceData().getArtists().get(0).getName();
                        String artistIds = musicInfo.getSourceData().getArtists().get(0).getId();
                        for (int j = 1; j < musicInfo.getSourceData().getArtists().size(); j++) {
                            artists += "," + musicInfo.getSourceData().getArtists().get(j).getName();
                            artistIds += "," + musicInfo.getSourceData().getArtists().get(j).getId();
                        }
                        music.setArtist(artists);
                        music.setArtistId(artistIds);
                        music.setCoverUri(musicInfo.getSourceData().getAlbum().getCover());
                        music.setCoverBig(musicInfo.getSourceData().getAlbum().getCover());
                        music.setCoverSmall(musicInfo.getSourceData().getAlbum().getCover());
                        musicList.add(music);
                    }
                    return Observable.fromArray(musicList);
                });
    }


    /**
     * 新建歌单
     *
     * @param name
     * @return
     */
    public static Observable<Playlist> createPlaylist(String name) {
        PlaylistInfo playlist = new PlaylistInfo(name);
        return getMusicApiService().createPlaylist(getToken(), playlist)
                .flatMap(myPlaylist -> Observable.create((ObservableOnSubscribe<Playlist>) emitter -> {
                    if (myPlaylist.getStatus().equals("true")) {
                        emitter.onNext(myPlaylist.getData());
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Throwable(myPlaylist.getMessage()));
                    }
                }));
    }

    public static Observable<Integer> deletePlaylist(String pid) {
        return getMusicApiService().deleteMusic(getToken(), pid)
                .flatMap(result -> Observable.fromArray(result.getData()));
    }

    public static Observable<String> renamePlaylist(String pid, String name) {
        PlaylistInfo playlist = new PlaylistInfo(name);
        return getMusicApiService().renameMusic(getToken(), pid, playlist)
                .flatMap(result -> Observable.fromArray(result.getStatus()));
    }

    public static Observable<String> collectMusic(String pid, Music music) {
        CollectionInfo collectionInfo = MusicApi.getCollectInfo(music);
        return getMusicApiService().collectMusic(getToken(), pid, collectionInfo)
                .flatMap(result -> Observable.fromArray(result.getStatus()));
    }

    public static Observable<String> uncollectMusic(String pid, Music music) {
        return getMusicApiService().uncollectMusic(getToken(), pid, music.getId(), music.getTypeName(true))
                .flatMap(result -> Observable.fromArray(result.getStatus()));
    }


    public static Observable<User> login(String token, String openid) {
        return ApiManager.getInstance().create(MusicApiService.class, Constants.BASE_PLAYER_URL)
                .getUserInfo(token, openid)
                .flatMap(userInfo -> Observable.fromArray(userInfo.getData()));
    }
}
