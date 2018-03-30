package com.cyl.musiclake.ui.zone;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.ui.login.user.UserStatus;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 永龙 on 2016/3/15.
 */
public class CommentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_cardview)
    CardView id_cardview;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_logo)
    ImageView user_logo;
    @BindView(R.id.content_text)
    TextView content_text;
    @BindView(R.id.item_comment_num)
    TextView comment_num;
    @BindView(R.id.item_love_num)
    TextView love_num;
    @BindView(R.id.item_comment_time)
    TextView item_comment_time;
    @BindView(R.id.send_comment)
    Button send_comment;
    @BindView(R.id.item_love)
    Button item_love;
    @BindView(R.id.et_comment)
    EditText mEditText;

    @BindView(R.id.comment_list)
    RecyclerView mRecyclerView;

    @OnClick(R.id.send_comment)
    public void send() {

        String userid = "";
        if (UserStatus.getstatus(this)) {
            user_id = UserStatus.getUserInfo(this).getId();
        } else {
            ToastUtils.show(this, "请登录！");
            return;
        }
        String message = mEditText.getText().toString().trim();
        if (message == null || message.length() <= 0) {
            ToastUtils.show(this, "评论内容不能为空");
            return;
        }
        sendComment(secret_id, userid, message);
    }

    @OnClick(R.id.id_cardview)
    void tonear() {
        if (UserStatus.getstatus(this)) {
            if (!UserStatus.getUserInfo(this).getId().equals(secret.getUser_id())) {
                Intent intent = new Intent(this, NearPeopleAcivity.class);
                intent.putExtra("userinfo", secret.getUser());
                startActivity(intent);
            }
        } else {
            ToastUtils.show(this, "请先登录！");
        }
    }

    @OnClick(R.id.item_love)
    void love() {
        String userid = "";
        if (UserStatus.getstatus(this)) {
            userid = UserStatus.getUserInfo(this).getId();
        } else {
            ToastUtils.show(this, "请登录！");
            return;
        }
        changeAgree(secret.getSecret_id(), userid);
    }

    private CommentAdapter adapter;
    private List<Comment> comments = new ArrayList<>();
    private Secret secret;
    String secret_id;
    String user_id = "";


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initView() {
        send_comment.setClickable(true);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SystemUtils.isLollipop()) {
            getWindow().setEnterTransition(new Explode());
        }


        adapter = new CommentAdapter(getApplicationContext(), comments);
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        secret_id = getIntent().getStringExtra(Extras.SECRET_ID);
        user_id = getIntent().getStringExtra(Extras.USER_ID);

        Log.e("rrr", secret_id + "====" + user_id);
        getComment(secret_id);

    }

    @Override
    protected void listener() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0)
                    send_comment.setClickable(false);
                else
                    send_comment.setClickable(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        if (secret != null) {
//            user_name.setText(secret.getUser().getUser_name());
//            love_num.setText(secret.getSecret_agreeNum() + "赞");
//            comment_num.setText(secret.getSecret_replyNum() + "评论");
//            content_text.setText(secret.getSecret_content());
//            content_text.setMaxLines(1000);
//            item_comment_time.setText("分享于" + FormatUtil.getTimeDifference(secret.getSecret_time()));
//            ImageLoader.getInstance().displayImage(
//                    secret.getUser().getUser_img(), user_logo, ImageUtils.getAlbumDisplayOptions());
//            if (secret.getIsAgree() == 1) {
//                item_love.setText("已赞");
//            } else {
//                item_love.setText("赞");
//            }
        }
    }


    private void getComment(String secret_id) {
//        OkHttpUtils.post().url(Constants.DEFAULT_URL)
//                .addParams(Constants.FUNC, Constants.GET_COMMENT_LIST)
//                .addParams(Constants.SECRET_ID, secret_id)
//                .addParams(Constants.USER_ID, user_id)
//                .build()
//                .execute(new CommentCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(CommentInfo response) {
//                        if (response.getStatus() == 1) {
//                            Log.e("aaaaaaaa", response.toString());
//                            secret = response.getData();
//                            comments = response.getComment();
//                            adapter.comments = comments;
//                            Log.e("aaaaaaaa", response.toString());
//                            mHandler.post(update_thread);
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });
    }

    private void sendComment(final String secret_id, final String user_id, String comment) {
//        OkHttpUtils.post().url(Constants.DEFAULT_URL)
//                .addParams(Constants.FUNC, Constants.COMMENT_ADD)
//                .addParams(Constants.SECRET_ID, secret_id)
//                .addParams(Constants.USER_ID, user_id)
//                .addParams(Constants.CONTENT, comment)
//                .build()
//                .execute(new SecretCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        ToastUtils.show(CommentActivity.this, "网络连接异常，请重试！");
//                    }
//
//                    @Override
//                    public void onResponse(SecretInfo response) {
//                        if (response.getStatus() == 1) {
//                            getComment(secret_id);
//                            ToastUtils.show(CommentActivity.this, response.getMessage());
//                            mEditText.setText("");
//                        }
//
//                    }
//                });
    }

    private void changeAgree(final String secret_id, final String user_id) {
//        OkHttpUtils.post().url(Constants.DEFAULT_URL)
//                .addParams(Constants.FUNC, Constants.CHANGE_AGREE)
//                .addParams(Constants.SECRET_ID, secret_id)
//                .addParams(Constants.USER_ID, user_id)
//                .build()
//                .execute(new StatusCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        ToastUtils.show(CommentActivity.this, R.string.error_connection);
//                    }
//
//                    @Override
//                    public void onResponse(StatusInfo response) {
//                        if (response.getStatus() == 1) {
//                            Log.e("=========", response.toString() + "==" + response.getAgree() + "---" + response.getNum());
//                            secret.setIsAgree(response.getAgree());
//                            secret.setSecret_agreeNum(response.getNum() + "");
//                            mHandler.post(update_thread);
//                        }
//                    }
//                });
    }

    //线程中运行该接口的run函数
    Runnable update_thread = new Runnable() {
        public void run() {
            if (secret != null) {
//                user_name.setText(secret.getUser().getUser_name());
//                love_num.setText(secret.getSecret_agreeNum() + "赞");
//                comment_num.setText(secret.getSecret_replyNum() + "评论");
//                content_text.setText(secret.getSecret_content());
//                content_text.setMaxLines(1000);
//                item_comment_time.setText("分享于" + FormatUtil.getTimeDifference(secret.getSecret_time()));
//                ImageLoader.getInstance().displayImage(
//                        secret.getUser().getUser_img(), user_logo, ImageUtils.getAlbumDisplayOptions());
//                if (secret.getIsAgree() == 1) {
//                    item_love.setText("已赞");
//                } else {
//                    item_love.setText("赞");
//                }
            }

        }
    };

}
