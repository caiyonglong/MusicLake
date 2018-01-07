package com.cyl.musiclake.ui.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：yonglong on 2016/9/26 23:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemHolder> {
    private Context mContext;
    private List<Music> songList = new ArrayList<>();

    private View mView;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setNewData(List<Music> songList) {
        this.songList = songList;
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public SearchAdapter(Context context, List<Music> songList) {
        this.mContext = context;
        this.songList = songList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_search, parent, false);
        SearchAdapter.ItemHolder itemHolder = new SearchAdapter.ItemHolder(mView);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.iv_more, position);
                }
            });

        }

        if (songList.get(position).getTitle() != null)
            holder.tv_name.setText(FileUtils.getTitle(songList.get(position).getTitle()));
        if (songList.get(position).getArtist() != null)
            holder.tv_artist.setText(FileUtils.getArtistAndAlbum(songList.get(position).getArtist(),
                    songList.get(position).getAlbum()));

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_artist;
        public ImageView iv_more;

        public ItemHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) mView.findViewById(R.id.music_item_tv_name);
            tv_artist = (TextView) mView.findViewById(R.id.music_item_tv_artist);
            iv_more = (ImageView) mView.findViewById(R.id.iv_more);
        }
    }
}
