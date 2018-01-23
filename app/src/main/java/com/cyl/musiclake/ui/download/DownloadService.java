package com.cyl.musiclake.ui.download;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cyl.musiclake.ui.common.Constants;
import com.cyl.musiclake.ui.download.db.DBDao;
import com.cyl.musiclake.ui.download.model.DownloadTaskInfo;
import com.cyl.musiclake.ui.download.model.FileState;
import com.cyl.musiclake.utils.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


/**
 * 下载服务
 */
public class DownloadService extends Service {

    private static final String TAG = "DownloadService";
    // 下载器
    private DownloadTask downloadTask;

    private DownloadListener downloadListener;

    private DBDao dao;
    /**
     * 存放各个下载器
     */
    private Map<String, DownloadTask> downloadTasks = new HashMap<String, DownloadTask>();

    /**
     * 存放每个下载文件的总长度
     */
    private Map<String, Integer> sizeMap = new HashMap<String, Integer>();
    /**
     * 存放每个下载文件完成的长度
     */
    private Map<String, Integer> progressMap = new HashMap<String, Integer>();

    /**
     * 消息处理 接收Download中每个线程传输过来的数据
     */
    private DownloadHandler mHandler;

    @SuppressLint("HandlerLeak")
    private class DownloadHandler extends Handler {
        private final WeakReference<DownloadService> mService;

        public DownloadHandler(DownloadService service) {
            mService = new WeakReference<DownloadService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String url = (String) msg.obj;
                    int length = msg.arg1;
                    int progress = progressMap.get(url);
                    progress = progress + length;
                    progressMap.put(url, progress);
                    Log.e(TAG, "下载进度：" + progress);
                    int size = sizeMap.get(url);
                    dao.updateFileState(url, length);
                    // 下载完成
                    if (progress == size) {
                        dao.updateStateByUrl(url, 0);
                        if (downloadListener != null) {
                            downloadListener.onDownloadFinished(new File(FileUtils.getMusicDir() + url.substring(url.lastIndexOf("/"))));
                        }
                        downloadTasks.get(url).delete(url);
                        downloadTasks.remove(url);
                        // 更新下载管理的进度
                        if (downloadTasks.isEmpty()) {
                            // 如果全部下载完成，关闭service
                            stopSelf();
                        }
                    }
                    if (downloadListener != null) {
                        // 更新下载管理的进度
                        downloadListener.oDownloading(url, progress);
                    }
                    break;
            }
        }
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new DownloadHandler(this);
        dao = new DBDao(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String urlPath = intent.getStringExtra("downloadUrl");
        String name = intent.getStringExtra("name");
        String flag = intent.getStringExtra("flag");
        String mid = intent.getStringExtra("mid");
        if (flag.equals("start")) {
            startDownload(mid, name, urlPath, true);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    DownloadManagerBinder myBinder;

    @Override
    public IBinder onBind(Intent intent) {
        if (myBinder == null) {
            myBinder = new DownloadManagerBinder(this);
        }
        return myBinder;
    }


    public class DownloadManagerBinder extends Binder implements IDownloadService {
        private final WeakReference<DownloadService> mService;

        private DownloadManagerBinder(final DownloadService service) {
            mService = new WeakReference<DownloadService>(service);
        }

        @Override
        public void updateStatus(String mid, String name, String url) {
            mService.get().updateStatus(mid, name, url);
        }

        @Override
        public void addProgressCallBack(DownloadListener downloadListener) {
            mService.get().setDownloadListener(downloadListener);
        }
    }

    /**
     * 开始下载
     *
     * @param urlPath 下载地址
     */
    private void startDownload(final String mid, final String name, final String urlPath,
                               final boolean isFirst) {
        Log.e(TAG, "开始下载: mid =" + mid + " name= " + name
                + " url=" + urlPath + " isFist=" + isFirst);
        // 初始化一个下载器
        downloadTask = downloadTasks.get(urlPath);
        if (null == downloadTask) {
            downloadTask = new DownloadTask(name, urlPath,
                    Constants.THREADCOUNT, this, mHandler);
            downloadTasks.put(urlPath, downloadTask);
        }
        if (downloadTask.isDownloading()) {
            return;
        }
        //开启一个线程下载
        new Thread() {
            public void run() {
                DownloadTaskInfo downloadTaskInfo = downloadTask.getDownloaderInfos();
                progressMap.put(urlPath, downloadTaskInfo.getProgress());
                if (sizeMap.get(urlPath) == null) {
                    sizeMap.put(urlPath, downloadTaskInfo.getSize());
                }
                // FileState state = dao.query(urlPath);
                if (isFirst) {
                    Log.e(TAG, "第一次下载：" + name);
                    FileState fileState = new FileState(mid, name, urlPath, 1,
                            downloadTaskInfo.getProgress(),
                            downloadTaskInfo.getSize());
                    dao.saveFileState(fileState);
                }
                downloadTask.download();
            }
        }.start();
    }

    /**
     * 更改下载状态（若文件正在下载，就暂停；若暂停，则开始下载）
     *
     * @param url 下载地址
     */
    public void updateStatus(String mid, String name, String url) {
        Log.e(TAG, "更新下载状态：暂停|下载 " + name);
        DownloadTask task = downloadTasks.get(url);
        if (task != null) {
            if (task.isDownloading()) {// 正在下载
                task.setPause();
                dao.updateStateByUrl(url, 2);
            } else if (task.isPause()) {// 暂停
                task.reset();
                dao.updateStateByUrl(url, 1);
                startDownload(mid, name, url, false);
            }
        } else {
            dao.updateStateByUrl(url, 1);
            startDownload(mid, name, url, false);
        }
    }
}
