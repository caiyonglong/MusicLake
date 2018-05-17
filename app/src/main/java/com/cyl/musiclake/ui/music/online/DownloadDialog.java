package com.cyl.musiclake.ui.music.online;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.download.TasksManager;
import com.cyl.musiclake.data.download.TasksManagerModel;
import com.cyl.musiclake.ui.music.online.adapter.FileDownloadListener;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.NetworkUtils;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ALL")
public class DownloadDialog extends DialogFragment {

    private static boolean result = false;

    public static DownloadDialog newInstance(Music song) {
        DownloadDialog dialog = new DownloadDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", song);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Music music = getArguments().getParcelable("music");
        return new MaterialDialog.Builder(getContext())
                .title("歌曲下载")
                .content("歌名：  " + music.getTitle() +
                        "\n歌曲id：" + music.getId() +
                        "\n下载地址：\n" + music.getUri()
                ).positiveText("确定")
                .negativeText("取消")
                .onPositive((materialDialog, dialogAction) -> {
                    if (music.getUri().isEmpty()) {
                        ToastUtils.show(getContext(), "下载地址异常！");
                    } else if (!NetworkUtils.isWifiConnected(getContext()) && SPUtils.getWifiMode()) {
                        ToastUtils.show(getContext(), "当前不在wifi环境，请在设置中关闭省流量模式");
                    } else {
                        ToastUtils.show("下载任务添加成功");
                        String path = FileUtils.getMusicDir() + music.getTitle() + ".mp3";
                        BaseDownloadTask task = FileDownloader.getImpl()
                                .create(music.getUri())
                                .setPath(path)
                                .setCallbackProgressTimes(100)
                                .setListener(new FileDownloadListener());
                        TasksManager.getImpl()
                                .addTaskForViewHolder(task);
                        TasksManagerModel model =
                                TasksManager.getImpl().addTask(task.getId(), music.getId(), music.getTitle(), music.getUri(), path);
                        task.start();
                    }
                }).build();
    }
}