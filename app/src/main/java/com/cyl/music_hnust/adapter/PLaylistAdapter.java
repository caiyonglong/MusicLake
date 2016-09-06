package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ItemHolder> {

    private Context context;
    public List<Music> musicInfos = new ArrayList<>();
    private OnRecyclerItemClick mOnRecyclerItemClickListener;


    public PlaylistAdapter(Context context, RecyclerView mRecyclerView, List<Music> musicInfos, OnRecyclerItemClick mOnRecyclerItemClickListener) {
        this.context = context;
        this.musicInfos = musicInfos;
        this.mOnRecyclerItemClickListener = mOnRecyclerItemClickListener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Music localItem = musicInfos.get(position);

        holder.title.setText(localItem.getAlbum());
        holder.artist.setText(localItem.getArtist());
    }

    @Override
    public int getItemCount() {
        return (null != musicInfos ? musicInfos.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView title, artist;

        public ItemHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.album_title);
            this.artist = (TextView) view.findViewById(R.id.album_artist);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnRecyclerItemClickListener!=null){
                mOnRecyclerItemClickListener.onItemClick(itemView);
            }
        }
    }

    public interface OnRecyclerItemClick {
        void onItemClick(View View);
    }
}