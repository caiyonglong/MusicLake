package com.cyl.musiclake.ui.music.online.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.music.online.adapter.NeteaseAdapter;
import com.cyl.musiclake.ui.music.online.contract.OnlineMusicListContract;
import com.cyl.musiclake.ui.music.online.presenter.OnlineMusicListPresenter;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.SizeUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
public class NeteaseMusicListActivity extends BaseActivity implements OnlineMusicListContract.View {

    private static final String TAG = "BaiduMusicListActivity";
    private List<NeteaseMusic> toplist = new ArrayList<>();
    private NeteaseAdapter mAdapter;

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

    private int idx;
    private String title;
    private String type;
    private String desc;
    private long time;
    private String pic;
    private OnlineMusicListPresenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_online;
    }

    @Override
    protected void initView() {
        idx = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        mToolbar.setTitle(title);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initHeaderView();
    }


    @Override
    protected void initData() {
        mPresenter = new OnlineMusicListPresenter(this);
        mPresenter.attachView(this);

        mAdapter = new NeteaseAdapter(toplist);
        mAdapter.setEnableLoadMore(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        showHeaderInfo();

        mPresenter.loadNeteaseMusicList(idx);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                LogUtil.e(TAG, toplist.get(position).toString());
                NeteaseApiServiceImpl.getMusicUrl(toplist.get(position))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Music>() {

                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Music music) {
                                if (music.getUri() != null) {
                                    PlayManager.playOnline(music);
                                } else {
                                    ToastUtils.show(music.toString());
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            NeteaseMusic music = toplist.get(position);
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_detail:
                        StringBuilder sb = new StringBuilder();
                        sb.append("艺术家：\n")
                                .append(music.getArtists().get(0).getName())
                                .append("\n")
                                .append("专辑：\n")
                                .append(music.getAlbum())
                                .append("\n")
                                .append("播放时长：\n")
                                .append(FormatUtil.formatTime(music.getDuration()))
                                .append("\n")
                                .append("文件路径：\n")
                                .append(music.getMp3Url())
                                .append(music.getStatus());

                        new MaterialDialog.Builder(NeteaseMusicListActivity.this)
                                .title("歌曲详情")
                                .content(sb.toString())
                                .positiveText("确定")
                                .build().show();
                        break;
                    case R.id.popup_song_goto_artist:
                        Log.e(TAG, music.toString());
                        break;
                    case R.id.popup_song_download:
                        break;
                }
                return false;
            });
            popupMenu.inflate(R.menu.popup_song_online);
            popupMenu.show();
        });
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

        mTvDate.setVisibility(View.GONE);
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
        mTvDate.setText(getString(R.string.recent_update, FormatUtil.distime(time)));
        mTvDesc.setText(desc);
        GlideApp.with(this)
                .load(pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvCover);
        mAdapter.setHeaderView(mViewHeader, 0);
    }

    @Override
    public void showOnlineMusicList(List<Music> musicList) {

    }

    @Override
    public void showTopList(NeteaseList musicList) {
        desc = musicList.getDescription();
        pic = musicList.getCoverImgUrl();
        time = musicList.getUpdateTime();
        showHeaderInfo();
        toplist = musicList.getTracks();
        mAdapter.setNewData(musicList.getTracks());
    }
}
