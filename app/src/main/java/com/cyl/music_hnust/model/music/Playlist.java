package com.cyl.music_hnust.model.music;

/**
 * 功能：本地歌单
 * 作者：yonglong on 2016/9/13 21:59
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class Playlist {
    //歌单id
    private String id ;
    //歌单名
    private String name ;
    //歌单名
    private int count ;

    public Playlist(String id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
