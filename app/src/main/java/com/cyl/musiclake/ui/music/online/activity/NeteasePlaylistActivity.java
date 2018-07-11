package com.cyl.musiclake.ui.music.online.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cyl.musiclake.ui.music.online.base.BasePlaylistActivity;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
public class NeteasePlaylistActivity extends BasePlaylistActivity {

    private static final String TAG = "BaiduMusicListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getPlaylist(getIntent().getStringExtra("id"), this);
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        mPresenter.getPlaylist(getIntent().getStringExtra("id"), this);
    }

    @Override
    public String getToolBarTitle() {
        return getIntent().getStringExtra("title");
    }
}
