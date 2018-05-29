package com.cyl.musiclake.common;

import android.os.Environment;

/**
 * Created by yonglong on 2016/5/8.
 */
public class Constants {

    //QQ APP_ID
    public static final String APP_ID = "101454823";

    //社区根目录(头像图片网址前缀)
    public static final String DEFAULT_IMGHEADER = "http://119.29.27.116/hkmusic";

    //社区后台操作php
    public static final String DEFAULT_URL = "http://119.29.27.116/hkmusic/operate.php";
    public static final String LOGIN_URL = "http://119.29.27.116/hkmusic/login.php";
    public static final String REGISTER_URL = "http://119.29.27.116/hkmusic/register.php";
    public static final String UPLOAD_URL = "http://119.29.27.116/hkmusic/upload_file.php";

    //用户邮箱
    public static final String USER_EMAIL = "email";
    //用户登录密码
    public static final String PASSWORD = "password";
    //用户名
    public static final String USERNAME = "username";
    //性别
    public static final String USER_SEX = "user_sex";
    //性别
    public static final String USER_IMG = "user_img";
    public static final String USER_COLLEGE = "user_college";
    public static final String USER_MAJOR = "user_major";
    public static final String USER_CLASS = "user_class";
    public static final String NICK = "nick";
    public static final String PHONE = "phone";
    public static final String SECRET = "secret";


    //更新用户信息

    public static final String UPDATE_USER = "updateUserInfo";

    //用户id
    public static final String USER_ID = "user_id";
    //动态id
    public static final String SECRET_ID = "secret_id";
    //内容[动态内容|评论内容]
    public static final String CONTENT = "content";

    //功能
    public static final String FUNC = "func";
    //动态secret
    public static final String SECRET_ADD = "add";
    public static final String COMMENT_ADD = "addComment";
    public static final String GET_SECRET_LIST = "getSecretList";
    public static final String GET_MYSECRET_LIST = "getMySecretList";
    public static final String PAGENUM = "pageNum";
    //点赞
    public static final String CHANGE_AGREE = "changeAgree";
    //评论
    public static final String GET_COMMENT_LIST = "getCommentList";
    //位置信息
    public static final String LOCATION = "location";
    public static final String LAT = "lat";//location_latitude
    public static final String LON = "lon";//location_longitude
    //清理位置信息
    public static final String CLEARN = "clearLocation";
    //附近
    public static final String NEAR = "near";
    //摇一摇歌曲
    public static final String SONG_ADD = "addSong";
    public static final String SONG = "song";

    //下载
    public static final String UPDATEAPP = "updateApp";

    //歌单
    public static final String PLAYLIST_ID = "playlist";


    //根据音乐id获取音乐详情接口
    public static final String DEFAULT_MUSIC_INTERFACE = "http://suen.pw/interface/music/api.php?operate=getInfo&&songID=";
    //根据关键词获取音乐接口
    public static final String DEFAULT_MUSIC_LIST_URL = "http://suen.pw/interface/music/api.php?operate=search&&key=";

    //音乐歌词根网址
    public static final String DEFAULT_MUSIC_LRCPATH = "http://tingapi.ting.baidu.com";
    //图片地址
    public static final String DEFAULT_USERIMG_PATH = Environment.getExternalStorageDirectory() + "/hkmusic/cache/";

    //头像上传网址
    public static final String DEFAULT_IMG_UPLOAD = "http://119.29.27.116/hcyl/music_BBS/upload_file.php";

    public static final String FORMAT = "json";

    //在线音乐
    public static final String FILENAME_MP3 = ".mp3";
    public static final String FILENAME_LRC = ".lrc";
    public static final int MUSIC_LIST_SIZE = 10;

    public static final String BASE_URL = "http://musicapi.leanapp.cn/";//"/ting";


    public static final String BASE_MUSIC_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=android&version=5.8.2.0&channel=huwei&operator=1&method=baidu.ting.billboard.billCategory&format=json&kflag=2";

    public static final String PLAY_MUSIC_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "method=baidu.ting.song.play&songid=877578";


    /**
     * 本地保存地址
     */
    public static final String LOCALPATH = "/mnt/sdcard/hkmusic/";
    /**
     * 下载的线程数量
     */
    public static final int THREADCOUNT = 3;
    /**
     * 下载管理广播的action
     */
    public static final String DOWNLOADMANAGEACTION = "com.cyl.multithreadeddownload.DownloadActivity";


    public static final String DOWNLOAD_FILENAME = "hkmusic.apk";

    public static final String DEAULT_NOTIFICATION = "notification";
    public static final String TRANSTITION_ALBUM = "transition_album_art";

    /**
     * QQ音乐Api*************************************************
     */
    public static final String BASE_URL_QQ_MUSIC_SEARCH = "http://c.y.qq.com/soso/fcgi-bin/search_cp?";

    public static final String BASE_URL_QQ_MUSIC_URL = "http://dl.stream.qqmusic.qq.com/";
    public static final String BASE_URL_QQ_MUSIC_KEY = "https://c.y.qq.com/base/fcgi-bin/fcg_musicexpress.fcg?";

    /**
     * 虾米音乐Api*************************************************
     */
    public static final String BASE_XIAMI_URL = "http://api.xiami.com/";
    /**
     * 酷狗音乐Api*************************************************
     */
    public static final String BASE_KUGOU_URL = "http://lyrics.kugou.com/";
    /**
     * 百度音乐Api*************************************************
     */
    public static final String BASE_URL_BAIDU_MUSIC = "http://musicapi.qianqian.com/";
    public static final String METHOD_CATEGORY = "baidu.ting.billboard.billCategory";
    public static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    public static final String METHOD_SEARCH_SUGGESTION = "baidu.ting.search.suggestion";
    public static final String METHOD_MUSIC_INFO = "baidu.ting.song.getInfos";
    public static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
    public static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.common";

    public static final String PARAM_METHOD = "method";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_PAGESIZE = "page_size";
    public static final String PARAM_OFFSET = "offset";
    public static final String PARAM_SONG_ID = "songid";
    public static final String PARAM_TING_UID = "tinguid";
    public static final String PARAM_QUERY = "query";

    public static final String URL_GET_SONG_INFO = "http://music.baidu.com/data/music/links?songIds=";


    /**
     * 在线歌单接口Api*************************************************
     */

    public static final String BASE_PLAYER_URL = "https://player-node.zzsun.cc/";
    public static final String BASE_TEST_PLAYER_URL = "https://player-test.zzsun.cc/";
    //    public static final String BASE_NETEASE_URL = "http://192.168.123.44:3000";
    public static final String BASE_NETEASE_URL = "https://netease.api.zzsun.cc/";
    //    public static final String BASE_NETEASE_URL = "http://musicapi.leanapp.cn/";
    //bugly app_id
    public static final String BUG_APP_ID = "fd892b37ea";

    /**
     * 关于的GitHub地址
     */
    public static final String ABOUT_MUSIC_LAKE = "https://github.com/caiyonglong/MusicLake";
    public static final String ABOUT_MUSIC_LAKE_ISSUES = "https://github.com/caiyonglong/MusicLake/issues/new";
    public static final String ABOUT_MUSIC_LAKE_URL = "https://github.com/caiyonglong/MusicLake/blob/develop/README.md";

}
