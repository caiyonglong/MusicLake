package com.cyl.musiclake.api.net;

/**
 * Des    : 请求成功回调类
 * Author : master.
 * Date   : 2018/5/27 .
 */
public interface RequestCallBack<T> {
    void success(T result);

    void error(String msg);
}
