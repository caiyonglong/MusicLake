package com.cyl.musiclake.ui.music.online.adapter;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.utils.ConvertUtils;

import java.util.List;

/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class NeteaseAdapter extends BaseQuickAdapter<NeteaseMusic, BaseViewHolder> {
//        RecyclerView.Adapter<SongAdapter.ItemHolder> {

    public NeteaseAdapter(List<NeteaseMusic> musicList) {
        super(R.layout.item_music, musicList);
    }

    @Override
    protected void convert(BaseViewHolder holder, NeteaseMusic item) {
        String url = item.getAlbum().getPicUrl();
        GlideApp.with(mContext)
                .asBitmap()
                .load(url)
                .error(R.drawable.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) holder.getView(R.id.iv_cover));

        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.getName()));
        StringBuilder tt = new StringBuilder();
        for (int i = 0; i < item.getArtists().size(); i++) {
            tt.append("-").append(item.getArtists().get(i).getName());
        }
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(tt.toString(), item.getAlbum().getName()));
        holder.addOnClickListener(R.id.iv_more);
    }


}