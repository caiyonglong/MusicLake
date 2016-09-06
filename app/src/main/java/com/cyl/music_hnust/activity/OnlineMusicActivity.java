package com.cyl.music_hnust.activity;

import android.os.Bundle;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.OnlineAdapter;
import com.cyl.music_hnust.model.OnlineMusic;
import com.cyl.music_hnust.model.OnlineMusicList;
import com.cyl.music_hnust.model.OnlinePlaylistMusic;

import java.util.List;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineMusicActivity extends BaseActivity {

    private OnlinePlaylistMusic mListInfo;
    private OnlineMusicList mOnlineMusicList;
    private List<OnlineMusic> mMusicList;
    private OnlineAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_online;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }
    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

    }
}
