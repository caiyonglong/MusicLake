package com.cyl.music_hnust.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


/**
 * 
 * 项目名称：MultithreadedDownload 类名称：Downloader 类描述： 下载器 创建人：wpy 创建时间：2014-10-11
 * 上午9:24:01
 * 
 */
public class Downloader {
	private String fileName;// 文件名称
	private String downloadPath;// 下载地址
	private String localPath;// 本地保存的地址
	private int threadCount;// 下载线程的数量
	private int fileSize;// 下载文件的大小
	private Context context;// 上下文
	private Handler mHandler;// 消息处理器
	private List<DownloadInfo> infos;// 存放下载信息的集合

	// 定义三种下载状态：初始化、下载中、暂停
	private static final int INIT = 1;
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;
	private int state = INIT;// 设置状态为初始化

	private SqliteDao dao;

	/**
	 * 构造函数
	 * 
	 * @param fileName
	 *            文件名称
	 * @param downloadPath
	 *            下载地址
	 * @param localPath
	 *            本地存储地址
	 * @param threadCount
	 *            线程数量
	 * @param context
	 *            上下文
	 * @param mHandler
	 *            消息处理器
	 */
	public Downloader(String fileName, String downloadPath, String localPath,
			int threadCount, Context context, Handler mHandler) {
		this.fileName = fileName;
		this.downloadPath = downloadPath;
		this.localPath = localPath;
		this.threadCount = threadCount;
		this.context = context;
		this.mHandler = mHandler;

		dao = new SqliteDao(context);
	}

	/**
	 * 获取下载器信息。 首先判断是否是第一次下载， 是第一次下载的话要进行初始化操作，并将下载器的信息保存到数据库中；
	 * 如果不是，就从数据库中读取之前下载的信息（起始位置，结束位置，文件大小等），并将下载信息返回给下载器。
	 * 
	 * @return 下载器信息(文件的大小、下载的完成度、下载器标识/下载地址)
	 */
	public DownloaderInfo getDownloaderInfos() {
		if (isFirstDownload(downloadPath)) {// 第一次下载

			init();
			if (fileSize > 0) {
				int range = fileSize / threadCount;

				Log.e("test>>", "每个线程下载的大小：" + range);

				infos = new ArrayList<DownloadInfo>();

				for (int i = 0; i < threadCount - 1; i++) {
					DownloadInfo info = new DownloadInfo(i, i * range, (i + 1)
							* range - 1, 0, downloadPath);

					Log.e("test>>", "线程<" + i + ">下载的大小：" + i * range + "---"
							+ ((i + 1) * range - 1));

					infos.add(info);
				}
				DownloadInfo info = new DownloadInfo(threadCount - 1,
						(threadCount - 1) * range, fileSize - 1, 0,
						downloadPath);

				Log.e("test>>", "线程<" + (threadCount - 1) + ">下载的大小："
						+ (threadCount - 1) * range + "---" + (fileSize - 1));

				infos.add(info);
				// 保存下载器信息到数据库
				dao.saveDownloadInfos(infos);

			}
			// 创建一个DownloaderInfo记录下载器的具体信息
			return new DownloaderInfo(fileSize, 0, downloadPath);

		} else {
			// 不是第一次下载，从数据库中获取已有的downloadPath下载地址的下载器的具体信息
			infos = dao.getDownloadInfos(downloadPath);
			int size = 0;// 文件总大小
			int completeSize = 0;// 下载的总长度
			for (DownloadInfo info : infos) {
				size = size + (info.getEndPos() - info.getStartPos() + 1);
				completeSize = completeSize + info.getCompeleteSize();
			}
			return new DownloaderInfo(size, completeSize, downloadPath);
		}

	}

