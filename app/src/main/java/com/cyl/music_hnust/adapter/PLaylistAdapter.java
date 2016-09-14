package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.LocalPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ItemHolder> {

    private Context context;
    private List<LocalPlaylist> localplaylists = new ArrayList<>();
    private OnRecyclerItemClick mOnRecyclerItemClickListener;

    public List<LocalPlaylist> getLocalplaylists() {
        return localplaylists;
    }

    public void setLocalplaylists(List<LocalPlaylist> localplaylists) {
        this.localplaylists = localplaylists;
    }

    public PlaylistAdapter(Context context, List<LocalPlaylist> localplaylists, OnRecyclerItemClick mOnRecyclerItemClickListener) {
        this.context = context;
        this.localplaylists = localplaylists;
        this.mOnRecyclerItemClickListener = mOnRecyclerItemClickListener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        LocalPlaylist localItem = localplaylists.get(position);

        holder.title.setText(localItem.getName());
        holder.artist.setText(localItem.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRecyclerItemClickListener!=null){
                    mOnRecyclerItemClickListener.onItemClick(v,localplaylists.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != localplaylists ? localplaylists.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        protected TextView title, artist;

        public ItemHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.album_title);
            this.artist = (TextView) view.findViewById(R.id.album_artist);
        }
    }

    public interface OnRecyclerItemClick {
        void onItemClick(View View,LocalPlaylist localPlaylist);
    }
}