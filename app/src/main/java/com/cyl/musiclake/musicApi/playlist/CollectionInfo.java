package com.cyl.musicapi.playlist;

import com.cyl.musiclake.musicApi.SourceData;

/**
 * Created by master on 2018/4/5.
 */

public class CollectionInfo {

    /**
     * song_id : 1769253963
     * vendor : xiami
     * sourceData : {"cp":false,"id":1769253963,"name":"认真的雪","album":{"id":358513,"name":"未完成的歌","cover":"https://pic.xiami.net/images/album/img44/7244/3585131260327414_2.jpg"},"source":"xiami","artists":[{"id":7244,"name":"薛之谦","avatar":"http://pic.xiami.net/images/artistlogo/24/14688337551624_1.jpg"}],"commentId":1769253963}
     */

    private String id;
    private String vendor;
    private SourceData sourceData;

    public CollectionInfo() {
    }

    public CollectionInfo(String song_id, String vendor) {
        this.id = song_id;
        this.vendor = vendor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public SourceData getSourceData() {
        return sourceData;
    }

    public void setSourceData(SourceData sourceData) {
        this.sourceData = sourceData;
    }


    @Override
    public String toString() {
        return "MusicInfo{" +
                "song_id='" + id + '\'' +
                ", vendor='" + vendor + '\'' +
                ", sourceData=" + sourceData +
                '}';
    }
}
