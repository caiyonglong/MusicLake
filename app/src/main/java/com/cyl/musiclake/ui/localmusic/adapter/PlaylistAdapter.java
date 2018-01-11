package com.cyl.musiclake.ui.localmusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Playlist;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistAdapter extends BaseQuickAdapter<Playlist, BaseViewHolder> {

    public PlaylistAdapter(List<Playlist> playlists) {
        super(R.layout.item_playlist, playlists);
    }

    @Override
    protected void convert(BaseViewHolder helper, Playlist playlist) {
        helper.setText(R.id.tv_name, playlist.getName());
        helper.setText(R.id.tv_num, playlist.getCount() + "首");
    }

}