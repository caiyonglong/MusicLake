package com.cyl.musiclake.ui.music.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：yonglong on 2016/8/9 10:50
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class Music implements Parcelable {
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

    protected Music(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        artistId = in.readLong();
        albumId = in.readLong();
        duration = in.readLong();
        uri = in.readString();
        lrcPath = in.readString();
        coverUri = in.readString();
        fileName = in.readString();
        cover = in.readParcelable(Bitmap.class.getClassLoader());
        fileSize = in.readLong();
        year = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

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
        return title ;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(artist);
        parcel.writeString(album);
        parcel.writeLong(artistId);
        parcel.writeLong(albumId);
        parcel.writeLong(duration);
        parcel.writeString(uri);
        parcel.writeString(lrcPath);
        parcel.writeString(coverUri);
        parcel.writeString(fileName);
        parcel.writeParcelable(cover, i);
        parcel.writeLong(fileSize);
        parcel.writeString(year);
    }

    public enum Type {
        ONLINE,
        LOCAL
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Music music = (Music) o;

        if (id != music.id) return false;
        if (artistId != music.artistId) return false;
        if (albumId != music.albumId) return false;
        if (duration != music.duration) return false;
        if (fileSize != music.fileSize) return false;
        if (type != music.type) return false;
        if (title != null ? !title.equals(music.title) : music.title != null) return false;
        if (artist != null ? !artist.equals(music.artist) : music.artist != null) return false;
        if (album != null ? !album.equals(music.album) : music.album != null) return false;
        if (uri != null ? !uri.equals(music.uri) : music.uri != null) return false;
        if (lrcPath != null ? !lrcPath.equals(music.lrcPath) : music.lrcPath != null) return false;
        if (coverUri != null ? !coverUri.equals(music.coverUri) : music.coverUri != null)
            return false;
        if (fileName != null ? !fileName.equals(music.fileName) : music.fileName != null)
            return false;
        if (cover != null ? !cover.equals(music.cover) : music.cover != null) return false;
        return year != null ? year.equals(music.year) : music.year == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (int) (artistId ^ (artistId >>> 32));
        result = 31 * result + (int) (albumId ^ (albumId >>> 32));
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        result = 31 * result + (lrcPath != null ? lrcPath.hashCode() : 0);
        result = 31 * result + (coverUri != null ? coverUri.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (cover != null ? cover.hashCode() : 0);
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
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
