package com.cyl.music_hnust.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.fragment.MainFragment;
import com.cyl.music_hnust.fragment.MyFragment;
import com.cyl.music_hnust.fragment.PlayFragment;
import com.cyl.music_hnust.fragment.TestFragment;
import com.cyl.music_hnust.map.BaseMapActivity;
import com.cyl.music_hnust.map.NearActivity;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.service.OnPlayerListener;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 描述
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, OnPlayerListener{



    ProgressBar mProgressBar;
    TextView tv_title,tv_artist;
    @Bind(R.id.play_control)
    FrameLayout play_control;
    @OnClick(R.id.play_control)
    public void show(){
        showPlayingFragment();
    }


    @Bind(R.id.album)
    ImageView album;

    @Bind(R.id.next_buttom)
    ImageButton next_buttom;
    @OnClick(R.id.next_buttom)
    public void onclik(){
        mPlayService.next();
    }

    @Bind(R.id.play_pause)
    ImageButton play_pause;
    @OnClick(R.id.play_pause)
    public void play_pause(){
        //点击后进入到播放状态
        play_state(true);
        mPlayService.playPause();
    }
    @Bind(R.id.pause_play)
    ImageButton pause_play;
    @OnClick(R.id.pause_play)
    public void pause_play(){
        //点击后进入到暂停状态
        play_state(false);
        mPlayService.pause();
    }

    private static DrawerLayout mDrawerLayout;
    private static FloatingActionButton mFloatingActionButton;
    public static PlayService mPlayService;
    MainFragment mainFragment =null;
    MyFragment myFragment =null;
    TestFragment testFragment =null;
    PlayFragment mPlayFragment =null;

    private ServiceConnection mServiceConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.MyBinder) service).getService();
            mPlayService.setOnPlayEventListener(MainActivity.this);
            mPlayService.updateMusicList();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;


    @Override
    public int getLayoutId() {
        bindService();
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        tv_title= (TextView) findViewById(R.id.title);
        tv_artist= (TextView) findViewById(R.id.artist);
        mProgressBar= (ProgressBar) findViewById(R.id.song_progress_normal);

    }
    @Override
    protected void initDatas() {
        //进度条样式
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mProgressBar.getLayoutParams();
        mProgressBar.measure(0, 0);
        layoutParams.setMargins(0, -(mProgressBar.getMeasuredHeight() / 2), 0, 0);
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setProgress(0);
        mProgressBar.setBackgroundColor(Color.WHITE);


        setNavigationView();
        initFragment();
    }
    @Override
    protected void listener() {
    }

    public static DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    private void initFragment() {
        MainFragment mainFragment = new MainFragment();
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
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    Runnable runnable = null;
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()){
            case R.id.nav_menu_music:
                item.setChecked(true);
                runnable= new Runnable() {
                    public void run() {
                        if (mainFragment==null){
                            mainFragment = new MainFragment();
                        }
                        switchFragment(mainFragment);
                    }
                };
                break;
            case R.id.nav_menu_com:
                item.setChecked(true);
                runnable= new Runnable() {
                    public void run() {
                        if (myFragment==null) {
                            myFragment = new MyFragment();
                        }
                        switchFragment(mainFragment);
                    }
                };
                break;
            case R.id.nav_menu_msg:
                item.setChecked(true);
                runnable= new Runnable() {
                    public void run() {
                        if (testFragment==null) {
                            testFragment = new TestFragment().newInstance();
                        }
                        switchFragment(mainFragment);
                    }
                };
                break;
            case R.id.nav_menu_map:
                item.setChecked(true);
                Intent intent1 = new Intent(this, BaseMapActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_menu_near:
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this,NearActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_setting:
                Intent intent3 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_menu_exit:
                finish();
                break;
        }
        if (runnable!=null){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_shuffle:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onUpdate(int progress) {
        mProgressBar.setProgress(progress);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onUpdate(progress);
        }
    }

    @Override
    public void onChange(Music music) {
        onPlay(music);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlay(music);
        }
    }

    private void onPlay(Music music) {
        if (music == null) {
            return;
        }
        play_state(true);
        ImageLoader.getInstance().displayImage(ImageUtils.getAlbumArtUri(Long.parseLong(music.getCoverUri())).toString(),album, ImageUtils.getAlbumDisplayOptions());
        tv_title.setText(music.getTitle());
        tv_artist.setText(music.getArtist());
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress(0);
    }

    @Override
    public void onPlayerPause() {
        play_state(false);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerPause();
        }

    }

    @Override
    public void onPlayerResume() {
        play_state(true);
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
    //底部播放按钮状态显示控制
    private void play_state(boolean isPlaying){
        if (isPlaying){
            pause_play.setVisibility(View.VISIBLE);
            play_pause.setVisibility(View.GONE);
        }else {
            pause_play.setVisibility(View.GONE);
            play_pause.setVisibility(View.VISIBLE);
        }
    }


    public static PlayService getmPlayService() {
        return mPlayService;
    }
}
