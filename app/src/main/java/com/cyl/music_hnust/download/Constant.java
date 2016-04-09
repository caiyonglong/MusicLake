package com.cyl.music_hnust.download;

/**
 * 
 * 项目名称：MultithreadedDownload 类名称：Constant 类描述： 常量 创建人：wpy 创建时间：2014-10-13
 * 上午10:23:13
 * 
 */
public class Constant {
	/**
	 * 下载的url地址
	 */
	public static final String URL1 = "http://downloadc.dewmobile.net/z/kuaiya282.apk";
	public static final String URL2 = "http://gdown.baidu.com/data/wisegame/1b9392eadc3bddf1/WeChat_480.apk";

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
