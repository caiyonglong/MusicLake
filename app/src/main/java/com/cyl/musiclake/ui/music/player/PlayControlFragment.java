package com.cyl.musiclake.ui.music.player;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
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
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.TransitionAnimationUtils;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.local.adapter.MyPagerAdapter;
import com.cyl.musiclake.ui.music.playqueue.PlayQueueDialog;
import com.cyl.musiclake.ui.UIUtils;
import com.cyl.musiclake.utils.ColorUtil;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.view.DepthPageTransformer;
import com.cyl.musiclake.view.LyricView;
import com.cyl.musiclake.view.MultiTouchViewPager;
import com.cyl.musiclake.view.PlayPauseView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayControlFragment extends BaseFragment<PlayPresenter> implements SeekBar.OnSeekBarChangeListener, PlayContract.View {

    private static final String TAG = "PlayControlFragment";
    public View topContainer;
    //整个容器
    @BindView(R.id.container)
    LinearLayout mContainer;
    @BindView(R.id.play_next)
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
    CircleImageView mIvAlbum;

    @BindView(R.id.skip_queue)
    MaterialIconView skip_queue;
    @BindView(R.id.previous)
    MaterialIconView skip_prev;
    @BindView(R.id.skip_next)
    MaterialIconView skip_next;
    @BindView(R.id.iv_love)
    public ImageView mIvLove;
    @BindView(R.id.iv_play_page_bg)
    ImageView ivPlayingBg;
    @BindView(R.id.playOrPause)
    PlayPauseView mPlayOrPause;
    @BindView(R.id.pb_loading)
    ProgressBar mLoadingPrepared;

    //textView
    @BindView(R.id.song_title)
    TextView mTvName;
    @BindView(R.id.song_artist)
    TextView mTvArtistAlbum;

    @BindView(R.id.song_elapsed_time)
    TextView tv_time;
    @BindView(R.id.song_duration)
    TextView tv_duration;

    @BindView(R.id.song_progress)
    SeekBar mSeekBar;
    @BindView(R.id.viewpager_player)
    MultiTouchViewPager mViewPager;

    //ViewPager中界面专辑和歌词
    private LyricView mLrcView;
    private Drawable mDrawable;

    private PlayQueueDialog playQueueDialog = null;
    private Palette mPalette;
    private Palette.Swatch mSwatch;
    private List<View> mViewPagerContent;
    private LinearInterpolator mLinearInterpolator = new LinearInterpolator();
    public ObjectAnimator operatingAnim;
    public long currentPlayTime = 0;

    @OnClick(R.id.skip_next)
    void next() {
        PlayManager.next();
    }

    @OnClick(R.id.play_next)
    void nextBottom() {
        PlayManager.next();
    }

    @OnClick(R.id.play_pause)
    void playOrPause() {
        PlayManager.playPause();
    }

    @OnClick(R.id.playOrPause)
    void playOrPauseF() {
        PlayManager.playPause();
    }

    @OnClick(R.id.previous)
    void prev() {
        PlayManager.prev();
    }

    @OnClick(R.id.iv_love)
    void love() {
        Music music = PlayManager.getPlayingMusic();
        if (music == null)
            return;
        UIUtils.INSTANCE.collectMusic(mIvLove, music);
    }


    @OnClick(R.id.skip_queue)
    void openPlayQueue() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        playQueueDialog = PlayQueueDialog.Companion.newInstance();
        playQueueDialog.show(fm, "fragment_bottom_dialog");
    }

    public static PlayControlFragment newInstance() {
        Bundle args = new Bundle();
        PlayControlFragment fragment = new PlayControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_player;
    }

    @Override
    public void initViews() {
        //初始化控件
        topContainer = rootView.findViewById(R.id.top_container);
        //初始化viewpager
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setPageTransformer(false, new DepthPageTransformer());
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setCurrentItem(0);
        }

        updatePlayStatus(PlayManager.isPlaying());
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void listener() {
        mSeekBar.setOnSeekBarChangeListener(this);
        topContainer.setOnClickListener(v -> {
            Intent intent = new Intent(mFragmentComponent.getActivity(), PlayerActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void loadData() {
        Music music = PlayManager.getPlayingMusic();
        mPresenter.updateNowPlaying(music);
    }

    private void setupViewPager(MultiTouchViewPager viewPager) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View lrcView = inflater.inflate(R.layout.frag_player_lrcview, null);
        mLrcView = lrcView.findViewById(R.id.lyricShow);
        mViewPagerContent = new ArrayList<>(1);
        mViewPagerContent.add(lrcView);
        viewPager.setAdapter(new MyPagerAdapter(mViewPagerContent));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.d("PlayControlFragment", "--" + position);
//                if (position == 1 && mSlidingUpPaneLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
//                    mSlidingUpPaneLayout.setTouchEnabled(false);
//                } else {
//                    mSlidingUpPaneLayout.setTouchEnabled(true);
//                }
                if (position == 0) {
                    mLrcView.updateView(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initAlbumPic(mIvAlbum);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(0);
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
            tv_time.setText(FormatUtil.INSTANCE.formatTime(progress));
            mLrcView.setCurrentTimeMillis(progress);
        } else {
            seekBar.setProgress(0);
        }
    }


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
    public void setPalette(Palette palette) {
        mPalette = palette;
        mSwatch = ColorUtil.getMostPopulousSwatch(palette);

        int paletteColor;
        if (mSwatch != null) {
            paletteColor = mSwatch.getRgb();
            int artistColor = mSwatch.getTitleTextColor();
            mTvName.setTextColor(ColorUtil.getOpaqueColor(artistColor));
            mTvArtistAlbum.setTextColor(artistColor);
        } else {
            mSwatch = palette.getMutedSwatch() == null ? palette.getVibrantSwatch() : palette.getMutedSwatch();
            if (mSwatch != null) {
                paletteColor = mSwatch.getRgb();
                int artistColor = mSwatch.getTitleTextColor();
                mTvName.setTextColor(ColorUtil.getOpaqueColor(artistColor));
                mTvArtistAlbum.setTextColor(artistColor);
            } else {
                paletteColor = Color.WHITE;
                mTvName.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                mTvArtistAlbum.setTextColor(ContextCompat.getColor(getContext(), android.R.color.secondary_text_light));
            }
        }
        //set icon color
        int blackWhiteColor = ColorUtil.getBlackWhiteColor(paletteColor);
        int statusBarColor = ColorUtil.getStatusBarColor(paletteColor);
//        StatusBarUtil.setColor(getActivity(), statusBarColor);
//        mLrcView.setHighLightTextColor(statusBarColor);
//        mLrcView.setDefaultColor(mSwatch.getBodyTextColor());
        tv_time.setTextColor(blackWhiteColor);
//        mTvArtistAlbum.setTextColor(blackWhiteColor);
        tv_duration.setTextColor(blackWhiteColor);
//        mLrcView.setHintColor(blackWhiteColor);
//        mBtnNext.setEnabled(true);
//        mBtnNext.setcolo(blackWhiteColor);
        skip_prev.setColor(blackWhiteColor);
        skip_next.setColor(blackWhiteColor);
        skip_queue.setColor(blackWhiteColor);
        mPlayOrPause.setBtnColor(blackWhiteColor);
        mPlayOrPause.setEnabled(true);
    }

    @Override
    public void showLyric(String lyricInfo, boolean isFilePath) {
        //初始化歌词配置
//        mLrcView.set(15.0f);
//        mLrcView.setTextSize(17.0f);
        mLrcView.setTouchable(false);
//        mLrcView.setPlayable(true);
        mLrcView.setOnPlayerClickListener((progress, content) -> {
            PlayManager.seekTo((int) progress);
            if (!PlayManager.isPlaying()) {
                PlayManager.playPause();
            }
        });
        if (lyricInfo != null) {
            if (isFilePath) {
                mLrcView.setLyricFile(new File(lyricInfo), "utf-8");
            } else {
                mLrcView.setLyricContent(lyricInfo);
            }
        } else {
            mLrcView.reset();
        }
    }


    @Override
    public void showLoading() {
        mLoadingPrepared.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingPrepared.setVisibility(View.GONE);
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
    public void onDestroy() {
        super.onDestroy();
        topContainer = null;
    }

    @Override
    public void setPlayingBitmap(@Nullable Bitmap albumArt) {
        //设置图片资源
        mIvAlbum.setImageBitmap(albumArt);

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
    public void setPlayingBg(@Nullable Drawable albumArt) {
        //加载背景图过度
        TransitionAnimationUtils.startChangeAnimation(ivPlayingBg, albumArt);
    }

    @Override
    public void updatePlayStatus(boolean isPlaying) {
        if (isPlaying) {
            mPlayPause.play();
            mPlayOrPause.play();
        } else {
            mPlayPause.pause();
            mPlayOrPause.pause();
        }
        if (operatingAnim != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isPlaying) {
                    operatingAnim.resume();
                } else {
                    operatingAnim.pause();
                }
            }
        }
    }

    @Override
    public void updatePlayMode() {

    }

    @Override
    public void updateProgress(int progress, int max) {
        mSeekBar.setProgress(progress);
        mProgressBar.setProgress(progress);
        tv_time.setText(FormatUtil.INSTANCE.formatTime(progress));
        mLrcView.setCurrentTimeMillis(progress);


        mSeekBar.setMax(max);
        mProgressBar.setMax(max);
        tv_duration.setText(FormatUtil.INSTANCE.formatTime(max));
    }

    @Override
    public void showNowPlaying(@Nullable Music music) {
        if (music != null) {
            rootView.setVisibility(View.VISIBLE);
            mTvName.setText(music.getTitle());
            mTvTitle.setText(music.getTitle());

            mTvArtist.setText(music.getArtist());
            mTvArtistAlbum.setText(music.getArtist());

            //更新收藏状态
            if (music.isLove()) {
                mIvLove.setImageResource(R.drawable.item_favorite_love);
            } else {
                mIvLove.setImageResource(R.drawable.item_favorite);
            }
        } else {
            rootView.setVisibility(View.GONE);
        }

    }
}
