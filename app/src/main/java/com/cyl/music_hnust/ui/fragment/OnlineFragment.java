package com.cyl.music_hnust.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.download.NetworkUtil;
import com.cyl.music_hnust.model.music.OnlinePlaylists;
import com.cyl.music_hnust.model.music.OnlinePlaylists.Billboard;
import com.cyl.music_hnust.ui.activity.OnlineMusicActivity;
import com.cyl.music_hnust.ui.adapter.OnlineAdapter;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineFragment extends BaseFragment implements OnlineAdapter.OnItemClickListener {

    private static final String TAG = "OnlineFragment";
    RecyclerView mRecyclerView;
    //适配器
    private OnlineAdapter mAdapter;
    //排行榜集合
    private List<Billboard> mBillboards = new ArrayList<>();
    private TextView tv_empty;
    private ProgressBar progress;
    private LinearLayout loading;

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview_online;
    }

    @Override
    public void initViews() {
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        loading = (LinearLayout) rootView.findViewById(R.id.loading);
        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

    }

    @Override
    protected void initDatas() {

        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //适配器
        mAdapter = new OnlineAdapter(getActivity(), mBillboards);
        mRecyclerView.setAdapter(mAdapter);
        init();

    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 初始化列表,当无数据时显示提示
     */
    private void init() {
        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            tv_empty.setText("网络不给力，请检查连接!");
            progress.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);

            new TTask().execute();
        }

    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onItemClick(View view, int position) {
//        ToastUtil.show(getContext(),"===="+position);
        Billboard billboard = mBillboards.get(position);
        Intent intent = new Intent(getActivity(), OnlineMusicActivity.class);
        intent.putExtra(Extras.BILLBOARD_TITLE, billboard.getName());
        intent.putExtra(Extras.BILLBOARD_TYPE, billboard.getType());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private class TTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(Constants.BASE_MUSIC_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                Log.e(TAG, "请求吗 :" + code);
                if (code == 200) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream is = conn.getInputStream(); // 字节流转换成字符串
                    int by = 0;
                    byte[] buffer = new byte[1024];
                    while ((by = is.read(buffer)) > 0) {
                        out.write(buffer, 0, by);
                    }
                    out.close();
                    String json = new String(out.toByteArray());
                    Log.e(TAG, "请求吗 :" + json);
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Gson gson = new Gson();
                OnlinePlaylists response = gson.fromJson(s, OnlinePlaylists.class);
                mBillboards = response.getContent();
                //移除T榜
                mBillboards.remove(3);
                mAdapter.setmBillboards(mBillboards);
                mAdapter.notifyDataSetChanged();

                Log.e(TAG, "post :" + response.toString());
            }
        }
    }
}