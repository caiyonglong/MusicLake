package com.cyl.musiclake.ui.onlinemusic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.download.model.FileState;

import java.util.List;

/**
 * 项目名称：MultithreadedDownload 类名称：DownloadAdapter 类描述：
 * 下载管理的适配器
 */
public class DownloadAdapter extends BaseQuickAdapter<FileState, BaseViewHolder> {

    public Context mContext;

    public DownloadAdapter(@Nullable List<FileState> data, Context mContext) {
        super(R.layout.item_download, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder holder, FileState fileState) {
        holder.setText(R.id.tv_name, fileState.getName());
        int percent = fileState.getFinish() / fileState.getFileSize();
        Button button = holder.getView(R.id.btn_status);
        if (fileState.getState() == 1) {
            button.setText(percent + "%");
        } else if (fileState.getState() == 3) {
            button.setText("继续");
        } else {
            button.setText("下载完成");
        }
        ProgressBar progressBar = holder.getView(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(percent);
        holder.addOnClickListener(R.id.btn_status);
    }

}
