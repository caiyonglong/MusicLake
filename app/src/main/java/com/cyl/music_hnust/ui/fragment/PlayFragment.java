package com.cyl.music_hnust.ui.fragment;

import android.animation.ObjectAnimator;
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

import com.bumptech.glide.Glide;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.bean.music.Music;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.service.RxBus;
import com.cyl.music_hnust.ui.adapter.MyPagerAdapter;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.utils.CoverLoader;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ToastUtils;
import com.cyl.music_hnust.view.PlayPauseButton;
import com.cyl.music_hnust.view.PlayPauseDrawable;
import com.cyl.music_hnust.view.LrcView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PlayFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static View topContainer;
    //整个容器
    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.next_buttom)
    ImageButton next_buttom;
    @Bind(R.id.song_progress_normal)
    ProgressBar mProgressBar;
    @Bind(R.id.play_pause)
    PlayPauseButton mPlayPause;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.artist)
    TextView artist;
    @Bind(R.id.album)
    ImageView iv_album;

    @Bind(R.id.ic_detail)
    ImageView ic_detail;
    @Bind(R.id.previous)
    ImageView skip_prev;
    @Bind(R.id.skip_next)
    ImageView skip_next;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.iv_play_page_bg)
    ImageView ivPlayingBg;

    //textView
    @Bind(R.id.song_title)
    TextView tv_title;
    @Bind(R.id.song_artist)
    TextView tv_artist;
    @Bind(R.id.song_elapsed_time)
    TextView tv_time;
    @Bind(R.id.song_duration)
    TextView tv_duration;
    @Bind(R.id.skip_lrc)
    TextView skip_lrc;

    @Bind(R.id.song_progress)
    SeekBar sk_progress;
    @Bind(R.id.viewpager_player)
    ViewPager mViewPager;
    @Bind(R.id.playpausefloating)
    FloatingActionButton playPauseFloating;

    @OnClick(R.id.ic_detail)
    void openPlayQueue() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (playQueueDialog == null) {
            playQueueDialog = new PlayQueueDialog();
        }
        if (mSwatch != null) {
            playQueueDialog.setPaletteSwatch(mSwatch);
        }
        playQueueDialog.show(fm, "fragment_bottom_dialog");
    }

    PlayQueueDialog playQueueDialog = null;
    Palette.Swatch mSwatch;
    static PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();

    List<View> mViewPagerContent;

    private LrcView mLrcView;
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

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            position = PlayManager.getCurrentPosition();
            sk_progress.setProgress(position);
            mProgressBar.setProgress(position);
            tv_time.setText(FormatUtil.formatTime(position));

            if (mLrcView.hasLrc()) {
                mLrcView.updateTime(position);
            }
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

        mPlayPause.setColor(getResources().getColor(R.color.colorPrimary, getActivity().getTheme()));
        playPauseDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        playPauseFloating.setImageDrawable(playPauseDrawable);

        //初始化viewpager
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setCurrentItem(0);
        }
    }


    /**
     * 一些监听事件
     */
    @Override
    protected void listener() {
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
        updatePlayPauseFloatingButton();
        updateView();
        initAlbumPic();
        initSubscriptionEvent();
    }

    /**
     * 初始化观察者模式
     */
    private void initSubscriptionEvent() {
        Subscription subscription = RxBus.getInstance()
                .toObservable(Music.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Music>() {
                    @Override
                    public void call(Music event) {
                        Log.e("----", event.toString());
                        updateView();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        RxBus.getInstance().addSubscription(this, subscription);
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
        mHandler.post(updateProgress);
        setLrc(music);
        setCoverAndBg(music);
        updatePlayPauseFloatingButton();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            mLrcView.onDrag(progress);
            tv_time.setText(FormatUtil.formatTime(progress));
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
            if (uri.endsWith(".mp3") && uri != null) {
                String lrcPath = uri.replace(".mp3", ".lrc");
                File file = new File(lrcPath);
                if (file.exists()) {
                    lrc_empty = false;
                    loadLrc(lrcPath, Music.Type.LOCAL);
                } else {
                    loadLrc("", Music.Type.LOCAL);
                }
            } else {
                loadLrc("", Music.Type.LOCAL);
            }
        } else {
            loadOnlineLrc(music.getLrcPath());
        }
    }

    private void loadOnlineLrc(String path) {
        Log.e("---", "path" + path);
//
//        loadLrc(lrcPath, online);
    }

    /**
     * 歌词视图加载歌词
     *
     * @param path
     * @param type
     */
    private void loadLrc(String path, Music.Type type) {

        mLrcView.loadLrc(path, type);
        // 清除tag
        mLrcView.setTag(null);
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            lrc_empty = true;
        }
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

            mSwatch = Palette.from(CoverLoader.getInstance().loadRound(music.getCoverUri()))
                    .generate().getVibrantSwatch();
        } else if (music.getType() == Music.Type.ONLINE) {
            if (music.getCoverUri() != null) {
                Glide.with(this)
                        .load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .into(civ_cover);
                Glide.with(this).load(music.getCoverUri())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover) //失败图片
                        .crossFade(1000)
                        .bitmapTransform(new BlurTransformation(getContext(), 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(ivPlayingBg);
                Glide.with(this)
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
        //歌词视图
        View lrcView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_player_lrcview, null);
        mLrcView = (LrcView) lrcView.findViewById(R.id.LyricShow);
        //专辑视图
        View coverView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_player_coverview, null);
        civ_cover = (CircleImageView) coverView.findViewById(R.id.civ_cover);

        mViewPagerContent = new ArrayList<>(2);

        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        viewPager.setAdapter(new MyPagerAdapter(mViewPagerContent));
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

    public static void updateDrawableView(PanelState newState) {
        if (newState == PanelState.EXPANDED) {
        }
    }


}
