package com.cyl.musiclake.ui.music.local.adapter;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.doupan.DoubanMusic;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.utils.ConvertUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
            MusicApi.getMusicAlbumInfo(item.getTitle() + "," + item.getArtist()).subscribe(new Observer<DoubanMusic>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(DoubanMusic doubanMusic) {
                    if (doubanMusic.getCount() >= 1) {
                        url = doubanMusic.getMusics().get(0).getImage();
                        item.setCoverBig(url);
                        item.setCoverSmall(url);
                        item.setCoverUri(url);
                        GlideApp.with(mContext)
                                .load(url)
                                .error(R.drawable.default_cover)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into((ImageView) holder.getView(R.id.iv_cover));
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            GlideApp.with(mContext)
                    .asBitmap()
                    .load(url)
                    .error(R.drawable.default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView) holder.getView(R.id.iv_cover));
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