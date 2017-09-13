package com.cyl.music_hnust.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.ui.adapter.CommunityAdapter;
import com.cyl.music_hnust.callback.SecretCallback;
import com.cyl.music_hnust.model.community.Secret;
import com.cyl.music_hnust.model.community.SecretInfo;
import com.cyl.music_hnust.model.user.User;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 附近人信息
 */
public class NearPeopleAcivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.user_logo)
    CircleImageView user_logo;
    @Bind(R.id.user_email)
    TextView user_email;
    @Bind(R.id.user_phone)
    TextView user_phone;
    @Bind(R.id.user_nick)
    TextView user_nick;
    @Bind(R.id.user_secret_num)
    TextView user_secret_num;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.info_card)
    CardView info_card;

    User userinfo;

    CommunityAdapter MyAdapter;

    private static List<Secret> mdatas = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.user_center_near;
    }

    @Override
    protected void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("");
    }

    @Override
    protected void initData() {
        userinfo = (User) getIntent().getSerializableExtra("userinfo");
        user_email.setText(userinfo.getUser_email() + "");
        user_phone.setText(userinfo.getPhone() + "");
        user_nick.setText(userinfo.getNick() + "");
        user_name.setText(userinfo.getUser_name() + "");

        user_secret_num.setText(getString(R.string.secret_num, 0));

        if (userinfo.getSecret()==2){
            info_card.setVisibility(View.GONE);
        }else {
            info_card.setVisibility(View.VISIBLE);
        }

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                userinfo.getUser_img(), user_logo, ImageUtils.getAlbumDisplayOptions());

        MyAdapter = new CommunityAdapter(this, mdatas);
        mRecyclerView.setAdapter(MyAdapter);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setSmoothScrollbarEnabled(true);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        getSecret(userinfo.getUser_id());

    }

    @Override
    protected void listener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSecret(String user_id) {
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC, Constants.GET_MYSECRET_LIST)
                .addParams(Constants.USER_ID, user_id)
                .build()
                .execute(new SecretCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtils.show(NearPeopleAcivity.this, R.string.error_connection);
                    }

                    @Override
                    public void onResponse(SecretInfo response) {
                        if (response.getStatus()==1) {
                            mdatas.clear();
                            mdatas.addAll(response.getData());
                            user_secret_num.setText(getString(R.string.secret_num, response.getData().size()));
                            MyAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mdatas.clear();
        user_secret_num.setText(getString(R.string.secret_num, 0));
        MyAdapter.notifyDataSetChanged();
    }
}
