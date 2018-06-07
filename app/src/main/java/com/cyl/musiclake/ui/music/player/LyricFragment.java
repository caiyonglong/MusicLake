package com.cyl.musiclake.ui.music.player;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.view.LyricView;

import java.io.File;

import butterknife.BindView;

/**
 * Des    :
 * Author : master.
 * Date   : 2018/6/6 .
 */
public class LyricFragment extends BaseFragment {

    @BindView(R.id.lyricShow)
    LyricView mLrcView;

    @Override
    public int getLayoutId() {
        return R.layout.frag_player_lrcview;
    }

    @Override
    public void initViews() {
    }

    @Override
    protected void initInjector() {

    }

    public void showLyric(String lyricInfo, boolean isFilePath) {
        //初始化歌词配置
//        mLrcView.setLineSpace(15.0f);
//        mLrcView.setTextSize(17.0f);
//        mLrcView.setTouchable(true);
//        mLrcView.setPlayable(true);
//        mLrcView.setOnPlayerClickListener((progress, content) -> {
//            PlayManager.seekTo((int) progress);
//            if (!PlayManager.isPlaying()) {
//                PlayManager.playPause();
//            }
//        });
//        if (lyricInfo != null) {
//            if (isFilePath) {
//                mLrcView.setLyricFile(new File(lyricInfo), "utf-8");
//            } else {
//                mLrcView.setLyricContent(lyricInfo, "utf-8");
//            }
//        } else {
//            mLrcView.reset("暂无歌词");
//        }
    }
}
