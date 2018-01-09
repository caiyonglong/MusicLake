package com.cyl.musiclake.ui.localmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ItemHolder> {

    private Context context;
    private List<Playlist> localplaylists = new ArrayList<>();
    private int VIEWTYPE = 1;

    public interface OnItemClickListener {
        void onItemClick(View view, Playlist playlist);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setLocalplaylists(List<Playlist> localplaylists) {
        this.localplaylists = localplaylists;
    }

    public PlaylistAdapter(Context context, List<Playlist> localplaylists) {
        this.context = context;
        this.localplaylists = localplaylists;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        Playlist localItem = localplaylists.get(position);

        holder.name.setText(localItem.getName());
        holder.num.setText(localItem.getCount() + "首");
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, localplaylists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != localplaylists ? localplaylists.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView num;

        public ItemHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.tv_name);
            this.num = (TextView) view.findViewById(R.id.tv_num);
        }
    }
}