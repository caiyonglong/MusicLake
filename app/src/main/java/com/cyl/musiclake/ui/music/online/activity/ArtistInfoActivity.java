package com.cyl.musiclake.ui.music.online.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.doupan.DoubanMusic;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.main.WebActivity;
import com.cyl.musiclake.ui.music.online.contract.ArtistInfoContract;
import com.cyl.musiclake.ui.music.online.presenter.ArtistInfoPresenter;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yonglong on 2016/11/30.
 */

public class ArtistInfoActivity extends BaseActivity implements ArtistInfoContract.View {

    private static final String TAG = "ArtistInfoActivity";
    @BindView(R.id.tv_desc)
    TextView mTvDesc;

    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.album_art)
    ImageView mAlbum;
    @BindView(R.id.progress)
    ProgressBar progress;

    ArtistInfoPresenter mPresenter;
    private String url;
    private String name;

    @OnClick(R.id.btn_detail)
    void toDetail() {
        if (url != null && name != null) {
            WebActivity.start(this, name, url);
        } else {
            ToastUtils.show("暂无信息");
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_artist;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        Music music = getIntent().getParcelableExtra(Extras.TING_UID);
        mPresenter = new ArtistInfoPresenter();
        mPresenter.attachView(this);
        mPresenter.loadArtistInfo(music);
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
    public void showMusicInfo(DoubanMusic doubanMusic) {
        CoverLoader.loadImageView(this, doubanMusic.getMusics().get(0).getImage(), mAlbum);
        DoubanMusic.MusicsBean.AttrsBean attrsBean = doubanMusic.getMusics().get(0).getAttrs();
        url = doubanMusic.getMusics().get(0).getAlt();
        name = attrsBean.getSinger().get(0);
        StringBuilder sb = new StringBuilder();
        sb.append("歌手：")
                .append(attrsBean.getSinger().get(0))
                .append("\n")
                .append("曲目：")
                .append(attrsBean.getTracks().get(0));
        mTvDesc.setText(sb.toString());
    }
}
