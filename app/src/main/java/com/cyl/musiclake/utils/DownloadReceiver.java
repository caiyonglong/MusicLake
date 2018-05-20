package com.cyl.musiclake.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.cyl.musiclake.utils.LogUtil;

/**
 * 下载完成广播接收器
 * Created by hzwangchenyan on 2015/12/30.
 */
public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("=========",intent.getAction());
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (id == UpdateUtils.sDownloadId) {
            String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager dManager = (DownloadManager) context
                    .getSystemService(serviceString);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager
                    .getUriForDownloadedFile(id);
            install.setDataAndType(downloadFileUri,
                    "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        } else {

        }
    }
}
