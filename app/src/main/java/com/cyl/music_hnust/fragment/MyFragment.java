package com.cyl.music_hnust.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cyl.music_hnust.CommentActivity;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.LoginActivity;
import com.cyl.music_hnust.NearPeopleAcivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.UserCenterMainAcivity;
import com.cyl.music_hnust.adapter.MyRecyclerViewAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.bean.Dynamic;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.http.HttpUtil;
import com.cyl.music_hnust.utils.SnackbarUtil;
import com.cyl.music_hnust.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Monkey on 2015/6/29.
 */
public class MyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, MyRecyclerViewAdapter.OnItemClickListener {

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
    MyHandler handler;

    public static MyFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MyFragment> myMusicfragment;

        private MyHandler(MyFragment myfragment) {
            myMusicfragment = new WeakReference<MyFragment>(myfragment);
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

                        mRecyclerViewAdapter.loadmore = "点击加载更多...";
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

                        mRecyclerViewAdapter.loadmore = "点击加载更多...";
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
    protected void listener() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_my;
    }

    @Override
    public void initViews() {
        myApplication = new MyApplication();
        mdatas = new ArrayList<>();
//        mRequestQueue = myApplication.getHttpQueues();
//
//        imageLoader = myApplication.getImageLoader();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handler = new MyHandler(MyFragment.this);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.id_recyclerview);

        configRecyclerView();
        volley_StringRequest_GET(0, null, null);
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark,
                R.color.setting_blue, R.color.setting_blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void configRecyclerView() {


        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), mdatas, imageLoader);
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
                volley_StringRequest_GET(0, null, null);

            }
        }, 1000);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.user_name:
            case R.id.user_logo:
                User user = UserStatus.getUserInfo(getContext());
                if (user.getUser_id() != null) {
                    if (user.getUser_id().equals(mdatas.get(position).getUser().getUser_id())) {
                        Intent it = new Intent(getContext(), UserCenterMainAcivity.class);
                        startActivity(it);
                    } else {
                        Log.e("LOOOOOO+++++0", mdatas.get(position).getUser().getUser_id());
                        Intent it = new Intent(getContext(), NearPeopleAcivity.class);
                        it.putExtra("flag", 2);
                        it.putExtra("user_id", mdatas.get(position).getUser().getUser_id());
                        startActivity(it);
                    }
                }else {
                    Intent it = new Intent(getContext(), LoginActivity.class);
                    startActivity(it);
                }

                break;
            case R.id.container:
            case R.id.content_text:
                Intent it = new Intent(getContext(), CommentActivity.class);
                it.putExtra("position", position);
                it.putExtra("flag", 0);
                it.putExtra("dynamic_id", mdatas.get(position).getDynamic_id());
                startActivity(it);
                SnackbarUtil.show(mRecyclerView, "comment+1", 0);
                break;
            case R.id.id_cardview_foot:
                int poi = 0;
                if (loadmoring) {
                    if (position - 1 >= 0) {
                        poi = position - 1;
                        String id = "1305030212";

                        String time = mdatas.get(poi).getTime();
                        moreSecret(1, time);
                    } else {
                        User userinfo = UserStatus.getUserInfo(getContext());
                        volley_StringRequest_GET(0, null, null);
                    }
                    mRecyclerViewAdapter.loadmore = "点击加载更多...";
                } else {
                    mRecyclerViewAdapter.loadmore = "暂无更多";
                }
                mRecyclerViewAdapter.notifyDataSetChanged();
                break;

        }
    }

    /**
     * 利用StringRequest实现Get请求
     */
    private void volley_StringRequest_GET(final int requestcode, String starttime, String serect_id) {
        String url = "";
        if (requestcode == 0) {
            url = "http://119.29.27.116/hcyl/music_BBS/operate.php?updateDetail&&user_id=1305030212";
        } else if (requestcode == 1) {
            url = "http://119.29.27.116/hcyl/music_BBS/operate.php?user_id=1305030212&moreSecret&&start=" + starttime;
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
                // VolleyLog.e("Error: ", error.getMessage());
            }
        });
        jsonObjectRequest.setTag("info");
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }

    private void moreSecret(final int requestcode, String starttime) {
        String url = "http://119.29.27.116/hcyl/music_BBS/operate.php?user_id=1305030212"
                + "&moreSecret&start=" + starttime;
        HttpUtil.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // VolleyLog.v("Response:%n %s", response.toString());
                try {
                    String responses = new String(responseBody, "utf-8");

                    Log.i("log", responses);
                    Message message = new Message();
                    message.what = 1;
                    JSONObject response = new JSONObject(responses);
                    JSONArray secretDetail = response.getJSONArray("secretDetail");

                    if (secretDetail.length() == 0) {
                        loadmoring = false;
                    } else {
                        loadmoring = true;
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response.toString());
                        Log.e("response", response.toString());
                        message.setData(bundle);
                    }
                    handler.sendMessage(message);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastUtil.show(getActivity(), "网络连接异常，请检查网络！");
            }
        });


    }


}

