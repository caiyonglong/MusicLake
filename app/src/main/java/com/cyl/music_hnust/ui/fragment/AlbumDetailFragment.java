package com.cyl.music_hnust.ui.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.SingerCallback;
import com.cyl.music_hnust.dataloaders.MusicLoader;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.Singer;
import com.cyl.music_hnust.ui.adapter.AlbumMusicAdapter;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 专辑
 */
public class AlbumDetailFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @Bind(R.id.album_art)
    ImageView album_art;


    long albumID;
    boolean isAlbum;
    String transitionName;
    String title;

    private AlbumMusicAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();

    Runnable loadSongs = new Runnable() {
        @Override
        public void run() {
            new loadPlaylist().execute("");
        }
    };

    public static AlbumDetailFragment newInstance(long id, boolean isAlbum, String title, String transitionName) {

        Bundle args = new Bundle();
        args.putLong(Extras.ALBUM_ID, id);
        args.putString(Extras.PLAYLIST_NAME, title);
        args.putBoolean("isAlbum", isAlbum);
        args.putString("transitionName", transitionName);
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initDatas() {
        albumID = getArguments().getLong(Extras.ALBUM_ID);
        isAlbum = getArguments().getBoolean("isAlbum");
        transitionName = getArguments().getString("transitionName");
        title = getArguments().getString(Extras.PLAYLIST_NAME);

        if (transitionName != null)
            album_art.setTransitionName(transitionName);
        if (title != null)
            collapsing_toolbar.setTitle(title);
        setAlbumart();
        loadSongs.run();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_album;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initViews() {

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    /**
     * 显示专辑图片
     */
    private void setAlbumart() {
        Log.e("====", albumID + "==" + title);
        if (isAlbum) {
            loadBitmap(ImageUtils.getAlbumArtUri(albumID).toString());
        } else {
//            loadArtist(title);
        }
    }

    private void loadArtist(String title) {
        OkHttpUtils.get().url("http://apis.baidu.com/geekery/music/singer")
                .addHeader("apikey", "0bbd28df93933b00fdbbd755f8769f1b")
                .addParams("name", title)
                .build()
                .execute(new SingerCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Singer response) {
                        if (response.getCode() == 0)
                            loadBitmap(response.getData().getImage());
                    }
                });
    }

    private void loadBitmap(String uri) {
        Log.e("EEEE", uri);
        ImageLoader.getInstance().displayImage(uri, album_art,
                new DisplayImageOptions.Builder().cacheInMemory(true)
                        .showImageOnFail(R.drawable.default_cover)
                        .resetViewBeforeLoading(true)
                        .build());
    }

    private class loadPlaylist extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (isAlbum) {
                Log.e("专辑id++++++", albumID + "==" + title + "");
                musicInfos = MusicLoader.getAlbumSongs(getContext(), albumID + "");
                Log.e("歌单id++++++", musicInfos.size() + "");
            } else {
                Log.e("歌单id++++++", albumID + "");
                musicInfos = MusicLoader.getArtistSongs(getContext(), albumID + "");
                Log.e("歌单id++++++", musicInfos.size() + "");
            }
            mAdapter = new AlbumMusicAdapter((AppCompatActivity) getActivity(), musicInfos);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            setRecyclerViewAapter();
        }

        @Override
        protected void onPreExecute() {
        }
    }

    private void setRecyclerViewAapter() {
        mRecyclerView.setAdapter(mAdapter);
    }

}
