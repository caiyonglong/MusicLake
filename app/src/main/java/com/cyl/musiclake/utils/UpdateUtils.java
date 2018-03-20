package com.cyl.musiclake.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.bean.UpdateInfo;
import com.cyl.musiclake.common.Constants;

/**
 * 功能: 软件更新工具类
 * 作者：yonglong on 2016/9/22 14:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class UpdateUtils {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    //检查更新
    public static void checkUpdate(final Activity activity) {
//        if (version <= preVersion) {
        ToastUtils.show(mContext, "暂无更新");
//        } else {
//            updateDialog(activity, response);
//        }
//        OkHttpUtils
//                .post()
//                .url(Constants.DEFAULT_URL)
//                .addParams(Constants.FUNC, Constants.UPDATEAPP)
//                .build()
//                .execute(new UpdateCallback() {
//                             @Override
//                             public void onError(Call call, Exception e) {
//                                 ToastUtils.show(mContext, R.string.error_connection);
//                             }
//
//                             @Override
//                             public void onResponse(UpdateInfo response) {
//
//                                 double version = response.getVersion();
//                                 double preVersion = 0;
//                                 try {
//                                     preVersion = Double.parseDouble(getVersion());
//                                 } catch (PackageManager.NameNotFoundException e) {
//                                     e.printStackTrace();
//                                 }
//                                 if (version <= preVersion) {
//                                     ToastUtils.show(mContext, "暂无更新");
//                                 } else {
//                                     updateDialog(activity, response);
//                                 }
//                             }
//
//                         }
//
//                );
    }

    private static void updateDialog(final Activity activity, final UpdateInfo updateInfo) {
        String message = "版本号：" + updateInfo.getVersion() + "\n" +
                "更新日志: " + updateInfo.getChangelog();
        new MaterialDialog.Builder(activity)
                .title("发现新版本")
                .content(message)
                .positiveText("立即更新")
                .onPositive((dialog, which) -> download(activity, updateInfo))
                .onNegative(null)
                .negativeText("稍后更新")
                .show();
    }

    /**
     * 获取当前程序的版本号
     */
    public static String getVersion() throws PackageManager.NameNotFoundException {
        //获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        return packageInfo.versionName;
    }

    public static long sDownloadId = 0;

    private static void download(Activity activity, UpdateInfo updateInfo) {

        try {

            DownloadManager manager = (DownloadManager) mContext
                    .getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(updateInfo.download_url));
            request.setDescription("更新APP");
            request.allowScanningByMediaScanner();// 设置可以被扫描到
            request.setVisibleInDownloadsUi(true);// 设置下载可见
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//下载完成后通知栏任然可见
//        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));// 解析fileName
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, Constants.DOWNLOAD_FILENAME);// 设置下载位置，sdcard/Download/fileName
            sDownloadId = manager.enqueue(request);// 加入下载并取得下载ID
            ToastUtils.show(mContext, "正在后台下载");

        } catch (Exception e) {
            ToastUtils.show(mContext, "下载异常，请稍后重试！");
        }
    }
}
