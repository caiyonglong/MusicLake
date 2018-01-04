package com.cyl.musiclake.ui.main;

import android.content.Intent;
import android.os.Handler;
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

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.login.LoginActivity;
import com.cyl.musiclake.ui.login.UserCenterAcivity;
import com.cyl.musiclake.ui.login.UserContract;
import com.cyl.musiclake.ui.login.UserPresenter;
import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.ui.map.ShakeActivity;
import com.cyl.musiclake.ui.music.activity.SearchActivity;
import com.cyl.musiclake.ui.music.fragment.DownloadFragment;
import com.cyl.musiclake.ui.music.fragment.PlayFragment;
import com.cyl.musiclake.ui.music.fragment.PlaylistFragment;
import com.cyl.musiclake.ui.music.fragment.SongsFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cyl.musiclake.ui.music.fragment.PlayFragment.topContainer;

/**
 * 描述 主要的Activity
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, UserContract.View {

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

    private Runnable runnable;
    private boolean login_status = false;
    UserPresenter mPresenter;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        navigateLibrary.run();
        navigatePlay.run();
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
        mPresenter = new UserPresenter();
        mPresenter.attachView(this);
        mPresenter.getUserInfo();
    }


    @Override
    protected void listener() {
        mSlidingUpPaneLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                View nowPlayingCard = topContainer;
                nowPlayingCard.setAlpha(1 - slideOffset);
            }

        });

        headerView.setOnClickListener(v -> {
            mDrawerLayout.closeDrawers();
            if (login_status) {
                turnToActivity(UserCenterAcivity.class);
            } else {
                turnToActivity(LoginActivity.class);
            }
        });

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
                runnable = navigateLibrary;
                break;
            case R.id.nav_menu_playlist:
                runnable = navigatePlaylist;
                break;
            case R.id.nav_menu_download:
                runnable = navigateDownload;
                break;
            case R.id.nav_menu_shake:
                item.setChecked(true);
                if (!login_status) {
                    turnToActivity(LoginActivity.class);
                } else {
                    turnToActivity(ShakeActivity.class);
                }
                break;
            case R.id.nav_menu_setting:
                turnToActivity(SettingsActivity.class);
                break;
            case R.id.nav_menu_exit:
                finish();
                break;
        }

        if (runnable != null) {
            mDrawerLayout.closeDrawers();
            Handler handler = new Handler();
            handler.postDelayed(() -> runnable.run(), 350);
        }

        return true;
    }

    /**
     * 跳转Activity
     *
     * @param cls
     */
    private void turnToActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }


    private Runnable navigateLibrary = new Runnable() {
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_menu_music).setChecked(true);
            Fragment fragment = MainFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();

        }
    };
    private Runnable navigatePlaylist = new Runnable() {
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_menu_playlist).setChecked(true);
            Fragment fragment = new PlaylistFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container, fragment).commit();

        }
    };
    private Runnable navigateDownload = new Runnable() {
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_menu_download).setChecked(true);
            Fragment fragment = MainFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commit();
        }
    };
    private Runnable navigatePlay = new Runnable() {
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_menu_download).setChecked(true);
            Fragment fragment = PlayFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.controls_container, fragment).commit();
        }
    };


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }


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
                Intent intent = new Intent(this, SearchActivity.class);
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


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorInfo(String msg) {

    }

    @Override
    public void updateView(User user) {
        if (user != null) {
            login_status = true;
            mName.setText(user.getName());
            mNick.setText(user.getNick());
            GlideApp.with(this)
                    .load(user.getUrl())
                    .placeholder(R.drawable.ic_account_circle)
                    .error(R.drawable.ic_account_circle)
                    .into(mAvatarIcon);
        } else {
            login_status = false;
            mAvatarIcon.setImageResource(R.drawable.ic_account_circle);
            mName.setText("湖科音乐湖");
            mNick.setText("未登录?去登录/注册吧!");
        }
    }
}
