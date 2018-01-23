package com.cyl.musiclake.ui.download.model;

/**
 * 下载信息类
 */
public class DownloadInfo {

    private int threadId;
    private int startPos;
    private int endPos;
    private int progress;
    private String url;

    /**
     * 构造函数
     *
     * @param threadId 下载器id/线程id
     * @param progress 完成的进度
     * @param url      下载器网络标识/下载地址
     */
    public DownloadInfo(int threadId, int startPos, int endPos,
                        int progress, String url) {
        this.threadId = threadId;
        this.startPos = startPos;
        this.endPos = endPos;
        this.progress = progress;
        this.url = url;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public int getCompleteSize() {
        return progress;
    }

    public void setCompleteSize(int progress) {
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
