package com.cyl.musiclake.api.netease;

/**
 * Created by master on 2018/3/20.
 */

public class NeteaseBase<T> {

    private T result;
    private int code;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
