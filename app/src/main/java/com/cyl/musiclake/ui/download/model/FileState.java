package com.cyl.musiclake.ui.download.model;

/**
 *
 *
 */
public class FileState {

    private String mid; //歌曲信息
    private String name;// 文件名称
    private String url;// 下载地址
    private int state;// 当前的下载状态 1 正在下载 0 已下载
    private int finish;// 下载的完成进度
    private int fileSize;// 文件的长度

    /**
     * @param name     文件名称
     * @param url      下载地址
     * @param state    当前的下载状态 1 正在下载 0 已下载
     * @param finish   下载的完成进度
     * @param fileSize 文件的长度
     */
    public FileState(String mid, String name, String url, int state, int finish,
                     int fileSize) {
        this.mid = mid;
        this.name = name;
        this.url = url;
        this.state = state;
        this.finish = finish;
        this.fileSize = fileSize;
    }

    public FileState() {

    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFinish() {
        return finish;
    }

    public void setCompleteSize(int finish) {
        this.finish = finish;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "FileState [name=" + name + ", url=" + url + ", state=" + state
                + ", finish=" + finish + ", fileSize=" + fileSize
                + "]";
    }

}
