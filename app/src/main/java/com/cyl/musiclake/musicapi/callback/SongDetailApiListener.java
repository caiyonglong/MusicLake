package com.cyl.musiclake.musicapi.callback;

import com.cyl.musiclake.musicapi.SearchResult;

import org.json.JSONObject;

/**
 * Created by master on 2018/5/15.
 */

public interface SongDetailApiListener {
    void getSearchResult(SearchResult searchResult);

    void getMusicInfo(JSONObject jsonObject);

    void getSongUrl(JSONObject jsonObject);

    void getComment(JSONObject jsonObject);

    void getLyric(JSONObject jsonObject);
}
