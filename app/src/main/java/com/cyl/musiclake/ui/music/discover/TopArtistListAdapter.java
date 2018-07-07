package com.cyl.musiclake.ui.music.discover;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.db.Artist;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class TopArtistListAdapter extends BaseQuickAdapter<Artist, BaseViewHolder> {

    public TopArtistListAdapter(List<Artist> list) {
        super(R.layout.item_discover_artist, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Artist artist) {
        if (artist.getName() == null)
            return;
        helper.setText(R.id.tv_name, artist.getName());
        CoverLoader.loadImageView(mContext, artist.getPicUrl(), helper.getView(R.id.iv_album));
    }
}