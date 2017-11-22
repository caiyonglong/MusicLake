package com.cyl.musiclake.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.mvp.presenter.MusicStateListener;
import com.cyl.musiclake.service.MusicPlayService;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.activity.BaseActivity;
import com.cyl.musiclake.ui.adapter.MyPagerAdapter;
import com.cyl.musiclake.ui.fragment.base.BaseFragment;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.view.LyricView;
import com.cyl.musiclake.view.PlayPauseButton;
import com.cyl.musiclake.view.PlayPauseDrawable;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MusicStateListener {

    private static final String TAG = "PlayFragment";
    public static View topContainer;
    //整个容器
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.next_buttom)
    ImageButton next_buttom;
    @BindView(R.id.song_progress_normal)
    ProgressBar mProgressBar;
    @BindView(R.id.play_pause)
    PlayPauseButton mPlayPause;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.artist)
    TextView artist;
    @BindView(R.id.album)
    ImageView iv_album;

    @BindView(R.id.ic_detail)
    ImageView ic_detail;
    @BindView(R.id.previous)
    ImageView skip_prev;
    @BindView(R.id.skip_next)
    ImageView skip_next;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_play_page_bg)
    ImageView ivPlayingBg;

    //textView
    @BindView(R.id.song_title)
    TextView tv_title;
    @BindView(R.id.song_artist)
    TextView tv_artist;
    @BindView(R.id.song_elapsed_time)
    TextView tv_time;
    @BindView(R.id.song_duration)
    TextView tv_duration;
    @BindView(R.id.skip_lrc)
    TextView skip_lrc;

    @BindView(R.id.song_progress)
    SeekBar sk_progress;
    @BindView(R.id.viewpager_player)
    ViewPager mViewPager;
    @BindView(R.id.playpausefloating)
    FloatingActionButton playPauseFloating;

    PlayQueueDialog playQueueDialog = null;
    Palette mPalette;
    static PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();

    private List<View> mViewPagerContent;
    private SlidingUpPanelLayout mSlidingUpPaneLayout;
    private LyricView mLrcView;
    private CircleImageView civ_cover;
    private int position;
    //是否有歌词
    private boolean lrc_empty = true;
    protected static Handler mHandler;

    public static PlayFragment newInstance() {
        Bundle args = new Bundle();
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.ic_detail)
    void openPlayQueue() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (playQueueDialog == null) {
            playQueueDialog = new PlayQueueDialog();
        }
        if (mPalette != null) {
            playQueueDialog.setPaletteSwatch(mPalette.getVibrantSwatch());
        }
        playQueueDialog.show(fm, "fragment_bottom_dialog");
    }

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            position = PlayManager.getCurrentPosition();
            sk_progress.setProgress(position);
            mProgressBar.setProgress(position);
            tv_time.setText(FormatUtil.formatTime(position));

            Log.d(TAG, "position" + position);
            mLrcView.setCurrentTimeMillis(position);
            mHandler.postDelayed(updateProgress, 50);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.frag_player;
    }

    /**
     * 初始化控件
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initViews() {
        //初始化控件
        topContainer = rootView.findViewById(R.id.top_container);
        mSlidingUpPaneLayout = (SlidingUpPanelLayout) rootView.getParent().getParent();
        mPlayPause.setColor(getResources().getColor(R.color.colorPrimary, getActivity().getTheme()));
        playPauseDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        playPauseFloating.setImageDrawable(playPauseDrawable);

        //初始化viewpager
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setCurrentItem(0);
        }
    }


    /**
     * 一些监听事件
     */
    @Override
    protected void listener() {
        iv_album.setOnClickListener(this);
        skip_next.setOnClickListener(this);
        skip_prev.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        skip_lrc.setOnClickListener(this);
        sk_progress.setOnSeekBarChangeListener(this);
        playPauseFloating.setOnClickListener(this);
        mPlayPause.setOnClickListener(this);
        next_buttom.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        mHandler = new Handler();
        ((BaseActivity)getActivity()).setMusicStateListenerListener(this);
        updatePlayPauseFloatingButton();
        updateView();

    }

    public void updateView() {
        Music music = PlayManager.getPlayingMusic();
        if (music == null) {
            return;
        }
        tv_title.setText(FileUtils.getTitle(music.getTitle()));
        title.setText(FileUtils.getTitle(music.getTitle()));
        tv_artist.setText(FileUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum()));
        artist.setText(FileUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum()));
        tv_duration.setText(FormatUtil.formatTime(music.getDuration()));
        sk_progress.setMax((int) music.getDuration());
        mProgressBar.setMax((int) music.getDuration());
        mLrcView.requestFocus();
        mHandler.post(updateProgress);
        setLrc(music);
        setCoverAndBg(music);
        initAlbumPic();
        updatePlayPauseFloatingButton();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_album:
                mSlidingUpPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.previous:
                PlayManager.prev();
                break;
            case R.id.skip_next:
                PlayManager.next();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.skip_lrc:
                break;
            case R.id.play_pause:
                PlayManager.playPause();
                break;
            case R.id.next_buttom:
                PlayManager.next();
                break;
            case R.id.playpausefloating:
                PlayManager.playPause();
                break;
        }

    }

    /**
     * 在线获取歌词
     */
    private void getlrc(Music.Type type) {
        if (type == Music.Type.ONLINE) {
            ToastUtils.show(getContext(), "在线歌曲");
        } else {
            ToastUtils.show(getContext(), "本地歌曲");
        }
    }


    /**
     * 退出播放页
     */
    private void onBackPressed() {
        getActivity().onBackPressed();
        iv_back.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_back.setEnabled(true);
            }
        }, 300);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (PlayManager.isPlaying() || PlayManager.isPause()) {
            int progress = seekBar.getProgress();
            PlayManager.seekTo(progress);
            tv_time.setText(FormatUtil.formatTime(progress));
            mLrcView.setCurrentTimeMillis(progress);
        } else {
            seekBar.setProgress(0);
        }
    }

    /**
     * 设置歌词路径
     *
     * @param music
     */
    private void setLrc(final Music music) {
        if (music.getType() == Music.Type.LOCAL) {
            String uri = music.getUri();
            if (uri.endsWith(".mp3")) {
                String lrcPath = uri.replace(".mp3", ".lrc");
                loadLrc(lrcPath, Music.Type.LOCAL);
            } else {
                loadLrc("", Music.Type.LOCAL);
            }
        } else {
            loadOnlineLrc(music.getLrcPath());
        }
    }

    private void loadOnlineLrc(String path) {
        Log.e("---", "path" + path);
//        loadLrc(lrcPath, online);
    }

    /**
     * 歌词视图加载歌词
     *
     * @param path
     * @param type
     */
    private void loadLrc(String path, Music.Type type) {

        mLrcView.setLineSpace(15.0f);
        mLrcView.setTextSize(17.0f);
        mLrcView.setPlayable(true);
        mLrcView.setOnPlayerClickListener(new LyricView.OnPlayerClickListener() {
            @Override
            public void onPlayerClicked(long progress, String content) {
                PlayManager.seekTo((int) progress);
                if (!PlayManager.isPlaying()) {
                    PlayManager.playPause();
                }
            }
        });
        File file = new File(path);
        mLrcView.setLyricFile(file, "utf-8");
    }

    /**
     * 初始化歌词图片
     *
     * @param music
     */
    private void setCoverAndBg(Music music) {
        if (music.getType() == Music.Type.LOCAL) {
            iv_album.setImageBitmap(CoverLoader.getInstance().loadRound(music.getCoverUri()));
            civ_cover.setImageBitmap(CoverLoader.getInstance().loadRound(music.getCoverUri()));
            ivPlayingBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music.getCoverUri()));

            mPalette = Palette.from(CoverLoader.getInstance().loadRound(music.getCoverUri()))
                    .generate();
        } else if (music.getType() == Music.Type.ONLINE) {
            if (music.getCoverUri() != null) {
                GlideApp.with(this)
                        .load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .into(civ_cover);
                GlideApp.with(this).load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .into(ivPlayingBg);
                GlideApp.with(this)
                        .load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .into(iv_album);
            }
        }

        if (operatingAnim != null) {
            if (PlayManager.isPlaying()) {
                operatingAnim.setCurrentPlayTime(currentPlayTime);
                operatingAnim.start();
            } else {
                operatingAnim.cancel();
                currentPlayTime = operatingAnim.getCurrentPlayTime();
            }
        }
    }


    public ObjectAnimator operatingAnim;
    public long currentPlayTime = 0;


    public void initAlbumPic() {
        /**
         * 旋转动画
         */
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim = ObjectAnimator.ofFloat(civ_cover, "rotation", 0, 359);
        operatingAnim.setDuration(20 * 1000);
        operatingAnim.setRepeatCount(-1);
        operatingAnim.setRepeatMode(ObjectAnimator.RESTART);
        operatingAnim.setInterpolator(lin);

    }

    private void setupViewPager(ViewPager viewPager) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //歌词视图
        View lrcView = inflater.inflate(R.layout.frag_player_lrcview, null);
        //专辑视图
        View coverView = inflater.inflate(R.layout.frag_player_coverview, null);
        civ_cover = (CircleImageView) coverView.findViewById(R.id.civ_cover);
        mLrcView = (LyricView) lrcView.findViewById(R.id.lyricShow);

        mViewPagerContent = new ArrayList<>(2);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        viewPager.setAdapter(new MyPagerAdapter(mViewPagerContent));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("PlayFragment", "--" + position);
                if (position == 1) {
                    mSlidingUpPaneLayout.setTouchEnabled(false);
//                    mSlidingUpPaneLayout.isTouchEnabled(true);
                } else {
                    mSlidingUpPaneLayout.setTouchEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void updatePlayPauseFloatingButton() {
        if (PlayManager.isPlaying()) {
            mPlayPause.setPlayed(true);
            playPauseDrawable.transformToPause(true);
        } else {
            mPlayPause.setPlayed(false);
            playPauseDrawable.transformToPlay(true);
        }
        mPlayPause.startAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (operatingAnim != null) {
            operatingAnim.cancel();
        }
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        updateView();
    }
}
