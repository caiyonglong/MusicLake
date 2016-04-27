package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cyl.music_hnust.adapter.CommentRecyclerViewAdapter;
import com.cyl.music_hnust.bean.Comment;
import com.cyl.music_hnust.bean.Common;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.fragment.MyFragment;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.http.HttpUtil;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ToastUtil;
import com.cyl.music_hnust.view.FullyLinearLayoutManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by 永龙 on 2016/3/15.
 */
public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
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
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    private static List<Comment> mDatas;
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
                    comment_num=comment_num+1;
                    item_action_comment.setText(comment_num + "评论");

                    ToastUtil.show(activity, "评论成功");
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
                    int num = (int) bundle.get("num");
                    MyFragment.mRecyclerViewAdapter.myDatas.get(Integer.parseInt(position)).setLove(num);
                    if (isagree == 1) {
                        love_num = love_num+1;
                        Toast.makeText(activity, "已赞", Toast.LENGTH_SHORT).show();
                        item_action_love.setText(love_num + "赞");
                        IsAgree.setImageResource(R.mipmap.ic_action_agree1);
                        MyFragment.mRecyclerViewAdapter.myDatas.get(Integer.parseInt(position)).setMyLove(true);

                    } else if (isagree == 0) {
                        love_num = love_num-1;
                        item_action_love.setText(love_num + "赞");

                        IsAgree.setImageResource(R.mipmap.ic_action_agree);
                        MyFragment.mRecyclerViewAdapter.myDatas.get(Integer.parseInt(position)).setMyLove(false);
                    }
                    MyFragment.mRecyclerViewAdapter.notifyDataSetChanged();


                    break;
            }
        }
    }

    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mRequestQueue = Volley.newRequestQueue(this);
        handler= new MyHandler(this);
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        Intent it = getIntent();
        position = it.getIntExtra("position", -1) + "";
        dynamic_id = it.getStringExtra("dynamic_id") + "";
        love_num = MyFragment.mdatas.get(Integer.parseInt(position)).getLove();
        comment_num = MyFragment.mdatas.get(Integer.parseInt(position)).getComment();

        Log.e("ee",love_num+"============"+comment_num+"");
        mDatas = new ArrayList<>();

        User userinfo = UserStatus.getUserInfo(getApplicationContext());
        volley_StringRequest_GET(userinfo.getUser_id(), dynamic_id, 1);

        Log.e("====", dynamic_id);
        initView();
        mLayoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentRecyclerViewAdapter(getApplicationContext(), mDatas);
        comment_list.setAdapter(adapter);

        comment_list.setLayoutManager(mLayoutManager);


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

        /**
         * 动态详情
         */
        String imgUrl = "http://119.29.27.116/hcyl/music_BBS";
        user_name.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getUser().getUser_name());
        user_logo.setImageUrl(imgUrl + MyFragment.mdatas.get(Integer.parseInt(position)).getUser().getUser_img(), imageLoader);
        user_logo.setDefaultImageResId(R.mipmap.user_icon_default_main);
        content_text.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getContent());
        content_time.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getTime());
        item_action_comment.setText(comment_num + "评论");
        item_action_love.setText(love_num + "赞");

        if (MyFragment.mdatas.get(Integer.parseInt(position)).isMyLove()) {
            IsAgree.setImageResource(R.mipmap.ic_action_agree1);
        } else {
            IsAgree.setImageResource(R.mipmap.ic_action_agree);
        }

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
                    volley_StringRequest_GET(userinfo1.getUser_id(), dynamic_id, 2);

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

                        volley_Request_GET(userinfo.getUser_id(), dynamic_id, content_comment);
                        comment_content.setText("");
                    } else {
                        Intent it = new Intent(this, LoginActivity.class);
                        startActivity(it);
                    }

                }

                break;
        }

    }

    /**
     * http://hcyl.sinaapp.com/music_BBS/operate.php?user_id=5&&newComment&&secret_id=18&&comment=内容
     * 利用StringRequest实现Get请求
     */
    private void volley_StringRequest_GET(String user_id, String secret_id, final int requestcode) {
        String url = "";
        if (requestcode == 1) {
            url = "http://119.29.27.116/hcyl/music_BBS/operate.php?" +
                    "user_id=" + user_id +
                    "&showSecretComment&&secret_id=" + secret_id;
        } else if (requestcode == 2) {
            url = "http://119.29.27.116/hcyl/music_BBS/operate.php?user_id=" + user_id + "&&changeAgree&&secret_id=" + secret_id;
        }
        // 1 创建RequestQueue对象

        // 2 创建StringRequest对象
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // VolleyLog.v("Response:%n %s", response.toString());
                        Log.i("log", response.toString());
                        Message message = new Message();
                        if (requestcode == 1) {

                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("response", response.toString());

                            message.setData(bundle);

                        } else {

                            message.what = 3;
                            try {
                                int agree = response.getInt("agree");

                                int num = response.getInt("num");
                                Bundle bundle = new Bundle();
                                bundle.putInt("agree", agree);
                                bundle.putInt("num", num);
                                message.setData(bundle);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendMessage(message);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ToastUtil.show(getApplicationContext(), "网络连接错误，请检查网络");
            }
        });
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }

    private void volley_Request_GET(String user_id, String secret_id, String comment) {

        String url = "http://119.29.27.116/hcyl/music_BBS/operate.php?" +
                "user_id=" + user_id +
                "&newComment&secret_id=" + secret_id +
                "&comment=" + comment +
                "&secretTime=" + FormatUtil.getTime();
        // 1 创建RequestQueue对象
        Log.e("path.", url + "");
        HttpUtil.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String str = new String(responseBody, "utf-8");
                    Log.i("log", str);

                    JSONObject response = new JSONObject(str);
                    Log.e("jsonobject", response.toString());
                    try {
                        int error_code = response.getInt("error");
                        Log.e("error_code", error_code + "");
                        if (error_code == -1) {
                            handler.sendEmptyMessage(0);
                        } else {
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = error_code;
                            handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                ToastUtil.show(getApplicationContext(), "网络异常，请检查网络！");
            }
        });
    }


}
