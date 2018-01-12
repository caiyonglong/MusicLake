package com.cyl.musiclake.ui.localmusic.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.FolderInfo;

import java.util.List;

/**
 * Created by D22434 on 2018/1/11.
 */

public class FolderAdapter extends BaseQuickAdapter<FolderInfo, BaseViewHolder> {

    public FolderAdapter(List<FolderInfo> folderInfos) {
        super(R.layout.item_music, folderInfos);
    }

    @Override
    protected void convert(BaseViewHolder holder, FolderInfo folderInfo) {
        holder.setText(R.id.tv_title, folderInfo.folderName);
        holder.setText(R.id.tv_artist, folderInfo.folderPath);
        holder.getView(R.id.iv_more).setVisibility(View.GONE);
        Drawable image = mContext.getResources().getDrawable(R.drawable.ic_folder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (image != null) {
                image.setTint(Color.BLUE);
            }
        }
        ImageView cover = holder.getView(R.id.iv_cover);
        cover.setImageDrawable(image);

    }
}