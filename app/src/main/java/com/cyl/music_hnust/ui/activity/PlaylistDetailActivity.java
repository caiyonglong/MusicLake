package com.cyl.music_hnust.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.dataloaders.MusicLoader;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.ui.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.SystemUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：yonglong on 2016/8/14 16:15
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.album_art)
    ImageView album_art;
    @Bind(R.id.foreground)
    View foreground;

    private LocalMusicAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();
    private String pid;
    private long album_id;
    //0代表专辑，1代表艺术家
    private int isAlbum;

    Runnable loadSongs = new Runnable() {
        @Override
        public void run() {
            new loadPlaylist().execute("");
        }
    };

    /**
     * 设置监听事件
     */
    @Override
    protected void listener() {


    }

    @Override
    protected int getLayoutResID() {
        return R.layout.playlist_detail;
    }

    private void setAlbumart() {

        Log.e("====", getIntent().getExtras().getString(Extras.PLAYLIST_NAME) + "==\n" + ImageUtils.getAlbumArtUri(getIntent().getExtras().getLong(Extras.ALBUM_ID)).toString());
        mToolbar.setTitle(getIntent().getExtras().getString(Extras.PLAYLIST_NAME));
        if (isAlbum == -1) {
            Log.e("====", getIntent().getExtras().getString(Extras.PLAYLIST_FOREGROUND_COLOR) + "==\n"
            );
            foreground.setBackgroundColor(getIntent().getExtras().getInt(Extras.PLAYLIST_FOREGROUND_COLOR));
            album_art.setBackgroundResource(getIntent().getExtras().getInt(Extras.PLAYLIST_BACKGROUND_IMAGE));
        } else {
            loadBitmap(ImageUtils.getAlbumArtUri(album_id).toString());
        }
    }

    private void loadBitmap(String uri) {
        Log.e("EEEE", uri);
        ImageLoader.getInstance().displayImage(uri, album_art,
                new DisplayImageOptions.Builder().cacheInMemory(true)
                        .showImageOnFail(R.drawable.default_cover)
                        .resetViewBeforeLoading(true)
                        .build());
    }

    @Override
    protected void initData() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pid = getIntent().getStringExtra(Extras.PLAYLIST_ID);
        isAlbum = getIntent().getIntExtra(Extras.ALBUM, -1);
        album_id = getIntent().getLongExtra(Extras.ALBUM_ID, -1);
        Log.e("pid", pid + "===" + isAlbum + "+++" + album_id + "00");
        setAlbumart();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);

        if (SystemUtils.isLollipop()) {
            getWindow().getEnterTransition().addListener(new EnterTransition());
        } else {
            loadSongs.run();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                final Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.action_delete_playlist:
                PlaylistLoader.deletePlaylist(this, pid);
                finish();
                break;
            case R.id.action_settings:
                final Intent intent2 = new Intent(this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_share:
                Intent intent3 = new Intent(PlaylistDetailActivity.this, EditActivity.class);
                String content = "";
                if (musicInfos.size() > 0) {
                    content = "分享歌单\n";
                }
                for (int i = 0; i < musicInfos.size(); i++) {
                    content += musicInfos.get(i).getTitle() + "---" + musicInfos.get(i).getArtist();
                    content += "\n";
                }
                intent3.putExtra("content", content);
                startActivity(intent3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAlbum != -1) {
            getMenuInflater().inflate(R.menu.menu_playlist, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_playlist_detail, menu);
        }
        return super.onCreateOptionsMenu(menu);

    }

    private class loadPlaylist extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (isAlbum == 0) {
                Log.e("歌单id++++++", album_id + "==" + getIntent().getExtras().getString(Extras.PLAYLIST_NAME) + "");
                musicInfos = MusicLoader.getAlbumSongs(PlaylistDetailActivity.this, album_id + "");
                Log.e("歌单id++++++", musicInfos.size() + "");
            } else if (isAlbum == 1) {
                Log.e("歌单id++++++", album_id + "");
                musicInfos = MusicLoader.getArtistSongs(PlaylistDetailActivity.this, album_id + "");
                Log.e("歌单id++++++", musicInfos.size() + "");
            } else {
                Log.e("歌单id++++++", pid + "");
                musicInfos = PlaylistLoader.getMusicForPlaylist(PlaylistDetailActivity.this, pid);
                Log.e("歌单id++++++", musicInfos.size() + "");
            }
            mAdapter = new LocalMusicAdapter(PlaylistDetailActivity.this, musicInfos);
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
//        if (SystemUtils.isLollipop()) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST, R.drawable.item_divider_white));
//                }
//            }, 250);
//        } else
//            mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST, R.drawable.item_divider_white));

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private class EnterTransition implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
            loadSongs.run();
        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }
}
