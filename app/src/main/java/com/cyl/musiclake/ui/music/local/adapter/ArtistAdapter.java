package com.cyl.musiclake.ui.music.local.adapter;

import android.os.Build;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.db.ArtistBean;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

public class ArtistAdapter extends BaseQuickAdapter<ArtistBean, BaseViewHolder> {

    public ArtistAdapter(List<ArtistBean> artistBeanList) {
        super(R.layout.item_playlist_grid, artistBeanList);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtistBean artistBean) {
        helper.setText(R.id.name, artistBean.getName());
        helper.setText(R.id.artist, artistBean.getMusicSize() + "首歌");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView(R.id.album).setTransitionName(Constants.TRANSTITION_ALBUM);
        }
        CoverLoader.loadImageViewByDouban(mContext, artistBean.getName(), helper.getView(R.id.album), null);
    }
}
