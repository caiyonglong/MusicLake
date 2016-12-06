package com.cyl.music_hnust.model.music;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 作者：yonglong on 2016/8/9 10:50
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class Music implements Serializable{
    // 歌曲类型 本地/网络
    private Type type;
    // [本地歌曲]歌曲id
    private long id;
    // 音乐标题
    private String title;
    // 艺术家
    private String artist;
    // 专辑
    private String album;
    // 专辑id
    private long artistId;
    // 专辑id
    private long albumId;
    // 持续时间
    private long duration;
    // 音乐路径
    private String uri;
    // [网络歌曲] 音乐歌词地址
    private String lrcPath;
    // [本地歌曲]专辑封面路径
    private String coverUri;
    // 文件名
    private String fileName;
    // [网络歌曲]专辑封面bitmap
    private Bitmap cover;
    // 文件大小
    private long fileSize;
    // 发行日期
    private String year;

    public Music() {
        this.id = -1;
        this.albumId = -1;
        this.artistId = -1;

        this.title = "未知";
        this.artist = "未知";
        this.album = "未知";

        this.uri = "";
        this.lrcPath = "";
        this.coverUri = "";
        this.fileSize = -1;
        this.duration = -1;
        this.cover = null;

        this.year = "未知";
        this.type = Type.LOCAL;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLrcPath() {
        return lrcPath;
    }

    public void setLrcPath(String lrcPath) {
        this.lrcPath = lrcPath;
    }

    public enum  Type {
        ONLINE,
        LOCAL
    }

    @Override
    public String toString() {
        return "Music{" +
                "album='" + album + '\'' +
                ", type=" + type +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", uri='" + uri + '\'' +
                ", coverUri='" + coverUri + '\'' +
                ", fileName='" + fileName + '\'' +
                ", cover=" + cover +
                ", fileSize=" + fileSize +
                ", year='" + year + '\'' +
                '}';
    }
}
