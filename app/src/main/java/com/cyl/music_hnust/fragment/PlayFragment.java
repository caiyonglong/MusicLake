package com.cyl.music_hnust.fragment;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.MainActivity;
import com.cyl.music_hnust.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.music.lyric.LrcView;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.utils.CoverLoader;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.Preferences;
import com.cyl.music_hnust.utils.SizeUtils;
import com.cyl.music_hnust.utils.StatusBarCompat;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.cyl.music_hnust.view.PlayPauseButton;
import com.cyl.music_hnust.view.PlayPauseDrawable;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 功能：歌曲播放详情页
 * 作者：yonglong on 2016/8/21 23:12
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlayFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, ViewPager.OnPageChangeListener {

    //整个容器
    LinearLayout container;
    //图片按钮
    ImageView skip_prev, skip_next, skip_mode, iv_back, ivPlayingBg, page_icon;
    //播放暂停按钮
    PlayPauseButton mPlayPause;
    //textView
    TextView tv_title, tv_artist, tv_time, tv_duration, skip_lrc;
    SeekBar sk_progress;
    List<View> mViewPagerContent;
    ViewPager mViewPager;
    FloatingActionButton playPauseFloating;
    PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();

    private LocalMusicAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();

    private LrcView mLrcView;
    private RecyclerView recyclerView;
    private CircleImageView civ_cover;
    //播放模式：0顺序播放、1随机播放、2单曲循环
    private int play_mode;
    //是否有歌词
    private boolean lrc_empty = true;

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
        skip_mode = (ImageView) rootView.findViewById(R.id.skip_mode);
        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        skip_lrc = (TextView) rootView.findViewById(R.id.skip_lrc);
        ivPlayingBg = (ImageView) rootView.findViewById(R.id.iv_play_page_bg);
        page_icon = (ImageView) rootView.findViewById(R.id.page_icon);
        skip_next = (ImageView) rootView.findViewById(R.id.next);
        playPauseFloating = (FloatingActionButton) rootView.findViewById(R.id.playpausefloating);
        container = (LinearLayout) rootView.findViewById(R.id.container);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager_player);


        if (playPauseFloating != null) {
            playPauseDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            playPauseFloating.setImageDrawable(playPauseDrawable);
            if (getmPlayService().isPlaying())
                playPauseDrawable.transformToPause(false);
            else playPauseDrawable.transformToPlay(false);
        }

        //初始化沉淀式标题栏
        initSystemBar();
        //初始化viewpager
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(3);
        }
        mViewPager.setCurrentItem(1);
        updatePlayMode();

        setRecyclerView();
    }


    /**
     * 一些监听事件
     */
    @Override
    protected void listener() {
        skip_next.setOnClickListener(this);
        skip_prev.setOnClickListener(this);
        skip_mode.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        skip_lrc.setOnClickListener(this);
        sk_progress.setOnSeekBarChangeListener(this);
        if (playPauseFloating != null)
            playPauseFloating.setOnClickListener(mButtonListener);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void initDatas() {
        sk_progress.setProgress(0);
        onPlay(getmPlayService().getPlayingMusic());


    }


    public void onUpdate(int progress) {
        sk_progress.setProgress(progress);
        tv_time.setText(FormatUtil.formatTime(progress));
        if (playPauseFloating != null)
            updatePlayPauseFloatingButton();
        if (mLrcView.hasLrc()) {
            mLrcView.updateTime(progress);
        }
    }

    public void onPlay(Music music) {
        if (music == null) {
            return;
        }
        tv_title.setText(FileUtils.getTitle(music.getTitle()));
        tv_artist.setText(FileUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum()));
        tv_duration.setText(FormatUtil.formatTime(music.getDuration()));
        sk_progress.setMax((int) music.getDuration());
        sk_progress.setProgress(0);
        setLrc(music);
        setCoverAndBg(music);
        reloadAdapter();
        recyclerView.scrollToPosition(getmPlayService().getmPlayingPosition());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                if (getmPlayService() == null) {
                    Log.e("222", "33333333333333333");
                } else {
                    getmPlayService().prev();
                }
                break;
            case R.id.next:
                if (getmPlayService() == null) {
                    Log.e("222", "33333333333333333");
                } else {
                    getmPlayService().next();
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.skip_mode:
                play_mode = Preferences.getPlayMode();
                play_mode = (play_mode + 1) % 3;
                Preferences.savePlayMode(play_mode);
                updatePlayMode();
                break;
            case R.id.skip_lrc:


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
            String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
//            loadLrc(lrcPath, Music.Type.ONLINE);
            loadOnlineLrc(music, Music.Type.ONLINE);
        }
    }

    private void loadOnlineLrc(final Music music, final Music.Type online) {
        final String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
        final String lrcFileName = FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
        File lrcFile = new File(FileUtils.getLrcDir() + lrcFileName);
        if (!TextUtils.isEmpty(music.getLrcPath()) && !lrcFile.exists()) {
            Log.e("---", "exists");
            OkHttpUtils.get().url(music.getLrcPath()).build()
                    .execute(new FileCallBack(FileUtils.getLrcDir(), lrcFileName) {

                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(File response) {
                            Log.e("---", lrcPath);

                            loadLrc(lrcPath, online);
                        }

                        @Override
                        public void inProgress(float progress, long total) {

                        }
                    });
        } else {
            loadLrc(lrcPath, online);
            Log.e("---", "emnnnn");
        }
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
            civ_cover.setImageBitmap(CoverLoader.getInstance().loadRound(music.getCoverUri()));
            ivPlayingBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music.getCoverUri()));
        } else {
            if (music.getCover() == null) {
                civ_cover.setImageResource(R.drawable.ic_empty_music2);
                ivPlayingBg.setImageResource(R.drawable.play_page_default_bg);
            } else {
                Bitmap cover = ImageUtils.resizeImage(music.getCover(), SizeUtils.getScreenWidth() / 2, SizeUtils.getScreenWidth() / 2);
//                cover = ImageUtils.createCircleImage(cover);
                civ_cover.setImageBitmap(cover);
                Bitmap bg = ImageUtils.blur(music.getCover(), ImageUtils.BLUR_RADIUS);
                ivPlayingBg.setImageBitmap(bg);
            }
        }
        initAlbumpic();
        operatingAnim.start();
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reloadAdapter();
    }

    public ObjectAnimator operatingAnim;


    public void initAlbumpic() {
        /**
         * 旋转动画
         */
        LinearInterpolator lin = new LinearInterpolator();

        operatingAnim = ObjectAnimator.ofFloat(civ_cover, "rotation", 0, 360);
        operatingAnim.setDuration(20000);
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
        //专辑视图
        View listView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_player_listview, null);
        recyclerView = (RecyclerView) listView.findViewById(R.id.recyclerView);

        mViewPagerContent = new ArrayList<>(3);

        mViewPagerContent.add(listView);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        viewPager.setAdapter(new MyPagerAdapter(mViewPagerContent));
    }


    public void onPlayerResume() {
        if (playPauseFloating != null)
            updatePlayPauseFloatingButton();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            operatingAnim.resume();
        }
    }

    public void onPlayerPause() {
        if (playPauseFloating != null)
            updatePlayPauseFloatingButton();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            operatingAnim.pause();
        }
    }

    private boolean duetoplaypause = false;

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            playPauseDrawable.transformToPlay(true);
            playPauseDrawable.transformToPause(true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getmPlayService().playPause();
                }
            }, 250);
        }
    };

    public void updatePlayPauseFloatingButton() {
        if (getmPlayService().isPlaying()) {
            playPauseDrawable.transformToPause(false);
        } else {
            playPauseDrawable.transformToPlay(false);
        }
    }

    public void updatePlayMode() {
        play_mode = Preferences.getPlayMode();
        switch (play_mode) {
            case 0:
                skip_mode.setImageResource(R.drawable.ic_repeat_white_24dp);
                ToastUtils.show(getContext(), "列表循环");
                break;
            case 1:
                skip_mode.setImageResource(R.drawable.ic_shuffle_white_24dp);
                ToastUtils.show(getContext(), "随机播放");
                break;
            case 2:
                skip_mode.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                ToastUtils.show(getContext(), "单曲循环");
                break;
        }

    }

    public void updatePageIcon(int current) {
        switch (current) {
            case 0:
                page_icon.setImageResource(R.drawable.page_icon_left);
                break;
            case 1:
                page_icon.setImageResource(R.drawable.page_icon_mid);
                break;
            case 2:
                page_icon.setImageResource(R.drawable.page_icon_right);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updatePageIcon(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    private void reloadAdapter() {
        musicInfos = MainActivity.mPlayService.getMusicList();
        mAdapter = new LocalMusicAdapter((AppCompatActivity) getActivity(), musicInfos);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        operatingAnim.cancel();
    }
}
