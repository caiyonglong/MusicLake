package com.cyl.musiclake.ui.localmusic.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.utils.ConvertUtils;

import java.util.List;

/**
 * Created by D22434 on 2017/9/26.
 */

public class PlayQueueAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {

    public PlayQueueAdapter(List<Music> musicList) {
        super(R.layout.item_play_queue, musicList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music item) {
        String url = item.getCoverUri();
        GlideApp.with(mContext)
                .asBitmap()
                .load(url)
                .error(R.drawable.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into((ImageView) holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_source, item.getTypeName());

        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.getTitle()));
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.getArtist(), item.getAlbum()));
        if (PlayManager.getPlayingMusic() != null && PlayManager.getPlayingMusic().equals(item)) {
            holder.getView(R.id.v_playing).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.v_playing).setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.iv_clear);
        holder.addOnClickListener(R.id.iv_love);
    }
}
