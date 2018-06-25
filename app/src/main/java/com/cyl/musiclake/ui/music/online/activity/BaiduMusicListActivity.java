package com.cyl.musiclake.ui.music.online.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.dialog.DownloadDialog;
import com.cyl.musiclake.ui.music.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.online.contract.BaiduListContract;
import com.cyl.musiclake.ui.music.online.presenter.BaiduListPresenter;
import com.cyl.musiclake.utils.CoverLoader;
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
public class BaiduMusicListActivity extends BaseActivity<BaiduListPresenter> implements BaiduListContract.View {

    private static final String TAG = "BaiduMusicListActivity";
    private List<Music> musicList = new ArrayList<>();
    private SongAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

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
    private int mCurrentCounter = 0;
    private int TOTAL_COUNTER = 0;
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
        setToolbarTitle(title);
        initHeaderView();
    }


    @Override
    protected void initData() {
        mAdapter = new SongAdapter(musicList);
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
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.play(position, musicList, Constants.BAIDU + type);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getItem(position);
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance(music)
                                .show(getSupportFragmentManager(), getLocalClassName());
                        break;
                    case R.id.popup_add_playlist:
                        ToastUtils.show("暂不支持添加百度音乐");
                        break;
                    case R.id.popup_song_download:
                        DownloadDialog.newInstance(music)
                                .show(getSupportFragmentManager(), getLocalClassName());
                        break;
                }
                return false;
            });
            popupMenu.inflate(R.menu.popup_song_online);
            popupMenu.show();
        });
        mAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            if (mCurrentCounter < TOTAL_COUNTER) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd();
            } else {
                //成功获取更多数据
                mPresenter.loadOnlineMusicList(type, limit, mOffset);
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
        CoverLoader.loadImageView(this, pic, mIvCover);
        mAdapter.setHeaderView(mViewHeader, 0);
    }

    @Override
    public void showOnlineMusicList(List<Music> musicList) {
        this.musicList.addAll(musicList);
        mAdapter.setNewData(this.musicList);
        mOffset = mOffset + limit;
        mCurrentCounter = mAdapter.getData().size();
        TOTAL_COUNTER = mOffset;
        mAdapter.loadMoreComplete();
    }
}
