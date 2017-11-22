package com.cyl.musiclake.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.mvp.model.music.OnlinePlaylists.Billboard;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ItemHolder> {

    private Context mContext;
    private List<Billboard> mBillboards = new ArrayList<>();


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public OnlineAdapter(Context context, List<Billboard> mBillboards) {
        this.mContext = context;
        this.mBillboards = mBillboards;
    }

    public void setmBillboards(List<Billboard> mBillboards) {
        this.mBillboards = mBillboards;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online_large, parent, false);
        ItemHolder itemHolder = new ItemHolder(v);
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
        }
        setData(mBillboards.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return (null != mBillboards ? mBillboards.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView title, tv_1, tv_2, tv_3;
        protected ImageView iv_cover;

        public ItemHolder(View view) {
            super(view);
            this.iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
            this.title = (TextView) view.findViewById(R.id.title);
            this.tv_1 = (TextView) view.findViewById(R.id.tv_music_1);
            this.tv_2 = (TextView) view.findViewById(R.id.tv_music_2);
            this.tv_3 = (TextView) view.findViewById(R.id.tv_music_3);
        }

    }

    private void setData(Billboard mBillboard, final ItemHolder holder) {


        GlideApp.with(mContext)
                .load(mBillboard.getPic_s192())
                .error(R.drawable.default_cover)
                .into(holder.iv_cover);
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(mBillboard.getPic_s192().trim(), holder.iv_cover, ImageUtils.getCoverDisplayOptions());

        holder.title.setText(mBillboard.getName());
        List<Billboard.MusicLists> musicLists = mBillboard.getContent();
        if (musicLists.size() >= 1) {
            holder.tv_1.setText(mContext.getString(R.string.song_list_item_title_1,
                    musicLists.get(0).getTitle(), musicLists.get(0).getAuthor()));
        } else {
            holder.tv_1.setText("");
        }
        if (musicLists.size() >= 2) {
            holder.tv_2.setText(mContext.getString(R.string.song_list_item_title_2,
                    musicLists.get(1).getTitle(), musicLists.get(1).getAuthor()));
        } else {
            holder.tv_2.setText("");
        }
        if (musicLists.size() >= 3) {
            holder.tv_3.setText(mContext.getString(R.string.song_list_item_title_3,
                    musicLists.get(2).getTitle(), musicLists.get(2).getAuthor()));
        } else {
            holder.tv_3.setText("");
        }
    }

}