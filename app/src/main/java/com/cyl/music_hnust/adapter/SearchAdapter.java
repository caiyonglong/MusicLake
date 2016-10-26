package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.SearchMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/9/26 23:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemHolder> {
    private Context mContext;
    private List<SearchMusic.SongList> songList = new ArrayList<>();

    private View mView;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public MusicRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MusicRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public SearchAdapter(Context context, List<SearchMusic.SongList> songList) {
        this.mContext = context;
        this.songList = songList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_search, null);
        SearchAdapter.ItemHolder itemHolder = new SearchAdapter.ItemHolder(mView);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
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

        if (songList.get(position).getTitle() != null)
            holder.tv_name.setText(Html.fromHtml(songList.get(position).getTitle()));
        if (songList.get(position).getAuthor() != null)
            holder.tv_artist.setText(Html.fromHtml(songList.get(position).getAuthor()));

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_artist;
        public TextView tv_time;
        public ImageButton music_item_ib_select;
        public ImageButton list_black_btn;
        public CardView music_container;

        public ItemHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) mView.findViewById(R.id.music_item_tv_name);
            tv_artist = (TextView) mView.findViewById(R.id.music_item_tv_artist);
            tv_time = (TextView) mView.findViewById(R.id.music_item_tv_time);
            music_container = (CardView) mView.findViewById(R.id.music_container);
            music_item_ib_select = (ImageButton) mView.findViewById(R.id.music_item_ib_select);
            list_black_btn = (ImageButton) mView.findViewById(R.id.list_black_btn);

        }
    }
}
