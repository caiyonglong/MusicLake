package com.cyl.musiclake.ui.music.playlist.edit;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.bean.data.PlaylistLoader;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/14 16:15
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistManagerActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mPlaylistRcv;

    private PlaylistEditAdapter mAdapter;
    private List<Playlist> playlists = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_playlist_edit;
    }

    @Override
    protected String setToolbarTitle() {
        return getString(R.string.playlist_manager);
    }

    @Override
    protected void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(false);

        mPlaylistRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlaylistRcv.setNestedScrollingEnabled(false);

        mAdapter = new PlaylistEditAdapter(playlists);
        mPlaylistRcv.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mPlaylistRcv);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mPlaylistRcv);

        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper);
        mAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mToolbar != null)
            mToolbar.setTitle(R.string.playlist_manager);
    }

    @Override
    protected void initData() {
        String type = getIntent().getStringExtra(Extras.PLAYLIST_TYPE);
        if (type.equals(Constants.PLAYLIST_CUSTOM_ID)) {
            playlists = PlaylistManagerUtils.INSTANCE.getPlaylists();
        } else {
            playlists = PlaylistLoader.INSTANCE.getAllPlaylist();
        }
        mAdapter.setNewData(playlists);
    }

    @Override
    protected void initInjector() {

    }

    public void delete(View view) {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content("是否删除歌单？")
                .onPositive((dialog, which) -> {
                    for (String key : mAdapter.getCheckedMap().keySet()) {
                        PlaylistManagerUtils.INSTANCE.deletePlaylist(mAdapter.getCheckedMap().get(key), s -> {
                            playlists.remove(mAdapter.getCheckedMap().get(key));
                            mAdapter.setNewData(playlists);
                            return null;
                        });
                    }
                    mAdapter.getCheckedMap().clear();
                    finish();
                })
                .positiveText("确定")
                .negativeText("取消")
                .show();
    }
}
