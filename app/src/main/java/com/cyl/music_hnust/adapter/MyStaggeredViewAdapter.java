package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyl.music_hnust.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyStaggeredViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public List<Integer> mHeights;

    public Context mContext;
    public List<String> mDatas;
    public LayoutInflater mLayoutInflater;
    public int type;

    public MyStaggeredViewAdapter(Context mContext, List<String> mDatas, int type) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.type = type;
        mLayoutInflater = LayoutInflater.from(mContext);
        mHeights = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (Math.random() * 100) + 200);
        }

    }

    /**
     * 创建ViewHolder
     */
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_playlist, parent, false);
        MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
        return mViewHolder;
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {

        if (type == 1) {
            if (mOnItemClickListener != null) {
                holder.playlist_album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.playlist_album, position);
                    }
                });

            }
        }
        ViewGroup.LayoutParams mLayoutParams = holder.playlist_album.getLayoutParams();
        mLayoutParams.height = mHeights.get(position);
        if (type == 2) {
            holder.playlist_container.setVisibility(View.VISIBLE);
            holder.playlist_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        if (mDatas.get(position)!=null)
        holder.playlist_name.setText(mDatas.get(position).toString());
        Bitmap bm=null;
        if (bm!=null) {
            Log.e("bm",bm+"");
            holder.playlist_album.setImageBitmap(bm);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setmHeights(List<String> al_playlist) {
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (Math.random() * 100) + 200);
        }

    }


}
