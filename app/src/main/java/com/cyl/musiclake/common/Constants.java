package com.cyl.musiclake.common;

/**
 * Created by yonglong on 2016/5/8.
 */
public class Constants {
    //歌曲操作区分
    public static final int OP_LOCAL = 0; //没有歌曲下载、删除、修改(后续会有)、添加到歌单
    public static final int OP_ONLINE = 1; //没有歌曲删除、修改、添加到歌单
    public static final int OP_PLAYLIST = 2;//修改(后续会有)、添加到歌单

    public static final String WEIBO = "weibo";
    //歌曲类型
    public static final String LOCAL = "local";
    public static final String QQ = "qq";
    public static final String XIAMI = "xiami";
    public static final String BAIDU = "baidu";
    public static final String NETEASE = "netease";
    public static final String VIDEO = "video";//本地视频
    public static final String YOUTUBE = "youtube";//YouTube

    //特殊歌单类型
    public static final String PLAYLIST_LOVE_ID = "love";
    public static final String PLAYLIST_HISTORY_ID = "history";
    public static final String PLAYLIST_LOCAL_ID = "local";
    public static final String PLAYLIST_QUEUE_ID = "queue";
    public static final String PLAYLIST_DOWNLOAD_ID = "download";

    //百度歌单
    public static final String PLAYLIST_BD_ID = "playlist_bd";
    //网易云歌单
    public static final String PLAYLIST_WY_ID = "playlist_wy";
    public static final String PLAYLIST_WY_RECOMMEND_ID = "playlist_wy_recommend_songs";
    //QQ歌单
    public static final String PLAYLIST_QQ_ID = "playlist_qq";
    //虾米歌单
    public static final String PLAYLIST_XIA_MI_ID = "playlist_xm";
    //在线歌单
    public static final String PLAYLIST_CUSTOM_ID = "custom_online";
    //
    public static final String PLAYLIST_SEARCH_ID = "playlist_search";
    public static final String PLAYLIST_IMPORT_ID = "playlist_import";
    //百度电台列表
    public static final String BAIDU_RADIO_LIST = "baidu_radio_list";
    public static final String NETEASE_ARITIST_LIST = "netease_artist_list";


    /**
     * 搜索过滤
     */
    public static final String SP_KEY_SEARCH_FILTER_NETEASE = "sp_netease";
    public static final String SP_KEY_SEARCH_FILTER_QQ = "sp_netease";
    public static final String SP_KEY_SEARCH_FILTER_XIAMI = "sp_xiami";
    public static final String SP_KEY_SEARCH_FILTER_BAIDU = "sp_baidu";
    public static final String SP_KEY_SEARCH_FILTER_ = "sp_netease";
    public static final String SP_KEY_SONG_QUALITY = "song_quality";

    //歌单操作
    public static final int PLAYLIST_ADD = 0;
    public static final int PLAYLIST_DELETE = 1;
    public static final int PLAYLIST_UPDATE = 2;
    public static final int PLAYLIST_RENAME = 3;

    //QQ APP_ID
    public static final String APP_ID = "101454823";

    //社区后台操作php
    public static final String DEFAULT_URL = "http://119.29.27.116/hkmusic/operate.php";
    public static final String LOGIN_URL = "http://119.29.27.116/hkmusic/login.php";
    public static final String REGISTER_URL = "http://119.29.27.116/hkmusic/register.php";
    public static final String UPLOAD_URL = "http://119.29.27.116/hkmusic/upload_file.php";

    //用户邮箱
    public static final String USER_EMAIL = "email";
    //用户登录密码
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String TOKEN_TIME = "token_time";
    public static final String LOGIN_STATUS = "login_status";
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
    //悬浮窗屏幕位置
    public static final String FLOAT_VIEW_X = "float_view_x";
    public static final String FLOAT_VIEW_Y = "float_view_y";


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

    //摇一摇歌曲
    public static final String SONG_ADD = "addSong";
    public static final String SONG = "song";

    //是否是缓存
    public static final String KEY_IS_CACHE = "is_cache";

    //歌单
    public static final String PLAYLIST_ID = "playlist";

    public static final String IS_URL_HEADER = "http";

    public static final String TEXT_PLAIN = "text/plain";

    /**
     * 悬浮窗权限requestCode
     */
    public static final int REQUEST_CODE_FLOAT_WINDOW = 0x123;
    public static final int REQUEST_CODE_LOGIN = 10001;

    //在线音乐
    public static final String FILENAME_MP3 = ".mp3";
    public static final String FILENAME_LRC = ".lrc";
    public static final int MUSIC_LIST_SIZE = 10;

    public static final String BASE_MUSIC_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=android&version=5.8.2.0&channel=huwei&operator=1&method=baidu.ting.billboard.billCategory&format=json&kflag=2";

    public static final String DEAULT_NOTIFICATION = "notification";
    public static final String TRANSTITION_ALBUM = "transition_album_art";

    /**
     * QQ音乐Api*************************************************
     */
    public static final String BASE_URL_QQ_MUSIC_URL = "https://u.y.qq.com";

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

    public static final String URL_GET_SONG_INFO = "http://music.baidu.com/data/music/links?songIds=";

    /**
     * 在线歌单接口Api*************************************************
     */
    public static final String BASE_PLAYER_URL = "https://player.zzsun.cc/";
    /**
     * 网易云音乐接口
     */
    public static final String BASE_NETEASE_URL = "http://musiclake.leanapp.cn";
    /**
     * Bugly app_id
     */
    public static final String BUG_APP_ID = "fd892b37ea";

    /**
     * 关于的GitHub地址
     */
    public static final String ABOUT_MUSIC_LAKE = "https://github.com/caiyonglong/MusicLake";
    public static final String ABOUT_MUSIC_LAKE_ISSUES = "https://github.com/caiyonglong/MusicLake/issues/new";
    public static final String ABOUT_MUSIC_LAKE_PC = "https://github.com/sunzongzheng/music/releases";
    public static final String ABOUT_MUSIC_LAKE_URL = "https://github.com/caiyonglong/MusicLake/blob/develop/README.md";


    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String APP_KEY = "3338754271";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SCOPE = "";

    //    public static final String SOCKET_URL = "http://39.108.214.63:15003";
    public static final String SOCKET_URL = "https://socket.zzsun.cc";
    public static final String OAUTH_GITHUB = "oauth_github";
    public static final String OAUTH_QQ = "oauth_qq";
    public static final String OAUTH_WEIBO = "oauth_weibo";
    public static final String GITHUB_BASE_URL = "https://github.com";
    public static final String GITHUB_REDIRECT_URI = "musiclake://oauth";
    public static final String GITHUB_CLIENT_ID = "05baa9742e6a72d662a6";
    public static final String GITHUB_CLIENT_SECRET = "776c29a9eb0822505829483a1dfcd19812ac622f";
    public static final String GOOGLE_DEVELOPER_KEY = "AIzaSyCVdXoMhAPa1UdAyxKUK5Xp-uxWG612OEg";
}
