package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.Secret;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ImageUtils;

import java.util.List;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by Monkey on 2015/6/29.
 */
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ItemHolder> {

    private ViewGroup parent;
    private int viewType;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public List<Secret> myDatas;
    public LayoutInflater mLayoutInflater;

    public CommunityAdapter(Context mContext, List<Secret> myDatas) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.myDatas = myDatas;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        this.viewType = viewType;
        View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
        ItemHolder mViewHolder = new ItemHolder(mView);
        return mViewHolder;


    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.container, position);
                }
            });
            holder.item_action_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.item_action_comment, position);
                }
            });
            holder.content_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.content_text, position);
                }
            });
            holder.user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.user_name, position);
                }
            });
            holder.user_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.user_logo, position);
                }
            });

        }
        Secret dynamic = myDatas.get(position);

        holder.user_name.setText(dynamic.getUser().getUser_name());
        holder.item_action_love.setText(dynamic.getSecret_agreeNum() + "赞");
        holder.item_action_comment.setText(dynamic.getSecret_replyNum() + "评论");
        String text =dynamic.getSecret_content().replace("\n"," ");
        holder.content_text.setText(text);
        holder.content_time.setText(FormatUtil.distime(dynamic.getSecret_time()));
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                dynamic.getUser().getUser_img(),holder.user_logo, ImageUtils.getAlbumDisplayOptions());

    }


    @Override
    public int getItemCount() {
        return myDatas.size();
    }


    public class ItemHolder extends ViewHolder {

        public TextView user_name;
        public LinearLayout container;
        public ImageView user_logo;
        public TextView content_time;
        public TextView content_text;
        public TextView item_action_comment;
        public TextView item_action_love;
//        public LinearLayout item_action_love_agree;

        public ItemHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_logo = (ImageView) itemView.findViewById(R.id.user_logo);
            content_time = (TextView) itemView.findViewById(R.id.content_time);
            content_text = (TextView) itemView.findViewById(R.id.content_text);
            item_action_comment = (TextView) itemView.findViewById(R.id.item_comment_num);
            item_action_love = (TextView) itemView.findViewById(R.id.item_love_num);
        }
    }
}
