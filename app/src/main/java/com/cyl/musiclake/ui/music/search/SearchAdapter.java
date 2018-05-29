package com.cyl.musiclake.ui.music.search;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.utils.ConvertUtils;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;


/**
 * 作者：yonglong on 2016/9/26 23:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {
    public SearchAdapter(List<Music> musicList) {
        super(R.layout.item_music_search, musicList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music item) {
        String url = item.getCoverUri();
        CoverLoader.loadImageView(mContext, url, holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_from, item.getTypeName(false));
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.getTitle()));
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.getArtist(), item.getAlbum()));
        holder.addOnClickListener(R.id.iv_more);
    }

}
