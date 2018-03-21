package com.cyl.musiclake.ui.music.online.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class TopListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TopListAdapter(List<String> list) {
        super(R.layout.item_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String title) {
        helper.setText(R.id.tv_title, title);
    }
}