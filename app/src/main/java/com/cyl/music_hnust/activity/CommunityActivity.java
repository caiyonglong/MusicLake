package com.cyl.music_hnust.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cyl.music_hnust.CommentActivity;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.NearPeopleAcivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.UserCenterMainAcivity;
import com.cyl.music_hnust.adapter.CommunityAdapter;
import com.cyl.music_hnust.model.Dynamic;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.SnackbarUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 功能：社区
 * 作者：yonglong on 2016/8/17 23:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class CommunityActivity extends BaseActivity implements XRecyclerView.LoadingListener, CommunityAdapter.OnItemClickListener {

    @Bind(R.id.community_RecyclerView)
    XRecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    CommunityAdapter MyAdapter;

    private static List<Dynamic> mdatas =new ArrayList<>();

    @Override
    protected void listener() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_community);
        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);

        initView();
        init();

    }

    private void init() {

        getDynamic();

        MyAdapter = new CommunityAdapter(this,mdatas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(MyAdapter);


        MyAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadingListener(this);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mRecyclerView.setRefreshing(true);
    }
    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        mRecyclerView.setLoadingMoreEnabled(true);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.user_name:
            case R.id.user_logo:
                User user = UserStatus.getUserInfo(this);
                if (user.getUser_id() != null) {
                    if (user.getUser_id().equals(mdatas.get(position).getUser().getUser_id())) {
                        Intent it = new Intent(this, UserCenterMainAcivity.class);
                        startActivity(it);
                    } else {
                        Log.e("LOOOOOO+++++0", mdatas.get(position).getUser().getUser_id());
                        Intent it = new Intent(this, NearPeopleAcivity.class);
                        it.putExtra("flag", 2);
                        it.putExtra("user_id", mdatas.get(position).getUser().getUser_id());
                        startActivity(it);
                    }
                }else {
                    Intent it = new Intent(this, LoginActivity.class);
                    startActivity(it);
                }
                break;
            case R.id.container:
            case R.id.content_text:
                Intent it = new Intent(this, CommentActivity.class);
                it.putExtra("position", position);
                it.putExtra("flag", 0);
                it.putExtra("dynamic_id", mdatas.get(position).getDynamic_id());
                startActivity(it);
                SnackbarUtil.show(mRecyclerView, "comment+1", 0);
                break;
        }
    }
    private void getDynamic(){
        OkHttpUtils.get().url(Constants.DEFAULT_URL)
                .addParams("user_id","1305030212")
                .addParams("updateDetail",null)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response.toString());
                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(String.valueOf(response));

                            mdatas = JsonParsing.getDynamic(dataJson);

                            MyAdapter.myDatas = mdatas;

                            MyAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
