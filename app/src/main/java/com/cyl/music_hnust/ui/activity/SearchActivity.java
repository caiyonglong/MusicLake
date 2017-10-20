package com.cyl.music_hnust.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.JsonCallback;
import com.cyl.music_hnust.download.DownloadService;
import com.cyl.music_hnust.bean.music.Music;
import com.cyl.music_hnust.bean.music.OnlineMusicInfo;
import com.cyl.music_hnust.bean.music.SearchMusic;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.service.PlayOnlineMusic;
import com.cyl.music_hnust.service.PlaySearchedMusic;
import com.cyl.music_hnust.ui.adapter.SearchAdapter;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchAdapter.OnItemClickListener {

    //搜索信息
    private String queryString;

    private SearchAdapter mAdapter;
    private MusicPlayService mMusicPlayService;
    private ProgressDialog mProgressDialog;
    private int mOffset = 10;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.xrecyclerview)
    XRecyclerView mRecyclerView;

    private List<SearchMusic.SongList> searchResults = new ArrayList<>();

    @Override
    protected void listener() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.acitvity_search;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

        mAdapter = new SearchAdapter(this, searchResults);
        mAdapter.setOnItemClickListener(this);

        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                mOffset = 10;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getString(R.string.search_tips));
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        try {
            Field field = searchView.getClass().getDeclaredField("mGoButton");
            field.setAccessible(true);
            ImageView mGoButton = (ImageView) field.get(searchView);
            mGoButton.setImageResource(R.drawable.ic_search_white_18dp);
            mGoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queryString = searchView.getQuery().toString().toString();
                    mProgressDialog.show();
                    if (queryString.length() > 0) {
                        mOffset = 10;
                        searchResults.clear();
                        getMusic(mOffset);
                    } else {
                        mProgressDialog.cancel();
                        ToastUtils.show(getApplicationContext(), "不能搜索空文本");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMusic(final int offset) {
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_SEARCH_MUSIC)
                .addParams(Constants.PARAM_QUERY, queryString)
                .addParams(Constants.PARAM_OFFSET, String.valueOf(offset))
                .build()
                .execute(new JsonCallback<SearchMusic>(SearchMusic.class) {
                    @Override
                    public void onResponse(SearchMusic response) {
                        if (offset == 0 && response == null) {
                            return;
                        } else if (offset == 0) {
//                            initHeader();
                        }
                        if (response == null || response.getSong_list() == null || response.getSong_list().size() == 0) {
                            mRecyclerView.loadMoreComplete();
                            return;
                        }
                        mOffset += Constants.MUSIC_LIST_SIZE;

                        searchResults.addAll(response.getSong_list());
                        mAdapter.notifyDataSetChanged();
                        mProgressDialog.cancel();
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
                        mProgressDialog.cancel();
                    }
                });
    }




    private void play(SearchMusic.SongList songList) {
        new PlaySearchedMusic(this, songList) {
            @Override
            public void onPrepare() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(Music music) {
                mProgressDialog.cancel();
                Log.e("***********", music.toString());
                mMusicPlayService.playMusic(music);
//                ToastUtils.show(getApplicationContext(),getString(R.string.now_play, music.getTitle()));
            }

            @Override
            public void onFail(Call call, Exception e) {
                mProgressDialog.cancel();
                ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {

        switch (view.getId()) {
            case R.id.iv_more:
                setOnPopupMenuListener(view, position);
                break;
            default:
                play(searchResults.get(position));
                break;
        }
    }

    private void setOnPopupMenuListener(View view, final int position) {
        PopupMenu mPopupmenu = new PopupMenu(this, view);
        mPopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        play(searchResults.get(position));
                        break;
                    case R.id.popup_song_detail:
                        getMusicInfo(searchResults.get(position));
                        break;
                    case R.id.popup_song_goto_artist:
                        Intent intent = new Intent(SearchActivity.this, ArtistInfoActivity.class);
                        intent.putExtra(Extras.TING_UID, searchResults.get(position).getArtist_id());
                        startActivity(intent);
                        break;
                    case R.id.popup_song_download:
                        SearchMusic.SongList search = searchResults.get(position);

                        OnlineMusicInfo onlineMusicInfo = new OnlineMusicInfo();
                        onlineMusicInfo.setAlbum_title(search.getAlbum_title());
                        onlineMusicInfo.setSong_id(search.getSong_id());
                        onlineMusicInfo.setTitle(search.getTitle());
                        onlineMusicInfo.setArtist_name(search.getAuthor());
                        onlineMusicInfo.setLrclink(search.getLrclink());
                        conver(onlineMusicInfo);
                        break;

                }
                return false;
            }
        });
        mPopupmenu.inflate(R.menu.popup_song_online);
        mPopupmenu.show();
    }

    private void getMusicInfo(SearchMusic.SongList music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("歌曲信息");
        StringBuilder sb = new StringBuilder();
        sb.append("歌曲名：")
                .append(FileUtils.getTitle(music.getTitle()))
                .append("\n\n")
                .append("歌手：")
                .append(FileUtils.getArtist(music.getAuthor()))
                .append("\n\n")
                .append("专辑：")
                .append(music.getAlbum_title())
                .append("\n\n")
                .append("歌曲Id：")
                .append(music.getSong_id())
                .append("\n\n")
                .append("专辑Id：")
                .append(music.getArtist_id())
                .append("\n\n")
                .append("歌词路径：")
                .append(music.getLrclink());
        dialog.setMessage(sb.toString());
        dialog.show();
    }

    private void conver(OnlineMusicInfo onlineMusicInfo) {

        new PlayOnlineMusic(this, onlineMusicInfo) {
            @Override
            public void onPrepare() {

            }

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onSuccess(Music music) {
                if (music.getUri()==null&&music.getUri().length()<=0){
                    ToastUtils.show(SearchActivity.this,"歌曲下载地址错误");
                    return;
                }
                Intent intent = new Intent(SearchActivity.this, DownloadService.class);
                intent.putExtra("downloadUrl", music.getUri());
                intent.putExtra("name", FileUtils.getMp3FileName(music.getArtist(),music.getTitle()));
                intent.putExtra("flag", "startDownload");
                startService(intent);
            }

            @Override
            public void onFail(Call call, Exception e) {
                ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
            }
        }.execute();
    }

}