	/**
	 * 初始化下载器(获取要下载文件的大小；根据本地地址，在本地创建一个相同大小的文件)
	 */
	private void init() {
		try {
			URL url = new URL(downloadPath);// 通过给定的下载地址得到一个url
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 得到一个http连接
			conn.setConnectTimeout(5 * 1000);// 设置连接超时为5秒钟
			conn.setRequestMethod("GET");// 设置连接方式为GET

			Log.e("test>>", "获取前  文件的大小：" + fileSize);

			int code = conn.getResponseCode();
			Log.e("test>>", "网络请求的返回码：" + code);
			// 如果http返回的代码是200或者206则为连接成功
			if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206) {
				fileSize = conn.getContentLength();// 得到文件的大小

				Log.e("test>>", "文件的大小：" + fileSize);

				if (fileSize <= 0) {
					Toast.makeText(context, "网络故障,无法获取文件大小", Toast.LENGTH_SHORT)
							.show();
				}
				File dir = new File(localPath);
				if (!dir.exists()) {// 文件不存在
					if (dir.mkdirs()) {
						System.out.println("mkdirs success.");
					}
				}
				File file = new File(localPath, fileName);
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.setLength(fileSize);
				accessFile.close();
			}

			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始下载数据
	 */
	public void download() {
		if (null != infos) {
			if (DOWNLOADING == state) {
				return;
			}
			state = DOWNLOADING;// 将下载状态设置为下载中
			for (DownloadInfo info : infos) {
				new DownloadThread(info.getThreadId(), info.getStartPos(),
						info.getEndPos(), info.getCompeleteSize(),
						info.getUrl()).start();
			}
		}
	}

	/**
	 * 
	 * 项目名称：MultithreadedDownload 类名称：DownloadThread 类描述： 一个内部类，建立一个下载线程 创建人：wpy
	 * 创建时间：2014-10-11 下午1:08:12
	 * 
	 */
	private class DownloadThread extends Thread {

		private int threadId;
		private int startPos;
		private int endPos;
		private int completeSize;
		private String urlPath;

		/**
		 * 下载线程类的构造函数
		 * 
		 * @param threadId
		 *            线程id
		 * @param startPos
		 *            开始下载的节点
		 * @param endPos
		 *            停止下载的节点
		 * @param completeSize
		 *            下载完成的进度
		 * @param urlPath
		 *            下载的地址
		 */
		public DownloadThread(int threadId, int startPos, int endPos,
				int completeSize, String urlPath) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.completeSize = completeSize;
			this.urlPath = urlPath;
		}

		@Override
		public void run() {
			HttpURLConnection connection = null;
			RandomAccessFile accessFile = null;
			InputStream inputStream = null;
			File file = new File(localPath, fileName);
			try {
				URL url = new URL(urlPath);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5 * 1000);
				connection.setRequestMethod("GET");

				// 设置http头中的Range字段，格式为：Range: bytes x - y
				// Range: 用于客户端到服务器端的请求，可通过该字段指定下载文件的某一段大小，及其单位。典型的格式如：
				// Range: bytes=0-499 下载第0-499字节范围的内容
				connection.setRequestProperty("Range", "bytes="
						+ (startPos + completeSize) + "-" + endPos);

				connection.connect();

				if (connection.getResponseCode() == 200
						|| connection.getResponseCode() == 206) {
					accessFile = new RandomAccessFile(file, "rwd");
					accessFile.seek(startPos + completeSize);// 设置从哪个位置写入数据

					inputStream = connection.getInputStream();
					byte[] buffer = new byte[4096];
					int length = -1;
					while ((length = inputStream.read(buffer)) != -1) {
						// 写入数据
						accessFile.write(buffer, 0, length);
						// 累加已经下载的长度
						completeSize = completeSize + length;
						// 更新数据中的信息
						dao.updataDownloadInfos(threadId, completeSize, urlPath);
						// 用消息将下载信息传给进度条，对进度条进行更新
						Message msg = Message.obtain();
						msg.what = 1;
						msg.obj = urlPath;
						msg.arg1 = length;
						mHandler.sendMessage(msg);// 给DownloadService发送消息

						// Log.e("test>>", "Downloader当前进度：" + completeSize);

						// 暂停
						if (PAUSE == state) {
							return;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭该关闭的东西
					inputStream.close();
					accessFile.close();
					connection.disconnect();
					// dao.closeDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断是否是第一次下载
	 * 
	 * @param downloadPath
	 *            下载地址
	 * @return true 第一次下载 false 再次下载
	 */
	private boolean isFirstDownload(String downloadPath) {
		return dao.isHasDownloadInfos(downloadPath);
	}

	/**
	 * 判断是否正在下载
	 * 
	 * @return true 是 false 否
	 */
	public boolean isDownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 判断是否暂停
	 * 
	 * @return true 是 false 否
	 */
	public boolean isPause() {
		return state == PAUSE;
	}

	/**
	 * 设置暂停
	 */
	public void setPause() {
		state = PAUSE;
	}

	/**
	 * 根据urlPath删除数据库中对应的下载器信息
	 * 
	 * @param urlPath
	 *            下载地址
	 */
	public void delete(String urlPath) {
		dao.delete(urlPath);
	}

	/**
	 * 重置下载状态
	 */
	public void reset() {
		state = INIT;
	}
}
