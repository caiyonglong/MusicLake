package com.cyl.musiclake.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.data.source.download.TasksManager;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.event.PlayQueueEvent;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.map.ShakeActivity;
import com.cyl.musiclake.ui.music.local.fragment.PlayControlFragment;
import com.cyl.musiclake.ui.music.online.activity.SearchActivity;
import com.cyl.musiclake.ui.my.LoginActivity;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.utils.CoverLoader;
import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cyl.musiclake.ui.music.local.fragment.PlayControlFragment.topContainer;

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

    public ImageView mImageView;
    CircleImageView mAvatarIcon;
    TextView mName;
    TextView mNick;

    private View headerView;
    private static final String TAG = "MainActivity";

    private boolean mIsLogin = false;

    Class<?> mTargetClass = null;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0);
        //菜单栏的头部控件初始化
        initNavView();
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        StatusBarUtil.setTransparentForImageView(this, null);

        setUserStatusInfo();
        /**登陆成功重新设置用户新*/
        RxBus.getInstance().register(LoginEvent.class).subscribe(event -> setUserStatusInfo());
        RxBus.getInstance().register(PlayQueueEvent.class).subscribe(event -> setPlaylistQueueChange());
    }

    private void initNavView() {
        headerView = mNavigationView.getHeaderView(0);
        mImageView = headerView.findViewById(R.id.header_bg);
        mAvatarIcon = headerView.findViewById(R.id.header_face);
        mName = headerView.findViewById(R.id.header_name);
        mNick = headerView.findViewById(R.id.header_nick);
    }

    @Override
    protected void initData() {
        String from = getIntent().getAction();
        if (from != null && from.equals(Constants.DEAULT_NOTIFICATION)) {
            mSlidingUpPaneLayout.setPanelState(PanelState.EXPANDED);
        }
        //加载主fragment
        navigateLibrary.run();
        navigatePlay.run();
    }


    @Override
    protected void listener() {
        mSlidingUpPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (newState == PanelState.EXPANDED) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                topContainer.setAlpha(1 - slideOffset * 2);
                if (topContainer.getAlpha() < 0) {
                    topContainer.setVisibility(View.GONE);
                } else {
                    topContainer.setVisibility(View.VISIBLE);
                    mSlidingUpPaneLayout.setTouchEnabled(true);
                }
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (mTargetClass != null) {
                    turnToActivity(mTargetClass);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

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
            case R.id.nav_login_status:
                if (mIsLogin) {
                    new MaterialDialog.Builder(this)
                            .title("音乐湖")
                            .content("您确定要退出或切换其他账号吗？")
                            .positiveText("确定")
                            .onPositive((materialDialog, dialogAction) -> {
                                UserStatus.clearUserInfo(this);
                                UserStatus.saveuserstatus(this, false);
                                Tencent.createInstance(Constants.APP_ID, this).logout(this);
                                RxBus.getInstance().post(new LoginEvent());
                            }).negativeText("取消").show();
                } else {
                    mTargetClass = LoginActivity.class;
                }
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_menu_shake:
                item.setChecked(true);
                if (!mIsLogin) {
                    mTargetClass = LoginActivity.class;
                } else {
                    mTargetClass = ShakeActivity.class;
                }
                break;
            case R.id.nav_menu_setting:
                mTargetClass = SettingsActivity.class;
                break;
            case R.id.nav_menu_exit:
                mTargetClass = null;
                finish();
                break;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

    /**
     * 跳转Activity
     *
     * @param cls
     */
    private void turnToActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
        mTargetClass = null;
    }


    private Runnable navigateLibrary = () -> {
        Fragment fragment = MainFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();

    };
    private Runnable navigatePlay = () -> {
        Fragment fragment = PlayControlFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.controls_container, fragment).commit();
    };


    //返回键
    @Override
    public void onBackPressed() {
        if (mSlidingUpPaneLayout != null &&
                (mSlidingUpPaneLayout.getPanelState() == PanelState.EXPANDED || mSlidingUpPaneLayout.getPanelState() == PanelState.ANCHORED)) {
            mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else if (isNavigatingMain()) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return super.onCreateOptionsMenu(menu);
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
                return true;
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
        return (currentFragment instanceof MainFragment);
    }


    @Override
    protected void onDestroy() {
        //结束下载任务
        TasksManager.getImpl().onDestroy();
        FileDownloader.getImpl().pauseAll();
        super.onDestroy();
    }

    private void setPlaylistQueueChange() {
        if (PlayManager.getPlayList().size() == 0) {
            mSlidingUpPaneLayout.setTouchEnabled(false);
        } else {
            mSlidingUpPaneLayout.setTouchEnabled(true);
        }
    }

    /**
     * 设置用户状态信息
     */
    private void setUserStatusInfo() {
        mIsLogin = UserStatus.getstatus(this);
        if (mIsLogin) {
            String url = UserStatus.getUserInfo(this).getAvatar();
            CoverLoader.loadImageView(this, url, R.drawable.ic_account_circle, mAvatarIcon);
            mName.setText(UserStatus.getUserInfo(this).getNick());
            mNick.setText("音乐湖");
            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle("注销登录").setIcon(R.drawable.ic_exit);
        } else {
            mAvatarIcon.setImageResource(R.drawable.ic_account_circle);
            mName.setText("音乐湖");
            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle("点我登录");
            mNick.setText("未登录?去登录/注册吧!");
        }
    }

}
