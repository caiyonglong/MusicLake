package com.cyl.musiclake.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.mvp.model.music.Playlist;
import com.cyl.musiclake.utils.FormatUtil;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid, parent,false);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        Playlist localItem = localplaylists.get(position);
        String time =localItem.getId();

        Log.e("时间====","===="+time+"===="+localItem.getId());
        holder.title.setText(localItem.getName());
        holder.time.setText(FormatUtil.distime(Long.parseLong(time)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,localplaylists.get(position));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != localplaylists ? localplaylists.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        protected TextView title, time;

        public ItemHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.album_title);
            this.time = (TextView) view.findViewById(R.id.album_artist);
        }
    }
}