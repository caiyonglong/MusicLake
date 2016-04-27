package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.MusicInfo;

import java.util.List;


public class AddMusicRecyclerviewAdapter extends
        RecyclerView.Adapter<AddMusicRecyclerviewAdapter.AddMusicRecyclerViewHolder> {
    private Context context; // 运行上下文

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
    public List<Boolean> Is_check;// 歌曲是否选中

    public AddMusicRecyclerviewAdapter(Context mContext, List<MusicInfo> mDatas, List<Boolean> Is_check) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.Is_check = Is_check;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public AddMusicRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.pl_songs_add, parent, false);
        AddMusicRecyclerViewHolder mViewHolder = new AddMusicRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final AddMusicRecyclerViewHolder holder, final int position) {

        if (mOnItemClickListener != null) {
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.container, position);
                }
            });

        }
        // 设置文字和图片
        if (Is_check.get(position)) {
            holder.icon.setBackgroundResource(R.drawable.checkbox_checked);
        } else {
            holder.icon.setBackgroundResource(R.drawable.checkbox_default);
        }

        if (mDatas.get(position).getName() != null)
            holder.songName.setText(position + 1 + "." + mDatas.get(position).getName().toString());
        if (mDatas.get(position).getArtist() != null)
            holder.singerName.setText("   " + mDatas.get(position).getArtist().toString() + "-" + mDatas.get(position).getAlbum().toString());

    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class AddMusicRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;// 放置增加或删除图标
        TextView songName;
        TextView singerName;
        ImageView albumPicture;// 专辑图片
        RelativeLayout container;// 专辑图片


        public AddMusicRecyclerViewHolder(View mView) {
            super(mView);
            icon = (ImageView) mView.findViewById(R.id.icon);
            songName = (TextView) mView.findViewById(R.id.songName);
            singerName = (TextView) mView.findViewById(R.id.singerName);
            albumPicture = (ImageView) mView.findViewById(R.id.albumPicture);
            container = (RelativeLayout) mView.findViewById(R.id.container);
        }
    }
}