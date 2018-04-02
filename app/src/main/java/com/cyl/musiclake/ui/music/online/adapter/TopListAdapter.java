package com.cyl.musiclake.ui.music.online.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class TopListAdapter extends BaseQuickAdapter<NeteaseList, BaseViewHolder> {

    public TopListAdapter(List<NeteaseList> list) {
        super(R.layout.item_top_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, NeteaseList neteaseList) {
        if (neteaseList.getName() == null || neteaseList.getCoverImgUrl() == null)
            return;
        GlideApp.with(mContext)
                .load(neteaseList.getCoverImgUrl())
                .centerCrop()
                .error(R.drawable.default_cover)
                .into((ImageView) helper.getView(R.id.iv_cover));

        helper.setText(R.id.title, neteaseList.getName());
        List<NeteaseMusic> musicLists = neteaseList.getTracks();
        if (musicLists.size() >= 1 && musicLists.get(0).getName() != null
                && musicLists.get(0).getArtists() != null) {
            helper.setText(R.id.tv_music_1, (mContext.getString(R.string.song_list_item_title_1,
                    musicLists.get(0).getName(), getAuthor(musicLists.get(0).getArtists()))));
        } else {
            helper.setText(R.id.tv_music_1, "");
        }
        if (musicLists.size() >= 2 && musicLists.get(1).getName() != null
                && getAuthor(musicLists.get(1).getArtists()) != null) {
            helper.setText(R.id.tv_music_2, mContext.getString(R.string.song_list_item_title_2,
                    musicLists.get(1).getName(), getAuthor(musicLists.get(1).getArtists())));
        } else {
            helper.setText(R.id.tv_music_2, "");
        }
        if (musicLists.size() >= 3 && musicLists.get(2).getName() != null
                && getAuthor(musicLists.get(2).getArtists()) != null) {
            helper.setText(R.id.tv_music_3, mContext.getString(R.string.song_list_item_title_3,
                    musicLists.get(2).getName(), getAuthor(musicLists.get(2).getArtists())));
        } else {
            helper.setText(R.id.tv_music_3, "");
        }
    }

    private String getAuthor(List<NeteaseMusic.ArtistsBeanX> artists) {
        if (artists.size() == 0) return null;

        StringBuilder artist = new StringBuilder(artists.get(0).getName());
        for (int i = 1; i < artists.size(); i++) {
            artist.append(",").append(artists.get(i).getName());
        }
        return artist.toString();
    }
}