package com.cyl.musiclake.ui.onlinemusic.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.baidu.BaiduMusicList.Billboard;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineAdapter extends BaseQuickAdapter<Billboard, BaseViewHolder> {
//        RecyclerView.Adapter<OnlineAdapter.ItemHolder> {

    public OnlineAdapter(List<Billboard> mBillboards) {
        super(R.layout.item_online_large, mBillboards);
    }

    @Override
    protected void convert(BaseViewHolder helper, Billboard mBillboard) {
        if (mBillboard.getName() == null || mBillboard.getPic_s192() == null)
            return;
        GlideApp.with(mContext)
                .load(mBillboard.getPic_s192())
                .error(R.drawable.default_cover)
                .into((ImageView) helper.getView(R.id.iv_cover));

        helper.setText(R.id.title, mBillboard.getName());
        List<Billboard.MusicLists> musicLists = mBillboard.getContent();
        if (musicLists.size() >= 1 && musicLists.get(0).getTitle() != null
                && musicLists.get(0).getAuthor() != null) {
            helper.setText(R.id.tv_music_1, (mContext.getString(R.string.song_list_item_title_1,
                    musicLists.get(0).getTitle(), musicLists.get(0).getAuthor())));
        } else {
            helper.setText(R.id.tv_music_1, "");
        }
        if (musicLists.size() >= 2 && musicLists.get(1).getTitle() != null
                && musicLists.get(1).getAuthor() != null) {
            helper.setText(R.id.tv_music_2, mContext.getString(R.string.song_list_item_title_2,
                    musicLists.get(1).getTitle(), musicLists.get(1).getAuthor()));
        } else {
            helper.setText(R.id.tv_music_2, "");
        }
        if (musicLists.size() >= 3 && musicLists.get(2).getTitle() != null
                && musicLists.get(2).getAuthor() != null) {
            helper.setText(R.id.tv_music_3, mContext.getString(R.string.song_list_item_title_3,
                    musicLists.get(2).getTitle(), musicLists.get(2).getAuthor()));
        } else {
            helper.setText(R.id.tv_music_3, "");
        }
    }
}