package com.cyl.musiclake.ui.localmusic.adapter;

import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Album;
import com.cyl.musiclake.ui.common.Constants;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

public class AlbumAdapter extends BaseQuickAdapter<Album, BaseViewHolder> {

    public AlbumAdapter(List<Album> albumList) {
        super(R.layout.item_playlist_grid, albumList);
    }

    @Override
    protected void convert(BaseViewHolder helper, Album album) {
        helper.setText(R.id.name, album.getName());
        helper.setText(R.id.artist, album.getArtistName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView(R.id.album).setTransitionName(Constants.TRANSTITION_ALBUM);
        }

        GlideApp.with(mContext)
                .load(CoverLoader.getInstance().getCoverUri(mContext, album.getId()))
                .error(R.drawable.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) helper.getView(R.id.album));

    }
}
