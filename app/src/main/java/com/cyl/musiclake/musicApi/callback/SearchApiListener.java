package com.cyl.musicapi.callback;

import com.cyl.musiclake.bean.Music;
import com.cyl.musicapi.callback.musicApi.SearchResult;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by master on 2018/5/15.
 */

public interface SearchApiListener {
    void searchResult(SearchResult searchResult);

    void songDetail(JSONObject jsonObject);

    void songUrl(JSONObject jsonObject);

    void getTopList(List<Music> musicList);

    void getComment(JSONObject jsonObject);

    void getLyric(JSONObject jsonObject);
}
