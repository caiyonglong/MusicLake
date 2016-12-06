//package com.cyl.music_hnust.fragment;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.cyl.music_hnust.R;
//import com.cyl.music_hnust.activity.PlaylistDetailActivity;
//import com.cyl.music_hnust.adapter.LocalMusicAdapter;
//import com.cyl.music_hnust.dataloaders.MusicLoader;
//import com.cyl.music_hnust.dataloaders.PlaylistLoader;
//import com.cyl.music_hnust.fragment.base.BaseFragment;
//import com.cyl.music_hnust.model.music.Music;
//import com.cyl.music_hnust.utils.Extras;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//
///**
// * 作者：yonglong on 2016/8/15 19:54
// * 邮箱：643872807@qq.com
// * 版本：2.5
// */
//public class AlbumDetailFragment extends BaseFragment{
//
//    @Bind(R.id.recyclerView)
//    RecyclerView mRecyclerView;
//    @Bind(R.id.toolbar)
//    Toolbar mToolbar;
//    @Bind(R.id.album_art)
//    ImageView album_art;
//
//    public static AlbumDetailFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        AlbumDetailFragment fragment = new AlbumDetailFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected void listener() {
//
//    }
//
//    @Override
//    protected void initDatas() {
//
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.frag_album;
//    }
//
//    @Override
//    public void initViews() {
//
//    }
//    Runnable loadSongs = new Runnable() {
//        @Override
//        public void run() {
//            new PlaylistDetailActivity.loadPlaylist().execute("");
//        }
//    };
//    private class loadPlaylist extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            if (isAlbum == 0) {
//                Log.e("歌单id++++++", album_id + "==" + getIntent().getExtras().getString(Extras.PLAYLIST_NAME) + "");
//                musicInfos = MusicLoader.getAlbumSongs(PlaylistDetailActivity.this, album_id + "");
//                Log.e("歌单id++++++", musicInfos.size() + "");
//            } else if (isAlbum == 1) {
//                Log.e("歌单id++++++", album_id + "");
//                musicInfos = MusicLoader.getArtistSongs(PlaylistDetailActivity.this, album_id + "");
//                Log.e("歌单id++++++", musicInfos.size() + "");
//            } else {
//                Log.e("歌单id++++++", playlist_id + "");
//                musicInfos = PlaylistLoader.getMusicForPlaylist(PlaylistDetailActivity.this, playlist_id);
//                Log.e("歌单id++++++", musicInfos.size() + "");
//            }
//            mAdapter = new LocalMusicAdapter(PlaylistDetailActivity.this, musicInfos);
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            setRecyclerViewAapter();
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//    }
//
//    private void setRecyclerViewAapter() {
//        mRecyclerView.setAdapter(mAdapter);
//
//    }
//
//}
