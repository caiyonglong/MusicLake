package com.cyl.music_hnust.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.CommunityAdapter;
import com.cyl.music_hnust.callback.SecretCallback;
import com.cyl.music_hnust.model.Secret;
import com.cyl.music_hnust.model.SecretInfo;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

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
    private int pagenum =1;
    private String user_id ="";

    private static List<Secret> mdatas =new ArrayList<>();

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
        MyAdapter = new CommunityAdapter(this,mdatas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(MyAdapter);


        MyAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadingListener(this);
        if (UserStatus.getstatus(this)){
            user_id = UserStatus.getUserInfo(this).getUser_id();
        }
        getSecret(pagenum,user_id);
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
        Log.e("111====1", String.valueOf(pagenum));
        getSecret(pagenum,user_id);
        mRecyclerView.refreshComplete();
    }
    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        Log.e("111======1", String.valueOf(pagenum));
        getSecret(pagenum,user_id);
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.user_name:
            case R.id.user_logo:
                break;
            case R.id.container:
            case R.id.content_text:
                Intent it = new Intent(this, CommentActivity1.class);
                it.putExtra(Extras.SECRET_ID,mdatas.get(position).getSecret_id());
                startActivity(it);
                break;
        }
    }
    private void getSecret(int PageNum , String user_id){
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC,Constants.GET_SECRET_LIST)
                .addParams(Constants.PAGENUM, String.valueOf(PageNum))
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
                            mRecyclerView.loadMoreComplete();
                            return;
                        }
                        pagenum = pagenum+1;
                        mdatas = response.getData();
                        MyAdapter.myDatas.addAll(mdatas);
                        MyAdapter.notifyDataSetChanged();
                    }
                });
    }
}
