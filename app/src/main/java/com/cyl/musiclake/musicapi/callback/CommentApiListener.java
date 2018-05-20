package com.cyl.musiclake.musicapi.callback;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.musicapi.SearchResult;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by master on 2018/5/15.
 */

public interface CommentApiListener {
    void getSearchResult(SearchResult searchResult);

    void getMusicInfo(JSONObject jsonObject);

    void getSongUrl(JSONObject jsonObject);

    void getTopList(List<Music> musicList);

    void getComment(JSONObject jsonObject);

    void getLyric(JSONObject jsonObject);
}
