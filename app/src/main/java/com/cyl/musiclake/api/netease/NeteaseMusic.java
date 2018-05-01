package com.cyl.musiclake.api.netease;

import java.io.Serializable;
import java.util.List;

/**
 * Created by master on 2018/3/20.
 */

public class NeteaseMusic implements Serializable {
    /**
     * name : 海阔天空
     * id : 400162138
     * position : 4
     * alias : []
     * status : 0
     * fee : 8
     * copyrightId : 7002
     * disc : 1
     * no : 4
     * artists : [{"name":"Beyond","id":11127,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]
     * album : {"name":"华纳23周年纪念精选系列","id":34430029,"type":"专辑","size":14,"picId":3273246124149810,"blurPicUrl":"http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg","companyId":0,"pic":3273246124149810,"picUrl":"http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg","publishTime":999273600007,"description":"","tags":"","company":"华纳唱片","briefDesc":"","artist":{"name":"","id":0,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0},"songs":[],"alias":[],"status":3,"copyrightId":7002,"commentThreadId":"R_AL_3_34430029","artists":[{"name":"Beyond","id":11127,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]}
     * starred : false
     * popularity : 100.0
     * score : 100
     * starredNum : 0
     * duration : 323693
     * playedNum : 0
     * dayPlays : 0
     * hearTime : 0
     * ringtone : null
     * crbt : null
     * audition : null
     * copyFrom :
     * commentThreadId : R_SO_4_400162138
     * rtUrl : null
     * ftype : 0
     * rtUrls : []
     * copyright : 1
     * hMusic : {"name":null,"id":1186538478,"size":12950508,"extension":"mp3","sr":44100,"dfsId":3274345635867873,"bitrate":320000,"playTime":323693,"volumeDelta":-1.11}
     * mMusic : {"name":null,"id":1186538479,"size":6475276,"extension":"mp3","sr":44100,"dfsId":3274345635867874,"bitrate":160000,"playTime":323693,"volumeDelta":-0.69}
     * lMusic : {"name":null,"id":1186538480,"size":3885183,"extension":"mp3","sr":44100,"dfsId":3274345635867875,"bitrate":96000,"playTime":323693,"volumeDelta":-0.72}
     * bMusic : {"name":null,"id":1186538480,"size":3885183,"extension":"mp3","sr":44100,"dfsId":3274345635867875,"bitrate":96000,"playTime":323693,"volumeDelta":-0.72}
     * mvid : 0
     * rtype : 0
     * rurl : null
     * mp3Url : http://m2.music.126.net/RLt9aRW7UaHB77IPjeEUGw==/3274345635867875.mp3
     */

    private String name;
    private int id;
    private int position;
    private int status;
    private int fee;
    private int copyrightId;
    private String disc;
    private int no;
    private AlbumBean al;
    private boolean starred;
    private double popularity;
    private int score;
    private int starredNum;
    private int duration;
    private int playedNum;
    private int dayPlays;
    private int hearTime;
    private Object ringtone;
    private Object crbt;
    private Object audition;
    private String copyFrom;
    private String commentThreadId;
    private Object rtUrl;
    private int ftype;
    private int copyright;
    private HMusicBean hMusic;
    private MMusicBean mMusic;
    private LMusicBean lMusic;
    private BMusicBean bMusic;
    private int mvid;
    private int rtype;
    private Object rurl;
    private String mp3Url;
    private List<?> alias;
    private List<ArtistsBeanX> ar;
    private List<?> rtUrls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getCopyrightId() {
        return copyrightId;
    }

