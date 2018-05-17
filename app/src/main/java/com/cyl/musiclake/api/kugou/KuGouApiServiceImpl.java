package com.cyl.musiclake.api.kugou;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.utils.LyricUtil;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by master on 2018/4/30.
 */

public class KuGouApiServiceImpl {

    private static final String TAG = "KuGouApiServiceImpl";

    public static KuGouApiService getApiService() {
        return ApiManager.getInstance().create(KuGouApiService.class, Constants.BASE_KUGOU_URL);
    }

    public static Observable<String> downloadLrcFile(final String title, final String artist, final long duration) {
        return getApiService().searchLyric(title, String.valueOf(duration))
                .subscribeOn(Schedulers.io())
                .flatMap(kuGouSearchLyricResult -> {
                    if (kuGouSearchLyricResult.status == 200
                            && kuGouSearchLyricResult.candidates != null
                            && kuGouSearchLyricResult.candidates.size() != 0) {
                        KuGouSearchLyricResult.Candidates candidates = kuGouSearchLyricResult.candidates.get(0);
                        return getApiService().getRawLyric(candidates.id, candidates.accesskey);
                    } else {
                        return Observable.just(null);
                    }
                })
                .map(kuGouRawLyric -> {
                    if (kuGouRawLyric == null) {
                        return null;
                    }
                    String rawLyric = LyricUtil.decryptBASE64(kuGouRawLyric.content);
                    LyricUtil.writeLrcToLoc(title, artist, rawLyric);
                    return rawLyric;
//                    return LyricUtil.writeLrcToLoc(title, artist, rawLyric);
                });
    }

    public static Observable<String> getLocalLyric(Music music) {
        if (LyricUtil.isLrcFileExist(music.getTitle(), music.getArtist())) {
            return LyricUtil.getLocalLyricFile(music.getTitle(), music.getArtist());
        } else {
            return downloadLrcFile(music.getTitle(), music.getArtist(), music.getDuration());
        }
    }
}
