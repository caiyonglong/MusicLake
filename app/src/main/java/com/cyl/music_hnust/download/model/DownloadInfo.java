package com.cyl.music_hnust.download.model;

/**
 * 
 * 项目名称：MultithreadedDownload 类名称：DownloadInfo 类描述： 下载的信息 创建人：wpy
 * 创建时间：2014-10-10 下午3:57:21
 * 
 */
public class DownloadInfo {

	private int threadId;
	private int startPos;
	private int endPos;
	private int compeleteSize;
	private String url;

	/**
	 * 构造函数
	 * 
	 * @param threadId
	 *            下载器id/线程id
	 * @param compeleteSize
	 *            完成的进度
	 * @param url
	 *            下载器网络标识/下载地址
	 */
	public DownloadInfo(int threadId, int startPos, int endPos,
			int compeleteSize, String url) {
		this.threadId = threadId;
		this.startPos = startPos;
		this.endPos = endPos;
		this.compeleteSize = compeleteSize;
		this.url = url;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public int getCompeleteSize() {
		return compeleteSize;
	}

	public void setCompeleteSize(int compeleteSize) {
		this.compeleteSize = compeleteSize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
