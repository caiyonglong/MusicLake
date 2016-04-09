//package com.cyl.music_hnust.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cyl.music_hnust.R;
//
//import java.util.List;
//
///**
// * Created by Monkey on 2015/6/29.
// */
//public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//
//    }
//
//    public OnItemClickListener mOnItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//    public Context mContext;
//    public List<Downloadinfo> downloadinfo;
//    public LayoutInflater mLayoutInflater;
//
//    public DownloadAdapter(Context mContext,List<Downloadinfo> downloadinfo) {
//        this.mContext = mContext;
//        this.downloadinfo = downloadinfo;
//        mLayoutInflater = LayoutInflater.from(mContext);
//
//    }
//
//    /**
//     * 创建ViewHolder
//     */
//    @Override
//    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View mView = mLayoutInflater.inflate(R.layout.item_download, parent, false);
//        DownloadViewHolder mViewHolder = new DownloadViewHolder(mView);
//        return mViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final DownloadViewHolder holder, final int position) {
//
//        if (mOnItemClickListener != null) {
//            holder.music_item_down.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.music_item_down, position);
////                    holder.music_item_down.setVisibility(View.GONE);
////                    holder.music_item_stop.setVisibility(View.VISIBLE);
//                }
//            });
//            holder.music_item_stop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.music_item_stop, position);
////                    holder.music_item_down.setVisibility(View.VISIBLE);
////                    holder.music_item_stop.setVisibility(View.GONE);
//                }
//            });
//
//        }
//        float num = (float) downloadinfo.get(position).getFileSize()
//                / downloadinfo.get(position).getCompleteSize();
//        // 把获取的浮点数转换为整数
//        int result = (int) (num * 100);
//        // 把下载的百分比显示在界面显示控件上
//
//        // 当下载完成时
//        if (downloadinfo.get(position).getCompleteSize()!=0&&downloadinfo.get(position).getFileSize()== downloadinfo.get(position).getCompleteSize())
//        {
//
//            // 使用Toast技术，提示用户下载完成
//            Toast.makeText(mContext, "下载完成",
//                    Toast.LENGTH_LONG).show();
//            downloadinfo.get(position).setState(0);
//            holder.music_item_down.setEnabled(false);
//            holder.music_item_stop.setEnabled(false);
//
//            holder.music_item_down.setText("下载完成");
//
//
//        }
//        holder.music_item_down.setText("下载"+result + "%");
//        holder.music_item_name.setText(downloadinfo.get(position).getMusicName());
//        holder.music_item_pro.setProgress(downloadinfo.get(position).getFileSize());
//        holder.music_item_pro.setMax(downloadinfo.get(position).getCompleteSize());
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return downloadinfo.size();
//    }
//
//    public class DownloadViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView music_item_name;
//        public ProgressBar music_item_pro;
//        public Button music_item_down;
//        public Button music_item_stop;
//
//        public DownloadViewHolder(View mView) {
//            super(mView);
//            music_item_name = (TextView) mView.findViewById(R.id.music_item_name);
//            music_item_pro = (ProgressBar) mView.findViewById(R.id.music_item_pro);
//            music_item_down = (Button) mView.findViewById(R.id.music_item_down);
//            music_item_stop = (Button) mView.findViewById(R.id.music_item_stop);
//        }
//    }
//}
