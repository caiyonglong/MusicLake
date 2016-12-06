package com.cyl.music_hnust.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyl.music_hnust.activity.NearPeopleAcivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.CommentActivity;
import com.cyl.music_hnust.callback.StatusCallback;
import com.cyl.music_hnust.model.community.Secret;
import com.cyl.music_hnust.model.user.StatusInfo;
import com.cyl.music_hnust.model.user.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by Monkey on 2015/6/29.
 */
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ItemHolder> {

    private ViewGroup parent;
    private int viewType;

    private Context mContext;
    private List<Secret> myDatas;
    private LayoutInflater mLayoutInflater;

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

        final Secret dynamic = myDatas.get(position);

        holder.user_name.setText(dynamic.getUser().getUser_name());
        holder.item_action_love.setText(dynamic.getSecret_agreeNum() + "赞");
        holder.item_action_comment.setText(dynamic.getSecret_replyNum() + "评论");
        String text = dynamic.getSecret_content().replace("\n", " ");
        holder.content_text.setText(text);
        holder.content_time.setText(FormatUtil.getTimeDifference(dynamic.getSecret_time()));
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                dynamic.getUser().getUser_img(), holder.user_logo, ImageUtils.getAlbumDisplayOptions());

        if (dynamic.getIsAgree() == 1) {
            holder.item_love.setText("已赞");
        } else {
            holder.item_love.setText("赞");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra(Extras.SECRET_ID, dynamic.getSecret_id());
                intent.putExtra(Extras.USER_ID, dynamic.getUser_id());
                if (SystemUtils.isLollipop()) {
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) mContext,
                            Pair.create((View) holder.user_name, "transition_name"),
                            Pair.create((View) holder.user_logo, "transition_logo"),
                            Pair.create((View) holder.content_text, "transition_content")).toBundle());
                } else {
                    mContext.startActivity(intent);
                }
            }
        });
        holder.item_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserStatus.getstatus(mContext)) {
                    changeAgree(dynamic.getSecret_id(), UserStatus.getUserInfo(mContext).getUser_id(), position);
                } else {
                    ToastUtils.show(mContext, "请先登录！");
                }
            }
        });
        holder.rl_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserStatus.getstatus(mContext)) {
                    if (!UserStatus.getUserInfo(mContext).getUser_id().equals(dynamic.getUser_id())) {
                        Intent intent = new Intent(mContext, NearPeopleAcivity.class);
                        intent.putExtra("userinfo", dynamic.getUser());
                        mContext.startActivity(intent);
                    }
                } else {
                    ToastUtils.show(mContext, "请先登录！");
                }
            }
        });


    }

    private void changeAgree(final String secret_id, final String user_id, final int position) {
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC, Constants.CHANGE_AGREE)
                .addParams(Constants.SECRET_ID, secret_id)
                .addParams(Constants.USER_ID, user_id)
                .build()
                .execute(new StatusCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtils.show(mContext, R.string.error_connection);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onResponse(StatusInfo response) {
                        Log.e("=========", response.toString() + "==" + response.getAgree() + "---" + response.getNum());
                        myDatas.get(position).setIsAgree(response.getAgree());
                        myDatas.get(position).setSecret_agreeNum(response.getNum() + "");
                        notifyDataSetChanged();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return myDatas.size();
    }


    public class ItemHolder extends ViewHolder {

        public TextView user_name;
        public RelativeLayout rl_user;
        public LinearLayout container;
        public CircleImageView user_logo;
        public TextView content_time;
        public TextView content_text;
        public TextView item_action_comment;
        public TextView item_action_love;
        public Button item_love;

        public ItemHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            rl_user = (RelativeLayout) itemView.findViewById(R.id.rl_user);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            item_love = (Button) itemView.findViewById(R.id.item_love);
            user_logo = (CircleImageView) itemView.findViewById(R.id.user_logo);
            content_time = (TextView) itemView.findViewById(R.id.item_comment_time);
            content_text = (TextView) itemView.findViewById(R.id.content_text);
            item_action_comment = (TextView) itemView.findViewById(R.id.item_comment_num);
            item_action_love = (TextView) itemView.findViewById(R.id.item_love_num);

        }
    }

}
