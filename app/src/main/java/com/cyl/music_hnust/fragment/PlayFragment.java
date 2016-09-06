package com.cyl.music_hnust.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.lyric.LrcView;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.utils.StatusBarCompat;
import com.cyl.music_hnust.utils.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;

/**
 * 作者：yonglong on 2016/8/21 23:12
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlayFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, PlayPauseButton.OnControlStatusChangeListener {

    Toolbar toolbar;
    RelativeLayout container;
    ImageView skip_prev, skip_next;
    PlayPauseButton play_pause;
    TextView tv_title, tv_artist, tv_time, tv_duration;
    SeekBar sk_progress;
    List<View> mViewPagerContent;
    ViewPager mViewPager;

    private LrcView mLrcView;


    @Override
    public int getLayoutId() {
        return R.layout.acitvity_player;
    }

    @Override
    public void initViews() {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        tv_title = (TextView) rootView.findViewById(R.id.song_title);
        tv_artist = (TextView) rootView.findViewById(R.id.song_artist);
        tv_time = (TextView) rootView.findViewById(R.id.song_elapsed_time);
        tv_duration = (TextView) rootView.findViewById(R.id.song_duration);
        sk_progress = (SeekBar) rootView.findViewById(R.id.song_progress);
        skip_prev = (ImageView) rootView.findViewById(R.id.previous);
        skip_next = (ImageView) rootView.findViewById(R.id.next);
        play_pause = (PlayPauseButton) rootView.findViewById(R.id.play_pause);
        container = (RelativeLayout) rootView.findViewById(R.id.container);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager_player);

        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("");
        }
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(3);
        }

    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_my, menu);
//    }

    @Override
    protected void listener() {
        skip_next.setOnClickListener(this);
        skip_prev.setOnClickListener(this);
        sk_progress.setOnSeekBarChangeListener(this);
        play_pause.setOnControlStatusChangeListener(this);
    }

    @Override
    protected void initDatas() {
        sk_progress.setProgress(0);
        onPlay(getmPlayService().getPlayingMusic());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbar.setEnabled(true);
                }
            }, 300);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onUpdate(int progress) {
        sk_progress.setProgress(progress);
        tv_time.setText(SystemUtils.formatTime(progress));
        if (mLrcView.hasLrc()) {
            mLrcView.updateTime(progress);
        }
    }

    public void onPlay(Music music) {
        if (music == null) {
            return;
        }
        play_pause.setPlayed(true);
        tv_title.setText(music.getTitle());
        tv_artist.setText(music.getArtist());
        tv_duration.setText(SystemUtils.formatTime(music.getDuration()));
        sk_progress.setMax((int) music.getDuration());
        sk_progress.setProgress(0);
        setLrc(music);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous:
                getmPlayService().prev();
                break;
            case R.id.next:
                getmPlayService().next();
                break;
        }

    }
    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = StatusBarCompat.getStatusBarHeight(getActivity());
            container.setPadding(0, top, 0, 0);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (getmPlayService().isPlaying() || getmPlayService().isPause()) {
            int progress = seekBar.getProgress();
            getmPlayService().seekTo(progress);
            mLrcView.onDrag(progress);
            tv_time.setText(SystemUtils.formatTime(progress));
        } else {
            seekBar.setProgress(0);
        }
    }


    private void setLrc(final Music music) {
        if (music.getType() == Music.Type.LOCAL) {
            String uri = music.getUri();
            if (uri.endsWith(".mp3") && uri!=null) {
                String lrcPath = uri.replace(".mp3", ".lrc");
                File file = new File(lrcPath);
                if (file.exists()) {
                    loadLrc(lrcPath);
                }
            }else {
                loadLrc("");
            }
        }
//        else {
//            String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
//            loadLrc(lrcPath);
//        }
    }

    private void loadLrc(String path) {
        mLrcView.loadLrc(path);
        // 清除tag
//        mLrcView.setTag(null);
    }
    private void setupViewPager(ViewPager viewPager) {
        View lrcView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_player_lrcview, null);
        mLrcView  = (LrcView) lrcView.findViewById(R.id.LyricShow);
        mViewPagerContent = new ArrayList<>(1);
        mViewPagerContent.add(lrcView);
        viewPager.setAdapter(new MyPagerAdapter(mViewPagerContent));
    }

    @Override
    public void onStatusChange(View view, boolean state) {
        if (state){
            getmPlayService().playPause();
        }else {
            getmPlayService().pause();
        }
    }

    public void onPlayerResume() {
        play_pause.setPlayed(true);
    }

    public void onPlayerPause() {
        play_pause.setPlayed(false);
    }


    //适配viewpager
    private class MyPagerAdapter extends PagerAdapter {
        private List<View> mViews;

        public MyPagerAdapter(List<View> views) {
            mViews = views;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }
    }
}
