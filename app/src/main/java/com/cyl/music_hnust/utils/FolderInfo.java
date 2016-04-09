package com.cyl.music_hnust.utils;

import java.util.List;

public class FolderInfo {

	private String musicFolder;// 歌曲隶属文件夹
	private List<MusicInfo> musicList;// 歌曲列表

	/**
	 * 获得文件夹路径名
	 *
	 * @return 文件夹路径名
	 */
	public String getMusicFolder() {
		return musicFolder;
	}

	/**
	 * 设置文件夹路径名
	 *
	 * @param musicFolder
	 *            文件夹路径名
	 */
	public void setMusicFolder(String musicFolder) {
		this.musicFolder = musicFolder;
	}

	/**
	 * 获得文件夹下的歌曲列表
	 *
	 * @return 歌曲列表
	 */
	public List<MusicInfo> getMusicList() {
		return musicList;
	}

	/**
	 * 设置文件夹下歌曲列表
	 *
	 * @param musicList
	 *            歌曲列表
	 */
	public void setMusicList(List<MusicInfo> musicList) {
		this.musicList = musicList;
	}

}