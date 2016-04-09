package com.cyl.music_hnust;

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
import com.cyl.music_hnust.view.FullyLinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 永龙 on 2016/3/15.
 */
public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView user_name;
    public NetworkImageView user_logo;
    public TextView content_time;
    public TextView content_text;
    public TextView item_action_comment;
    public TextView item_action_love;
    public EditText comment_content;
    public Button comment_commit;
    public TextView loadmore;
    private ImageButton back;


    private RecyclerView comment_list;
    private String position;
    private String dynamic_id;
    private CommentRecyclerViewAdapter adapter;
    private FullyLinearLayoutManager mLayoutManager;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader ;

    private List<Comment> mDatas;

    Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(),"评论成功",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    mDatas.clear();
                    mDatas = (List<Comment>) msg.obj;
                    Log.e("size",mDatas.size()+"");
                    adapter.mDatas=mDatas;
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    String error_code = (String) msg.obj;
                    Toast.makeText(getApplicationContext(),"评论失败,错误代码为"+error_code,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mRequestQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        Intent it =getIntent();
        position = it.getIntExtra("position",-1)+"";
        dynamic_id = it.getStringExtra("dynamic_id")+"";

        mDatas =new ArrayList<>();

        User userinfo = UserStatus.getUserInfo(getApplicationContext());
        volley_StringRequest_GET(userinfo.getUser_id(),dynamic_id);

        Log.e("====", dynamic_id);
        initView();
        mLayoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentRecyclerViewAdapter(getApplicationContext(),mDatas);
        comment_list.setAdapter(adapter);

        comment_list.setLayoutManager(mLayoutManager);




    }

    private void initView() {
        user_name = (TextView) findViewById(R.id.user_name);
        user_logo = (NetworkImageView) findViewById(R.id.user_logo);
        content_time = (TextView) findViewById(R.id.content_time);
        content_text = (TextView) findViewById(R.id.content_text);
        item_action_comment = (TextView) findViewById(R.id.item_action_comment);
        item_action_love = (TextView) findViewById(R.id.item_action_love);
        back = (ImageButton) findViewById(R.id.backImageButton);

        back.setOnClickListener(this);
        loadmore = (TextView) findViewById(R.id.loadmore);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_commit = (Button) findViewById(R.id.comment_commit);
        comment_list = (RecyclerView) findViewById(R.id.comment_list);

        /**
         * 动态详情
         */
        user_name.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getUser().getUser_name());
        user_logo.setImageUrl(MyFragment.mdatas.get(Integer.parseInt(position)).getUser().getUser_img(), imageLoader);
        user_logo.setDefaultImageResId(R.mipmap.user_icon_default_main);
        content_text.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getContent());
        content_time.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getTime());
        item_action_comment.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getComment()+"评论");
        item_action_love.setText(MyFragment.mdatas.get(Integer.parseInt(position)).getLove()+"赞");


        comment_commit.setOnClickListener(this);
        loadmore.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backImageButton:
                finish();
                break;
            case R.id.comment_commit:
                String content_comment = comment_content.getText().toString().trim();
                if (!TextUtils.isEmpty(content_comment)) {
                    Map<String, Object> map = new HashMap<>();
                    String datetime = Common.getDate(getApplicationContext());

                    User userinfo = UserStatus.getUserInfo(getApplicationContext());
                    if (userinfo.getUser_name()!=null) {
                        Comment comment = new Comment();

                        comment.setUser(userinfo);
                        comment.setCommentContent(content_comment);
                        comment.setTime(datetime);


                        adapter.mDatas.add(comment);
                        adapter.notifyDataSetChanged();

                        volley_Request_GET(userinfo.getUser_id(),dynamic_id,content_comment);
                        comment_content.setText("");
                    }else {
                        Intent it = new Intent(this,LoginActivity.class);
                        startActivity(it);
                    }

                }

                break;
        }

    }
    /**http://hcyl.sinaapp.com/music_BBS/operate.php?user_id=5&&newComment&&secret_id=18&&comment=内容
     * 利用StringRequest实现Get请求
     */
    private void volley_StringRequest_GET(String user_id,String secret_id) {

        String url = "http://119.29.27.116/hcyl/music_BBS/operate.php?user_id="+user_id+"&&showSecretComment&&secret_id="+secret_id;
        // 1 创建RequestQueue对象

        // 2 创建StringRequest对象
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // VolleyLog.v("Response:%n %s", response.toString());
                        Log.i("log", response.toString());

                        try {
                            mDatas = new ArrayList<>();
                            mDatas= JsonParsing.getComment(response);
                            Message message =new Message();
                            message.what=1;
                            message.obj = mDatas;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                // VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }
    private void volley_Request_GET(String user_id,String secret_id,String comment) {


        String url = "http://119.29.27.116/hcyl/music_BBS/operate.php?user_id="+user_id+"&&newComment&&secret_id="+secret_id+"&&comment="+comment;
        // 1 创建RequestQueue对象

        // 2 创建StringRequest对象
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("jsonobject",response.toString());
                        try {
                            int error_code= response.getInt("error");
                            Log.e("error_code",error_code+"");
                            if (error_code==-1){
                                handler.sendEmptyMessage(0);
                            }else {
                                Message msg= new Message();
                                msg.what =2;
                                msg.obj =error_code;
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                // VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }


}
