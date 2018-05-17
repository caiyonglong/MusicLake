package com.cyl.musiclake.ui.music.list.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.data.PlaylistLoader;
import com.cyl.musiclake.ui.music.list.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.search.SearchActivity;
import com.cyl.musiclake.ui.zone.EditActivity;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.ImageUtils;
import com.cyl.musiclake.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/14 16:15
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.album_art)
    ImageView album_art;
    @BindView(R.id.foreground)
    View foreground;

    private SongAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();
    private String pid;
    private long album_id;
    //0代表专辑，1代表艺术家
    private int isAlbum;

    /**
     * 设置监听事件
     */
    @Override
    protected void listener() {


    }

    @Override
    protected int getLayoutResID() {
        return R.layout.frag_playlist_detail;
    }

    private void setAlbumart() {
        setToolbarTitle(getIntent().getExtras().getString(Extras.PLAYLIST_NAME));
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
        CoverLoader.loadImageView(this, uri, album_art);
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
        if (SystemUtils.isLollipop()) {
            getWindow().getEnterTransition().addListener(new EnterTransition());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                final Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.action_delete_playlist:
                PlaylistLoader.deletePlaylist(this, pid);
                finish();
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private class EnterTransition implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
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
