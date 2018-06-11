package com.cyl.musiclake.ui.music.discover;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class TopListAdapter extends BaseQuickAdapter<Playlist, BaseViewHolder> {

    public TopListAdapter(List<Playlist> list) {
        super(R.layout.item_top_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Playlist neteaseList) {
        if (neteaseList.getName() == null)
            return;
        helper.setText(R.id.title, neteaseList.getName());
        if (neteaseList.getCoverUrl() == null)
            return;
        CoverLoader.loadImageView(mContext, neteaseList.getCoverUrl(), helper.getView(R.id.iv_cover));
    }
}