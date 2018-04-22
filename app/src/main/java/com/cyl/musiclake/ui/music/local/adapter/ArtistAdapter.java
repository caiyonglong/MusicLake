package com.cyl.musiclake.ui.music.local.adapter;

import android.os.Build;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

public class ArtistAdapter extends BaseQuickAdapter<Artist, BaseViewHolder> {

    public ArtistAdapter(List<Artist> artistList) {
        super(R.layout.item_playlist_grid, artistList);
    }

    @Override
    protected void convert(BaseViewHolder helper, Artist artist) {
        helper.setText(R.id.name, artist.getName());
        helper.setText(R.id.artist, artist.getCount() + "首歌");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView(R.id.album).setTransitionName(Constants.TRANSTITION_ALBUM);
        }
        CoverLoader.loadImageViewByDouban(mContext, artist.getName(), helper.getView(R.id.album), null);
    }
}
