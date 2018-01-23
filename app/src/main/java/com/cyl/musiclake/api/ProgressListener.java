package com.cyl.musiclake.api;

/**
 * Author   : D22434
 * version  : 2018/1/22
 * function :
 */

public abstract class ProgressListener {
    public abstract void onLoading(long total, long progress, boolean finish);
}