    public void setCopyrightId(int copyrightId) {
        this.copyrightId = copyrightId;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public AlbumBean getAlbum() {
        return al;
    }

    public void setAlbum(AlbumBean al) {
        this.al = al;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStarredNum() {
        return starredNum;
    }

    public void setStarredNum(int starredNum) {
        this.starredNum = starredNum;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlayedNum() {
        return playedNum;
    }

    public void setPlayedNum(int playedNum) {
        this.playedNum = playedNum;
    }

    public int getDayPlays() {
        return dayPlays;
    }

    public void setDayPlays(int dayPlays) {
        this.dayPlays = dayPlays;
    }

    public int getHearTime() {
        return hearTime;
    }

    public void setHearTime(int hearTime) {
        this.hearTime = hearTime;
    }

    public Object getRingtone() {
        return ringtone;
    }

    public void setRingtone(Object ringtone) {
        this.ringtone = ringtone;
    }

    public Object getCrbt() {
        return crbt;
    }

    public void setCrbt(Object crbt) {
        this.crbt = crbt;
    }

    public Object getAudition() {
        return audition;
    }

    public void setAudition(Object audition) {
        this.audition = audition;
    }

    public String getCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    public String getCommentThreadId() {
        return commentThreadId;
    }

    public void setCommentThreadId(String commentThreadId) {
        this.commentThreadId = commentThreadId;
    }

    public Object getRtUrl() {
        return rtUrl;
    }

    public void setRtUrl(Object rtUrl) {
        this.rtUrl = rtUrl;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public HMusicBean getHMusic() {
        return hMusic;
    }

    public void setHMusic(HMusicBean hMusic) {
        this.hMusic = hMusic;
    }

    public MMusicBean getMMusic() {
        return mMusic;
    }

    public void setMMusic(MMusicBean mMusic) {
        this.mMusic = mMusic;
    }

    public LMusicBean getLMusic() {
        return lMusic;
    }

    public void setLMusic(LMusicBean lMusic) {
        this.lMusic = lMusic;
    }

    public BMusicBean getBMusic() {
        return bMusic;
    }

    public void setBMusic(BMusicBean bMusic) {
        this.bMusic = bMusic;
    }

    public int getMvid() {
        return mvid;
    }

    public void setMvid(int mvid) {
        this.mvid = mvid;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public Object getRurl() {
        return rurl;
    }

    public void setRurl(Object rurl) {
        this.rurl = rurl;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public List<?> getAlias() {
        return alias;
    }

    public void setAlias(List<?> alias) {
        this.alias = alias;
    }

    public List<ArtistsBeanX> getArtists() {
        return ar;
    }

    public String getAuthors() {
        if (ar.size() == 0) return null;

        StringBuilder artist = new StringBuilder(ar.get(0).getName());
        for (int i = 1; i < ar.size(); i++) {
            artist.append(",").append(ar.get(i).getName());
        }
        return artist.toString();
    }

    public String getAuthorIds() {
        if (ar.size() == 0) return null;

        StringBuilder artist = new StringBuilder(ar.get(0).getId());
        for (int i = 1; i < ar.size(); i++) {
            artist.append(",").append(ar.get(i).getId());
        }
        return artist.toString();
    }


    public void setArtists(List<ArtistsBeanX> ar) {
        this.ar = ar;
    }

    public List<?> getRtUrls() {
        return rtUrls;
    }

    public void setRtUrls(List<?> rtUrls) {
        this.rtUrls = rtUrls;
    }

    public static class AlbumBean implements Serializable {
        /**
         * name : 华纳23周年纪念精选系列
         * id : 34430029
         * type : 专辑
         * size : 14
         * picId : 3273246124149810
         * blurPicUrl : http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg
         * companyId : 0
         * pic : 3273246124149810
         * picUrl : http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg
         * publishTime : 999273600007
         * description :
         * tags :
         * company : 华纳唱片
         * briefDesc :
         * artist : {"name":"","id":0,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}
         * songs : []
         * alias : []
         * status : 3
         * copyrightId : 7002
         * commentThreadId : R_AL_3_34430029
         * artists : [{"name":"Beyond","id":11127,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]
         */

        private String name;
        private int id;
        private String type;
        private int size;
        private long picId;
        private String blurPicUrl;
        private int companyId;
        private long pic;
        private String picUrl;
        private long publishTime;
        private String description;
        private String tags;
        private String company;
        private String briefDesc;
        private ArtistBean artist;
        private int status;
        private int copyrightId;
        private String commentThreadId;
        private List<?> songs;
        private List<?> alias;
        private List<ArtistsBean> artists;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getPicId() {
            return picId;
        }

        public void setPicId(long picId) {
            this.picId = picId;
        }

        public String getBlurPicUrl() {
            return blurPicUrl;
        }

        public void setBlurPicUrl(String blurPicUrl) {
            this.blurPicUrl = blurPicUrl;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public long getPic() {
            return pic;
        }

        public void setPic(long pic) {
            this.pic = pic;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
        }

        public ArtistBean getArtist() {
            return artist;
        }

        public void setArtist(ArtistBean artist) {
            this.artist = artist;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(int copyrightId) {
            this.copyrightId = copyrightId;
        }

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public void setCommentThreadId(String commentThreadId) {
            this.commentThreadId = commentThreadId;
        }

        public List<?> getSongs() {
            return songs;
        }

        public void setSongs(List<?> songs) {
            this.songs = songs;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsBean> artists) {
            this.artists = artists;
        }

        public static class ArtistBean implements Serializable {
            /**
             * name :
             * id : 0
             * picId : 0
             * img1v1Id : 0
             * briefDesc :
             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * albumSize : 0
             * alias : []
             * trans :
             * musicSize : 0
             */

            private String name;
            private int id;
            private int picId;
            private int img1v1Id;
            private String briefDesc;
            private String picUrl;
            private String img1v1Url;
            private int albumSize;
            private String trans;
            private int musicSize;
            private List<?> alias;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public int getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(int img1v1Id) {
                this.img1v1Id = img1v1Id;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public int getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(int albumSize) {
                this.albumSize = albumSize;
            }

            public String getTrans() {
                return trans;
            }

            public void setTrans(String trans) {
                this.trans = trans;
            }

            public int getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(int musicSize) {
                this.musicSize = musicSize;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }

        public static class ArtistsBean implements Serializable {
            /**
             * name : Beyond
             * id : 11127
             * picId : 0
             * img1v1Id : 0
             * briefDesc :
             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * albumSize : 0
             * alias : []
             * trans :
             * musicSize : 0
             */

            private String name;
            private int id;
            private int picId;
            private int img1v1Id;
            private String briefDesc;
            private String picUrl;
            private String img1v1Url;
            private int albumSize;
            private String trans;
            private int musicSize;
            private List<?> alias;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public int getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(int img1v1Id) {
                this.img1v1Id = img1v1Id;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public int getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(int albumSize) {
                this.albumSize = albumSize;
            }

            public String getTrans() {
                return trans;
            }

            public void setTrans(String trans) {
                this.trans = trans;
            }

            public int getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(int musicSize) {
                this.musicSize = musicSize;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }
    }

    public static class HMusicBean implements Serializable {
        /**
         * name : null
         * id : 1186538478
         * size : 12950508
         * extension : mp3
         * sr : 44100
         * dfsId : 3274345635867873
         * bitrate : 320000
         * playTime : 323693
         * volumeDelta : -1.11
         */

        private Object name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class MMusicBean implements Serializable {
        /**
         * name : null
         * id : 1186538479
         * size : 6475276
         * extension : mp3
         * sr : 44100
         * dfsId : 3274345635867874
         * bitrate : 160000
         * playTime : 323693
         * volumeDelta : -0.69
         */

        private Object name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class LMusicBean implements Serializable {
        /**
         * name : null
         * id : 1186538480
         * size : 3885183
         * extension : mp3
         * sr : 44100
         * dfsId : 3274345635867875
         * bitrate : 96000
         * playTime : 323693
         * volumeDelta : -0.72
         */

        private Object name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class BMusicBean implements Serializable {
        /**
         * name : null
         * id : 1186538480
         * size : 3885183
         * extension : mp3
         * sr : 44100
         * dfsId : 3274345635867875
         * bitrate : 96000
         * playTime : 323693
         * volumeDelta : -0.72
         */

        private Object name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class ArtistsBeanX implements Serializable {
        /**
         * name : Beyond
         * id : 11127
         * picId : 0
         * img1v1Id : 0
         * briefDesc :
         * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
         * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
         * albumSize : 0
         * alias : []
         * trans :
         * musicSize : 0
         */

        private String name;
        private int id;
        private int picId;
        private int img1v1Id;
        private String briefDesc;
        private String picUrl;
        private String img1v1Url;
        private int albumSize;
        private String trans;
        private int musicSize;
        private List<?> alias;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public int getImg1v1Id() {
            return img1v1Id;
        }

        public void setImg1v1Id(int img1v1Id) {
            this.img1v1Id = img1v1Id;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getImg1v1Url() {
            return img1v1Url;
        }

        public void setImg1v1Url(String img1v1Url) {
            this.img1v1Url = img1v1Url;
        }

        public int getAlbumSize() {
            return albumSize;
        }

        public void setAlbumSize(int albumSize) {
            this.albumSize = albumSize;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        public int getMusicSize() {
            return musicSize;
        }

        public void setMusicSize(int musicSize) {
            this.musicSize = musicSize;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }
    }
}
