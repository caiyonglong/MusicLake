package com.cyl.musiclake.ui.music.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
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
import com.cyl.musiclake.ui.music.contract.PlayControlsContract;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.presenter.MusicStateListener;
import com.cyl.musiclake.ui.music.presenter.PlayControlsPresenter;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.music.adapter.MyPagerAdapter;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.music.dialog.PlayQueueDialog;
import com.cyl.musiclake.utils.ColorUtil;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.ImageUtils;
import com.cyl.musiclake.view.DepthPageTransformer;
import com.cyl.musiclake.view.LyricView;
import com.cyl.musiclake.view.PlayPauseButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, MusicStateListener, PlayControlsContract.View {

    private static final String TAG = "PlayFragment";
    public static View topContainer;
    //整个容器
    @BindView(R.id.container)
    LinearLayout mContainer;
    @BindView(R.id.next_buttom)
    ImageButton mBtnNext;
    @BindView(R.id.song_progress_normal)
    ProgressBar mProgressBar;
    @BindView(R.id.play_pause)
    PlayPauseButton mPlayPause;
    @BindView(R.id.title)
    TextView mTvTitle;
    @BindView(R.id.artist)
    TextView mTvArtist;
    @BindView(R.id.album)
    ImageView mIvAlbum;

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
    @BindView(R.id.playOrPause)
    ImageView mPlayOrPause;

    //textView
    @BindView(R.id.song_title)
    TextView mTvName;
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


    LyricView mLrcView;
    CircleImageView mCivImage;


    @OnClick(R.id.iv_back)
    void back() {
        onBackPressed();
    }

    private PlayQueueDialog playQueueDialog = null;
    private Palette mPalette;
    private Palette.Swatch mSwatch;
    private boolean isDebug = true;
    private int mProgress;
    private Handler mHandler;
    private List<View> mViewPagerContent;
    private SlidingUpPanelLayout mSlidingUpPaneLayout;
    private PlayControlsPresenter mPresenter = new PlayControlsPresenter();
    private LinearInterpolator mLinearInterpolator = new LinearInterpolator();


    @OnClick(R.id.skip_next)
    void next() {
        mPresenter.onNextClick();
    }

    @OnClick(R.id.next_buttom)
    void nextButtom() {
        mPresenter.onNextClick();
    }

    @OnClick(R.id.play_pause)
    void playOrPause() {
        mPresenter.onPlayPauseClick();
    }

    @OnClick(R.id.playOrPause)
    void playOrPauseF() {
        mPresenter.onPlayPauseClick();
    }

    @OnClick(R.id.previous)
    void prev() {
        mPresenter.onPreviousClick();
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
            if (isDebug) Log.d(TAG, "mProgress" + mProgress);
            mProgress = PlayManager.getCurrentPosition();
            sk_progress.setProgress(mProgress);
            mProgressBar.setProgress(mProgress);
            tv_time.setText(FormatUtil.formatTime(mProgress));
            mLrcView.setCurrentTimeMillis(mProgress);
            mHandler.postDelayed(updateProgress, 50);
        }
    };

    public static PlayFragment newInstance() {
        Bundle args = new Bundle();
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_player;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initViews() {
        //初始化控件
        topContainer = rootView.findViewById(R.id.top_container);
        mSlidingUpPaneLayout = (SlidingUpPanelLayout) rootView.getParent().getParent();
        mPlayPause.setColor(getResources().getColor(R.color.colorPrimary, getActivity().getTheme()));
        //初始化viewpager
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setPageTransformer(false, new DepthPageTransformer());
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void listener() {
        sk_progress.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void initDatas() {
        mHandler = new Handler();
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).setMusicStateListenerListener(this);
        }
        mPresenter.attachView(this);
        mPresenter.updateNowPlayingCard();
    }

    private void setupViewPager(ViewPager viewPager) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View lrcView = inflater.inflate(R.layout.frag_player_lrcview, null);
        View coverView = inflater.inflate(R.layout.frag_player_coverview, null);

        mLrcView = lrcView.findViewById(R.id.lyricShow);
        mCivImage = coverView.findViewById(R.id.civ_cover);

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
                } else {
                    mSlidingUpPaneLayout.setTouchEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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


    private void loadLrc(File file) {
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
        if (file != null && file.exists()) {
            Log.e(TAG, "file" + file.getAbsolutePath());
            mLrcView.setLyricFile(file, "utf-8");
        } else {
            mLrcView.reset("暂无歌词");
        }
    }

    /**
     * 初始化歌词图片
     *
     * @param music
     */
    private void setCoverAndBg(Music music) {
        if (music.getType() == Music.Type.LOCAL) {
            mIvAlbum.setImageBitmap(CoverLoader.getInstance().loadRound(music.getCoverUri()));
            mCivImage.setImageBitmap(CoverLoader.getInstance().loadRound(music.getCoverUri()));
            ivPlayingBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music.getCoverUri()));

            mPalette = Palette.from(CoverLoader.getInstance().loadRound(music.getCoverUri()))
                    .generate();
        } else if (music.getType() == Music.Type.ONLINE) {
            if (music.getCoverUri() != null) {
                GlideApp.with(this)
                        .load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .into(mCivImage);
                GlideApp.with(this).load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .into(ivPlayingBg);
                GlideApp.with(this)
                        .load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .into(mIvAlbum);
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

    /**
     * 旋转动画
     */
    public void initAlbumPic(View view) {
        operatingAnim = ObjectAnimator.ofFloat(view, "rotation", 0, 359);
        operatingAnim.setDuration(20 * 1000);
        operatingAnim.setRepeatCount(-1);
        operatingAnim.setRepeatMode(ObjectAnimator.RESTART);
        operatingAnim.setInterpolator(mLinearInterpolator);

    }


    public void updatePlayPauseFloatingButton() {
        if (PlayManager.isPlaying()) {
            mPlayPause.setPlayed(true);
        } else {
            mPlayPause.setPlayed(false);
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
        mPresenter.updateNowPlayingCard();
        mPresenter.loadLyric();
    }

    @Override
    public void setAlbumArt(Bitmap albumArt) {
        mIvAlbum.setImageBitmap(albumArt);
        mCivImage.setImageBitmap(albumArt);
        ivPlayingBg.setImageBitmap(ImageUtils.blur(albumArt, 50));
        initAlbumPic(mCivImage);
    }

    @Override
    public void setAlbumArt(Drawable albumArt) {
        ivPlayingBg.setBackground(albumArt);
    }

    @Override
    public void setTitle(String title) {
        mTvName.setText(title);
        mTvTitle.setText(title);
    }

    @Override
    public void setArtist(String artist) {
        tv_artist.setText(artist);
        mTvArtist.setText(artist);
    }

    @Override
    public void setPalette(Palette palette) {
        mPalette = palette;
        mSwatch = ColorUtil.getMostPopulousSwatch(palette);

        int paletteColor = Color.BLACK;
        if (mSwatch != null) {
            paletteColor = mSwatch.getRgb();
            int artistColor = mSwatch.getTitleTextColor();
            mTvName.setTextColor(ColorUtil.getOpaqueColor(artistColor));
            tv_artist.setTextColor(artistColor);
        } else {
            mSwatch = palette.getMutedSwatch() == null ? palette.getVibrantSwatch() : palette.getMutedSwatch();
            if (mSwatch != null) {
                paletteColor = mSwatch.getRgb();
                int artistColor = mSwatch.getTitleTextColor();
                mTvName.setTextColor(ColorUtil.getOpaqueColor(artistColor));
                tv_artist.setTextColor(artistColor);
            } else {
//                paletteColor= Color.parseColor();
                mTvName.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                tv_artist.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            }

        }
        //set icon color
        int blackWhiteColor = ColorUtil.getBlackWhiteColor(paletteColor);
        if (playQueueDialog != null && mSwatch != null) {
            playQueueDialog.setPaletteSwatch(mSwatch);
        }
        mLrcView.setHighLightTextColor(blackWhiteColor);
        mLrcView.setDefaultColor(blackWhiteColor);
        tv_time.setTextColor(blackWhiteColor);
        tv_duration.setTextColor(blackWhiteColor);
//        mLrcView.setTouchable(false);
        mLrcView.setHintColor(blackWhiteColor);
//        mPlayPause.setDrawableColor(blackWhiteColor);
//        mPlayPause.setCircleColor(blackWhiteColor);
//        mPlayPause.setCircleAlpah(0);
//        mPlayPause.setEnabled(true);
        mBtnNext.setEnabled(true);
//        mBtnNext.setColor(blackWhiteColor);
//        skip_prev.setColor(blackWhiteColor);
//        next.setColor(blackWhiteColor);
//        iconPlayQueue.setColor(blackWhiteColor);

        //set timely color
//        setTimelyColor(blackWhiteColor);

        //set seekbar progressdrawable
//        ScaleDrawable scaleDrawable = (ScaleDrawable) ((LayerDrawable) mSeekBar.getProgressDrawable()).findDrawableByLayerId(R.id.progress);
//        GradientDrawable gradientDrawable = (GradientDrawable) scaleDrawable.getDrawable();
//        gradientDrawable.setColors(new int[]{blackWhiteColor, blackWhiteColor, blackWhiteColor});

    }

    @Override
    public void showLyric(File file) {
        loadLrc(file);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setPlayPauseButton(boolean isPlaying) {
        mPlayPause.setPlayed(isPlaying);
        mPlayPause.startAnimation();
        if (operatingAnim != null) {
            if (isPlaying) {
                mPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle));
                operatingAnim.resume();
            } else {
                mPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle));
                operatingAnim.pause();
            }
        }
    }

    @Override
    public boolean getPlayPauseStatus() {
        return mPlayPause.isPlayed();
    }

    @Override
    public void startUpdateProgress() {
        if (operatingAnim != null) {
            operatingAnim.start();
        }
        mHandler.post(updateProgress);
    }

    @Override
    public void setProgressMax(int max) {
        sk_progress.setMax(max);
        mProgressBar.setMax(max);
        tv_duration.setText(FormatUtil.formatTime(max));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
