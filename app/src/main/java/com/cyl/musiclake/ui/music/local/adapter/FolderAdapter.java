package com.cyl.musiclake.ui.music.local.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.FolderInfo;

import java.util.List;

/**
 * Created by D22434 on 2018/1/11.
 */

public class FolderAdapter extends BaseQuickAdapter<FolderInfo, BaseViewHolder> {

    public FolderAdapter(List<FolderInfo> folderInfos) {
        super(R.layout.item_folder, folderInfos);
    }

    @Override
    protected void convert(BaseViewHolder holder, FolderInfo folderInfo) {
        holder.setText(R.id.tv_title, folderInfo.folderName);
        holder.setText(R.id.tv_artist, folderInfo.folderPath);
    }
}