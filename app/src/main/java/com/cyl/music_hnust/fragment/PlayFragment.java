package com.cyl.music_hnust.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.lyric.LrcView;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.utils.CoverLoader;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.StatusBarCompat;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.view.PlayPauseButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 功能：歌曲播放详情页
 * 作者：yonglong on 2016/8/21 23:12
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlayFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //整个容器
    LinearLayout container;
    //图片按钮
    ImageView skip_prev, skip_next ,iv_back,ivPlayingBg;
    //播放暂停按钮
    PlayPauseButton mPlayPause;
    View playPauseWrapper;
    //textView
    TextView tv_title, tv_artist, tv_time, tv_duration;
    SeekBar sk_progress;
    List<View> mViewPagerContent;
    ViewPager mViewPager;

    private LrcView mLrcView;
    private static CircleImageView civ_cover;


    @Override
    public int getLayoutId() {
        return R.layout.acitvity_player;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initViews() {
        //初始化控件
        tv_title = (TextView) rootView.findViewById(R.id.song_title);
        tv_artist = (TextView) rootView.findViewById(R.id.song_artist);
        tv_time = (TextView) rootView.findViewById(R.id.song_elapsed_time);
        tv_duration = (TextView) rootView.findViewById(R.id.song_duration);
        sk_progress = (SeekBar) rootView.findViewById(R.id.song_progress);
        skip_prev = (ImageView) rootView.findViewById(R.id.previous);
        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        ivPlayingBg = (ImageView) rootView.findViewById(R.id.iv_play_page_bg);
        skip_next = (ImageView) rootView.findViewById(R.id.next);
        mPlayPause = (PlayPauseButton) rootView.findViewById(R.id.play_pause);
        playPauseWrapper = rootView.findViewById(R.id.playpausewrapper);
        container = (LinearLayout) rootView.findViewById(R.id.container);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager_player);

        if (mPlayPause != null && getActivity() != null) {
            mPlayPause.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }

        //初始化沉淀式标题栏
        initSystemBar();
        //初始化viewpager
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(3);
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
        sk_progress.setOnSeekBarChangeListener(this);
        if (playPauseWrapper != null)
            playPauseWrapper.setOnClickListener(mButtonListener);
    }

    @Override
    protected void initDatas() {
        sk_progress.setProgress(0);
        onPlay(getmPlayService().getPlayingMusic());
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
        tv_title.setText(music.getTitle());
        tv_artist.setText(music.getArtist());
        tv_duration.setText(SystemUtils.formatTime(music.getDuration()));
        sk_progress.setMax((int) music.getDuration());
        sk_progress.setProgress(0);
        setLrc(music);
        setCoverAndBg(music);

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
            case R.id.iv_back:
                onBackPressed();;
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
        if (getmPlayService().isPlaying() || getmPlayService().isPause()) {
            int progress = seekBar.getProgress();
            getmPlayService().seekTo(progress);
            mLrcView.onDrag(progress);
            tv_time.setText(SystemUtils.formatTime(progress));
        } else {
            seekBar.setProgress(0);
        }
    }

    /**
     * 设置歌词路径
     * @param music
     */
    private void setLrc(final Music music) {
        if (music.getType() == Music.Type.LOCAL) {
            String uri = music.getUri();
            if (uri.endsWith(".mp3") && uri!=null) {
                String lrcPath = uri.replace(".mp3", ".lrc");
                File file = new File(lrcPath);
                if (file.exists()) {
                    loadLrc(lrcPath,Music.Type.LOCAL);
                }else {
                    loadLrc("", Music.Type.LOCAL);
                }
            }else {
                loadLrc("", Music.Type.LOCAL);
            }
        }
        else {
            String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
            loadLrc(lrcPath, Music.Type.ONLINE);
        }
    }

    /**
     * 歌词视图加载歌词
     * @param path
     * @param type
     */
    private void loadLrc(String path, Music.Type type) {
        mLrcView.loadLrc(path,type);
        // 清除tag
        mLrcView.setTag(null);
    }

    /**
     * 初始化歌词图片
     * @param music
     */
    private void setCoverAndBg(Music music) {

        if (music.getType() == Music.Type.LOCAL) {
//            civ_cover.setImageBitmap(CoverLoader.getInstance().loadThumbnail(music.getCoverUri()));
            ivPlayingBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music.getCoverUri()));
        } else {
            if (music.getCover() == null) {
//                mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(null));
                ivPlayingBg.setImageResource(R.drawable.play_page_default_bg);
            } else {
//                Bitmap cover = ImageUtils.resizeImage(music.getCover(), SizeUtils.getScreenWidth() / 2, SizeUtils.getScreenWidth() / 2);
//                cover = ImageUtils.createCircleImage(cover);
//                civ_cover.setImageBitmap(cover);
                Bitmap bg = ImageUtils.blur(music.getCover(), ImageUtils.BLUR_RADIUS);
                ivPlayingBg.setImageBitmap(bg);
            }
        }
        initAlbumpic();
    }

    private static Context mContext;
    public static ObjectAnimator operatingAnim;


    public static void initAlbumpic() {
        /**
         * 旋转动画
         */
        LinearInterpolator lin = new LinearInterpolator();

        operatingAnim = ObjectAnimator.ofFloat(civ_cover, "rotation", 0, 360);
        operatingAnim.setDuration(10000);
        operatingAnim.setRepeatCount(-1);
        operatingAnim.setRepeatMode(ObjectAnimator.RESTART);
        operatingAnim.setInterpolator(lin);

        operatingAnim.start();
    }
    private void setupViewPager(ViewPager viewPager) {
        //歌词视图
        View lrcView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_player_lrcview, null);
        mLrcView  = (LrcView) lrcView.findViewById(R.id.LyricShow);
        //专辑视图
        View coverView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_player_coverview, null);
        civ_cover  = (CircleImageView) lrcView.findViewById(R.id.civ_cover);
        mViewPagerContent = new ArrayList<>(2);

        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        viewPager.setAdapter(new MyPagerAdapter(mViewPagerContent));
    }



    public void onPlayerResume() {
        mPlayPause.setPlayed(true);
    }

    public void onPlayerPause() {
        mPlayPause.setPlayed(false);
    }

    private boolean duetoplaypause = false;

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            } else {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getmPlayService().playPause();
                }
            }, 200);


        }
    };


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
