package com.cyl.music_hnust.utils;

import android.os.Environment;

/**
 * Created by yonglong on 2016/5/8.
 */
public class Constants {
    //社区根目录(头像图片网址前缀)
    public static final String DEFAULT_IMGHEADER = "http://119.29.27.116/hcyl/music_BBS";

    //社区后台操作php
    public static final String DEFAULT_URL = "http://119.29.27.116/hcyl/music_BBS/operate.php?";

    //根据音乐id获取音乐详情接口
    public static final String DEFAULT_MUSIC_INTERFACE = "http://suen.pw/interface/music/api.php?operate=getInfo&&songID=";
   //根据关键词获取音乐接口
    public static final String DEFAULT_MUSIC_LIST_URL = "http://suen.pw/interface/music/api.php?operate=search&&key=";
    //音乐歌词根网址
    public static final String DEFAULT_MUSIC_LRCPATH = "http://tingapi.ting.baidu.com";
    //音乐歌词根网址
    public static final String DEFAULT_USERIMG_PATH = Environment.getExternalStorageDirectory() + "/hkmusic/cache/";

    //头像上传网址
    public static final String DEFAULT_IMG_UPLOAD = "http://119.29.27.116/hcyl/music_BBS/upload_file.php";

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

}
