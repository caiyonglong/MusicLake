package com.cyl.music_hnust;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.MyViewPagerAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.fragment.MusicFragment;
import com.cyl.music_hnust.fragment.MusicListFragment;
import com.cyl.music_hnust.fragment.MyFragment;
import com.cyl.music_hnust.http.HttpByGet;
import com.cyl.music_hnust.lyric.LrcProcess;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.CommonUtils;
import com.cyl.music_hnust.utils.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/3/12.
 */
public class PlayerActivity extends AppCompatActivity {
    private ImageButton mFrontImageButton, mPauseImageButton,
            mNextImageButton, mFavorImageButton, title_left;
    private ImageView page_icon,mIvBg;
    private TextView tv_songName, tv_singerName, tv_curcentTime, tv_allTime;
    private ViewPager viewpager_player;
    private SeekBar seekBar1;// 播放进度条
    private View mControllerRoot; //控制模块
    public static MusicPlayService mService;
    private MyViewPagerAdapter myViewPagerAdapter;

    private int page[] = { R.mipmap.page_icon_left, R.mipmap.page_icon_mid,
            R.mipmap.page_icon_right };
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    String[] mTitles;

    private ArrayList<ObjectAnimator> mAnimList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_player);
     //   MyApplication application = (MyApplication) getApplication();
        mService = MyActivity.application.getmService();
        //广播
        playerReceiver = new PlayerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_ACTION);
        registerReceiver(playerReceiver, intentFilter);

       // initFragment();
        initView();
        setListener();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                mControllerRoot.setAlpha(0f);
                mAnimList = new ArrayList<>();
            }

            @Override
            protected Boolean doInBackground(Void... params) {


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                if (!b) return;
                init();
                initPlayMode();
                translateAnim(mControllerRoot);
            }
        }.execute();
    }

    private void initPlayMode() {
    }

    private void init() {
        initTitle();
        initBackGround();
        initLrc(mService.getPath());
        initAlbum();
        initFragment();
    }

    private void initAlbum() {

        if (mService.getSong().isFavorite())
            mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_star_style);
        else
            mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_nostar_style);


    }

    private void initTitle() {
        MusicInfo song = mService.getSong();
        tv_songName.setText(song == null ? "湖科音乐" : song.getName() + " ");
        tv_singerName.setText(song == null ? "属于我的音乐"
                : song.getArtist() + " " + " - " + song.getAlbum() + " ");
        alphaAnim(tv_songName, 200);
        alphaAnim(tv_singerName, 400);
    }

    private void initFragment() {
        mTitles = getResources().getStringArray(R.array.player_titles);
        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            MusicListFragment mFragment = new MusicListFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(i, mFragment);

        }
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),mTitles,mFragments);
        viewpager_player.setAdapter(myViewPagerAdapter);
        viewpager_player.setCurrentItem(1);
        viewpager_player.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page_icon.setImageResource(page[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initView() {

        mFrontImageButton = (ImageButton) findViewById(R.id.activity_player_ib_previous);
        mPauseImageButton = (ImageButton) findViewById(R.id.activity_player_ib_play);
        mNextImageButton = (ImageButton) findViewById(R.id.activity_player_ib_next);
        mFavorImageButton = (ImageButton) findViewById(R.id.activity_player_ib_favorite);
        title_left = (ImageButton) findViewById(R.id.title_left);

        tv_songName = (TextView) findViewById(R.id.tv_song_name);
        tv_singerName = (TextView) findViewById(R.id.tv_artist);
        tv_curcentTime = (TextView) findViewById(R.id.activity_player_tv_time_current);
        tv_allTime = (TextView) findViewById(R.id.activity_player_tv_time_total);
        seekBar1 = (SeekBar) findViewById(R.id.activity_player_seek);
        page_icon = (ImageView) findViewById(R.id.pageicon);
        mIvBg = (ImageView) findViewById(R.id.iv_bg);

        mControllerRoot = findViewById(R.id.controller_root);

        viewpager_player = (ViewPager) findViewById(R.id.viewpager_player);



        // 启动
        handler.post(updateThread);
    }

    private Bitmap mBgBitmap;
    private Bitmap mAlbumBitmap;

    public void initBackGround() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap original = null;
                final MusicInfo song = mService.getSongs().get(mService.getCurrentListItme());
                if (song == null) {
                }
                if (song != null
                        && !TextUtils.isEmpty(song.getAlbumPic()) ) {
                    if (HttpByGet.isURL(song.getAlbumPic())){
                        final Bitmap[] bitmap = {null};
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bitmap[0] =HttpByGet.getHttpBitmap(song.getAlbumPic());
                            }
                        }).start();
                        original =bitmap[0];
                    }else {
                        original = CommonUtils.scaleBitmap(getApplicationContext(), song.getAlbumPic());
                    }

                }
                if (original == null) {
                    original = BitmapFactory.decodeResource(getResources(), R.drawable.base_bg);
                }
                Bitmap result = null;
                try {
                    result = CommonUtils.doBlur(original, 50, false);
                } catch (Error e) {
                    e.printStackTrace();
                }
                original.recycle();
                return result;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                recycleBitmap(mIvBg, mBgBitmap);
                mBgBitmap = bitmap;
                mIvBg.setImageBitmap(mBgBitmap);
                alphaAnim(mIvBg, 0);
            }
        }.execute();
    }
    private void recycleBitmap(ImageView iv, Bitmap bitmap) {
        if (bitmap != null
                && !bitmap.isRecycled()) {
            iv.setImageBitmap(null);
            bitmap.recycle();
        }
    }

    private void alphaAnim(final View view, int delay) {
        alphaAnim(view, 1000, delay);
    }

    private void alphaAnim(final View view, int duration, int delay) {
        //if (mLastSongId == mCurSongId) return;
        view.setAlpha(0.0f);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        animator.setDuration(duration);
        animator.setStartDelay(delay);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        mAnimList.add(animator);
    }

    private void translateAnim(final View view) {
        alphaAnim(view, 200, 500);
        view.post(new Runnable() {
            @Override
            public void run() {
                view.setTranslationY(view.getHeight());
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0.0f);
                animator.setDuration(250);
                animator.setStartDelay(500);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.start();

            }
        });
    }

    private static Handler handler = new Handler();
    Runnable updateThread = new Runnable() {
        public void run() {
            // 获得歌曲的长度并设置成播放进度条的最大值
            seekBar1.setMax(mService.getDuration());
            // 获得歌曲现在播放位置并设置成播放进度条的值
            seekBar1.setProgress(mService.getCurrent());


            tv_curcentTime.setText(formatTime(mService.getCurrent()));
            tv_allTime.setText(formatTime(mService.getDuration()));
            // 每次延迟100毫秒再启动线程
            handler.postDelayed(updateThread, 100);
        }
    };

    private void setListener() {
        // 暂停or开始
        mPauseImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                if (mService.isPlay()) {
//                    MusicListFragment.iv_album.setAnimation(MusicListFragment.operatingAnim);
//                } else {
//                    MusicListFragment.operatingAnim.cancel();
//                 //   MusicListFragment.iv_album.setAnimation(MusicListFragment.operatingAnim);
//                }
//                mService.pausePlay();
                Intent it = new Intent(CTL_ACTION);
                it.putExtra("control", 1);
                sendBroadcast(it);
            }
        });
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 下一首
        mNextImageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //mService.nextMusic();
                Intent it = new Intent(CTL_ACTION);
                it.putExtra("control", 3);
                sendBroadcast(it);
            }
        });
        // 上一首
        mFrontImageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //mService.frontMusic();
                Intent it = new Intent(CTL_ACTION);
                it.putExtra("control", 2);
                sendBroadcast(it);
            }
        });
        mFavorImageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DBDao dbDao = new DBDao(getApplicationContext());
                if (!mService.getSong().isFavorite()) {
                    dbDao.update(mService.getSong().getName(),true);
                    mService.getSong().setFavorite(true);
                    mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_star_style);
                }
                else{
                    dbDao.update(mService.getSong().getName(),false);
                    mService.getSong().setFavorite(false);
                    mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_nostar_style);
                }

            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // fromUser判断是用户改变的滑块的值
                if (fromUser == true) {
                    mService.movePlay(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * 格式化时间，将其变成00:00的形式
     */
    public String formatTime(int time) {
        int secondSum = time / 1000;
        int minute = secondSum / 60;
        int second = secondSum % 60;

        String result = "";
        if (minute < 10)
            result = "0";
        result = result + minute + ":";
        if (second < 10)
            result = result + "0";
        result = result + second;

        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        recycleBitmap(mIvBg, mBgBitmap);
        unregisterReceiver(playerReceiver);
    }

    PlayerReceiver playerReceiver;

    public static final String CTL_ACTION = "hk.music.action.CTL_ACTION"; //播放控制
    public static final String UPDATE_ACTION = "hk.music.action.UPDATE_ACTION"; //更新UI

    private class PlayerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int update = intent.getIntExtra("update", -1);
            int current = intent.getIntExtra("current", -1);
            String name = intent.getStringExtra("name");
            String artist = intent.getStringExtra("artist");
            if (current >= 0) {
                initTitle();
            }
            switch (update) {
                case 1://播放
                    init();
                    MusicListFragment.iv_album.setAnimation(MusicListFragment.operatingAnim);
                    mPauseImageButton.setBackgroundResource(R.drawable.player_btn_pause_style);
                    break;
                case 2: //暂停
                    init();
                    MusicListFragment.operatingAnim.cancel();
                    mPauseImageButton.setBackgroundResource(R.drawable.player_btn_play_style);
                    break;
            }
        }
    }


    public LrcProcess mLrcProcess;

    public void initLrc(String path) {
        // /////////////////////// 初始化歌词配置 /////////////////////// //

        Log.e("初始化歌词配置",path+"====");
//        mLrcProcess = new LrcProcess();
//        // 读取歌词文件
//        mLrcProcess.readLRC(path);
//        // 传回处理后的歌词文件
//        lrcList = mLrcProcess.getLrcContent();
////        Log.e("lrcList",lrcList.size()+"====");
////        if (lrcList!=null) {
//            MusicListFragment.lyric.setSentenceEntities(lrcList);
//            // 切换带动画显示歌词
//            MusicListFragment.lyric.setAnimation(AnimationUtils.loadAnimation(
//                    getApplicationContext(), R.anim.alpha_z));
//            // 启动线程
//            mHandler.post(mRunnable);
////        }
        // /////////////////////// 初始化歌词配置 /////////////////////// //
    }


    Handler mHandler = new Handler();
    // 歌词滚动线程
    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            MusicListFragment.lyric.SetIndex(LrcIndex());
            MusicListFragment.lyric.invalidate();
            mHandler.postDelayed(mRunnable, 100);
        }
    };
    // 创建对象
    private List<LrcProcess.LrcContent> lrcList = new ArrayList<LrcProcess.LrcContent>();
    // 初始化歌词检索值
    private int index = 0;
    // 初始化歌曲播放时间的变量
    private int CurrentTime = 0;
    // 初始化歌曲总时间的变量
    private int CountTime = 0;

    /**
     * 歌词同步处理类
     */
    public int LrcIndex() {
        if (mService.getmMediaPlayer().isPlaying()) {
            // 获得歌曲播放在哪的时间
            CurrentTime = mService.getmMediaPlayer().getCurrentPosition();
            // 获得歌曲总时间长度
            CountTime = mService.getmMediaPlayer().getDuration();
        }
        if (CurrentTime < CountTime) {

            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (CurrentTime < lrcList.get(i).getLrc_time() && i == 0) {
                        index = i;
                    }
                    if (CurrentTime > lrcList.get(i).getLrc_time()
                            && CurrentTime < lrcList.get(i + 1).getLrc_time()) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && CurrentTime > lrcList.get(i).getLrc_time()) {
                    index = i;
                }
            }
        }
        return index;
    }


}
