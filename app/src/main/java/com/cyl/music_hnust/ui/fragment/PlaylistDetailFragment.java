package com.cyl.music_hnust.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.ui.activity.EditActivity;
import com.cyl.music_hnust.ui.activity.SearchActivity;
import com.cyl.music_hnust.ui.activity.SettingsActivity;
import com.cyl.music_hnust.ui.adapter.SongAdapter;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.utils.Extras;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.album_art)
    ImageView album_art;
    @Bind(R.id.foreground)
    View foreground;

    private SongAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();
    private String mId;
    private String title;


    public static PlaylistDetailFragment newInstance(String id, String title) {

        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putString(Extras.PLAYLIST_ID, id);
        args.putString(Extras.PLAYLIST_NAME, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.playlist_detail;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle("歌单列表");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mId = getArguments().getString(Extras.PLAYLIST_ID);
        title = getArguments().getString(Extras.PLAYLIST_NAME);
        mToolbar.setTitle(title);
        loadMusic.run();
    }

    Runnable loadMusic = new Runnable() {
        @Override
        public void run() {
            new loadPlaylist().execute("");
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_search:
                final Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.action_delete_playlist:
                PlaylistLoader.deletePlaylist(getActivity(), mId);
                getActivity().onBackPressed();
                break;
            case R.id.action_settings:
                final Intent intent2 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_share:
                Intent intent3 = new Intent(getActivity(), EditActivity.class);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_playlist_detail, menu);
    }


    private class loadPlaylist extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            musicInfos = PlaylistLoader.getMusicForPlaylist(getActivity(), mId);
            Log.e("歌单id++++++", musicInfos.size() + "");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            updateView();
        }

        @Override
        protected void onPreExecute() {
        }
    }

    private void updateView() {
        mAdapter = new SongAdapter((AppCompatActivity) getActivity(), musicInfos);

    }


}
