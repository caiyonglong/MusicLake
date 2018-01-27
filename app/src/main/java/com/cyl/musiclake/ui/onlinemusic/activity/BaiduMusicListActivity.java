package com.cyl.musiclake.ui.onlinemusic.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.common.Extras;
import com.cyl.musiclake.ui.localmusic.adapter.SongAdapter;
import com.cyl.musiclake.ui.onlinemusic.contract.OnlineMusicListContract;
import com.cyl.musiclake.ui.onlinemusic.presenter.OnlineMusicListPresenter;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.SizeUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
public class BaiduMusicListActivity extends BaseActivity implements OnlineMusicListContract.View {

    private static final String TAG = "BaiduMusicListActivity";
    private List<Music> musicList = new ArrayList<>();
    private SongAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private View mViewHeader;
    ImageView mIvBackground;
    ImageView mIvCover;
    TextView mTvTitle;
    TextView mTvDate;
    TextView mTvDesc;

    private int mOffset = 0;
    private String title;
    private String type;
    private String desc;
    private String pic;
    private OnlineMusicListPresenter mPresenter;
    private int mCurrentCounter = 0;
    private int TOTAL_COUNTER = 10;
    private int limit = 10;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_online;
    }

    @Override
    protected void initView() {
        title = getIntent().getStringExtra(Extras.BILLBOARD_TITLE);
        type = getIntent().getStringExtra(Extras.BILLBOARD_TYPE);
        desc = getIntent().getStringExtra(Extras.BILLBOARD_DESC);
        pic = getIntent().getStringExtra(Extras.BILLBOARD_ALBUM);
        mToolbar.setTitle(title);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initHeaderView();
    }


    @Override
    protected void initData() {
        mPresenter = new OnlineMusicListPresenter(this);
        mPresenter.attachView(this);

        mAdapter = new SongAdapter(musicList);
        mAdapter.setUpFetchEnable(false);
        mAdapter.setEnableLoadMore(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        showHeaderInfo();

        mPresenter.loadOnlineMusicList(type, 10, mOffset);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getItem(position);
            mPresenter.playCurrentMusic(music);
        });
//        mAdapter.setUpFetchListener(() -> {
//            mOffset = 0;
//            mPresenter.loadOnlineMusicList(type, 10, mOffset);
//        });
        mAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            if (mCurrentCounter >= TOTAL_COUNTER) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd();
            } else {
                //成功获取更多数据
                mPresenter.loadOnlineMusicList(type, limit, mOffset);
                mCurrentCounter = mAdapter.getData().size();
                TOTAL_COUNTER = limit + mOffset;
            }
        }, 1000), mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    private void initHeaderView() {
        mViewHeader = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(this, 150));
        mViewHeader.setLayoutParams(params);

        mIvCover = (ImageView) mViewHeader.findViewById(R.id.iv_cover);
        mTvTitle = (TextView) mViewHeader.findViewById(R.id.tv_title);
        mTvDate = (TextView) mViewHeader.findViewById(R.id.tv_update_date);
        mTvDesc = (TextView) mViewHeader.findViewById(R.id.tv_comment);

        mTvDate.setText("最近更新：" + FormatUtil.distime(System.currentTimeMillis()));
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showErrorInfo(String msg) {
        ToastUtils.show(this, msg);
        mAdapter.loadMoreFail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHeaderInfo() {
        mTvTitle.setText(title);
//        mTvDate.setText(getString(R.string.recent_update, playlistInfo.getUpdate_date()));
        mTvDesc.setText(desc);
        GlideApp.with(this)
                .load(pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvCover);
        mAdapter.setHeaderView(mViewHeader, 0);
    }

    @Override
    public void showOnlineMusicList(List<Music> musicList) {
        if (mOffset == 0) {
            mAdapter.setNewData(musicList);
        } else {
            mAdapter.addData(musicList);
        }
        mOffset = +limit;
        mAdapter.loadMoreComplete();
    }
}
