//package com.cyl.musiclake.ui.music.online.adapter;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.cyl.musiclake.R;
//import com.cyl.musiclake.api.netease.NeteaseMusic;
//import com.cyl.musiclake.utils.ConvertUtils;
//import com.cyl.musiclake.utils.CoverLoader;
//
//import java.util.List;
//
///**
// * 功能：本地歌曲item
// * 作者：yonglong on 2016/8/8 19:44
// * 邮箱：643872807@qq.com
// * 版本：2.5
// */
//public class NeteaseAdapter extends BaseQuickAdapter<NeteaseMusic, BaseViewHolder> {
//
//    public NeteaseAdapter(List<NeteaseMusic> musicList) {
//        super(R.layout.item_music, musicList);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder holder, NeteaseMusic item) {
//        String url = item.getAlbum().getPicUrl();
//        CoverLoader.loadImageView(mContext, url, holder.getView(R.id.iv_cover));
//        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.getName()));
//        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(getAuthor(item.getArtists()), item.getAlbum().getName()));
//        holder.addOnClickListener(R.id.iv_more);
//    }
//
//
//    private String getAuthor(List<NeteaseMusic.ArtistsBeanX> artists) {
//        if (artists.size() == 0) return null;
//
//        StringBuilder artist = new StringBuilder(artists.get(0).getName());
//        for (int i = 1; i < artists.size(); i++) {
//            artist.append(",").append(artists.get(i).getName());
//        }
//        return artist.toString();
//    }
//
//}