package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.bean.Dynamic;
import com.cyl.music_hnust.utils.FormatUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

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
    public List<String> mDatas;
    public List<Dynamic> myDatas;
    public boolean loadmoring = false;
    public String loadmore = "点击加载更多数据...";
    public String url_header = "http://119.29.27.116/hcyl/music_BBS";
    public LayoutInflater mLayoutInflater;
    public ImageLoader imageLoader;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public MyRecyclerViewAdapter(Context mContext, List<Dynamic> myDatas, ImageLoader imageLoader) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.myDatas = myDatas;
        this.imageLoader = imageLoader;
//        mDatas = new ArrayList<>();
//        for (int i = 'A'; i <= 'z'; i++) {
//            mDatas.add((char) i + "");
//        }
//        num = new ArrayList<>();
//        for (int i = 'A'; i <= 'z'; i++) {
//            num.add(0);
//        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        this.viewType = viewType;
        if (viewType == TYPE_ITEM) {
            View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
            MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
            return mViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View mView = mLayoutInflater.inflate(R.layout.item_foot, parent, false);
            FooterViewHolder footerViewHolder = new FooterViewHolder(mView);
            return footerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof MyRecyclerViewHolder) {
            final MyRecyclerViewHolder holder1 = (MyRecyclerViewHolder) holder;
            if (mOnItemClickListener != null) {
                holder1.item_action_love_agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder1.item_action_love_agree, position);
                    }
                });
                holder1.item_action_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder1.item_action_comment, position);
                    }
                });
                holder1.content_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder1.content_text, position);
                    }
                });

            }
            holder1.user_name.setText(myDatas.get(position).getUser().getUser_name());
            holder1.item_action_love.setText(myDatas.get(position).getLove() + "赞");
            holder1.item_action_comment.setText(myDatas.get(position).getComment() + "评论");
            holder1.content_text.setText(myDatas.get(position).getContent());
            holder1.content_time.setText(FormatUtil.distime(myDatas.get(position).getTime()));
            //  holder1.user_logo.setDefaultImageResId(R.mipmap.user_icon_default_main);
            holder1.user_logo.setErrorImageResId(R.mipmap.user_icon_default_main);
            holder1.user_logo.setImageUrl(url_header+myDatas.get(position).getUser().getUser_img(), imageLoader);

            if (myDatas.get(position).isMyLove()){
                holder1.item_action_love_img.setImageResource(R.mipmap.ic_action_agree1);
            }else {
                holder1.item_action_love_img.setImageResource(R.mipmap.ic_action_agree);
            }

        } else if (holder instanceof FooterViewHolder) {
            final FooterViewHolder hodler2 = (FooterViewHolder) holder;
            if (mOnItemClickListener != null) {
                hodler2.foot_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(hodler2.foot_text, position);
                    }
                });
            }
            hodler2.foot_text.setText(loadmore);

        }
    }


    @Override
    public int getItemCount() {
        return myDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class FooterViewHolder extends ViewHolder {
        public CardView foot;
        public TextView foot_text;

        public FooterViewHolder(View mView) {
            super(mView);
            foot = (CardView) mView.findViewById(R.id.id_cardview_foot);
            foot_text = (TextView) mView.findViewById(R.id.foot_text);

        }
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView user_name;
        public NetworkImageView user_logo;
        public TextView content_time;
        public TextView content_text;
        public TextView item_action_comment;
        public TextView item_action_love;
        public ImageView item_action_love_img;
        public LinearLayout item_action_love_agree;

        public MyRecyclerViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_logo = (NetworkImageView) itemView.findViewById(R.id.user_logo);
            content_time = (TextView) itemView.findViewById(R.id.content_time);
            content_text = (TextView) itemView.findViewById(R.id.content_text);
            item_action_comment = (TextView) itemView.findViewById(R.id.item_action_comment);
            item_action_love = (TextView) itemView.findViewById(R.id.item_action_love);
            item_action_love_img = (ImageView) itemView.findViewById(R.id.item_action_love_img);
            item_action_love_agree = (LinearLayout) itemView.findViewById(R.id.item_action_love_agree);
        }
    }
}
