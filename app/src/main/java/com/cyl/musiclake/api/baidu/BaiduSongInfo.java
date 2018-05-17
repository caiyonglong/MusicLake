package com.cyl.musiclake.api.baidu;

import java.util.List;

/**
 * 作者：yonglong on 2016/9/9 04:08
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class BaiduSongInfo {

    /**
     * errorCode : 22000
     * data : {"xcode":"ad6e8c055ff4dfd9261032699fa1be7e","songList":[{"queryId":"569080829","songId":569080829,"songName":"无问西东","artistId":"45561","artistName":"王菲","albumId":569080827,"albumName":"无问西东","songPicSmall":"http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_90,h_90","songPicBig":"http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_150,h_150","songPicRadio":"http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_300,h_300","lrcLink":"http://qukufile2.qianqian.com/data2/lrc/ef80d282b94f37e92bee6e5b9b417124/569080826/569080826.lrc","version":"影视原声","copyType":0,"time":290,"linkCode":22000,"songLink":"http://zhangmenshiting.qianqian.com/data2/music/b008dff3c9df62831582ddea13f64921/569080852/569080829104400128.mp3?xcode=ad6e8c055ff4dfd92bb21879f3eb947d","showLink":"http://zhangmenshiting.qianqian.com/data2/music/b008dff3c9df62831582ddea13f64921/569080852/569080829104400128.mp3?xcode=ad6e8c055ff4dfd92bb21879f3eb947d","format":"mp3","rate":128,"size":4638118,"relateStatus":"0","resourceType":"0","source":"web"}]}
     */

    private int errorCode;
    private DataBean data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * xcode : ad6e8c055ff4dfd9261032699fa1be7e
         * songList : [{"queryId":"569080829","songId":569080829,"songName":"无问西东","artistId":"45561","artistName":"王菲","albumId":569080827,"albumName":"无问西东","songPicSmall":"http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_90,h_90","songPicBig":"http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_150,h_150","songPicRadio":"http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_300,h_300","lrcLink":"http://qukufile2.qianqian.com/data2/lrc/ef80d282b94f37e92bee6e5b9b417124/569080826/569080826.lrc","version":"影视原声","copyType":0,"time":290,"linkCode":22000,"songLink":"http://zhangmenshiting.qianqian.com/data2/music/b008dff3c9df62831582ddea13f64921/569080852/569080829104400128.mp3?xcode=ad6e8c055ff4dfd92bb21879f3eb947d","showLink":"http://zhangmenshiting.qianqian.com/data2/music/b008dff3c9df62831582ddea13f64921/569080852/569080829104400128.mp3?xcode=ad6e8c055ff4dfd92bb21879f3eb947d","format":"mp3","rate":128,"size":4638118,"relateStatus":"0","resourceType":"0","source":"web"}]
         */

        private String xcode;
        private List<SongListBean> songList;

        public String getXcode() {
            return xcode;
        }

        public void setXcode(String xcode) {
            this.xcode = xcode;
        }

        public List<SongListBean> getSongList() {
            return songList;
        }

        public void setSongList(List<SongListBean> songList) {
            this.songList = songList;
        }

        public static class SongListBean {
            /**
             * queryId : 569080829
             * songId : 569080829
             * songName : 无问西东
             * artistId : 45561
             * artistName : 王菲
             * albumId : 569080827
             * albumName : 无问西东
             * songPicSmall : http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_90,h_90
             * songPicBig : http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_150,h_150
             * songPicRadio : http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_300,h_300
             * lrcLink : http://qukufile2.qianqian.com/data2/lrc/ef80d282b94f37e92bee6e5b9b417124/569080826/569080826.lrc
             * version : 影视原声
             * copyType : 0
             * time : 290
             * linkCode : 22000
             * songLink : http://zhangmenshiting.qianqian.com/data2/music/b008dff3c9df62831582ddea13f64921/569080852/569080829104400128.mp3?xcode=ad6e8c055ff4dfd92bb21879f3eb947d
             * showLink : http://zhangmenshiting.qianqian.com/data2/music/b008dff3c9df62831582ddea13f64921/569080852/569080829104400128.mp3?xcode=ad6e8c055ff4dfd92bb21879f3eb947d
             * format : mp3
             * rate : 128
             * size : 4638118
             * relateStatus : 0
             * resourceType : 0
             * source : web
             */

            private String queryId;
            private String songId;
            private String songName;
            private String artistId;
            private String artistName;
            private long albumId;
            private String albumName;
            private String songPicSmall;
            private String songPicBig;
            private String songPicRadio;
            private String lrcLink;
            private String version;
            private int copyType;
            private int time;
            private int linkCode;
            private String songLink;
            private String showLink;
            private String format;
            private int rate;
            private long size;
            private String relateStatus;
            private String resourceType;
            private String source;

            public String getQueryId() {
                return queryId;
            }

            public void setQueryId(String queryId) {
                this.queryId = queryId;
            }

            public String getSongId() {
                return songId;
            }

            public void setSongId(String songId) {
                this.songId = songId;
            }

            public String getSongName() {
                return songName;
            }

            public void setSongName(String songName) {
                this.songName = songName;
            }

            public String getArtistId() {
                return artistId;
            }

            public void setArtistId(String artistId) {
                this.artistId = artistId;
            }

            public String getArtistName() {
                return artistName;
            }

            public void setArtistName(String artistName) {
                this.artistName = artistName;
            }

            public long getAlbumId() {
                return albumId;
            }

            public void setAlbumId(long albumId) {
                this.albumId = albumId;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String albumName) {
                this.albumName = albumName;
            }

            public String getSongPicSmall() {
                return songPicSmall;
            }

            public void setSongPicSmall(String songPicSmall) {
                this.songPicSmall = songPicSmall;
            }

            public String getSongPicBig() {
                return songPicBig;
            }

            public void setSongPicBig(String songPicBig) {
                this.songPicBig = songPicBig;
            }

            public String getSongPicRadio() {
                return songPicRadio;
            }

            public void setSongPicRadio(String songPicRadio) {
                this.songPicRadio = songPicRadio;
            }

            public String getLrcLink() {
                return lrcLink;
            }

            public void setLrcLink(String lrcLink) {
                this.lrcLink = lrcLink;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public int getCopyType() {
                return copyType;
            }

            public void setCopyType(int copyType) {
                this.copyType = copyType;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public int getLinkCode() {
                return linkCode;
            }

            public void setLinkCode(int linkCode) {
                this.linkCode = linkCode;
            }

            public String getSongLink() {
                return songLink;
            }

            public void setSongLink(String songLink) {
                this.songLink = songLink;
            }

            public String getShowLink() {
                return showLink;
            }

            public void setShowLink(String showLink) {
                this.showLink = showLink;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public int getRate() {
                return rate;
            }

            public void setRate(int rate) {
                this.rate = rate;
            }

            public long getSize() {
                return size;
            }

            public void setSize(long size) {
                this.size = size;
            }

            public String getRelateStatus() {
                return relateStatus;
            }

            public void setRelateStatus(String relateStatus) {
                this.relateStatus = relateStatus;
            }

            public String getResourceType() {
                return resourceType;
            }

            public void setResourceType(String resourceType) {
                this.resourceType = resourceType;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }
        }
    }
}
