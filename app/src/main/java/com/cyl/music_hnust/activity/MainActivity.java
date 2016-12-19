package com.cyl.music_hnust.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.map.BaseMapActivity;
import com.cyl.music_hnust.fragment.DownloadFragment;
import com.cyl.music_hnust.fragment.MainFragment;
import com.cyl.music_hnust.fragment.PlayFragment;
import com.cyl.music_hnust.fragment.PlaylistFragment;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.user.User;
import com.cyl.music_hnust.model.user.UserStatus;
import com.cyl.music_hnust.service.OnPlayerListener;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.Preferences;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.view.PlayPauseButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述 主要的Activity
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnPlayerListener{

    //进度条
    @Bind(R.id.song_progress_normal)
    ProgressBar mProgressBar;
    //歌名
    @Bind(R.id.title)
    TextView tv_title;
    //歌手
    @Bind(R.id.artist)
    TextView tv_artist;
    //底部
    @Bind(R.id.play_control)
    RelativeLayout play_control;

    //底部点击事件
    @OnClick(R.id.play_control)
    public void show() {
        showPlayingFragment();
    }

    //音乐专辑图片
    @Bind(R.id.album)
    ImageView album;
    //下一首按钮
    @Bind(R.id.next_buttom)
    ImageButton next_buttom;

    //下一首点击事件
    @OnClick(R.id.next_buttom)
    public void onclik() {
        mPlayService.next();
    }

    //暂停or播放按钮
    @Bind(R.id.play_pause)
    PlayPauseButton mPlayPause;
    //暂停播放按钮的点击效果
    @Bind(R.id.playpausewrapper)
    View playpausewrapper;

    /**
     * 播放状态
     */
    Boolean duetoplaypause = false;

    @OnClick(R.id.playpausewrapper)
    public void playpausewrapper() {
        //点击后进入到播放状态
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
                playOrPause();
            }
        }, 200);
    }

    /**
     * 播放OR暂停
     */
    public void playOrPause() {
        try {
            if (mPlayService != null) {
                if (mPlayService.isPlaying()) {
                    mPlayService.pause();
                } else {
                    mPlayService.playPause();
                }
            }
        } catch (final Exception ignored) {
        }
    }

    /**
     * 更新播放按钮状态
     */
    public void updateState() {
        if (mPlayService.isPlaying()) {
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            }
        } else {
            if (mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
        }
    }


    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;


    public static PlayService mPlayService;
    private ImageView iv_bg;
    private CircleImageView user_face;
    private TextView tv_nick, tv_name;

    boolean login_status;

    //跳转Intent
    private Intent intent = null;

    boolean on = Preferences.isNightMode();

    PlayFragment mPlayFragment = null;
    PlaylistFragment playlistFragment = null;

    static MainActivity mainActivity = null;

    //替换Mainfragment
    Runnable main = new Runnable() {
        @Override
        public void run() {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, mainFragment).commitAllowingStateLoss();
        }
    };
    //替换Mainfragment
    Runnable playlist = new Runnable() {
        @Override
        public void run() {
            playlistFragment = new PlaylistFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, playlistFragment).commitAllowingStateLoss();
        }
    };
    //替换Mainfragment
    Runnable download = new Runnable() {
        @Override
        public void run() {
            DownloadFragment fragment = new DownloadFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
        }
    };


    /**
     * service服务连接
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.MyBinder) service).getService();
            mPlayService.setOnPlayEventListener(MainActivity.this);
            mPlayService.updateMusicList();
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        //初始化菜单栏
        setNavigationView();
    }

    @Override
    protected void initData() {
        bindService();
    }

    @Override
    protected void listener() {

    }

    /**
     * 服务绑定后，初始化视图。
     */
    private void init() {
        //加载Mainfragment
        main.run();
        //更新底部控制栏
        updateControl(mPlayService.getPlayingMusic());
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        intent = new Intent(this, PlayService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 初始化菜单栏
     */
    private void setNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        //菜单栏的头部控件初始化
        View headerView = mNavigationView.inflateHeaderView(R.layout.header_nav);
        iv_bg = (ImageView) headerView.findViewById(R.id.header_bg);
        user_face = (CircleImageView) headerView.findViewById(R.id.header_face);
        tv_name = (TextView) headerView.findViewById(R.id.header_name);
        tv_nick = (TextView) headerView.findViewById(R.id.header_nick);

        initNav();
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_status = UserStatus.getstatus(MainActivity.this);
                if (login_status) {
                    intent = new Intent(MainActivity.this, UserCenterAcivity.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                mDrawerLayout.closeDrawers();
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化菜单栏的头部
     */
    private void initNav() {
        login_status = UserStatus.getstatus(this);
        if (login_status) {
            User user = UserStatus.getUserInfo(this);
            if (user.getUser_name() != null && user.getUser_name().length() > 0) {
                tv_name.setText(user.getUser_name());
            } else if (user.getUser_email() != null && user.getUser_email().length() > 0) {
                tv_name.setText(user.getUser_email());
            } else {
                tv_name.setText(user.getUser_id());
            }
            if (user.getNick() != null && user.getNick().length() > 0) {
                tv_nick.setText(user.getNick());
            } else {
                tv_nick.setText("湖科音乐湖");
            }
            if (user.getUser_img() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImageDir() + user.getUser_id() + ".png");
                if (bitmap != null) {
                    user_face.setImageBitmap(bitmap);
                    bitmap.recycle();
                } else
                    ImageLoader.getInstance().displayImage(user.getUser_img(), user_face, ImageUtils.getAlbumDisplayOptions());
            }
        } else {
            user_face.setImageResource(R.drawable.ic_account_circle);
            tv_name.setText("湖科音乐湖");
            tv_nick.setText("未登录?去登录/注册吧!");
        }
    }

    /**
     * 菜单条目点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_menu_music:
                item.setChecked(true);
                mHandler.postDelayed(main, 100);
                break;
            case R.id.nav_menu_playlist:
                item.setChecked(true);
                mHandler.postDelayed(playlist, 100);
                break;
            case R.id.nav_menu_download:
                item.setChecked(true);
                mHandler.postDelayed(download, 100);
                break;
            case R.id.nav_menu_shake:
                item.setChecked(true);
                if (login_status) {
                    intent = new Intent(MainActivity.this, ShakeActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_menu_near:
                if (login_status) {
                    intent = new Intent(MainActivity.this, BaseMapActivity.class);
                    intent.putExtra("fromActivity", "Near");
                    startActivity(intent);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_menu_setting:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_exit:
                finish();
                break;
            case R.id.nav_menu_help:
                intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:643872807@qq.com");
                intent.setData(data);
                startActivity(intent);
                break;
        }
        return true;


    }

    /**
     * 更新歌曲播放进度条
     *
     * @param progress
     */
    @Override
    public void onUpdate(int progress) {
        mProgressBar.setProgress(progress);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onUpdate(progress);
        }
    }

    /**
     * 切换音乐
     * 音乐播放
     *
     * @param music
     */
    @Override
    public void onChange(Music music) {
        //更新底部控制栏
        updateControl(music);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlay(music);
        }
    }

    /**
     * 更新底部控制栏
     *
     * @param music
     */
    private void updateControl(Music music) {
        if (music == null) {
            return;
        }
        updateState();
        //歌曲专辑图片更新
        if (music.getType() == Music.Type.LOCAL) {
            ImageLoader.getInstance().displayImage(ImageUtils.getAlbumArtUri(music.getAlbumId()).toString(),
                    album,
                    new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .showImageOnFail(R.drawable.default_cover)
                            .showImageForEmptyUri(R.drawable.default_cover)
                            .showImageOnLoading(R.drawable.default_cover)
                            .build());
            ImageLoader.getInstance().displayImage(ImageUtils.getAlbumArtUri(music.getAlbumId()).toString(),
                    iv_bg,
                    new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .showImageOnFail(R.drawable.default_cover)
                            .showImageForEmptyUri(R.drawable.default_cover)
                            .showImageOnLoading(R.drawable.default_cover)
                            .build());

        } else {
            if (music.getCover() != null) {
                album.setImageBitmap(music.getCover());
                iv_bg.setImageBitmap(music.getCover());
            } else {
                album.setImageResource(R.drawable.default_cover);
                iv_bg.setImageResource(R.drawable.bg_header);
            }
        }
        tv_title.setText(FileUtils.getTitle(music.getTitle()));
        tv_artist.setText(FileUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum()));
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress(0);
        //按钮颜色
        mPlayPause.setColor(Color.parseColor("#259b24"));
    }


    /**
     * 播放暂停
     */
    @Override
    public void onPlayerPause() {
        updateState();
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerPause();
        }

    }

    /**
     * 恢复播放
     */
    @Override
    public void onPlayerResume() {
        updateState();
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerResume();
        }
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

    //播放列表是否隐藏
    Boolean isPlayFragmentShow = false;

    /**
     * 隐藏播放列表
     */
    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    /**
     * 播放列表显示
     */
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
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    View view = null;


    public static PlayService getmPlayService() {
        return mPlayService;
    }

    /**
     * 生命周期onresume
     */
    @Override
    protected void onResume() {
        super.onResume();
        mainActivity = this;
        on = Preferences.isNightMode();
        initNav();
    }


    /**
     * 菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    /**
     * 菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_search:
                intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Extras.TO_PLAYLISTDETAIL) {
            if (playlistFragment != null) {
                playlistFragment.updatePlaylists();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null)
            unbindService(mServiceConnection);
    }
}
