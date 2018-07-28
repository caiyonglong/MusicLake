package com.cyl.musiclake.ui.music.playlist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.data.db.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.ui.OnlinePlaylistUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * 作者：yonglong on 2016/8/14 16:15
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistManagerActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mPlaylistRcv;

    private PlaylistEditAdapter mAdapter;
    private List<Playlist> playlists = OnlinePlaylistUtils.INSTANCE.getPlaylists();

    public Map<String, Playlist> checkedMap = new HashMap<>();

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
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            CheckBox cb = view.findViewById(R.id.cb_playlist);
            cb.setChecked(!cb.isChecked());
            Playlist item = playlists.get(position);
            if (cb.isChecked()) {
                checkedMap.put(String.valueOf(item.getId()), item);
            } else {
                checkedMap.remove(String.valueOf(item.getId()));
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
//        PlaylistModel.Companion.loadAllPlaylist(playlists -> {
//            this.playlists = playlists;
//        playlists =
//        mAdapter.setNewData(playlists);
//            return null;
//        }, error -> {
//            ToastUtils.show(error);
//            return null;
//        });
    }

    @Override
    protected void initInjector() {

    }

    public void delete(View view) {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content("是否删除歌单？")
                .onPositive((dialog, which) -> {
                    for (String key : checkedMap.keySet()) {
                        OnlinePlaylistUtils.INSTANCE.deletePlaylist(checkedMap.get(key), s -> {
                            playlists.remove(checkedMap.get(key));
                            mAdapter.setNewData(playlists);
                            return null;
                        });
                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .show();
    }
}
