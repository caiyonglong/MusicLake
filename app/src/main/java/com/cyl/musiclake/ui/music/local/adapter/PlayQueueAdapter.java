package com.cyl.musiclake.ui.music.local.adapter;

import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.utils.ColorUtil;
import com.cyl.musiclake.utils.ConvertUtils;

import java.util.List;

/**
 * Created by D22434 on 2017/9/26.
 */

public class PlayQueueAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {

    private Palette.Swatch mSwatch;

    public PlayQueueAdapter(List<Music> musicList) {
        super(R.layout.item_play_queue, musicList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music item) {

        holder.setText(R.id.tv_source, item.getTypeName());
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.getTitle()));
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.getArtist(), item.getAlbum()));

        if (mSwatch != null) {
            holder.setTextColor(R.id.tv_title, mSwatch.getBodyTextColor());
            holder.setTextColor(R.id.tv_artist, mSwatch.getTitleTextColor());
            ((ImageView) holder.getView(R.id.iv_clear)).setColorFilter(ColorUtil.getBlackWhiteColor(mSwatch.getRgb()));

            if (PlayManager.getPlayingMusic().equals(item)) {
                holder.getView(R.id.v_playing).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.v_playing).setVisibility(View.GONE);
            }
        }

        holder.addOnClickListener(R.id.iv_clear);
        holder.addOnClickListener(R.id.iv_love);
    }

    public void setPaletteSwatch(Palette.Swatch swatch) {
        mSwatch = swatch;
        notifyDataSetChanged();
    }

}
