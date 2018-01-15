package com.cyl.musiclake.ui.localmusic.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.data.model.MetaChangedEvent;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.localmusic.adapter.MyPagerAdapter;
import com.cyl.musiclake.ui.localmusic.contract.PlayControlsContract;
import com.cyl.musiclake.ui.localmusic.dialog.PlayQueueDialog;
import com.cyl.musiclake.ui.localmusic.presenter.PlayControlsPresenter;
import com.cyl.musiclake.utils.ColorUtil;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.ImageUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.view.DepthPageTransformer;
import com.cyl.musiclake.view.LyricView;
import com.cyl.musiclake.view.PlayPauseView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, PlayControlsContract.View {

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
    PlayPauseView mPlayPause;
    @BindView(R.id.title)
    TextView mTvTitle;
    @BindView(R.id.artist)
    TextView mTvArtist;
    @BindView(R.id.album)
    ImageView mIvAlbum;

    @BindView(R.id.ic_detail)
    ImageView ic_detail;
    @BindView(R.id.previous)
    MaterialIconView skip_prev;
    @BindView(R.id.skip_next)
    MaterialIconView skip_next;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_play_page_bg)
    ImageView ivPlayingBg;
    @BindView(R.id.playOrPause)
    PlayPauseView mPlayOrPause;

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
    SeekBar mSeekBar;
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
    private List<View> mViewPagerContent;
    private SlidingUpPanelLayout mSlidingUpPaneLayout;
    private PlayControlsPresenter mPresenter;
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
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void initDatas() {
        mPresenter = new PlayControlsPresenter(getContext());
        mPresenter.attachView(this);

        RxBus.getInstance().register(MetaChangedEvent.class)
                .subscribe(metaChangedEvent -> {
                    mPresenter.updateNowPlayingCard();
                    mPresenter.loadLyric();
                });
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
        initAlbumPic(mCivImage);
    }


    /**
     * 退出播放页
     */
    private void onBackPressed() {
        getActivity().onBackPressed();
//        iv_back.setEnabled(false);
//        mHandler.postDelayed(() -> iv_back.setEnabled(true), 300);
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
        mLrcView.setOnPlayerClickListener((progress, content) -> {
            PlayManager.seekTo((int) progress);
            if (!PlayManager.isPlaying()) {
                PlayManager.playPause();
            }
        });
        if (file != null && file.exists()) {
            Log.e(TAG, "file" + file.getAbsolutePath());
            mLrcView.setLyricFile(file, "utf-8");
        } else {
            mLrcView.reset("暂无歌词");
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

    @Override
    public void setAlbumArt(Bitmap albumArt) {
        //设置图片资源
        mIvAlbum.setImageBitmap(albumArt);
        mCivImage.setImageBitmap(albumArt);
        ivPlayingBg.setImageBitmap(ImageUtils.blur(albumArt, 50));

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
        int statusBarColor = ColorUtil.getStatusBarColor(paletteColor);
        if (playQueueDialog != null && mSwatch != null) {
            playQueueDialog.setPaletteSwatch(mSwatch);
        }
        mLrcView.setHighLightTextColor(blackWhiteColor);
        mLrcView.setDefaultColor(blackWhiteColor);
        tv_time.setTextColor(blackWhiteColor);
        tv_duration.setTextColor(blackWhiteColor);
//        mLrcView.setTouchable(false);
        mLrcView.setHintColor(blackWhiteColor);
//        mBtnNext.setEnabled(true);
//        mBtnNext.setcolo(blackWhiteColor);
        skip_prev.setColor(blackWhiteColor);
        skip_next.setColor(blackWhiteColor);
        mPlayOrPause.setBtnColor(blackWhiteColor);
        mPlayOrPause.setEnabled(true);
    }

    @Override
    public void showLyric(File file) {
        loadLrc(file);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setPlayPauseButton(boolean isPlaying) {
        if (isPlaying) {
            mPlayPause.play();
            mPlayOrPause.play();
        } else {
            mPlayPause.pause();
            mPlayOrPause.pause();
        }
        if (operatingAnim != null) {
            if (isPlaying) {
                operatingAnim.resume();
            } else {
                operatingAnim.pause();
            }
        }
    }

    @Override
    public boolean getPlayPauseStatus() {
        return mPlayPause.isPlaying();
    }

    @Override
    public void updateProgress(int progress) {

        mSeekBar.setProgress(progress);
        mProgressBar.setProgress(progress);
        tv_time.setText(FormatUtil.formatTime(progress));
        mLrcView.setCurrentTimeMillis(progress);
    }

    @Override
    public void setProgressMax(int max) {
        mSeekBar.setMax(max);
        mProgressBar.setMax(max);
        tv_duration.setText(FormatUtil.formatTime(max));
    }

    @Override
    public void setErrorInfo(String message) {
        ToastUtils.show(getContext(), message);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (operatingAnim != null && operatingAnim.isPaused()) {
                operatingAnim.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (operatingAnim != null) {
                operatingAnim.pause();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
    }

}
