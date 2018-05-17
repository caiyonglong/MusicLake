package com.cyl.musiclake.ui.music.local.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LocalAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<Integer> values = new ArrayList<>();

    public LocalAdapter() {
        super(R.layout.item_layout_view);
        List<String> names = new ArrayList<>();
        String[] titles = {"本地音乐", "播放历史", "本地收藏", "下载"};
        names.clear();
        values.clear();
        for (int i = 0; i < 4; i++) {
            names.add(titles[i]);
            values.add(0);
        }
        setNewData(names);
    }

    @Override
    protected void convert(BaseViewHolder helper, String name) {
        MaterialIconView mIcon = helper.getView(R.id.iv_icon);
        if (helper.getLayoutPosition() == 0) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.LIBRARY_MUSIC);
            mIcon.setColorResource(R.color.colorPrimary);
        } else if (helper.getLayoutPosition() == 1) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.HEART_OUTLINE);
            mIcon.setColorResource(R.color.orange);
        } else if (helper.getLayoutPosition() == 2) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.TIMETABLE);
            mIcon.setColorResource(R.color.red);
        } else if (helper.getLayoutPosition() == 3) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.ARROW_COLLAPSE_DOWN);
        }
        helper.setText(R.id.tv_name, name);
        helper.setText(R.id.tv_desc, values.get(helper.getLayoutPosition()) + "首");
    }

    public void setSongsNum(int position, int num) {
        values.set(position, num);
        notifyItemChanged(position, num);
    }
}