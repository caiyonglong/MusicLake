package com.cyl.musiclake.api.doupan;

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
//https://api.douban.com/v2/music/search?q=素颜&count=1
    public static Observable<DoubanMusic> getMusicInfo(String info) {
        Map<String, String> params = new HashMap<>();
        params.put("q", info);
        params.put("count", "10");
        return ApiManager.getInstance().create(DoubanApiService.class, "https://api.douban.com/")
                .searchMusic("search", params);

    }
}
