package com.cyl.musiclake.api;


import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.api.qq.QQApiServiceImpl;
import com.cyl.musiclake.api.xiami.XiamiServiceImpl;
import com.cyl.musiclake.bean.Music;

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
        return Observable.merge(QQApiServiceImpl.search(key, limit, page), XiamiServiceImpl.search(key, limit, page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     */
    public static Observable<Music> getMusicInfo(Music music) {
        return QQApiServiceImpl.getMusicInfo(music);
    }
}
