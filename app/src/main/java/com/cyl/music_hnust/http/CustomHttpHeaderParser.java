package com.cyl.music_hnust.http;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by 永龙 on 2016/4/4.
 */
public class CustomHttpHeaderParser extends HttpHeaderParser {
    public static Cache.Entry parseCacheHeaders(NetworkResponse response,long cacheTime){
        Cache.Entry entry = parseCacheHeaders(response);
        long now = System.currentTimeMillis();
        long softExpire = now+cacheTime;
        entry.softTtl =softExpire;
        return entry;
    }
//    public Request add(Request request){
//        request.setRequestQueue();
//    }
}
