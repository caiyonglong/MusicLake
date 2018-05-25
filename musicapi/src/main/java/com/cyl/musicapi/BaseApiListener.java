package com.cyl.musicapi;

import com.cyl.musiclake.musicapi.bean.LyricInfo;
import com.cyl.musiclake.musicapi.bean.NeteaseBean;
import com.cyl.musiclake.musicapi.bean.SongBean;
import com.cyl.musiclake.musicapi.bean.SongComment;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by master on 2018/5/15.
 */

public interface BaseApiListener {
    void searchResult(JSONObject searchResult);

    void songDetail(JSONObject jsonObject);

    void songUrl(SongBean jsonObject);

    void getTopList(NeteaseBean musicList);

    void getComment(SongComment jsonObject);

    void getLyric(LyricInfo jsonObject);
}
