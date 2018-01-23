package com.cyl.musiclake.ui.download;

/**
 * Author   : D22434
 * version  : 2018/1/23
 * function :
 */

public interface IDownloadService {

    /**
     * 更新下载状态
     *
     * @param mid
     * @param name
     * @param url
     */
    public void updateStatus(String mid, String name, String url);

    /**
     * 增加进度回调
     *
     * @param downloadListener
     */
    public void addProgressCallBack(DownloadListener downloadListener);
}
