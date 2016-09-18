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
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

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

    public static List<Dynamic> mdatas;

    @Override
    protected void listener() {
        MyAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadingListener(this);
    }

    @Override
    protected void initDatas() {
        mdatas = new ArrayList<>();
        getDynamic();
        MyAdapter = new CommunityAdapter(this,mdatas);
        mRecyclerView.setAdapter(MyAdapter);
    }

    @Override
    public int getLayoutId() {

        return R.layout.acitvity_community;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
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
        getDynamic();
        mRecyclerView.setRefreshing(true);
        MyAdapter.myDatas = mdatas;
        MyAdapter.notifyDataSetChanged();

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
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {

                        Log.e("Response",response.toString());
                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(String.valueOf(response));

                            mdatas = JsonParsing.getDynamic(dataJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                        Log.e("Exception",e.toString());
                    }

                    @Override
                    public void onResponse(Object response) {
                        Log.e("OBject",response.toString());
                    }
                });

    }
}
