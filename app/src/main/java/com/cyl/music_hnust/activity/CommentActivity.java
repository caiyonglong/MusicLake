package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.CommentRecyclerViewAdapter;
import com.cyl.music_hnust.callback.CommentCallback;
import com.cyl.music_hnust.callback.SecretCallback;
import com.cyl.music_hnust.model.Comment;
import com.cyl.music_hnust.model.CommentInfo;
import com.cyl.music_hnust.model.Secret;
import com.cyl.music_hnust.model.SecretInfo;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.cyl.music_hnust.view.FullyLinearLayoutManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 永龙 on 2016/3/15.
 */
public class CommentActivity extends BaseActivity{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.user_logo)
    ImageView user_logo;
    @Bind(R.id.content_time)
    TextView content_time;
    @Bind(R.id.content_text)
    TextView content_text;
    @Bind(R.id.item_comment_num)
    TextView comment_num;
    @Bind(R.id.item_love_num)
    TextView love_num;
    @Bind(R.id.send_comment)
    Button send_comment;
    @Bind(R.id.et_comment)
    EditText mEditText;

    @Bind(R.id.comment_list)
    RecyclerView mRecyclerView;

    @OnClick(R.id.send_comment)
    public void send(){
        String message = mEditText.getText().toString().trim();
        if (message==null||message.length()<=0){
            ToastUtils.show(this,"不能发送空");
            return;
        }
        if (user_id==null||user_id.length()==0){
            ToastUtils.show(this,"请登录！");
            return;
        }
        sendComment(secret_id,user_id,message);
    }

    private static CommentRecyclerViewAdapter adapter;
    private FullyLinearLayoutManager mLayoutManager;

    private List<Comment> comments =new ArrayList<>();
    private Secret secret;
    String secret_id;
    String user_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        send_comment.setClickable(true);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length()==0)
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

        mLayoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentRecyclerViewAdapter(getApplicationContext(), comments);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(mLayoutManager);

        secret_id = getIntent().getStringExtra(Extras.SECRET_ID);
        user_id = getIntent().getStringExtra(Extras.USER_ID);
        Log.e("rrr",secret_id+"===="+user_id);
        getComment(secret_id);
    }

    @Override
    protected void listener() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        if (secret!=null) {
            user_name.setText(secret.getUser().getUser_name());
            love_num.setText(secret.getSecret_agreeNum() + "赞");
            comment_num.setText(secret.getSecret_replyNum() + "评论");
            content_text.setText(secret.getSecret_content());
            content_text.setMaxLines(1000);
            content_time.setText(secret.getSecret_time());
            ImageLoader.getInstance().displayImage(
                    secret.getUser().getUser_img(), user_logo, ImageUtils.getAlbumDisplayOptions());
        }
    }


    private void getComment(String secret_id){
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC,Constants.GET_COMMENT_LIST)
                .addParams(Constants.SECRET_ID, secret_id)
                .addParams(Constants.USER_ID,user_id)
                .build()
                .execute(new CommentCallback() {
                    @Override
                        public void onError(Call call, Exception e) {
                            Log.e("aaaaaaaa","dddd"+call+"++++++");
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(CommentInfo response) {
                        Log.e("aaaaaaaa",response.toString());
                        secret = response.getData();
                        comments = response.getComment();
                        adapter.comments= comments;
                        Log.e("aaaaaaaa",response.toString());
                        initData();
                        adapter.notifyDataSetChanged();

                    }
                });
    }
    private void sendComment(final String secret_id , final String user_id, String comment){
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC,Constants.COMMENT_ADD)
                .addParams(Constants.SECRET_ID, secret_id)
                .addParams(Constants.USER_ID,user_id)
                .addParams(Constants.CONTENT,comment)
                .build()
                .execute(new SecretCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("eee","cccccc");
                    }

                    @Override
                    public void onResponse(SecretInfo response) {
                        Log.e("eee","eeeeee");
                        Log.e("eee",response.getMessage());
                        Log.e("eee",response.getStatus()+"+++");
                        if (response.getStatus() ==1 ){
                            getComment(secret_id);
                        }

                    }
                });
    }

}
