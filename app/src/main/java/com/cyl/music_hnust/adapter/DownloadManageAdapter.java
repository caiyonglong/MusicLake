package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.download.FileState;
import com.cyl.music_hnust.download.SqliteDao;
import com.cyl.music_hnust.service.DownloadService;

import java.util.ArrayList;
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


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public Context mContext;
    public LayoutInflater mLayoutInflater;
    private List<FileState> fileStates;
    private SqliteDao dao;
    public boolean[] isPause ;

    public DownloadManageAdapter(Context context, List<FileState> fileStates,
                                 SqliteDao dao) {
        this.mContext = context;
        this.fileStates = fileStates;
        this.dao = dao;
        mLayoutInflater = LayoutInflater.from(mContext);
        isPause = new boolean[fileStates.size()];
        for (int i =0 ; i<fileStates.size();i++){
            isPause[i] = false;
        }

    }

    /**
     * 创建ViewHolder
     */
    @Override
    public DownloadManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_download, parent, false);
        DownloadManageViewHolder mViewHolder = new DownloadManageViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final DownloadManageViewHolder holder, final int position) {

        if (mOnItemClickListener != null) {
            holder.btn_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.btn_stop, position);
                }
            });
            holder.btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.btn_continue, position);
                }
            });

        }

        final FileState fileState = fileStates.get(position);

        holder.tv_name.setText(fileState.getName());

        if (0 == fileState.getState()) {// 下载完成

            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.btn_stop.setText("已下载");
            holder.btn_stop.setClickable(false);
        } else if (1 == fileState.getState()) {// 正在下载

            int completeSize = fileState.getCompleteSize();
            // Log.e("test>>", "progressBar  completeSize当前进度：" + completeSize);
            int fileSize = fileState.getFileSize();
            // Log.e("test>>", "progressBar文件大小：" + fileSize);
            float num = (float) completeSize / (float) fileSize;
            int result = (int) (num * 100);
            holder.progressBar.setProgress(result);
            holder.tv_per.setText(result + "%");
            
            if (!isPause[position]){
                holder.btn_stop.setVisibility(View.VISIBLE);
                holder.btn_continue.setVisibility(View.GONE);
            }else {
                holder.btn_stop.setVisibility(View.GONE);
                holder.btn_continue.setVisibility(View.VISIBLE);
            }

        }
        // 当文件下载完成
        if (fileState.getCompleteSize() == fileState.getFileSize()) {
            fileState.setState(0);
            fileStates.set(position, fileState);
            holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
            holder.btn_stop.setText("已下载");
            holder.tv_per.setVisibility(View.GONE);
            holder.btn_stop.setClickable(false);
        }





    }


    @Override
    public int getItemCount() {
        return fileStates.size();
    }

    public class DownloadManageViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_per;
        public ProgressBar progressBar;
        public Button btn_stop;
        public Button btn_continue;

        public DownloadManageViewHolder(View mView) {
            super(mView);
            //iv_icon = (ImageView) mView.findViewById(R.id.iv_icon);
            tv_name = (TextView) mView.findViewById(R.id.tv_name);
            tv_per = (TextView) mView.findViewById(R.id.tv_per);
            progressBar = (ProgressBar) mView
                    .findViewById(R.id.progressBar);
            progressBar.setMax(100);
            btn_stop = (Button) mView.findViewById(R.id.btn_stop);
            btn_continue = (Button) mView
                    .findViewById(R.id.btn_continue);
        }
    }
}
