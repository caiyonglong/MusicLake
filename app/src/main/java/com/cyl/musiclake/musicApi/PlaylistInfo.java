package com.cyl.musiclake.musicApi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by master on 2018/4/5.
 */

public class PlaylistInfo {

    @SerializedName("name")
    String name;

    public PlaylistInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
