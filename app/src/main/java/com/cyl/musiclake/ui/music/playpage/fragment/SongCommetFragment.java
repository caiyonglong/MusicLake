package com.cyl.musiclake.ui.music.playpage.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Des    :
 * Author : master.
 * Date   : 2018/6/6 .
 */
public class SongCommetFragment extends BaseFragment {

    @BindView(R.id.rv_comment)
    RecyclerView mCommentRsv;

    @Override
    public int getLayoutId() {
        return R.layout.frag_player_comment;
    }

    @Override
    public void initViews() {
        mCommentRsv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initInjector() {

    }

}
