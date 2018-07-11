package com.cyl.musiclake.ui.music.discover;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musicapi.baidu.RadioChannel;
import com.cyl.musiclake.R;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class BaiduRadioAdapter extends BaseQuickAdapter<RadioChannel, BaseViewHolder> {

    public BaiduRadioAdapter(List<RadioChannel> list) {
        super(R.layout.item_top_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, RadioChannel channel) {
        helper.setText(R.id.title, channel.getName());
        CoverLoader.loadImageView(mContext, channel.getThumb(), helper.getView(R.id.iv_cover));
    }
}