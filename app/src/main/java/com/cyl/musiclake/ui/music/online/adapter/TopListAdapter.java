package com.cyl.musiclake.ui.music.online.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class TopListAdapter extends BaseQuickAdapter<Playlist, BaseViewHolder> {

    public TopListAdapter(List<Playlist> list) {
        super(R.layout.item_top_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Playlist neteaseList) {
        if (neteaseList.getName() == null)
            return;
        helper.setText(R.id.title, neteaseList.getName());
        if (neteaseList.getCoverUrl() == null)
            return;
        CoverLoader.loadImageView(mContext, neteaseList.getCoverUrl(), helper.getView(R.id.iv_cover));
        List<Music> musicLists = neteaseList.getMusicList().subList(0, 3);
        if (musicLists.size() >= 1 && musicLists.get(0).getTitle() != null
                && musicLists.get(0).getArtist() != null) {
            helper.setText(R.id.tv_music_1, (mContext.getString(R.string.song_list_item_title_1,
                    musicLists.get(0).getTitle(), musicLists.get(0).getAlbum())));
        } else {
            helper.setText(R.id.tv_music_1, "");
        }
        if (musicLists.size() >= 2 && musicLists.get(1).getTitle() != null
                && musicLists.get(1).getArtist() != null) {
            helper.setText(R.id.tv_music_2, mContext.getString(R.string.song_list_item_title_2,
                    musicLists.get(1).getTitle(), musicLists.get(1).getAlbum()));
        } else {
            helper.setText(R.id.tv_music_2, "");
        }
        if (musicLists.size() >= 3 && musicLists.get(2).getTitle() != null
                && musicLists.get(2).getArtist() != null) {
            helper.setText(R.id.tv_music_3, mContext.getString(R.string.song_list_item_title_3,
                    musicLists.get(2).getTitle(), musicLists.get(2).getAlbum()));
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