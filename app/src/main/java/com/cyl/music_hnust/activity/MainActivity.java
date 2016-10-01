package com.cyl.music_hnust.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.UserCenterMainAcivity;
import com.cyl.music_hnust.fragment.MainFragment;
import com.cyl.music_hnust.fragment.PlayFragment;
import com.cyl.music_hnust.map.BaseMapActivity;
import com.cyl.music_hnust.map.RadarActivity;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.service.OnPlayerListener;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.CoverLoader;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.SystemUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnPlayerListener {


    @Bind(R.id.song_progress_normal)
    ProgressBar mProgressBar;
    TextView tv_title, tv_artist;
    @Bind(R.id.play_control)
    FrameLayout play_control;

    @OnClick(R.id.play_control)
    public void show() {
        showPlayingFragment();
    }

    @Bind(R.id.album)
    ImageView album;

    @Bind(R.id.next_buttom)
    ImageButton next_buttom;

    @OnClick(R.id.next_buttom)
    public void onclik() {
        mPlayService.next();
    }

    @Bind(R.id.play_pause)
    ImageButton play_pause;

    @OnClick(R.id.play_pause)
    public void play_pause() {
        //点击后进入到播放状态
        play_state(true);
        mPlayService.playPause();
    }

    @Bind(R.id.pause_play)
    ImageButton pause_play;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @OnClick(R.id.pause_play)
    public void pause_play() {
        //点击后进入到暂停状态
        play_state(false);
        mPlayService.pause();
    }

    private static DrawerLayout mDrawerLayout;
    private static FloatingActionButton mFloatingActionButton;
    public static PlayService mPlayService;
    private PopupWindow mPopupWindow;
    private ImageView iv_bg;
    private CircleImageView user_face;
    private TextView tv_nick,tv_name;



    private MainFragment mainFragment = null;
    private PlayFragment mPlayFragment = null;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.MyBinder) service).getService();
            mPlayService.setOnPlayEventListener(MainActivity.this);
            init();
            mPlayService.updateMusicList();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        SystemUtils.setSystemBarTransparent(this);
        initView();
    }

    private void initView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        tv_title = (TextView) findViewById(R.id.title);
        tv_artist = (TextView) findViewById(R.id.artist);

        //进度条样式
        mProgressBar.setProgress(0);


        bindService();
        setNavigationView();

        play_control.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }

    @Override
    protected void listener() {

    }

    private void init() {
        initFragment();
        onPlay(mPlayService.getPlayingMusic());
    }


    public static DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    private void initFragment() {
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mainFragment).commit();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        View headerView = mNavigationView.inflateHeaderView(R.layout.header_nav);
        iv_bg = (ImageView) headerView.findViewById(R.id.header_bg);
        user_face = (CircleImageView) headerView.findViewById(R.id.header_face);
        tv_name = (TextView) headerView.findViewById(R.id.header_name);
        tv_nick = (TextView) headerView.findViewById(R.id.header_nick);
        initNav();
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status= UserStatus.getstatus(MainActivity.this);
                Intent intent =null;
                if (status){
                    intent = new Intent(MainActivity.this, UserCenterMainAcivity.class);
                }else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                mDrawerLayout.closeDrawers();
                startActivity(intent);
            }
        });
    }

    private void initNav() {
        Music music = null;
        if (getmPlayService()!=null){
            music = getmPlayService().getPlayingMusic();
        }
        if (music!=null){
            if (music.getCover() != null) {
                iv_bg.setImageBitmap(music.getCover());
            } else {
                Bitmap cover = CoverLoader.getInstance().loadNormal(music.getCoverUri());
                iv_bg.setImageBitmap(cover);
            }
        }
        boolean status= UserStatus.getstatus(this);
        if (status){
            User user = UserStatus.getUserInfo(this);
            if (user.getUser_name()!=null&&user.getUser_name().length()>0){
                tv_name.setText(user.getUser_name());
            }else {
                tv_name.setText("佚名");
            }
            if (user.getUser_email()!=null&&user.getUser_name().length()>0){
                tv_nick.setText(user.getUser_email());
            }
            if (user.getUser_img()!=null){
                ImageLoader.getInstance().displayImage(user.getUser_img(),user_face, ImageUtils.getAlbumDisplayOptions());
            }
        }else {
            tv_name.setText("暂无登录！");
            tv_nick.setText("湖科音乐湖");
        }
    }

    Runnable runnable = null;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_menu_music:
                item.setChecked(true);
                runnable = new Runnable() {
                    public void run() {
                        if (mainFragment == null) {
                            mainFragment = new MainFragment();
                        }
                        switchFragment(mainFragment);
                    }
                };
                break;
            case R.id.nav_menu_com:
                item.setChecked(true);
//
//                runnable = new Runnable() {
//                    public void run() {
//                        CommunityFragment communityFragment = new CommunityFragment();
//                        switchFragment(communityFragment);
//                    }
//                };

                Intent intent4 = new Intent(this, CommunityActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_menu_msg:
                item.setChecked(true);
                break;
            case R.id.nav_menu_map:
                item.setChecked(true);
                Intent intent1 = new Intent(this, BaseMapActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_menu_near:
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, RadarActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_setting:
                Intent intent3 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_menu_exit:
                finish();
                break;
        }
        if (runnable != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, 350);

        }
        return true;


    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

    }

    //更新进度条
    @Override
    public void onUpdate(int progress) {
        mProgressBar.setProgress(progress);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onUpdate(progress);
        }
    }

    //切换歌曲
    @Override
    public void onChange(Music music) {
        onPlay(music);
        initNav();
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlay(music);
        }
    }

    //播放音乐
    private void onPlay(Music music) {
        if (music == null) {
            return;
        }
        play_state(true);
        if (music.getCover() != null) {
            album.setImageBitmap(music.getCover());
        } else {
            Bitmap cover = CoverLoader.getInstance().loadThumbnail(music.getCoverUri());
            album.setImageBitmap(cover);
        }
        tv_title.setText(music.getTitle());
        tv_artist.setText(music.getArtist());
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress(0);

    }


    @Override
    public void onPlayerPause() {
        play_state(false);
        initNav();
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerPause();
        }

    }

    @Override
    public void onPlayerResume() {
        play_state(true);
        initNav();
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerResume();
        }
        play_pause.setVisibility(View.GONE);
        pause_play.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    //返回键
    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    //播放列表隐藏
    Boolean isPlayFragmentShow = false;

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commit();
        isPlayFragmentShow = false;
    }

    //播放列表显示
    private void showPlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new PlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commit();
        isPlayFragmentShow = true;
    }

    View view = null;


    //底部播放按钮状态显示控制
    private void play_state(boolean isPlaying) {
        if (isPlaying) {
            pause_play.setVisibility(View.VISIBLE);
            play_pause.setVisibility(View.GONE);
        } else {
            pause_play.setVisibility(View.GONE);
            play_pause.setVisibility(View.VISIBLE);
        }
    }

    public static PlayService getmPlayService() {
        return mPlayService;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNav();
    }
}
