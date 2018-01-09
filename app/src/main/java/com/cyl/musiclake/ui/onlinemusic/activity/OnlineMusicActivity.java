package com.cyl.musiclake.ui.onlinemusic.activity;

import android.os.AsyncTask;
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
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.download.download.DownloadInfo;
import com.cyl.musiclake.ui.localmusic.adapter.OnlineMusicAdapter;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineMusicInfo;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineMusicList;
import com.cyl.musiclake.utils.Constants;
import com.cyl.musiclake.utils.Extras;
import com.cyl.musiclake.utils.SizeUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineMusicActivity extends BaseActivity implements OnlineMusicAdapter.OnItemClickListener {

    private OnlineMusicList mMusicList;
    private static final String TAG = "OnlineMusicActivity";

    private List<OnlineMusicInfo> mMusicLists = new ArrayList<>();
    private List<Music> mMusicls = new ArrayList<>();
    private OnlineMusicAdapter mAdapter;

    @BindView(R.id.xrecyclerview)
    XRecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private View vHeader;
    private MaterialDialog mProgressDialog;
    private int mOffset = 0;
    private String title;
    private String type;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_online;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra(Extras.BILLBOARD_TITLE);
        type = getIntent().getStringExtra(Extras.BILLBOARD_TYPE);
        mToolbar.setTitle(title);
        init();
        getMusic(mOffset);
    }

    private void init() {

        mProgressDialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .build();


        vHeader = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(this, 150));
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
                mOffset = 0;
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


    private void onLoad() {
        getMusic(mOffset);
    }

    private void getMusic(final int offset) {
        new TTask().execute();
//        OkHttpUtils.get().url(Constants.BASE_URL)
//                .addParams(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST)
//                .addParams(Constants.PARAM_TYPE, type)
//                .addParams(Constants.PARAM_SIZE, String.valueOf(Constants.MUSIC_LIST_SIZE))
//                .addParams(Constants.PARAM_OFFSET, String.valueOf(offset))
//                .build()
//                .execute(new JsonCallback<OnlineMusicList>(OnlineMusicList.class) {
//                    @Override
//                    public void onResponse(OnlineMusicList response) {
//                        Log.e("ddd11111111111", response.toString() + "" + "=====");
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        mRecyclerView.loadMoreComplete();
//                        if (e instanceof RuntimeException) {
//                            // 歌曲全部加载完成
//                            mRecyclerView.setLoadingMoreEnabled(false);
//                            return;
//                        }
//                        if (offset == 0) {
//                        } else {
////                            ToastUtils.show(R.string.load_fail);
//                        }
//                    }
//                });
    }

    private void play(OnlineMusicInfo musicInfo) {
//        new PlayOnlineMusic(this, musicInfo) {
//
//            @Override
//            public void onPrepare() {
//                mProgressDialog.show();
//            }
//
//            @SuppressLint("StringFormatInvalid")
//            @Override
//            public void onSuccess(Music music) {
//                mProgressDialog.cancel();
//                Log.e("***********", music.toString());
//                PlayManager.playOnline(music);
////                ToastUtils.show(getApplicationContext(),getString(R.string.now_play, music.getTitle()));
//            }
//
//            @Override
//            public void onFail(Call call, Exception e) {
//                mProgressDialog.cancel();
//                ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
//            }
//        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void conver(List<OnlineMusicInfo> mMusicLists) {

        for (int i = 0; i < mMusicLists.size(); i++) {
//            new PlayOnlineMusic(this, mMusicLists.get(i)) {
//
//                @Override
//                public void onPrepare() {
//                    mProgressDialog.show();
//                }
//
//                @SuppressLint("StringFormatInvalid")
//                @Override
//                public void onSuccess(Music music) {
//                    mProgressDialog.cancel();
////                    Log.e("***********", music.toString());
//                    mMusicls.add(music);
//                    Log.e("mMusicls", mMusicls.size() + "" + "=====");
//                }
//
//                @Override
//                public void onFail(Call call, Exception e) {
//                    mProgressDialog.cancel();
//                    ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
//                }
//            }.execute();
        }
    }

    private void initHeader() {
        final ImageView ivHeaderBg = (ImageView) vHeader.findViewById(R.id.iv_header_bg);
        final ImageView ivCover = (ImageView) vHeader.findViewById(R.id.iv_cover);
        TextView tvTitle = (TextView) vHeader.findViewById(R.id.tv_title);
        TextView tvUpdateDate = (TextView) vHeader.findViewById(R.id.tv_update_date);
        TextView tvComment = (TextView) vHeader.findViewById(R.id.tv_comment);
        tvTitle.setText(mMusicList.getBillboard().getName());
        if (mMusicList.getBillboard().getUpdate_date() == null)
            mMusicList.getBillboard().setUpdate_date("暂无记录");
        tvUpdateDate.setText(getString(R.string.recent_update, mMusicList.getBillboard().getUpdate_date()));
        tvComment.setText(mMusicList.getBillboard().getComment());
//        ImageSize imageSize = new ImageSize(200, 200);
//        ImageLoader.getInstance().loadImage(mMusicList.getBillboard().getPic_s640(), imageSize,
//                ImageUtils.getCoverDisplayOptions(), new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        if (loadedImage != null) {
//                            ivCover.setImageBitmap(loadedImage);
//                            ivHeaderBg.setImageBitmap(ImageUtils.blur(loadedImage, ImageUtils.BLUR_RADIUS));
//                        }
//                    }
//                });
    }

    @Override
    public void onItemClick(View view, int position) {
//        play(mMusicLists.get(position));
        new PlayOnlineTask().execute(mMusicLists.get(position).getSong_id());
//        mMusicPlayService.setMyMusicList(mMusicls);
//        PlayManager.playOnline(mMusicls.get(position));
    }


    private class PlayOnlineTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String baseUrl = Constants.BASE_URL + "?" +
                    Constants.PARAM_METHOD + "=" + Constants.METHOD_DOWNLOAD_MUSIC + "&" +
                    Constants.PARAM_SONG_ID + "=" + params[0];
            Log.e(TAG, "请求吗 :" + baseUrl);
            try {
                URL url = new URL(baseUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                Log.e(TAG, "请求吗 :" + code);
                if (code == 200) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream is = conn.getInputStream(); // 字节流转换成字符串
                    int by = 0;
                    byte[] buffer = new byte[1024];
                    while ((by = is.read(buffer)) > 0) {
                        out.write(buffer, 0, by);
                    }
                    out.close();
                    String json = new String(out.toByteArray());
                    Log.e(TAG, "请求吗 :" + json);
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.cancel();
            if (s != null) {
                Gson gson = new Gson();
                DownloadInfo response = gson.fromJson(s, DownloadInfo.class);
                DownloadInfo.SongInfo songInfo = response.getSonginfo();
                DownloadInfo.JBitrate jBitrate = response.getBitrate();

                Music music = new Music();
                music.setType(Music.Type.ONLINE);
                music.setAlbum(songInfo.getAlbum_title());
                music.setArtist(songInfo.getAuthor());
                music.setTitle(songInfo.getTitle());
                music.setLrcPath(songInfo.getLrclink());
                music.setCoverUri(songInfo.getPic_big().split("@")[0]);
                music.setUri(jBitrate.getFile_link());
                Log.e(TAG, "post :" + music.toString());
                PlayManager.playOnline(music);

            }
        }
    }


    private class TTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String baseUrl = Constants.BASE_URL + "?" +
                    Constants.PARAM_METHOD + "=" + Constants.METHOD_GET_MUSIC_LIST + "&" +
                    Constants.PARAM_TYPE + "=" + type + "&" +
                    Constants.PARAM_SIZE + "=10" + "&" +
                    Constants.PARAM_OFFSET + "=" + mOffset;

            Log.e(TAG, "请求吗 :" + baseUrl);
            try {
                URL url = new URL(baseUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                Log.e(TAG, "请求吗 :" + code);
                if (code == 200) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream is = conn.getInputStream(); // 字节流转换成字符串
                    int by = 0;
                    byte[] buffer = new byte[1024];
                    while ((by = is.read(buffer)) > 0) {
                        out.write(buffer, 0, by);
                    }
                    out.close();
                    String json = new String(out.toByteArray());
                    Log.e(TAG, "请求吗 :" + json);
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Gson gson = new Gson();
                OnlineMusicList response = gson.fromJson(s, OnlineMusicList.class);

                mMusicList = response;
                if (mOffset == 0 && response == null) {
                    return;
                } else if (mOffset == 0) {
                    initHeader();
                }
                if (response == null || response.getSong_list() == null || response.getSong_list().size() == 0) {
                    mRecyclerView.loadMoreComplete();
                    return;
                }
                mOffset += Constants.MUSIC_LIST_SIZE;

                Log.e("ddd11111111111", response.getSong_list().size() + "" + "=====");
                mMusicLists.addAll(response.getSong_list());
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
