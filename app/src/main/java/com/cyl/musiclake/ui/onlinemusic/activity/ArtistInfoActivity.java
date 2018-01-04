package com.cyl.musiclake.ui.onlinemusic.activity;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.onlinemusic.contract.ArtistInfoContract;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineArtistInfo;
import com.cyl.musiclake.ui.onlinemusic.presenter.ArtistInfoPresenter;
import com.cyl.musiclake.utils.Extras;
import com.cyl.musiclake.utils.ToastUtils;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/30.
 */

public class ArtistInfoActivity extends BaseActivity implements ArtistInfoContract.View {

    private static final String TAG = "ArtistInfoActivity";
    @BindView(R.id.li_container)
    LinearLayout li_container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.progress)
    ProgressBar progress;

    ArtistInfoPresenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_artist;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        String tingUid = getIntent().getStringExtra(Extras.TING_UID);
        mPresenter = new ArtistInfoPresenter();
        mPresenter.attachView(this);
        mPresenter.loadArtistInfo(tingUid);
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

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showErrorInfo(String msg) {
        ToastUtils.show(this, msg);
    }

    @Override
    public void showArtistInfo(OnlineArtistInfo artistInfo) {

        String name = artistInfo.getName();
        String avatarUri = artistInfo.getAvatar_s1000();
        String country = artistInfo.getCountry();
        String constellation = artistInfo.getConstellation();
        float stature = artistInfo.getStature();
        float weight = artistInfo.getWeight();
        String birth = artistInfo.getBirth();
        String intro = artistInfo.getIntro();
        String url = artistInfo.getUrl();
        if (!TextUtils.isEmpty(avatarUri)) {
            ImageView ivAvatar = new ImageView(this);
            ivAvatar.setImageResource(R.drawable.default_cover);
            ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideApp.with(this)
                    .load(url)
                    .into(ivAvatar);

            li_container.addView(ivAvatar);
        }
        if (!TextUtils.isEmpty(name)) {
            toolbar.setTitle(name);
            TextView tvName = new TextView(this);
            tvName.setTextSize(18);
            tvName.setPadding(0, 0, 0, 10);
            tvName.setText("姓名：" + name);
            li_container.addView(tvName);
        }
        if (!TextUtils.isEmpty(country)) {
            TextView tvCountry = new TextView(this);
            tvCountry.setTextSize(18);
            tvCountry.setPadding(0, 0, 0, 10);
            tvCountry.setText("国籍：" + country);
            li_container.addView(tvCountry);
        }
        if (!TextUtils.isEmpty(constellation) && !constellation.equals("未知")) {
            TextView tvConstellation =
                    new TextView(this);
            tvConstellation.setTextSize(18);
            tvConstellation.setText("星座：" + constellation);
            li_container.addView(tvConstellation);
        }
        if (stature != 0f) {
            TextView tvStature = new TextView(this);
            tvStature.setTextSize(18);
            tvStature.setPadding(0, 0, 0, 10);
            tvStature.setText("身高：" + stature);
            li_container.addView(tvStature);
        }
        if (weight != 0f) {
            TextView tvWeight = new TextView(this);
            tvWeight.setTextSize(18);
            tvWeight.setPadding(0, 0, 0, 10);
            tvWeight.setText("体重：" + weight);
            li_container.addView(tvWeight);
        }
        if (!TextUtils.isEmpty(birth) && !birth.equals("0000-00-00")) {
            TextView tvBirth = new TextView(this);
            tvBirth.setTextSize(18);
            tvBirth.setPadding(0, 0, 0, 10);
            tvBirth.setText("出生日期：" + birth);
            li_container.addView(tvBirth);
        }
        if (!TextUtils.isEmpty(intro)) {
            TextView tvIntro = new TextView(this);
            tvIntro.setTextSize(18);
            tvIntro.setPadding(0, 0, 0, 10);
            tvIntro.setText("简介：" + intro);
            li_container.addView(tvIntro);
        }
        if (!TextUtils.isEmpty(url)) {
            TextView tvUrl = new TextView(this);
            String html = "<font color='#2196F3'><a href='%s'>查看更多信息</a></font>";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvUrl.setText(Html.fromHtml(String.format(html, url), 1));
            } else {
                tvUrl.setText(Html.fromHtml(String.format(html, url)));
            }
            tvUrl.setMovementMethod(LinkMovementMethod.getInstance());
            tvUrl.setPadding(0, 0, 0, 10);
            tvUrl.setGravity(Gravity.CENTER);
            li_container.addView(tvUrl);
        }

        if (li_container.getChildCount() == 0) {
            loading.setVisibility(View.VISIBLE);
            tv_empty.setText("暂无信息");
            progress.setVisibility(View.GONE);
        }
    }

}
