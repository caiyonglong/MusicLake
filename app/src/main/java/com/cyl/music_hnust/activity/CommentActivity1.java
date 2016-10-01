package com.cyl.music_hnust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.CommentRecyclerViewAdapter;
import com.cyl.music_hnust.callback.SecretCallback;
import com.cyl.music_hnust.model.Comment;
import com.cyl.music_hnust.model.Common;
import com.cyl.music_hnust.model.Secret;
import com.cyl.music_hnust.model.SecretInfo;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.cyl.music_hnust.view.FullyLinearLayoutManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 永龙 on 2016/3/15.
 */
public class CommentActivity1 extends AppCompatActivity implements View.OnClickListener {
    public TextView user_name;
    public NetworkImageView user_logo;
    public TextView content_time;
    public TextView content_text;
    public static TextView item_action_comment;
    public static TextView item_action_love;
    public EditText comment_content;
    public ImageButton comment_commit;
    public static ImageButton IsAgree;
    public TextView loadmore;
    private ImageButton back;
    private LinearLayout area_commit;

    private FloatingActionButton mFloatingActionButton;


    private RecyclerView comment_list;
    private static String position;
    private String dynamic_id;
    private static CommentRecyclerViewAdapter adapter;
    private FullyLinearLayoutManager mLayoutManager;

    private static List<Comment> mDatas;
    public static Secret mdatas;

    MyHandler handler;

    static int love_num;
    static int comment_num;

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mactivity;

        private MyHandler(Activity activity) {
            mactivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mactivity.get();
            switch (msg.what) {
                case 0:
                    comment_num = comment_num + 1;
                    item_action_comment.setText(comment_num + "评论");

                    ToastUtils.show(activity, "评论成功");
                    break;
                case 1:
                    mDatas.clear();
                    String response = msg.getData().getString("response");
                    try {
                        JSONObject dataJson = new JSONObject(response);
                        mDatas = JsonParsing.getComment(dataJson);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("size", mDatas.size() + "");
                    adapter.mDatas = mDatas;
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(activity, "评论失败,请检查网络是否正常", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    Log.e("position===handler", position + "");
                    int isagree = (int) bundle.get("agree");
//                    int num = (int) bundle.get("num");
//                    CommunityFragment.mRecyclerViewAdapter.myDatas.get(Integer.parseInt(position)).setLove(num);
//                    if (isagree == 1) {
//                        love_num = love_num + 1;
//                        Toast.makeText(activity, "已赞", Toast.LENGTH_SHORT).show();
//                        item_action_love.setText(love_num + "赞");
//                        IsAgree.setImageResource(R.mipmap.ic_action_agree1);
//                        CommunityFragment.mRecyclerViewAdapter.myDatas.get(Integer.parseInt(position)).setMyLove(true);
//
//                    } else if (isagree == 0) {
//                        love_num = love_num - 1;
//                        item_action_love.setText(love_num + "赞");
//
//                        IsAgree.setImageResource(R.mipmap.ic_action_agree);
//                        CommunityFragment.mRecyclerViewAdapter.myDatas.get(Integer.parseInt(position)).setMyLove(false);
//                    }
//                    CommunityFragment.mRecyclerViewAdapter.notifyDataSetChanged();


                    break;
            }
        }
    }

    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(CommentActivity1.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        initView();

        mLayoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentRecyclerViewAdapter(getApplicationContext(), mDatas);
        comment_list.setAdapter(adapter);
        comment_list.setLayoutManager(mLayoutManager);

        initData();


    }

    private void initData() {
        String secret_id = getIntent().getStringExtra("secret_id");

        /**
         * 动态详情
         */

        user_name.setText(mdatas.getUser().getNick());
//        user_logo.setImageUrl(mdatas.getUser().getUser_img(), imageLoader);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                mdatas.getUser().getUser_img(),user_logo, ImageUtils.getAlbumDisplayOptions()
        );
        user_logo.setDefaultImageResId(R.drawable.ic_account_circle);
        content_text.setText(mdatas.getSecret_content());
        content_time.setText(mdatas.getSecret_time());
        item_action_comment.setText(mdatas.getReport_num() + "评论");
        item_action_love.setText(mdatas.getSecret_agreeNum() + "赞");

        if (mdatas.getIsAgree()==1) {
            IsAgree.setImageResource(R.mipmap.ic_action_agree1);
        } else {
            IsAgree.setImageResource(R.mipmap.ic_action_agree);
        }

    }

    private void initView() {
        user_name = (TextView) findViewById(R.id.user_name);
        user_logo = (NetworkImageView) findViewById(R.id.user_logo);
        content_time = (TextView) findViewById(R.id.content_time);
        content_text = (TextView) findViewById(R.id.content_text);
        item_action_comment = (TextView) findViewById(R.id.item_comment_num);
        item_action_love = (TextView) findViewById(R.id.item_love_num);
        back = (ImageButton) findViewById(R.id.backImageButton);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.add_comment);
        area_commit = (LinearLayout) findViewById(R.id.area_commit);
        back.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        loadmore = (TextView) findViewById(R.id.loadmore);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_commit = (ImageButton) findViewById(R.id.comment_commit);
        IsAgree = (ImageButton) findViewById(R.id.IsAgree);
        comment_list = (RecyclerView) findViewById(R.id.comment_list);



        comment_commit.setOnClickListener(this);
        IsAgree.setOnClickListener(this);
        loadmore.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImageButton:
                finish();
                break;
            case R.id.add_comment:
                mFloatingActionButton.setVisibility(View.GONE);
                area_commit.setVisibility(View.VISIBLE);
                break;
            case R.id.IsAgree:
                User userinfo1 = UserStatus.getUserInfo(getApplicationContext());
                if (userinfo1.getUser_name() != null) {
                } else {
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(it);
                }
                break;
            case R.id.comment_commit:
                String content_comment = comment_content.getText().toString().trim();
                if (!TextUtils.isEmpty(content_comment)) {
                    Map<String, Object> map = new HashMap<>();
                    String datetime = Common.getDate(getApplicationContext());

                    User userinfo = UserStatus.getUserInfo(getApplicationContext());
                    if (userinfo.getUser_name() != null) {
                        Comment comment = new Comment();

                        comment.setUser(userinfo);
                        comment.setCommentContent(content_comment);
                        comment.setTime(datetime);


                        adapter.mDatas.add(comment);
                        adapter.notifyDataSetChanged();

                        comment_content.setText("");
                    } else {
                        Intent it = new Intent(this, LoginActivity.class);
                        startActivity(it);
                    }

                }

                break;
        }

    }
    private void getComment(String secret_id , String user_id){
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC,Constants.GET_COMMENT_LIST)
                .addParams(Constants.SECRET_ID, secret_id)
                .addParams(Constants.USER_ID,user_id)
                .build()
                .execute(new SecretCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("dddd","err");
                    }
                    @Override
                    public void onResponse(SecretInfo response) {

                        if (response.getData() == null || response.getData().size()==0) {
                            return;
                        }

                    }
                });
    }

}
