package com.cyl.musiclake.ui.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.MessageEvent;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.SocketOnlineEvent;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.event.CountDownEvent;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.UIUtilsKt;
import com.cyl.musiclake.ui.chat.ChatActivity;
import com.cyl.musiclake.ui.map.ShakeActivity;
import com.cyl.musiclake.ui.music.importplaylist.ImportPlaylistActivity;
import com.cyl.musiclake.ui.music.player.PlayControlFragment;
import com.cyl.musiclake.ui.music.search.SearchActivity;
import com.cyl.musiclake.ui.my.LoginActivity;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.ui.settings.AboutActivity;
import com.cyl.musiclake.ui.settings.SettingsActivity;
import com.cyl.musiclake.utils.CountDownUtils;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.utils.Tools;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cyl.musiclake.ui.UIUtilsKt.logout;
import static com.cyl.musiclake.ui.UIUtilsKt.updateLoginToken;

/**
 * 描述 主要的Activity
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.sliding_layout)
    public SlidingUpPanelLayout mSlidingUpPaneLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    Switch mSwitchCountDown;
    TextView mSwitchCountDownTv;

    public ImageView mImageView;
    CircleImageView mAvatarIcon;
    TextView mName;
    TextView mLoginTv;
    TextView mOnlineNumTv;

    private PlayControlFragment controlFragment;
    private static final String TAG = "MainActivity";

    private boolean mIsCountDown = false;
    private boolean mIsLogin = false;

    Class<?> mTargetClass = null;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        transparentStatusBar(this);
        //菜单栏的头部控件初始化
        initNavView();
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        disableNavigationViewScrollbars(mNavigationView);
        checkLoginStatus();
        initCountDownView();
    }


    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMetaChangedEvent(MetaChangedEvent event) {
        updatePlaySongInfo(event.getMusic());
    }

    private void updatePlaySongInfo(Music music) {
        if (mSlidingUpPaneLayout == null) return;
        if (music != null) {
            mSlidingUpPaneLayout.setPanelHeight(getResources().getDimensionPixelOffset(R.dimen.dp_56));
            CoverLoader.loadBigImageView(this, music, mImageView);
        } else {
            mSlidingUpPaneLayout.setPanelHeight(0);
            mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
        }
    }

    private void initNavView() {
        View mHeaderView = mNavigationView.getHeaderView(0);
        mImageView = mHeaderView.findViewById(R.id.header_bg);
        mAvatarIcon = mHeaderView.findViewById(R.id.header_face);
        mName = mHeaderView.findViewById(R.id.header_name);
        mLoginTv = mHeaderView.findViewById(R.id.user_login_tv);
    }

    @Override
    protected void initData() {
        String from = getIntent().getAction();
        if (from != null && from.equals(Constants.DEAULT_NOTIFICATION)) {
            mSlidingUpPaneLayout.setPanelHeight(getResources().getDimensionPixelOffset(R.dimen.dp_56));
            mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
        }
        updatePlaySongInfo(PlayManager.getPlayingMusic());
        //加载主fragment
        navigateLibrary.run();
        navigatePlay.run();
    }

    @Override
    protected void initInjector() {
    }


    @Override
    protected void listener() {
        mSlidingUpPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                LogUtil.d(TAG, "onPanelStateChanged " + newState);
                if (newState == PanelState.EXPANDED) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                LogUtil.d(TAG, "onPanelSlide, offset " + slideOffset);
                if (controlFragment.topContainer != null) {
                    controlFragment.topContainer.setAlpha(1 - slideOffset * 2);
                    if (controlFragment.topContainer.getAlpha() < 0) {
                        controlFragment.topContainer.setVisibility(View.GONE);
                    } else {
                        controlFragment.topContainer.setVisibility(View.VISIBLE);
                        mSlidingUpPaneLayout.setTouchEnabled(true);
                    }
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
                                logout();
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
            case R.id.nav_menu_playQueue:
                NavigationHelper.INSTANCE.navigatePlayQueue(this);
                break;
            case R.id.nav_menu_import:
                mTargetClass = ImportPlaylistActivity.class;
                break;
            case R.id.nav_menu_setting:
                mTargetClass = SettingsActivity.class;
                break;
            case R.id.nav_menu_online_num:
                mTargetClass = ChatActivity.class;
                break;
            case R.id.nav_menu_feedback:
                Tools.INSTANCE.feeback(this);
                break;
            case R.id.nav_menu_about:
                mTargetClass = AboutActivity.class;
                break;
            case R.id.nav_menu_equalizer:
                try {
                    Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                    effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, PlayManager.getAudioSessionId());
                    startActivityForResult(effects, 666);
                } catch (Exception e) {
                    ToastUtils.show("设备不支持均衡！");
                }
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
        controlFragment = PlayControlFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.controls_container, controlFragment).commit();
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


    private void setPlaylistQueueChange() {
//        if (PlayManager.getPlayList().size() == 0) {
//            mSlidingUpPaneLayout.setPanelState(PanelState.HIDDEN);
//        } else if (PlayManager.getPlayList().size() >= 0 && mSlidingUpPaneLayout.getPanelState() == PanelState.HIDDEN) {
//            mSlidingUpPaneLayout.setPanelState(PanelState.EXPANDED);
//        }
    }

    /**
     * 设置用户状态信息
     */
    private void setUserStatusInfo(Boolean isLogin, User user) {
        mIsLogin = isLogin;
        if (mIsLogin && user != null) {
            String url = user.getAvatar();
            CoverLoader.loadImageView(this, url, R.drawable.ic_account_circle, mAvatarIcon);
            mName.setText(user.getNick());
//            mExitTv.setText(getString(R.string.logout_hint));
            mLoginTv.setVisibility(View.GONE);
            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle(getResources().getString(R.string.logout_hint))
                    .setIcon(R.drawable.ic_exit);
        } else {
            mAvatarIcon.setImageResource(R.drawable.ic_account_circle);
            mName.setText(getResources().getString(R.string.app_name));
            mLoginTv.setVisibility(View.VISIBLE);

            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle(getResources().getString(R.string.login_hint))
                    .setIcon(R.drawable.ic_exit);

            mNavigationView.getMenu().removeItem(R.id.nav_menu_online_num);
        }
    }

    /**
     * 登陆成功重新设置用户新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(LoginEvent event) {
        setUserStatusInfo(event.getStatus(), event.getUser());
    }

    /**
     * 更新在线用户数量
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateOnlineInfo(SocketOnlineEvent event) {
        if (mOnlineNumTv != null) {
            mOnlineNumTv.setText(String.valueOf(event.getNum()));
        }
    }

    /**
     * 更新歌单
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePlaylist(PlaylistEvent event) {
        if (event.getType().equals(Constants.PLAYLIST_QUEUE_ID)) {
            setPlaylistQueueChange();
        } else if (event.getType().equals(Constants.PLAYLIST_LOVE_ID)) {
            Music music = PlayManager.getPlayingMusic();
            if (music != null && music.isLove()) {
                controlFragment.mIvLove.setImageResource(R.drawable.item_favorite_love);
            } else if (music != null && !music.isLove()) {
                controlFragment.mIvLove.setImageResource(R.drawable.item_favorite);
            }
        }
    }

    /**
     * 检查QQ登录状态
     */
    private void checkLoginStatus() {
        if (UserStatus.getLoginStatus() && !UserStatus.getTokenStatus()) {
            updateLoginToken();
        } else if (UserStatus.getLoginStatus()) {
            updateUserInfo(new LoginEvent(true, UserStatus.getUserInfo()));
        }
    }

    /**
     * 去掉navigationView的滚动条
     *
     * @param navigationView
     */
    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    /**
     * 更新歌单
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishCountDown(CountDownEvent event) {
        if (event.isStop()) {
            mSwitchCountDown.setChecked(false);
            mSwitchCountDownTv.setVisibility(View.GONE);
        } else {
            mSwitchCountDown.setChecked(true);
            mSwitchCountDownTv.setVisibility(View.VISIBLE);
        }
    }


    private void initCountDownView() {
        View numItem = mNavigationView.getMenu().findItem(R.id.nav_menu_online_num).getActionView();
        mOnlineNumTv = numItem.findViewById(R.id.msg_num_tv);
        mOnlineNumTv.setText("0");
        MusicApp.socketManager.toggleSocket(MusicApp.isOpenSocket);

        View item = mNavigationView.getMenu().findItem(R.id.nav_menu_count_down).getActionView();
        mSwitchCountDown = item.findViewById(R.id.count_down_switch);
        mSwitchCountDownTv = item.findViewById(R.id.count_down_tv);
        mSwitchCountDown.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                CountDownUtils.INSTANCE.setTextViewListener(mSwitchCountDownTv);
                UIUtilsKt.showCountDown(this, checked -> {
                    mSwitchCountDown.setChecked(checked);
                    if (checked) {
                        mSwitchCountDownTv.setVisibility(View.VISIBLE);
                    } else {
                        mSwitchCountDownTv.setVisibility(View.GONE);
                    }
                    return null;
                });
            } else {
                mSwitchCountDownTv.setVisibility(View.GONE);
                CountDownUtils.INSTANCE.removeTextViewListener(mSwitchCountDownTv);
            }
        });
    }
}
