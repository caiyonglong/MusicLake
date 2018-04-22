package com.cyl.musiclake.ui.music.local.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.utils.ConvertUtils;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SongAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {
    //        RecyclerView.Adapter<SongAdapter.ItemHolder> {
    String url = null;

    public SongAdapter(List<Music> musicList) {
        super(R.layout.item_music, musicList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music item) {
        url = item.getCoverUri();
        if (url == null) {
            url = item.getTitle() + "," + item.getArtist();
            CoverLoader.loadImageViewByDouban(mContext, url, holder.getView(R.id.iv_cover), null);
        } else {
            CoverLoader.loadImageView(mContext, url, holder.getView(R.id.iv_cover));
        }

        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.getTitle()));
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.getArtist(), item.getAlbum()));
//        if (PlayManager.getPlayingMusic() != null && PlayManager.getPlayingMusic().equals(localItem)) {
//            holder.v_playing.setVisibility(View.VISIBLE);
//        } else {
//            holder.v_playing.setVisibility(View.GONE);
//        }
        holder.addOnClickListener(R.id.iv_more);
    }


}