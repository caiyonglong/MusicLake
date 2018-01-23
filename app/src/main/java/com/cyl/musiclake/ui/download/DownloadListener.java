package com.cyl.musiclake.ui.download;

import java.io.File;

/**
 * Created by yonglong on 2016/11/15.
 */

public interface DownloadListener {
    /**
     * 下载进度
     *
     * @param url      下载链接
     * @param finished 下载进度
     */
    public void oDownloading(String url, int finished);

    /**
     * 下载完成
     *
     * @param downloadFile 下载完成后的文件
     */
    public void onDownloadFinished(File downloadFile);
}
