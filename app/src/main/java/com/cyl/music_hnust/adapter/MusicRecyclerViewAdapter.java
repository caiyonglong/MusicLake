package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.music_hnust.PlaylistSongActivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.utils.MusicInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<MusicRecyclerViewAdapter.MusicRecyclerViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public Context mContext;
    public List<MusicInfo> mDatas;
    public LayoutInflater mLayoutInflater;

    public MusicRecyclerViewAdapter(Context mContext, List<MusicInfo> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    /**
     * 创建ViewHolder
     */
    @Override
    public MusicRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_music, parent, false);
        MusicRecyclerViewHolder mViewHolder = new MusicRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final MusicRecyclerViewHolder holder, final int position) {

        if (mOnItemClickListener != null) {
            holder.music_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.list_black_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.list_black_btn, position);
                }
            });

        }
        if (PlaylistSongActivity.idEdit){
            holder.list_black_btn.setVisibility(View.GONE);
        }


        if (mDatas.get(position).getName()!=null)
        holder.tv_name.setText(position+1+"."+mDatas.get(position).getName().toString());
        if (mDatas.get(position).getArtist()!=null)
        holder.tv_artist.setText("   "+mDatas.get(position).getArtist().toString()+"-"+mDatas.get(position).getAlbum().toString());

    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MusicRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public TextView tv_artist;
        public TextView tv_time;
        public ImageButton music_item_ib_select;
        public ImageButton list_black_btn;
        public CardView music_container;

        public MusicRecyclerViewHolder(View mView) {
            super(mView);
            tv_name = (TextView) mView.findViewById(R.id.music_item_tv_name);
            tv_artist = (TextView) mView.findViewById(R.id.music_item_tv_artist);
            tv_time = (TextView) mView.findViewById(R.id.music_item_tv_time);
            music_container = (CardView) mView.findViewById(R.id.music_container);
            music_item_ib_select = (ImageButton) mView.findViewById(R.id.music_item_ib_select);
            list_black_btn = (ImageButton) mView.findViewById(R.id.list_black_btn);
        }
    }
}
