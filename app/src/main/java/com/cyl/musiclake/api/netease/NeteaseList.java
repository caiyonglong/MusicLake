package com.cyl.musiclake.api.netease;

import java.util.List;

/**
 * Created by master on 2018/3/20.
 */

public class NeteaseList {

    /**
     * subscribers : []
     * subscribed : false
     * creator : {"defaultAvatar":false,"province":110000,"authStatus":1,"followed":false,"avatarUrl":"http://p1.music.126.net/QWMV-Ru_6149AKe0mCBXKg==/1420569024374784.jpg","accountStatus":0,"gender":1,"city":110101,"birthday":-2209017600000,"userId":1,"userType":2,"nickname":"网易云音乐","signature":"欢迎使用网易云音乐，有任何问题可以联系@云音乐客服， 我们会尽快答复。有关独立音乐人和独立厂牌请站内私信@原创君，DJ入驻联系邮箱： yunmusic@163.com","description":"网易云音乐官方账号","detailDescription":"网易云音乐官方账号","avatarImgId":1420569024374784,"backgroundImgId":2002210674180202,"backgroundUrl":"http://p1.music.126.net/pmHS4fcQtcNEGewNb5HRhg==/2002210674180202.jpg","authority":3,"mutual":false,"expertTags":null,"experts":null,"djStatus":10,"vipType":11,"remarkName":null,"backgroundImgIdStr":"2002210674180202","avatarImgIdStr":"1420569024374784"}
     * artists : null
     * highQuality : false
     * coverImgUrl : http://p1.music.126.net/GhhuF6Ep5Tq9IEvLsyCN7w==/18708190348409091.jpg
     * playCount : 1694660096
     * anonimous : false
     * ordered : true
     * tags : []
     * status : 0
     * trackUpdateTime : 1521446410721
     * trackCount : 196
     * updateTime : 1521079202746
     * commentThreadId : A_PL_0_3778678
     * totalDuration : 0
     * coverImgId : 18708190348409092
     * createTime : 1378721406014
     * userId : 1
     * newImported : false
     * specialType : 10
     * privacy : 0
     * description : 云音乐热歌榜：云音乐用户一周内收听所有线上歌曲 官方TOP排行榜，每周四更新。
     * adType : 0
     * cloudTrackCount : 0
     * subscribedCount : 2447608
     * trackNumberUpdateTime : 1521079202561
     * name : 云音乐热歌榜
     * id : 3778678
     * shareCount : 14472
     * coverImgId_str : 18708190348409091
     * ToplistType : H
     * commentCount : 172552
     */
    private boolean subscribed;
    private CreatorBean creator;
    private Object artists;
    private boolean highQuality;
    private String coverImgUrl;
    private int playCount;
    private boolean anonimous;
    private boolean ordered;
    private int status;
    private long trackUpdateTime;
    private int trackCount;
    private long updateTime;
    private String commentThreadId;
    private int totalDuration;
    private long coverImgId;
    private long createTime;
    private int userId;
    private boolean newImported;
    private int specialType;
    private int privacy;
    private String description;
    private int adType;
    private int cloudTrackCount;
    private int subscribedCount;
    private long trackNumberUpdateTime;
    private String name;
    private int id;
    private int shareCount;
    private String coverImgId_str;
    private String ToplistType;
    private int commentCount;
    private List<?> subscribers;
    private List<?> tags;
    private List<NeteaseMusic> tracks;

    public List<NeteaseMusic> getTracks() {
        return tracks;
    }

    public void setTracks(List<NeteaseMusic> tracks) {
        this.tracks = tracks;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
    }

    public Object getArtists() {
        return artists;
    }

    public void setArtists(Object artists) {
        this.artists = artists;
    }

    public boolean isHighQuality() {
        return highQuality;
    }

    public void setHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public boolean isAnonimous() {
        return anonimous;
    }

