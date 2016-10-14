package com.cyl.music_hnust.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.music_hnust.Json.JsonCallback;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.OnlineMusicAdapter;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.model.OnlineMusicInfo;
import com.cyl.music_hnust.model.OnlineMusicList;
import com.cyl.music_hnust.service.PlayOnlineMusic;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.SizeUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineMusicActivity extends BaseActivity implements OnlineMusicAdapter.OnItemClickListener {

    private OnlineMusicList mMusicList;

    private List<OnlineMusicInfo> mMusicLists = new ArrayList<>();
    private List<Music> mMusicls = new ArrayList<>();
    private OnlineMusicAdapter mAdapter;

    @Bind(R.id.main_content)
    LinearLayout main_content;
    @Bind(R.id.xrecyclerview)
    XRecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private View vHeader;
    private PlayService mPlayService;
    private ProgressDialog mProgressDialog;
    private int mOffset = 0;
    private String title;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

//初始化黄油刀控件绑定框架
        ButterKnife.bind(this);

        title = getIntent().getStringExtra(Extras.BILLBOARD_TITLE);
        type = getIntent().getStringExtra(Extras.BILLBOARD_TYPE);

        setTitle(title);
        init();
    }

    private void init() {

        bindService();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));

        vHeader = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(this,150));
        vHeader.setLayoutParams(params);
        mAdapter = new OnlineMusicAdapter(this, mMusicLists);
        mAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addHeaderView(vHeader);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                getMusic(mOffset);
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                getMusic(mOffset);
                mRecyclerView.loadMoreComplete();
            }
        });

    }



    @Override
    protected void listener() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.MyBinder) service).getService();
            onLoad();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void onLoad() {
        getMusic(mOffset);
    }
    private void getMusic(final int offset) {
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST)
                .addParams(Constants.PARAM_TYPE, type)
                .addParams(Constants.PARAM_SIZE, String.valueOf(Constants.MUSIC_LIST_SIZE))
                .addParams(Constants.PARAM_OFFSET, String.valueOf(offset))
                .build()
                .execute(new JsonCallback<OnlineMusicList>(OnlineMusicList.class) {
                    @Override
                    public void onResponse(OnlineMusicList response) {
                        mMusicList = response;
                        if (offset == 0 && response == null) {
                            return;
                        } else if (offset == 0) {
                            initHeader();
                        }
                        if (response == null || response.getSong_list() == null || response.getSong_list().size() == 0) {
                            mRecyclerView.loadMoreComplete();
                            return;
                        }
                        mOffset += Constants.MUSIC_LIST_SIZE;
                        conver(response.getSong_list());

                        Log.e("ddd11111111111",mMusicls.size()+""+"=====");
                        mMusicLists.addAll(response.getSong_list());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mRecyclerView.loadMoreComplete();
                        if (e instanceof RuntimeException) {
                            // 歌曲全部加载完成
                            mRecyclerView.setLoadingMoreEnabled(false);
                            return;
                        }
                        if (offset == 0) {
                        } else {
//                            ToastUtils.show(R.string.load_fail);
                        }
                    }
                });
    }

    private void play(OnlineMusicInfo musicInfo) {
        new PlayOnlineMusic(this, musicInfo) {

            @Override
            public void onPrepare() {
                mProgressDialog.show();
            }

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onSuccess(Music music) {
                mProgressDialog.cancel();
                Log.e("***********",music.toString());
                mPlayService.playMusic(music);
//                ToastUtils.show(getApplicationContext(),getString(R.string.now_play, music.getTitle()));
            }

            @Override
            public void onFail(Call call, Exception e) {
                mProgressDialog.cancel();
                ToastUtils.show(getApplicationContext(),R.string.unable_to_play);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        unbindService(mPlayServiceConnection);
        super.onDestroy();
    }

    private void conver(List<OnlineMusicInfo> mMusicLists){

        for (int i=0;i<mMusicLists.size();i++) {
            new PlayOnlineMusic(this, mMusicLists.get(i)) {

                @Override
                public void onPrepare() {
                    mProgressDialog.show();
                }

                @SuppressLint("StringFormatInvalid")
                @Override
                public void onSuccess(Music music) {
                    mProgressDialog.cancel();
//                    Log.e("***********", music.toString());
                    mMusicls.add(music);
                    Log.e("mMusicls",mMusicls.size()+""+"=====");
                }
                @Override
                public void onFail(Call call, Exception e) {
                    mProgressDialog.cancel();
                    ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
                }
            }.execute();
        }
    }

    private void initHeader() {
        final ImageView ivHeaderBg = (ImageView) vHeader.findViewById(R.id.iv_header_bg);
        final ImageView ivCover = (ImageView) vHeader.findViewById(R.id.iv_cover);
        TextView tvTitle = (TextView) vHeader.findViewById(R.id.tv_title);
        TextView tvUpdateDate = (TextView) vHeader.findViewById(R.id.tv_update_date);
        TextView tvComment = (TextView) vHeader.findViewById(R.id.tv_comment);
        tvTitle.setText(mMusicList.getBillboard().getName());
        tvUpdateDate.setText(getString(R.string.recent_update, mMusicList.getBillboard().getUpdate_date()));
        tvComment.setText(mMusicList.getBillboard().getComment());
        ImageSize imageSize = new ImageSize(200, 200);
        ImageLoader.getInstance().loadImage(mMusicList.getBillboard().getPic_s640(), imageSize,
                ImageUtils.getCoverDisplayOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if(loadedImage!=null) {
                            ivCover.setImageBitmap(loadedImage);
                            ivHeaderBg.setImageBitmap(ImageUtils.blur(loadedImage, ImageUtils.BLUR_RADIUS));
                        }
                    }
                });
    }
    @Override
    public void onItemClick(View view, int position) {
//        play(mMusicLists.get(position));
        mPlayService.setMyMusicList(mMusicls);
        mPlayService.playMusic(mMusicls.get(position));
    }
}
