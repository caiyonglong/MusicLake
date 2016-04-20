package com.cyl.music_hnust;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.MyViewPagerAdapter;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.fragment.MusicListFragment;
import com.cyl.music_hnust.http.HttpByGet;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.CommonUtils;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/3/12.
 */
public class PlayerActivity extends AppCompatActivity {
    private ImageButton mModeImageButton, mFrontImageButton, mPauseImageButton,
            mNextImageButton, mFavorImageButton, title_left;
    private ImageView page_icon, mIvBg;
    private TextView tv_songName, tv_singerName, tv_curcentTime, tv_allTime;
    private ViewPager viewpager_player;
    private SeekBar seekBar1;// 播放进度条
    private View mControllerRoot; //控制模块
    public static MusicPlayService mService;
    private MyViewPagerAdapter myViewPagerAdapter;

    private int page[] = {R.mipmap.page_icon_left, R.mipmap.page_icon_mid,
            R.mipmap.page_icon_right};
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    String[] mTitles;

    private ArrayList<ObjectAnimator> mAnimList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_player);

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
      //  initLrc(mService.getPath());
        initAlbum();
        initFragment();
    }

    private void initAlbum() {


        if (mService.getSong().isFavorite()&&mService.getSong()!=null)
            mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_star_style);
        else
            mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_nostar_style);


    }

    private void initTitle() {
        MusicInfo song=null;
        if (mService.getSong()!=null) {
            song = mService.getSong();
        }
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
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
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

        mModeImageButton = (ImageButton) findViewById(R.id.activity_player_ib_mode);
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mode = prefs.getString("mode_list", "0");
        if ("0".equals(mode)) {
            mModeImageButton.setBackgroundResource(R.drawable.player_btn_mode_normal_style);

        } else if ("1".equals(mode)) {
            mModeImageButton.setBackgroundResource(R.drawable.player_btn_mode_random_style);
        } else if ("2".equals(mode)) {
            mModeImageButton.setBackgroundResource(R.drawable.player_btn_mode_repeat_style);
        }

        // 启动
        handler.post(updateThread);
    }

    private Bitmap mBgBitmap;
    private Bitmap mAlbumBitmap;
    MusicInfo song=null;

    public void initBackGround() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap original = null;
                if (mService.getSongs() != null&&mService.getCurrentListItme()!=-1) {
                    song = mService.getSongs().get(mService.getCurrentListItme());
                }
                if (song == null) {

                }
                if (song != null
                        && !TextUtils.isEmpty(song.getAlbumPic())) {
                    if (HttpByGet.isURL(song.getAlbumPic())) {
                        final Bitmap[] bitmap = {null};
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bitmap[0] = HttpByGet.getHttpBitmap(song.getAlbumPic());
                            }
                        }).start();
                        original = bitmap[0];
                    } else {
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

            if (mService.mMediaPlayer!=null){
                seekBar1.setMax(mService.getDuration());
                // 获得歌曲现在播放位置并设置成播放进度条的值
                seekBar1.setProgress(mService.getCurrent());


                tv_curcentTime.setText(formatTime(mService.getCurrent()));
                tv_allTime.setText(formatTime(mService.getDuration()));
            }else {
                seekBar1.setMax(0);
                // 获得歌曲现在播放位置并设置成播放进度条的值
                seekBar1.setProgress(0);


                tv_curcentTime.setText(formatTime(0));
                tv_allTime.setText(formatTime(0));
            }

            // 每次延迟100毫秒再启动线程
            handler.postDelayed(updateThread, 100);
        }
    };

    private void setListener() {
        // 暂停or开始
        mPauseImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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
        mModeImageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String mode = prefs.getString("mode_list", "0");
                if ("0".equals(mode)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("mode_list", "1");
                    mModeImageButton.setBackgroundResource(R.drawable.player_btn_mode_random_style);
                    editor.commit();
                    ToastUtil.show(getApplicationContext(), "随机播放");
                } else if ("1".equals(mode)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("mode_list", "2");
                    mModeImageButton.setBackgroundResource(R.drawable.player_btn_mode_repeat_style);
                    editor.commit();
                    ToastUtil.show(getApplicationContext(), "单曲循环");
                } else if ("2".equals(mode)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("mode_list", "0");
                    mModeImageButton.setBackgroundResource(R.drawable.player_btn_mode_normal_style);
                    editor.commit();
                    ToastUtil.show(getApplicationContext(), "顺序播放");
                }

            }
        });
        mFavorImageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DBDao dbDao = new DBDao(getApplicationContext());
                if (mService.getSong()!=null) {
                    if (!mService.getSong().isFavorite()) {
                        dbDao.update(mService.getSong().getName(), true);
                        mService.getSong().setFavorite(true);
                        mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_star_style);
                    } else {
                        dbDao.update(mService.getSong().getName(), false);
                        mService.getSong().setFavorite(false);
                        mFavorImageButton.setBackgroundResource(R.drawable.player_btn_favorite_nostar_style);
                    }
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


}
