package com.cyl.musiclake.api.doupan;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.net.ApiManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Author   : D22434
 * version  : 2018/3/30
 * function :
 */

public class DoubanApiServiceImpl {

    private static final String TAG = "DoubanApiServiceImpl";

    public static Observable<DoubanMusic> getMusicInfo(Music music) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_CATEGORY);// key
        params.put("q", music.getTitle() + "-" + music.getArtist());
        params.put("count", "1");
        return ApiManager.getInstance().create(DoubanApiService.class)
                .searchMusic("search", params);
    }
}
