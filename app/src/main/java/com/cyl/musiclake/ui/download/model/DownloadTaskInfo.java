package com.cyl.musiclake.ui.download.model;

import com.cyl.musiclake.ui.download.DownloadListener;

/**
 * 下载任务信息
 */
public class DownloadTaskInfo {
    private int size;// 文件的大小
    private int progress;// 下载的完成度
    private String url;// 下载器标识
    private DownloadListener downloadListener;

    /**
     * @param size     下载文件的大小
     * @param progress 下载完成进度
     * @param url      下载器标识/下载地址
     */
    public DownloadTaskInfo(int size, int progress, String url) {
        this.size = size;
        this.progress = progress;
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setFileSize(int size) {
        this.size = size;
    }

    public int getProgress() {
        return progress;
    }

    public void setComplete(int progress) {
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DownloadTaskInfo{" +
                "size=" + size +
                ", progress=" + progress +
                ", url='" + url + '\'' +
                ", downloadListener=" + downloadListener +
                '}';
    }
}
