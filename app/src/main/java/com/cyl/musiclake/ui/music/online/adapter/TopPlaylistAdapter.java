package com.cyl.musiclake.ui.music.online.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class TopPlaylistAdapter extends BaseQuickAdapter<NeteaseList, BaseViewHolder> {

    public TopPlaylistAdapter(List<NeteaseList> list) {
        super(R.layout.item_album_grid, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, NeteaseList neteaseList) {
        if (neteaseList.getName() == null || neteaseList.getCoverImgUrl() == null)
            return;
        CoverLoader.loadImageView(mContext, neteaseList.getCoverImgUrl(), helper.getView(R.id.iv_cover));
        helper.setText(R.id.tv_name, neteaseList.getName());
        helper.setText(R.id.tv_creator, neteaseList.getCreator().getNickname());

    }

}