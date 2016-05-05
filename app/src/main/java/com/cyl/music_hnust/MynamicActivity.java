package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.adapter.MyRecyclerViewAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.bean.Dynamic;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.utils.SnackbarUtil;
import com.cyl.music_hnust.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonglong on 2016/5/5.
 */
public class MynamicActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, MyRecyclerViewAdapter.OnItemClickListener {

    private static SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    //    private LinearLayoutManager mLayoutManager;
    public static MyRecyclerViewAdapter mRecyclerViewAdapter;
    private RequestQueue mRequestQueue;
    private MyApplication myApplication;
    private ImageLoader imageLoader;
    public static List<Dynamic> mdatas;
    public static int position;
    public static boolean loadmoring = true;
    private User userinfo;
    MyHandler handler;

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> myMusicfragment;

        private MyHandler(Activity myfragment) {
            myMusicfragment = new WeakReference<Activity>(myfragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (loadmoring) {


                        Bundle bundle = new Bundle();
                        bundle = msg.getData();
                        String response = (String) bundle.get("response");

                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(response);

                            mdatas = JsonParsing.getDynamic(dataJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("size", mdatas.size() + "");
                        mRecyclerViewAdapter.myDatas = mdatas;
                        mRecyclerViewAdapter.notifyDataSetChanged();

                        mRecyclerViewAdapter.loadmore = "下拉加载更多...";
                    } else {
                        mRecyclerViewAdapter.loadmore = "暂无更多";
                    }
                    break;
                case 1:
                    if (loadmoring) {
                        // mdatas.clear();
                        List<Dynamic> newdatas = new ArrayList<>();
                        Bundle bundle = new Bundle();
                        bundle = msg.getData();
                        String response = (String) bundle.get("response");

                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(response);

                            newdatas = JsonParsing.getDynamic(dataJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("size", mdatas.size() + "");
                        mdatas.addAll(newdatas);
                        mRecyclerViewAdapter.myDatas.addAll(newdatas);
                        mRecyclerViewAdapter.notifyDataSetChanged();

                        mRecyclerViewAdapter.loadmore = "下拉加载更多...";
                    } else {
                        mRecyclerViewAdapter.loadmore = "暂无更多";
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;

                case 3:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myApplication = new MyApplication();
        mdatas = new ArrayList<>();
        mRequestQueue = myApplication.getHttpQueues();
        userinfo = UserStatus.getUserInfo(this);

        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
            }

            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }
        });


        handler = new MyHandler(this);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);

        configRecyclerView();
//        mLayoutManager = new LinearLayoutManager(getActivity());
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark,
                R.color.setting_blue, R.color.setting_blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

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


    private void configRecyclerView() {


        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(getApplicationContext(), mdatas, imageLoader);
        mRecyclerViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onRefresh() {

        // 刷新时模拟数据的变化
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

                volley_StringRequest_GET( 0,userinfo.getUser_id());

            }
        }, 1000);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.container:
            case R.id.content_text:
                Intent it = new Intent(getApplicationContext(), CommentActivity.class);
                it.putExtra("position", position);
                it.putExtra("flag", 1);
                it.putExtra("dynamic_id", mdatas.get(position).getDynamic_id());
                startActivity(it);
                SnackbarUtil.show(mRecyclerView, "comment+1", 0);
                break;

        }
    }

    /**
     * 利用StringRequest实现Get请求
     */
    private void volley_StringRequest_GET( final int requestcode, String user_id) {
        String url = "";
        if (requestcode == 0) {
            url = "http://119.29.27.116/hcyl/music_BBS/operate.php?MyDetail&user_id="+user_id;
        }
        // 2 创建StringRequest对象
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // VolleyLog.v("Response:%n %s", response.toString());
                        Log.i("log", response.toString());
                        try {


                            Message message = new Message();
                            message.what = requestcode;
                            if (requestcode != 2) {
                                JSONArray secretDetail = response.getJSONArray("secretDetail");
                                if (secretDetail.length() == 0) {
                                    loadmoring = false;
                                } else {
                                    loadmoring = true;


                                    Bundle bundle = new Bundle();
                                    bundle.putString("response", response.toString());
                                    message.setData(bundle);
                                }
                            }
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ToastUtil.show(getApplicationContext(), "网络连接异常，请检查网络！");
            }
        });
        jsonObjectRequest.setTag("info");
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }


}
