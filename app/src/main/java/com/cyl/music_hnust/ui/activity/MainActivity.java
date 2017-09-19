package com.cyl.music_hnust.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.user.User;
import com.cyl.music_hnust.model.user.UserStatus;
import com.cyl.music_hnust.ui.activity.map.BaseMapActivity;
import com.cyl.music_hnust.ui.fragment.DownloadFragment;
import com.cyl.music_hnust.ui.fragment.LocalFragment;
import com.cyl.music_hnust.ui.fragment.MainFragment;
import com.cyl.music_hnust.ui.fragment.PlayFragment;
import com.cyl.music_hnust.ui.fragment.PlaylistFragment;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述 主要的Activity
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPaneLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    ImageView mImageView;
    CircleImageView mAvatarIcon;
    TextView mNick, mName;

    boolean login_status;
    private static final String TAG = "MainActivity";
    private Intent intent = null;
    PlaylistFragment playlistFragment = null;

    //替换Mainfragment
    Runnable main = new Runnable() {
        @Override
        public void run() {
            MainFragment mainFragment = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commitAllowingStateLoss();
        }
    };


    //替换Mainfragment
    Runnable detail = new Runnable() {
        @Override
        public void run() {
            PlayFragment playFragment = PlayFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.controls_container, playFragment).commitAllowingStateLoss();
        }
    };

    //替换Mainfragment
    Runnable playlist = new Runnable() {
        @Override
        public void run() {
            playlistFragment = new PlaylistFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playlistFragment).commitAllowingStateLoss();
        }
    };
    //替换Mainfragment
    Runnable download = new Runnable() {
        @Override
        public void run() {
            DownloadFragment fragment = new DownloadFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //初始化菜单栏
        setNavigationView();
    }

    @Override
    protected void initData() {
        main.run();
        detail.run();
        mSlidingUpPaneLayout.addPanelSlideListener(new PanelSlideListener() {

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (newState.equals(PanelState.COLLAPSED)) {

                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                View nowPlayingCard = PlayFragment.topContainer;
                nowPlayingCard.setAlpha(1 - slideOffset);
            }


        });
        mSlidingUpPaneLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
            }
        });


    }

    @Override
    protected void listener() {

    }


    /**
     * 初始化菜单栏
     */
    private void setNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        //菜单栏的头部控件初始化
        View headerView = mNavigationView.inflateHeaderView(R.layout.header_nav);
        mImageView = headerView.findViewById(R.id.header_bg);
        mAvatarIcon = headerView.findViewById(R.id.header_face);
        mName = headerView.findViewById(R.id.header_name);
        mNick = headerView.findViewById(R.id.header_nick);

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
                mName.setText(user.getUser_name());
            } else if (user.getUser_email() != null && user.getUser_email().length() > 0) {
                mName.setText(user.getUser_email());
            } else {
                mName.setText(user.getUser_id());
            }
            if (user.getNick() != null && user.getNick().length() > 0) {
                mNick.setText(user.getNick());
            } else {
                mNick.setText("湖科音乐湖");
            }
            if (user.getUser_img() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImageDir() + user.getUser_id() + ".png");
                if (bitmap != null) {
                    mAvatarIcon.setImageBitmap(bitmap);
                    bitmap.recycle();
                } else
                    ImageLoader.getInstance().displayImage(user.getUser_img(), mAvatarIcon, ImageUtils.getAlbumDisplayOptions());
            }
        } else {
            mAvatarIcon.setImageResource(R.drawable.ic_account_circle);
            mName.setText("湖科音乐湖");
            mNick.setText("未登录?去登录/注册吧!");
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


    //返回键
    @Override
    public void onBackPressed() {
        if (mSlidingUpPaneLayout != null &&
                (mSlidingUpPaneLayout.getPanelState() == PanelState.EXPANDED || mSlidingUpPaneLayout.getPanelState() == PanelState.ANCHORED)) {
            mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 生命周期onresume
     */
    @Override
    protected void onResume() {
        super.onResume();
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
                if (isNavigatingMain()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
                break;
            case R.id.action_search:
                intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNavigatingMain() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return (currentFragment instanceof MainFragment || currentFragment instanceof LocalFragment
                || currentFragment instanceof PlaylistFragment || currentFragment instanceof DownloadFragment);
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
    }
}
