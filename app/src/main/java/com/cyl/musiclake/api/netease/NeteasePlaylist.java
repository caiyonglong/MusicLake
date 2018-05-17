package com.cyl.musiclake.api.netease;

import java.util.List;

/**
 * Created by master on 2018/4/26.
 */

public class NeteasePlaylist {

    /**
     * playlists : []
     * code : 200
     * more : true
     * lasttime : 1524404830233
     * total : 234
     */
    private int code;
    private boolean more;
    private long lasttime;
    private int total;
    private List<NeteaseList> playlists;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public long getLasttime() {
        return lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<NeteaseList> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<NeteaseList> playlists) {
        this.playlists = playlists;
    }
}
