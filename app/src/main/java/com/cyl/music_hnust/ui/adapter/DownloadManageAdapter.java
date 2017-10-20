package com.cyl.music_hnust.ui.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.download.DownloadListener;
import com.cyl.music_hnust.download.DownloadService;
import com.cyl.music_hnust.download.db.SqliteDao;
import com.cyl.music_hnust.download.model.FileState;
import com.cyl.music_hnust.bean.music.Music;
import com.cyl.music_hnust.utils.Constants;

import java.io.File;
import java.util.List;

/**
 * 项目名称：MultithreadedDownload 类名称：DownloadManageAdapter 类描述：
 * 下载管理的适配器
 */
public class DownloadManageAdapter extends
        RecyclerView.Adapter<DownloadManageAdapter.DownloadManageViewHolder> {

    /**
     * 设置（更新）list数据
     *
     * @param data
     */
    public void setList(List<FileState> data) {
        this.fileStates = data;
    }

    public void setChange(FileState fileState) {
        Intent intent = new Intent();
        intent.setClass(mContext, DownloadService.class);
        intent.putExtra("downloadUrl", fileState.getUrl());
        intent.putExtra("name", fileState.getName());
        intent.putExtra("flag", "changeState");
        mContext.startService(intent);
    }

    public Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FileState> fileStates;
    private List<Music> musics;
    private boolean[] isPause;
    public UpdateReceiver receiver;

    public DownloadManageAdapter(Context context, List<FileState> fileStates) {
        this.mContext = context;
        this.fileStates = fileStates;
        mLayoutInflater = LayoutInflater.from(mContext);


        receiver = new UpdateReceiver();
        receiver.registerAction(Constants.DOWNLOADMANAGEACTION);

        isPause = new boolean[fileStates.size()+5];
        for (int i = 0; i < fileStates.size()+5; i++) {
            isPause[i] = false;
        }

    }
    View mView;
    /**
     * 创建ViewHolder
     */
    @Override
    public DownloadManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = mLayoutInflater.inflate(R.layout.item_download, parent, false);
        DownloadManageViewHolder mViewHolder = new DownloadManageViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final DownloadManageViewHolder holder, final int position) {

        holder.btn_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPause[position]) {
                    holder.btn_control.setText("下载中");
                    isPause[position] = false;
                } else {
                    holder.btn_control.setText("已暂停");
                    isPause[position] = true;
                }
                setChange(fileStates.get(position));
                notifyDataSetChanged();
            }
        });

        final FileState fileState = fileStates.get(position);

        holder.tv_name.setText(fileState.getName());

        if (0 == fileState.getState()) {// 下载完成

            holder.progressBar.setVisibility(View.GONE);
            holder.tv_per.setVisibility(View.GONE);
            holder.btn_control.setText("下载完成");
            holder.btn_control.setClickable(false);
            fileStates = new SqliteDao(mContext).getFileStates();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else if (1 == fileState.getState()) {// 正在下载

            int completeSize = fileState.getCompleteSize();
            // Log.e("test>>", "progressBar  completeSize当前进度：" + completeSize);
            int fileSize = fileState.getFileSize();
            // Log.e("test>>", "progressBar文件大小：" + fileSize);
            float num = (float) completeSize / (float) fileSize;
            int result = (int) (num * 100);
            holder.progressBar.setProgress(result);
            holder.tv_per.setText(result + "%");

            if (!isPause[position]) {
                holder.btn_control.setText("下载中");
            } else {
                holder.btn_control.setText("已暂停");
            }

        }
        // 当文件下载完成
        if (fileState.getCompleteSize() == fileState.getFileSize()) {
            fileState.setState(0);
            fileStates.set(position, fileState);
            holder.progressBar.setVisibility(ProgressBar.GONE);
            holder.btn_control.setText("下载完成");
            holder.tv_per.setVisibility(View.GONE);
            holder.btn_control.setClickable(false);
        }

        DownloadListener downloadListener = new DownloadListener() {
            @Override
            public void oDownloading(String url, int finished) {

            }

            @Override
            public void onDownloadFinished(File downloadFile) {

            }
        };

    }


    @Override
    public int getItemCount() {
        return fileStates.size();
    }

    public class DownloadManageViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public TextView tv_per;
        public ProgressBar progressBar;
        public Button btn_control;

        public DownloadManageViewHolder(View mView) {
            super(mView);
            tv_name = (TextView) mView.findViewById(R.id.tv_name);
            tv_per = (TextView) mView.findViewById(R.id.tv_per);
            progressBar = (ProgressBar) mView
                    .findViewById(R.id.progressBar);
            progressBar.setMax(100);
            btn_control = (Button) mView.findViewById(R.id.btn_control);
        }
    }


    /**
     * 项目名称：MultithreadedDownload 类名称：UpdateReceiver 类描述：
     * 接收器类，用来接收后台service发送过来的下载进度 创建人：wpy 创建时间：2014-10-13 上午10:11:20
     */
    private class UpdateReceiver extends BroadcastReceiver {
        /**
         * 注册广播接收器
         *
         * @param action
         */
        public void registerAction(String action) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(action);
            mContext.registerReceiver(this, intentFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.DOWNLOADMANAGEACTION)) {
                String url = intent.getStringExtra("url");
                int completeSize = intent.getIntExtra("completeSize", 0);
                for (int i = 0; i < fileStates.size(); i++) {
                    FileState fileState = fileStates.get(i);
                    if (fileState.getUrl().equals(url)) {
                        fileState.setCompleteSize(completeSize);
                        fileStates.set(i, fileState);
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    class DownloadImp implements DownloadListener{

        @Override
        public void oDownloading(String url, int finished) {

        }

        @Override
        public void onDownloadFinished(File downloadFile) {

        }
    }


}
