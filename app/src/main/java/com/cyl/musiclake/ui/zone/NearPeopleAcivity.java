package com.cyl.musiclake.ui.zone;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.ui.my.user.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 附近人信息
 */
public class NearPeopleAcivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_logo)
    CircleImageView user_logo;
    @BindView(R.id.user_email)
    TextView user_email;
    @BindView(R.id.user_phone)
    TextView user_phone;
    @BindView(R.id.user_nick)
    TextView user_nick;
    @BindView(R.id.user_secret_num)
    TextView user_secret_num;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.info_card)
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
        user_email.setText(userinfo.getEmail());
        user_phone.setText(userinfo.getPhone());
        user_nick.setText(userinfo.getNick());
        user_name.setText(userinfo.getName());

        user_secret_num.setText(getString(R.string.secret_num, 0));

        if (userinfo.getSecret() == 2) {
            info_card.setVisibility(View.GONE);
        } else {
            info_card.setVisibility(View.VISIBLE);
        }

        GlideApp.with(this)
                .load(userinfo.getAvatar())
                .into(user_logo);


        MyAdapter = new CommunityAdapter(this, mdatas);
        mRecyclerView.setAdapter(MyAdapter);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        getSecret(userinfo.getId());

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
//        OkHttpUtils.post().url(Constants.DEFAULT_URL)
//                .addParams(Constants.FUNC, Constants.GET_MYSECRET_LIST)
//                .addParams(Constants.USER_ID, user_id)
//                .build()
//                .execute(new SecretCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        ToastUtils.show(NearPeopleAcivity.this, R.string.error_connection);
//                    }
//
//                    @Override
//                    public void onResponse(SecretInfo response) {
//                        if (response.getStatus() == 1) {
//                            mdatas.clear();
//                            mdatas.addAll(response.getData());
//                            user_secret_num.setText(getString(R.string.secret_num, response.getData().size()));
//                            MyAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mdatas.clear();
        user_secret_num.setText(getString(R.string.secret_num, 0));
        MyAdapter.notifyDataSetChanged();
    }
}
