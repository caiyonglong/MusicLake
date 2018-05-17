package com.cyl.musiclake.api;


import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.api.doupan.DoubanApiServiceImpl;
import com.cyl.musiclake.api.doupan.DoubanMusic;
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.qq.QQApiServiceImpl;
import com.cyl.musiclake.api.xiami.XiamiServiceImpl;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.musicApi.CollectionInfo;
import com.cyl.musiclake.musicApi.SourceData;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author   : D22434
 * version  : 2018/3/9
 * function : 接口数据集中到一个类中管理。
 */

public class MusicApi {
    private static final String TAG = "MusicApi";

    /**
     * 获取歌词
     *
     * @param music
     * @return
     */
    public static Observable<String> getLyricInfo(Music music) {
        if (music.getType() == Music.Type.QQ) {
            return QQApiServiceImpl.getQQLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (music.getType() == Music.Type.BAIDU) {
            return BaiduApiServiceImpl.getBaiduLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (music.getType() == Music.Type.XIAMI) {
            return XiamiServiceImpl.getXimaiLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (music.getType() == Music.Type.NETEASE) {
            return NeteaseApiServiceImpl.getNeteaseLyric(music)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return null;
        }
    }

    /**
     * 搜索音乐
     *
     * @param key
     * @param limit
     * @param page
     * @return
     */
    public static Observable<List<Music>> searchMusic(String key, int limit, int page) {
        return Observable.merge(QQApiServiceImpl.search(key, limit, page),
                XiamiServiceImpl.search(key, limit, page),
                NeteaseApiServiceImpl.search(key, limit, page - 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     */
    public static Observable<Music> getMusicInfo(Music music) {
        if (music.getType() == Music.Type.QQ) {
            return QQApiServiceImpl.getMusicInfo(music);
        } else if (music.getType() == Music.Type.NETEASE) {
            return NeteaseApiServiceImpl.getMusicUrl(music);
        } else if (music.getType() == Music.Type.XIAMI) {
            return XiamiServiceImpl.getMusicInfo(music);
        } else {
            return null;
        }
    }

    public static Observable<NeteaseList> getTopList(int id) {
        return NeteaseApiServiceImpl.getTopList(id);
    }

    /**
     * 获取精品歌单
     *
     * @param
     * @return
     */
    public static Observable<List<NeteaseList>> getTopPlaylist() {
        return NeteaseApiServiceImpl.getTopPlaylist();
    }

    /**
     * 加载图片
     */
    public static Observable<DoubanMusic> getMusicAlbumInfo(String info) {
        return DoubanApiServiceImpl.getMusicInfo(info);
    }

    public static CollectionInfo getCollectInfo(Music music) {
        CollectionInfo collectionInfo = new CollectionInfo(music.getId(), music.getTypeName(true));
        SourceData sourceData = new SourceData();
        sourceData.setCp(false);
        sourceData.setId(music.getId());
        sourceData.setSource(music.getTypeName(true));
        sourceData.setName(music.getTitle());
        sourceData.setAlbum(new SourceData.AlbumBean(music.getAlbumId() + "", music.getAlbum(), music.getCoverUri()));
        try {
            String[] artistIds = music.getArtistId().split(",");
            String[] artists = music.getArtist().split(",");
            List<SourceData.ArtistsBean> artistsBeans = new ArrayList<>();
            for (int i = 0; i < artists.length; i++) {
                SourceData.ArtistsBean artistsBean = new SourceData.ArtistsBean(artistIds[i],
                        artists[i]);
                artistsBeans.add(artistsBean);
            }
            sourceData.setArtists(artistsBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collectionInfo.setSourceData(sourceData);
        LogUtil.e(TAG, collectionInfo.toString());
        return collectionInfo;
    }
}
