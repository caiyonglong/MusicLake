package com.cyl.musiclake.ui.music.mv;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.musicapi.netease.CommentsItemInfo;
import com.cyl.musicapi.netease.MvInfoDetail;
import com.cyl.musicapi.netease.MvInfoDetailInfo;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.ui.music.discover.MvDetailPresenter;
import com.cyl.musiclake.view.custom.DisplayUtils;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
public class MvDetailActivity extends BaseActivity<MvDetailPresenter> implements MvDetailContract.View, OnPreparedListener {

    private static final String TAG = "MvDetailActivity";
    private List<MvInfoDetail> mvInfoDetails = new ArrayList<>();
    private SimiMvListAdapter mAdapter;
    private MvCommentAdapter mCommentAdapter;
    private MvCommentAdapter mHotCommentAdapter;

    @BindView(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.rv_similar_mv)
    RecyclerView mRecyclerView;

    @BindView(R.id.rv_hot_comment)
    RecyclerView mRvHotComment;

    @BindView(R.id.rv_comment)
    RecyclerView mRvComment;
    @BindView(R.id.singerView)
    View mSingerView;
    @BindView(R.id.singerTv)
    TextView mTvSinger;

    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.tv_title)
    TextView mTvName;
    @BindView(R.id.tv_artist)
    TextView mTvArtist;
    @BindView(R.id.tv_play_count)
    TextView mTvPlayCount;
    @BindView(R.id.tv_like_count)
    TextView mTvLikeCount;
    @BindView(R.id.tv_share_count)
    TextView mTvShareCount;
    @BindView(R.id.tv_collect_count)
    TextView mTvCollectCount;
    @BindView(R.id.tv_comment_count)
    TextView mTvCommentCount;
    @BindView(R.id.tv_mv_detail)
    TextView mTvMvDetail;
    @BindView(R.id.tv_publish_time)
    TextView mTvPublishTime;
    @BindView(R.id.fullscreenIv)
    ImageView mFullScreenIv;

    //是否是横屏
    boolean isPortrait = true;

    @OnClick(R.id.fullscreenIv)
    void fullscreen() {
        if (isPortrait) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ViewGroup.LayoutParams ll = mVideoView.getLayoutParams();
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT;
            ll.height = ViewGroup.LayoutParams.MATCH_PARENT;
            isPortrait = false;
            mFullScreenIv.setImageResource(R.drawable.ic_fullscreen_exit_white_36dp);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ViewGroup.LayoutParams ll = mVideoView.getLayoutParams();
            ll.height = DisplayUtils.dp2px(200f);
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT;
            isPortrait = true;
            mFullScreenIv.setImageResource(R.drawable.ic_fullscreen_white);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isPortrait) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ViewGroup.LayoutParams ll = mVideoView.getLayoutParams();
            ll.height = DisplayUtils.dp2px(200f);
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT;
            isPortrait = true;
            mFullScreenIv.setImageResource(R.drawable.ic_fullscreen_white);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_mv_detail;
    }

    @Override
    protected void initView() {
        mAdapter = new SimiMvListAdapter(mvInfoDetails);
        mAdapter.setEnableLoadMore(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter.bindToRecyclerView(mRecyclerView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                Log.e(TAG, scrollY + "---" + oldScrollY);
                if (scrollY > 300 && scrollY <= 400) {
                    mSingerView.setVisibility(View.VISIBLE);
                    mSingerView.setAlpha((scrollY - 300) * 0.1f);
                } else if (scrollY < 300 && mSingerView.getVisibility() == View.VISIBLE) {
                    mSingerView.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected String setToolbarTitle() {
        return getIntent().getStringExtra(Extras.MV_TITLE);
    }

    @Override
    protected void initData() {
        String mVid = getIntent().getStringExtra(Extras.MV_ID);
        showLoading();
        mPresenter.loadMvDetail(mVid);
        mPresenter.loadSimilarMv(mVid);
        mPresenter.loadMvComment(mVid);
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void listener() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showError(String message, boolean showRetryButton) {
        super.showError(message, showRetryButton);
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        initData();
    }

    @Override
    public void showMvList(List<MvInfoDetail> mvList) {
        mAdapter.setNewData(mvList);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, MvDetailActivity.class);
            intent.putExtra(Extras.MV_TITLE, mvList.get(position).getName());
            intent.putExtra(Extras.MV_ID, String.valueOf(mvList.get(position).getId()));
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void showMvDetailInfo(MvInfoDetailInfo mvInfoDetailInfo) {
        hideLoading();
        if (mvInfoDetailInfo != null && mvInfoDetailInfo.getBrs().getP720() != null) {
            mNestedScrollView.setVisibility(View.VISIBLE);
            String url = mvInfoDetailInfo.getBrs().getP720();
            initPlayer();
            //For now we just picked an arbitrary item to play
            mVideoView.setPreviewImage(Uri.parse(mvInfoDetailInfo.getCover()));
            mVideoView.setVideoURI(Uri.parse(url));
            updateMvInfo(mvInfoDetailInfo);
        }
    }

    @Override
    public void showMvHotComment(List<CommentsItemInfo> mvHotCommentInfo) {
        if (mHotCommentAdapter == null) {
            mHotCommentAdapter = new MvCommentAdapter(mvHotCommentInfo);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //初始化评论adapter
            mRvHotComment.setLayoutManager(layoutManager);
            mRvHotComment.setAdapter(mHotCommentAdapter);
            mRvHotComment.setNestedScrollingEnabled(false);
            mHotCommentAdapter.bindToRecyclerView(mRvHotComment);

        } else {
            mHotCommentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showMvComment(List<CommentsItemInfo> mvCommentInfo) {
        if (mCommentAdapter == null) {
            mCommentAdapter = new MvCommentAdapter(mvCommentInfo);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //初始化评论adapter
            mRvComment.setLayoutManager(layoutManager);
            mRvComment.setAdapter(mCommentAdapter);
            mRvComment.setNestedScrollingEnabled(false);
            mCommentAdapter.bindToRecyclerView(mRvComment);

        } else {
            mCommentAdapter.notifyDataSetChanged();
        }
    }

    public void updateMvInfo(MvInfoDetailInfo info) {
        mTvPlayCount.setText(getString(R.string.play_count, info.getPlayCount()));
        mTvLikeCount.setText(String.valueOf(info.getLikeCount()));
        mTvShareCount.setText(String.valueOf(info.getShareCount()));
        mTvCollectCount.setText(String.valueOf(info.getSubCount()));
        mTvCommentCount.setText(String.valueOf(info.getCommentCount()));
        mTvName.setText(info.getName());
        mTvArtist.setText(info.getArtistName());
        mTvSinger.setText(info.getArtistName());
        mTvMvDetail.setText(info.getDesc());
        mTvPublishTime.setText(getString(R.string.publish_time, info.getPublishTime()));
        updateTitle(info.getName());

        mSingerView.setOnClickListener(v -> {
            Artist artist = new Artist();
            artist.setArtistId(String.valueOf(info.getArtistId()));
            artist.setType(Constants.NETEASE);
            artist.setName(info.getArtistName());
            NavigationHelper.INSTANCE.navigateToPlaylist(this, artist, null);
        });
    }

    private void initPlayer() {
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setRepeatMode(Player.REPEAT_MODE_ONE);
    }

    @Override
    public void onPrepared() {
        mVideoView.start();
    }

    private int getFullscreenUiFlags() {
        return View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    }

    private int getStableUiFlags() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    }
}
