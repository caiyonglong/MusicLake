package com.cyl.musiclake.ui.download.model;

/**
 * 
 * 项目名称：MultithreadedDownload 类名称：FileState 类描述： 下载文件的状态信息 创建人：wpy
 * 创建时间：2014-10-11 下午5:16:55
 * 
 */
public class FileState {

	private String name;// 文件名称
	private String url;// 下载地址
	private int state;// 当前的下载状态 1 正在下载 0 已下载
	private int completeSize;// 下载的完成进度
	private int fileSize;// 文件的长度

	/**
	 * 
	 * @param name
	 *            文件名称
	 * @param url
	 *            下载地址
	 * @param state
	 *            当前的下载状态 1 正在下载 0 已下载
	 * @param completeSize
	 *            下载的完成进度
	 * @param fileSize
	 *            文件的长度
	 */
	public FileState(String name, String url, int state, int completeSize,
			int fileSize) {
		this.name = name;
		this.url = url;
		this.state = state;
		this.completeSize = completeSize;
		this.fileSize = fileSize;
	}

	public FileState() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCompleteSize() {
		return completeSize;
	}

	public void setCompleteSize(int completeSize) {
		this.completeSize = completeSize;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public String toString() {
		return "FileState [name=" + name + ", url=" + url + ", state=" + state
				+ ", completeSize=" + completeSize + ", fileSize=" + fileSize
				+ "]";
	}

}
