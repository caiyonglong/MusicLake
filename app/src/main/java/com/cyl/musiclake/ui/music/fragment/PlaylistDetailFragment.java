package com.cyl.musiclake.ui.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.music.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.model.data.PlaylistLoader;
import com.cyl.musiclake.ui.zone.EditActivity;
import com.cyl.musiclake.utils.Extras;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.album_art)
    ImageView album_art;

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
        setHasOptionsMenu(true);
    }

    @Override
    protected void initDatas() {
        mId = getArguments().getString(Extras.PLAYLIST_ID);
        title = getArguments().getString(Extras.PLAYLIST_NAME);

        mToolbar.setTitle(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        musicInfos = PlaylistLoader.getMusicForPlaylist(getActivity(), mId);
        mAdapter = new SongAdapter((AppCompatActivity) getActivity(), musicInfos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("是否删除这个歌单？")
                        .onPositive((dialog, which) -> {
                            PlaylistLoader.deletePlaylist(getActivity(), mId);
                            RxBus.getInstance().post("update");
                            onBackPress();
                        })
                        .positiveText("确定")
                        .negativeText("取消")
                        .show();
                break;
            case R.id.action_share:
                Intent intent3 = new Intent(getActivity(), EditActivity.class);
                StringBuilder content = new StringBuilder();
                if (musicInfos.size() > 0) {
                    content = new StringBuilder("分享歌单\n");
                }
                for (int i = 0; i < musicInfos.size(); i++) {
                    content.append(musicInfos.get(i).getTitle()).append("---").append(musicInfos.get(i).getArtist());
                    content.append("\n");
                }
                intent3.putExtra("content", content.toString());
                startActivity(intent3);
                break;
        }
        return true;
    }

    private void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_playlist_detail, menu);
    }


}
