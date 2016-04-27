package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.MusicInfo;

import java.util.List;

/**
 * Created by 永龙 on 2016/3/27.
 */
public class PopupPlayListAdapter extends  RecyclerView.Adapter<PopupPlayListAdapter.PopupPlayListViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }
    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public List<MusicInfo> playlist;
    public int playIndexPosition;
    private Context context;
    public LayoutInflater mLayoutInflater;

    public PopupPlayListAdapter(Context context, List<MusicInfo> playlist,int poi) {

        this.playlist = playlist;
        this.context = context;
        this.playIndexPosition = poi;
        mLayoutInflater = LayoutInflater.from(context);

    }

    /**
     * 创建ViewHolder
     */
    @Override
    public PopupPlayListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.popup_list_item, parent, false);
        PopupPlayListViewHolder mViewHolder = new PopupPlayListViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final PopupPlayListViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.listitemBG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.listitemBG, position);
                }
            });

        }
        if (playIndexPosition ==position ){
            holder.songNameTextView.setTextColor(R.color.setting_blue);
        }else {
            holder.songNameTextView.setTextColor(Color.GRAY);
        }
        holder.songNameTextView.setText(playlist.get(position).getName());
        holder.singerNameTextView.setText(playlist.get(position).getArtist().toString());

       // holder.singerImageView.setText(playlist.get(position).getTime().toString());
     //   holder.index_comment.setText(position+1+"楼");

    }


    @Override
    public int getItemCount() {
        return playlist.size();
    }

    public class PopupPlayListViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView songNameTextView;
        private TextView singerNameTextView;
        private RelativeLayout listitemBG;

        public PopupPlayListViewHolder(View mView) {
            super(mView);
            songNameTextView = (TextView) mView.findViewById(R.id.song_name);
            listitemBG = (RelativeLayout) mView.findViewById(R.id.listitemBG);
            singerNameTextView = (TextView) mView.findViewById(R.id.artist);
        }
    }

}
