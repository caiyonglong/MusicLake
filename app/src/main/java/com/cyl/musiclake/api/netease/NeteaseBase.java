package com.cyl.musiclake.api.netease;

/**
 * Created by master on 2018/3/20.
 */

public class NeteaseBase<T> {

    private T playlist;
    private int code;

    public T getResult() {
        return playlist;
    }

    public void setResult(T playlist) {
        this.playlist = playlist;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
