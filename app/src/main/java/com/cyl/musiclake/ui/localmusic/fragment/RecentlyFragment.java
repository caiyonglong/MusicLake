package com.cyl.musiclake.ui.localmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.PlayHistoryLoader;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.PageAdapter;
import com.cyl.musiclake.ui.zone.EditActivity;
import com.cyl.musiclake.utils.Extras;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Monkey on 2015/6/29.
 */
public class RecentlyFragment extends BaseFragment {
    @BindView(R.id.m_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    PageAdapter adapter;

    public static RecentlyFragment newInstance() {
        Bundle args = new Bundle();
        RecentlyFragment fragment = new RecentlyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_music;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle("播放历史");
        mTabLayout.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initDatas() {
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageAdapter(getChildFragmentManager());
        adapter.addFragment(SongsFragment.newInstance(Extras.PLAY_HISTORY), "歌曲");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("是否清空播放历史？")
                        .onPositive((dialog, which) -> {
                            PlayHistoryLoader.clearPlayHistory(getActivity());
                            adapter.notifyDataSetChanged();
                        })
                        .positiveText("确定")
                        .negativeText("取消")
                        .show();
                break;
            case R.id.action_share:
                Intent intent3 = new Intent(getActivity(), EditActivity.class);
                StringBuilder content = new StringBuilder();
                List<Music> musicList = PlayHistoryLoader.getPlayHistory(getContext());
                if (musicList.size() > 0) {
                    content = new StringBuilder("分享歌单\n");
                }
                for (int i = 0; i < musicList.size(); i++) {
                    content.append(musicList.get(i).getTitle()).append("---").append(musicList.get(i).getArtist());
                    content.append("\n");
                }
                intent3.putExtra("content", content.toString());
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_playlist_detail, menu);
    }
}
