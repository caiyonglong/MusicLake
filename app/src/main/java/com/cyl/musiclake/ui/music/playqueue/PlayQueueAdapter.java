package com.cyl.musiclake.ui.music.playqueue;

import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.utils.ColorUtil;
import com.cyl.musiclake.utils.ConvertUtils;

import java.util.List;

/**
 * Created by D22434 on 2017/9/26.
 */

public class PlayQueueAdapter extends BaseItemDraggableAdapter<Music, BaseViewHolder> {

    private Palette.Swatch mSwatch;
    private boolean isDialog = true;

    public PlayQueueAdapter(List<Music> musicList, boolean isDialog) {
        super(R.layout.item_play_queue, musicList);
        this.isDialog = isDialog;
    }

    public PlayQueueAdapter(List<Music> musicList) {
        super(R.layout.item_play_queue, musicList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music item) {

        holder.setText(R.id.tv_source, item.getTypeName(false));
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.getTitle()));
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.getArtist(), item.getAlbum()));

        if (mSwatch != null) {
            holder.setTextColor(R.id.tv_title, mSwatch.getBodyTextColor());
            holder.setTextColor(R.id.tv_artist, mSwatch.getTitleTextColor());
            ((ImageView) holder.getView(R.id.iv_clear)).setColorFilter(ColorUtil.getBlackWhiteColor(mSwatch.getRgb()));
        }
        if (PlayManager.getPlayingId().equals(item.getId())) {
            holder.getView(R.id.v_playing).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.v_playing).setVisibility(View.GONE);
        }
        if (isDialog) {
            holder.setImageResource(R.id.iv_clear, R.drawable.ic_clear);
        } else {
            holder.setImageResource(R.id.iv_clear, R.drawable.ic_menu_black);
        }

        holder.addOnClickListener(R.id.iv_clear);
        holder.addOnClickListener(R.id.iv_love);
    }

    public void setPaletteSwatch(Palette.Swatch swatch) {
        mSwatch = swatch;
        notifyDataSetChanged();
    }

}
