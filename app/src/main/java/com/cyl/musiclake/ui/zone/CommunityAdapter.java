package com.cyl.musiclake.ui.zone;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by Monkey on 2015/6/29.
 */
public class CommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Secret> myDatas;
    private LayoutInflater mLayoutInflater;

    public CommunityAdapter(Context mContext, List<Secret> myDatas) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.myDatas = myDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View view;
        switch (viewType) {
            //其他无法处理的情况使用viewholder_article_simple
            default:
            case TYPE_NORMAL:
                view = mLayoutInflater.inflate(
                        R.layout.item_main, parent, false);
                vh = new CommViewHolder(view);
                return vh;
            case TYPE_FOOTER:
                view = mLayoutInflater.inflate(
                        R.layout.item_foot, parent, false);
                vh = new FooterViewHolder(view);
                return vh;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //这时候 article是 null，先把 footer 处理了
        if (holder instanceof FooterViewHolder) {
//            ((FooterViewHolder) holder).rcvLoadMore.isShown();
            return;
        }

        if (holder instanceof CommViewHolder) {
            final Secret dynamic = myDatas.get(position);
            final CommViewHolder newHolder = (CommViewHolder) holder;

            newHolder.user_name.setText(dynamic.getUser().getName());
            newHolder.item_action_love.setText(dynamic.getSecret_agreeNum() + "赞");
            newHolder.item_action_comment.setText(dynamic.getSecret_replyNum() + "评论");
            String text = dynamic.getSecret_content().replace("\n", " ");
            newHolder.content_text.setText(text);
//        newHolder.content_time.setText(FormatUtil.getTimeDifference(dynamic.getSecret_time()));
//            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
//                    dynamic.getUser().getUrl(), newHolder.user_logo, ImageUtils.getAlbumDisplayOptions());

            if (dynamic.getIsAgree() == 1) {
                newHolder.item_love.setText("已赞");
            } else {
                newHolder.item_love.setText("赞");
            }

            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtra(Extras.SECRET_ID, dynamic.getSecret_id());
                    intent.putExtra(Extras.USER_ID, dynamic.getUser_id());
                    if (SystemUtils.isLollipop()) {
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                                (Activity) mContext,
                                Pair.create((View) newHolder.user_name, "transition_name"),
                                Pair.create((View) newHolder.user_logo, "transition_logo"),
                                Pair.create((View) newHolder.content_text, "transition_content")).toBundle());
                    } else {
                        mContext.startActivity(intent);
                    }
                }
            });
            newHolder.item_love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserStatus.getstatus(mContext)) {
                        changeAgree(dynamic.getSecret_id(), UserStatus.getUserInfo(mContext).getId(), position);
                    } else {
                        ToastUtils.show(mContext, "请先登录！");
                    }
                }
            });
            newHolder.rl_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserStatus.getstatus(mContext)) {
//                        if (!UserStatus.getUserInfo(mContext).getUser_id().equals(dynamic.getUser_id())) {
//                            Intent intent = new Intent(mContext, NearPeopleAcivity.class);
//                            intent.putExtra("userinfo", dynamic.getUser());
//                            mContext.startActivity(intent);
//                        }
                    } else {
                        ToastUtils.show(mContext, "请先登录！");
                    }
                }
            });
        }

    }

    public final static int TYPE_FOOTER = 3;//底部--往往是loading_more
    public final static int TYPE_NORMAL = 1; // 正常的一条文章

    @Override
    public int getItemViewType(int position) {

        Secret secret = myDatas.get(position);
        if (secret == null) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }

    }

    private void changeAgree(final String secret_id, final String user_id, final int position) {
//        OkHttpUtils.post().url(Constants.DEFAULT_URL)
//                .addParams(Constants.FUNC, Constants.CHANGE_AGREE)
//                .addParams(Constants.SECRET_ID, secret_id)
//                .addParams(Constants.USER_ID, user_id)
//                .build()
//                .execute(new StatusCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        ToastUtils.show(mContext, R.string.error_connection);
//                        notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onResponse(StatusInfo response) {
//                        LogUtil.e("=========", response.toString() + "==" + response.getAgree() + "---" + response.getNum());
//                        myDatas.get(position).setIsAgree(response.getAgree());
//                        myDatas.get(position).setSecret_agreeNum(response.getNum() + "");
//                        notifyDataSetChanged();
//                    }
//                });
    }


    @Override
    public int getItemCount() {
        return myDatas.size();
    }


    /**
     * 条目
     */
    public class CommViewHolder extends ViewHolder {

        public TextView user_name;
        public RelativeLayout rl_user;
        public LinearLayout container;
        public CircleImageView user_logo;
        //        public TextView content_time;
        public TextView content_text;
        public TextView item_action_comment;
        public TextView item_action_love;
        public Button item_love;

        public CommViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            rl_user = (RelativeLayout) itemView.findViewById(R.id.rl_user);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            item_love = (Button) itemView.findViewById(R.id.item_love);
            user_logo = (CircleImageView) itemView.findViewById(R.id.user_logo);
//            content_time = (TextView) itemView.findViewById(R.id.item_comment_time);
            content_text = (TextView) itemView.findViewById(R.id.content_text);
            item_action_comment = (TextView) itemView.findViewById(R.id.item_comment_num);
            item_action_love = (TextView) itemView.findViewById(R.id.item_love_num);

        }
    }

    /**
     * 底部加载更多
     */
    public class FooterViewHolder extends ViewHolder {
        ProgressBar rcvLoadMore;

        public FooterViewHolder(View itemView) {
            super(itemView);
            rcvLoadMore = (ProgressBar) itemView.findViewById(R.id.rcv_load_more);
        }
    }


}
