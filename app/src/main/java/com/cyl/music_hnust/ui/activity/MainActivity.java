package com.cyl.music_hnust.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.bean.user.User;
import com.cyl.music_hnust.bean.user.UserStatus;
import com.cyl.music_hnust.ui.fragment.DownloadFragment;
import com.cyl.music_hnust.ui.fragment.MainFragment;
import com.cyl.music_hnust.ui.fragment.PlayFragment;
import com.cyl.music_hnust.ui.fragment.PlaylistFragment;
import com.cyl.music_hnust.ui.fragment.SongsFragment;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述 主要的Activity
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPaneLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    ImageView mImageView;
    CircleImageView mAvatarIcon;
    TextView mName;
    TextView mNick;

    private View headerView;
    private static final String TAG = "MainActivity";

    private MainFragment mainFragment;
    private PlayFragment playFragment;
    private PlaylistFragment playlistFragment;
    private DownloadFragment downloadFragment;

    private boolean login_status;
    private Intent intent = null;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initMainPager();
        initPlay();
        //菜单栏的头部控件初始化
        headerView = mNavigationView.getHeaderView(0);
        mImageView = headerView.findViewById(R.id.header_bg);
        mAvatarIcon = headerView.findViewById(R.id.header_face);
        mName = headerView.findViewById(R.id.header_name);
        mNick = headerView.findViewById(R.id.header_nick);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
    }

    @Override
    protected void initData() {
        initNav();
    }


    @Override
    protected void listener() {
        mSlidingUpPaneLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (newState.equals(PanelState.COLLAPSED)) {
                    mSlidingUpPaneLayout.setTouchEnabled(true);
                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                View nowPlayingCard = PlayFragment.topContainer;
                nowPlayingCard.setAlpha(1 - slideOffset);
            }

        });

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
        switch (item.getItemId()) {
            case R.id.nav_menu_music:
                initMainPager();
                break;
            case R.id.nav_menu_playlist:
                initPlaylist();
                break;
            case R.id.nav_menu_download:
                initDownload();
                break;
            case R.id.nav_menu_shake:
                item.setChecked(true);
                if (!login_status) {
                    toLoginActivity();
                } else {
                    toShakeActivity();
                }
                break;
            case R.id.nav_menu_setting:
                toSettingsActivity();
                break;
            case R.id.nav_menu_exit:
                finish();
                break;
        }
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void toLoginActivity() {
        intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void toShakeActivity() {
        intent = new Intent(MainActivity.this, ShakeActivity.class);
        startActivity(intent);
    }

    private void toSettingsActivity() {
        intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }


    private void initMainPager() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            transaction.add(R.id.fragment_container, mainFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(mainFragment);
        transaction.commit();
    }

    private void initPlaylist() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (playlistFragment == null) {
            playlistFragment = new PlaylistFragment();
            transaction.add(R.id.fragment_container, playlistFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(playlistFragment);
        transaction.commit();
    }

    private void initPlay() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (playFragment == null) {
            playFragment = PlayFragment.newInstance();
            transaction.add(R.id.controls_container, playFragment);
        }
        //显示需要显示的fragment
        transaction.show(playFragment);
        transaction.commit();
    }

    private void initDownload() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (downloadFragment == null) {
            downloadFragment = new DownloadFragment();
            transaction.add(R.id.fragment_container, downloadFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(downloadFragment);
        transaction.commit();
    }


    private void hideFragment(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (playlistFragment != null) {
            transaction.hide(playlistFragment);
        }
        if (downloadFragment != null) {
            transaction.hide(downloadFragment);
        }
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
        return (currentFragment instanceof MainFragment || currentFragment instanceof SongsFragment
                || currentFragment instanceof PlaylistFragment || currentFragment instanceof DownloadFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
