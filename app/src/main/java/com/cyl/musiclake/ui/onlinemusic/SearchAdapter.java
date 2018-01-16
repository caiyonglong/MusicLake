package com.cyl.musiclake.ui.onlinemusic;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FileUtils;

import java.util.List;


/**
 * 作者：yonglong on 2016/9/26 23:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {
    //        RecyclerView.Adapter<SearchAdapter.ItemHolder> {
    public SearchAdapter(List<Music> musicList) {
        super(R.layout.item_music_search, musicList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music item) {
        String url;
        if (item.getType() == Music.Type.LOCAL && item.getAlbumId() != -1) {
            url = CoverLoader.getInstance().getCoverUri(mContext, item.getAlbumId());
        } else {
            url = item.getCoverUri();
        }
        GlideApp.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) holder.getView(R.id.iv_cover));

        if (item.getType() == Music.Type.QQ) {
            holder.setText(R.id.tv_from, "来源于QQ");
        } else if (item.getType() == Music.Type.XIAMI) {
            holder.setText(R.id.tv_from, "来源于虾米");
        }
        holder.setText(R.id.tv_title, FileUtils.getTitle(item.getTitle()));
        holder.setText(R.id.tv_artist, FileUtils.getArtistAndAlbum(item.getArtist(), item.getAlbum()));
//        if (PlayManager.getPlayingMusic() != null && PlayManager.getPlayingMusic().equals(localItem)) {
//            holder.v_playing.setVisibility(View.VISIBLE);
//        } else {
//            holder.v_playing.setVisibility(View.GONE);
//        }
        holder.addOnClickListener(R.id.iv_more);
    }

}