    public void setAnonimous(boolean anonimous) {
        this.anonimous = anonimous;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTrackUpdateTime() {
        return trackUpdateTime;
    }

    public void setTrackUpdateTime(long trackUpdateTime) {
        this.trackUpdateTime = trackUpdateTime;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCommentThreadId() {
        return commentThreadId;
    }

    public void setCommentThreadId(String commentThreadId) {
        this.commentThreadId = commentThreadId;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public long getCoverImgId() {
        return coverImgId;
    }

    public void setCoverImgId(long coverImgId) {
        this.coverImgId = coverImgId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isNewImported() {
        return newImported;
    }

    public void setNewImported(boolean newImported) {
        this.newImported = newImported;
    }

    public int getSpecialType() {
        return specialType;
    }

    public void setSpecialType(int specialType) {
        this.specialType = specialType;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public int getCloudTrackCount() {
        return cloudTrackCount;
    }

    public void setCloudTrackCount(int cloudTrackCount) {
        this.cloudTrackCount = cloudTrackCount;
    }

    public int getSubscribedCount() {
        return subscribedCount;
    }

    public void setSubscribedCount(int subscribedCount) {
        this.subscribedCount = subscribedCount;
    }

    public long getTrackNumberUpdateTime() {
        return trackNumberUpdateTime;
    }

    public void setTrackNumberUpdateTime(long trackNumberUpdateTime) {
        this.trackNumberUpdateTime = trackNumberUpdateTime;
    }

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

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getCoverImgId_str() {
        return coverImgId_str;
    }

    public void setCoverImgId_str(String coverImgId_str) {
        this.coverImgId_str = coverImgId_str;
    }

    public String getToplistType() {
        return ToplistType;
    }

    public void setToplistType(String ToplistType) {
        this.ToplistType = ToplistType;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<?> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<?> subscribers) {
        this.subscribers = subscribers;
    }

    public List<?> getTags() {
        return tags;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
    }

    public static class CreatorBean {
        /**
         * defaultAvatar : false
         * province : 110000
         * authStatus : 1
         * followed : false
         * avatarUrl : http://p1.music.126.net/QWMV-Ru_6149AKe0mCBXKg==/1420569024374784.jpg
         * accountStatus : 0
         * gender : 1
         * city : 110101
         * birthday : -2209017600000
         * userId : 1
         * userType : 2
         * nickname : 网易云音乐
         * signature : 欢迎使用网易云音乐，有任何问题可以联系@云音乐客服， 我们会尽快答复。有关独立音乐人和独立厂牌请站内私信@原创君，DJ入驻联系邮箱： yunmusic@163.com
         * description : 网易云音乐官方账号
         * detailDescription : 网易云音乐官方账号
         * avatarImgId : 1420569024374784
         * backgroundImgId : 2002210674180202
         * backgroundUrl : http://p1.music.126.net/pmHS4fcQtcNEGewNb5HRhg==/2002210674180202.jpg
         * authority : 3
         * mutual : false
         * expertTags : null
         * experts : null
         * djStatus : 10
         * vipType : 11
         * remarkName : null
         * backgroundImgIdStr : 2002210674180202
         * avatarImgIdStr : 1420569024374784
         */

        private boolean defaultAvatar;
        private int province;
        private int authStatus;
        private boolean followed;
        private String avatarUrl;
        private int accountStatus;
        private int gender;
        private int city;
        private long birthday;
        private int userId;
        private int userType;
        private String nickname;
        private String signature;
        private String description;
        private String detailDescription;
        private long avatarImgId;
        private long backgroundImgId;
        private String backgroundUrl;
        private int authority;
        private boolean mutual;
        private Object expertTags;
        private Object experts;
        private int djStatus;
        private int vipType;
        private Object remarkName;
        private String backgroundImgIdStr;
        private String avatarImgIdStr;

        public boolean isDefaultAvatar() {
            return defaultAvatar;
        }

        public void setDefaultAvatar(boolean defaultAvatar) {
            this.defaultAvatar = defaultAvatar;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getAuthStatus() {
            return authStatus;
        }

        public void setAuthStatus(int authStatus) {
            this.authStatus = authStatus;
        }

        public boolean isFollowed() {
            return followed;
        }

        public void setFollowed(boolean followed) {
            this.followed = followed;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(int accountStatus) {
            this.accountStatus = accountStatus;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDetailDescription() {
            return detailDescription;
        }

        public void setDetailDescription(String detailDescription) {
            this.detailDescription = detailDescription;
        }

        public long getAvatarImgId() {
            return avatarImgId;
        }

        public void setAvatarImgId(long avatarImgId) {
            this.avatarImgId = avatarImgId;
        }

        public long getBackgroundImgId() {
            return backgroundImgId;
        }

        public void setBackgroundImgId(long backgroundImgId) {
            this.backgroundImgId = backgroundImgId;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public void setBackgroundUrl(String backgroundUrl) {
            this.backgroundUrl = backgroundUrl;
        }

        public int getAuthority() {
            return authority;
        }

        public void setAuthority(int authority) {
            this.authority = authority;
        }

        public boolean isMutual() {
            return mutual;
        }

        public void setMutual(boolean mutual) {
            this.mutual = mutual;
        }

        public Object getExpertTags() {
            return expertTags;
        }

        public void setExpertTags(Object expertTags) {
            this.expertTags = expertTags;
        }

        public Object getExperts() {
            return experts;
        }

        public void setExperts(Object experts) {
            this.experts = experts;
        }

        public int getDjStatus() {
            return djStatus;
        }

        public void setDjStatus(int djStatus) {
            this.djStatus = djStatus;
        }

        public int getVipType() {
            return vipType;
        }

        public void setVipType(int vipType) {
            this.vipType = vipType;
        }

        public Object getRemarkName() {
            return remarkName;
        }

        public void setRemarkName(Object remarkName) {
            this.remarkName = remarkName;
        }

        public String getBackgroundImgIdStr() {
            return backgroundImgIdStr;
        }

        public void setBackgroundImgIdStr(String backgroundImgIdStr) {
            this.backgroundImgIdStr = backgroundImgIdStr;
        }

        public String getAvatarImgIdStr() {
            return avatarImgIdStr;
        }

        public void setAvatarImgIdStr(String avatarImgIdStr) {
            this.avatarImgIdStr = avatarImgIdStr;
        }
    }
}
