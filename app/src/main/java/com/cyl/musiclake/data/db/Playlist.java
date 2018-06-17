//package com.cyl.musiclake.data.db;
//
//import com.cyl.musiclake.data.db.Music;
//
//import org.litepal.annotation.Column;
//import org.litepal.crud.DataSupport;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 功能：本地歌单
// * 作者：yonglong on 2016/9/13 21:59
// * 邮箱：643872807@qq.com
// * 版本：2.5
// */
//public class Playlist extends DataSupport implements Serializable {
//    //歌单id
//    private String pid;
//    //歌单名
//    private String name;
//    //歌单名
//    @Column(defaultValue = "0")
//    private int count;
//    //更新日期
//    private long updateDate;
//    //创建日期
//    private long date;
//    //描述
//    private String des;
//    @Column(defaultValue = "date asc")
//    private String order;
//    private String coverUrl;
//    //类型 0：本地歌单 1：在线歌单
//    private int type;
//
//    //歌曲集合
//    private List<Music> musicList = new ArrayList<>();
//
//    public Playlist() {
//    }
//
//    public Playlist(String id, String name) {
//        this.pid = id;
//        this.name = name;
//    }
//
//    public Playlist(String id, String name, int count, long date, String order) {
//        this.pid = id;
//        this.name = name;
//        this.count = count;
//        this.date = date;
//        this.order = order;
//    }
//
//    public long getUpdateDate() {
//        return updateDate;
//    }
//
//    public void setUpdateDate(long updateDate) {
//        this.updateDate = updateDate;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public String getId() {
//        return pid;
//    }
//
//    public void setId(String id) {
//        this.pid = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    public long getDate() {
//        return date;
//    }
//
//    public void setDate(long date) {
//        this.date = date;
//    }
//
//    public String getDes() {
//        return des;
//    }
//
//    public void setDes(String des) {
//        this.des = des;
//    }
//
//    public String getOrder() {
//        return order;
//    }
//
//    public void setOrder(String order) {
//        this.order = order;
//    }
//
//    public String getCoverUrl() {
//        return coverUrl;
//    }
//
//    public void setCoverUrl(String coverUrl) {
//        this.coverUrl = coverUrl;
//    }
//
//    public List<Music> getMusicList() {
//        return musicList;
//    }
//
//    public void setMusicList(List<Music> musicList) {
//        this.musicList = musicList;
//    }
//
//    @Override
//    public String toString() {
//        return name;
//    }
//}
