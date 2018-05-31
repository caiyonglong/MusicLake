package com.cyl.musiclake.ui.music.search;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;

import java.util.List;


/**
 * 作者：yonglong on 2016/9/26 23:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchSuggestionAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SearchSuggestionAdapter(List<String> suggestions) {
        super(R.layout.item_search_suggestion, suggestions);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.item_suggestion_query, item);
    }
}
