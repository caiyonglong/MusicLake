//package com.cyl.music_hnust.adapter;
//
//import android.content.ContentUris;
//import android.content.Context;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.cyl.music_hnust.R;
//import com.cyl.music_hnust.view.RoundedImageView;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Monkey on 2015/6/29.
// */
//public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.PlaylistRecyclerViewHolder> {
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
//    public OnItemClickListener mOnItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//
//    public Context mContext;
//    public List<String> mDatas;
//    public int length=0;
//    public LayoutInflater mLayoutInflater;
//    public int type;
//
//    public PlaylistRecyclerViewAdapter(Context mContext, List<String> mDatas,int type) {
//        this.mContext = mContext;
//        this.mDatas = mDatas;
//        this.type = type;
//        mLayoutInflater = LayoutInflater.from(mContext);
//
//    }
//
//    /**
//     * 创建ViewHolder
//     */
//    @Override
//    public PlaylistRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View mView = mLayoutInflater.inflate(R.layout.item_download, parent, false);
//        PlaylistRecyclerViewHolder mViewHolder = new PlaylistRecyclerViewHolder(mView);
//        return mViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final PlaylistRecyclerViewHolder holder, final int position) {
//
//        if (mOnItemClickListener != null) {
//            holder.music_item_down.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.music_item_down, position);
//                }
//            });
//        }
//        holder.music_item_name.setText(mDatas.get(position).toString().substring(3));
//        holder.music_item_seek.setProgress(length);
//
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mDatas.size();
//    }
//
//    public class PlaylistRecyclerViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView music_item_name;
//        public SeekBar music_item_seek;
//        public Button music_item_down;
//        public PlaylistRecyclerViewHolder(View mView) {
//            super(mView);
//            music_item_name = (TextView) mView.findViewById(R.id.music_item_name);
//            music_item_seek = (SeekBar) mView.findViewById(R.id.music_item_seek);
//            music_item_down = (Button) mView.findViewById(R.id.music_item_down);
//        }
//    }
//
//}
