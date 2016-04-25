package com.cyl.music_hnust;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.MyViewPagerAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.bean.Dynamic;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.fragment.MusicFragment;
import com.cyl.music_hnust.fragment.MyFragment;
import com.cyl.music_hnust.map.BaseMapActivity;
import com.cyl.music_hnust.map.NearActivity;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.SnackbarUtil;
import com.cyl.music_hnust.view.RoundedImageView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

public class MyActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener, MusicFragment.Callbacks {

    //初始化各种控件，照着xml中的顺序写
    private DrawerLayout mDrawerLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFloatingActionButton;
    private NavigationView mNavigationView;
    private RoundedImageView id_header_face;
    private TextView id_header_name;
    private TextView signature;


    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;

    public static MyApplication application;

    public static MusicPlayService mService;
    public static List<MusicInfo> songs;


    public static final String PREFERENCES_NAME = "settings";// SharedPreferences名称
    public static final String PREFERENCES_MODE = "mode";// 存储播放模式
    public static final String PREFERENCES_SCAN = "scan";// 存储是否扫描过
    public static final String PREFERENCES_SKIN = "skin";// 存储背景图
    public static final String PREFERENCES_LYRIC = "lyric";// 存储歌词高亮颜色

    public static final String BROADCAST_ACTION_SCAN = "com.cwd.cmeplayer.action.scan";// 扫描广播标志


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //   application = (MyApplication) getApplication();

//        if (mService == null) {
//            mService = new MusicPlayService();
//            application.setmService(mService);
//            Log.e("11", "d222d");
//        } else {
//            mService = application.getmService();
//            application.setmService(mService);
//        }

        init();


    }

    private void init() {
        // 初始化各种控件
        initViews();
        regist();//注册广播
        playIntent = new Intent(getApplicationContext(), MusicPlayService.class);// 绑定服务
        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();

        // 对各种控件进行设置、适配、填充数据
        configViews();
        initServiceConnection();
    }

    private void regist() {
        receiver = new MainReceiver();
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_ACTION);
        registerReceiver(receiver, intentFilter);
    }


    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            if (i == 1) {
                MyFragment mFragment = new MyFragment();
                mFragment.setArguments(mBundle);
                mFragments.add(i, mFragment);
            } else if (i == 0) {
                MusicFragment mFragment = new MusicFragment();
                mFragment.setArguments(mBundle);
                mFragments.add(i, mFragment);
            }
        }

    }


    private void configViews() {

        // 设置显示Toolbar
        setSupportActionBar(mToolbar);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                // Boolean status =UserStatus.getstatus(getApplicationContext());
                User userinfo = UserStatus.getUserInfo(getApplicationContext());
                if (userinfo.getUser_name() != null) {
//
//                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//                    String syncConnPref = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_CONN, "");

                    id_header_name.setText(userinfo.getUser_name().toString());
                    signature.setText(userinfo.getUser_id().toString());
                    String path = Environment.getExternalStorageDirectory() + "/hkmusic/cache/" + userinfo.getUser_id() + ".png";

                    if (userinfo.getUser_img() != null) {
                        path = userinfo.getUser_img();

                    }
                    File file = new File(path);
                    if (file.exists())
                    id_header_face.setImageBitmap(UserCenterMainAcivity.getLoacalBitmap(path));

                    UserStatus.saveuserstatus(MyActivity.this, true);
                } else {

                    UserStatus.saveuserstatus(MyActivity.this, false);
                    id_header_name.setText("未登录");
                    signature.setText("");
                    id_header_face.setImageResource(R.mipmap.user_icon_default_main);

                }

            }
        };
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        View view = mNavigationView.inflateHeaderView(R.layout.header_nav);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        mNavigationView.inflateMenu(R.menu.menu_nav);
        mNavigationView.setItemIconTintList(null);

        id_header_face = (RoundedImageView) view.findViewById(R.id.id_header_face);
        signature = (TextView) view.findViewById(R.id.signature);
        id_header_name = (TextView) view.findViewById(R.id.id_header_name);
        id_header_face.setOnClickListener(this);


        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavgationViewMenuItemSelected(mNavigationView);

        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(3);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

        // 设置FloatingActionButton的点击事件
        mFloatingActionButton.setOnClickListener(this);


    }

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav
     */
    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                String msgString = "";

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_shake:
                        if (!UserStatus.getstatus(getApplicationContext())) {
                            msgString = "请登录！";
                            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(it);
                        } else {
                            Intent it2 = new Intent(getApplicationContext(), ShakeActivity.class);
                            startActivity(it2);
                        }
                        break;
                    case R.id.nav_menu_near:
                        if (!UserStatus.getstatus(getApplicationContext())) {
                            msgString = "请登录！";
                            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(it);
                        } else {
                            Intent it2 = new Intent(getApplicationContext(), NearActivity.class);
                            startActivity(it2);
                        }
                        break;
                    case R.id.nav_menu_map:
                        Intent intent = new Intent(getApplicationContext(), BaseMapActivity.class);
                        startActivity(intent);
                        msgString = (String) menuItem.getTitle();
                        break;
                    case R.id.nav_menu_setting:
                        Intent it = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(it);
                        break;
                    case R.id.nav_menu_exit://退出程序
                        //   close();
                        exitProgram();
//                        MyActivity.this.finish();
                        break;
                    case R.id.nav_menu_scan:
                        Intent intent1 = new Intent(getApplicationContext(), ScanActivity.class);
                        startActivity(intent1);
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                // android-support-design兼容包中新添加的一个类似Toast的控件。
                SnackbarUtil.show(mViewPager, msgString, 0);

                return true;
            }
        });
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        //   mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_coordinatorlayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.id_appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.add_dynamic);
        mFloatingActionButton.setVisibility(View.GONE);

        mNavigationView = (NavigationView) findViewById(R.id.id_navigationview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent it3 = new Intent(this, SearchActivity.class);
            startActivity(it3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
        if (position == 1) {
            mFloatingActionButton.setVisibility(View.VISIBLE);
        } else {
            mFloatingActionButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            if (requestCode == 1) {
                Dynamic dynamic = new Dynamic();
                User userinfo = UserStatus.getUserInfo(getApplicationContext());

                dynamic.setLove(0);
                dynamic.setMyLove(false);
                Log.e("Time", FormatUtil.getTime());
                dynamic.setTime(FormatUtil.getTime());
                dynamic.setComment(0);
                dynamic.setContent(data.getStringExtra("content"));
                dynamic.setUser(userinfo);
                //   dynamic = (Dynamic) data.getSerializableExtra("comment");
                MyFragment.mdatas.add(0, dynamic);
                MyFragment.mRecyclerViewAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(1);
                Log.e("-----", dynamic.getContent());


            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_header_face:
                User userinfo = UserStatus.getUserInfo(getApplicationContext());
                if (userinfo.getUser_name() != null) {
                    Intent it = new Intent(this, UserCenterMainAcivity.class);
                    startActivity(it);
                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    startActivity(it);
                }
                mDrawerLayout.closeDrawers();
                break;
            case R.id.add_dynamic:
                Intent it = new Intent(this, EditActivity.class);
                startActivityForResult(it, 1);
                break;

        }
    }


    MainReceiver receiver;
    public static final String CTL_ACTION = "hk.music.action.CTL_ACTION"; //播放控制
    public static final String UPDATE_ACTION = "hk.music.action.UPDATE_ACTION"; //更新UI

    @Override
    public void OnFragmentClick(View v) {

        Intent it = new Intent(CTL_ACTION);
        switch (v.getId()) {
            case R.id.next_buttom:
                it.putExtra("control", 3);
                break;
            case R.id.play_buttom:
                it.putExtra("control", 1);
                break;
        }
        sendBroadcast(it);
    }


    private class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int update = intent.getIntExtra("update", -1);
            int current = intent.getIntExtra("current", -1);
            String name = intent.getStringExtra("name");
            String artist = intent.getStringExtra("artist");
            String pic = intent.getStringExtra("pic");
            if (current >= 0) {
                Log.e("==", pic + "");
                MusicFragment.initBackGround(getApplicationContext(), pic);
                MusicFragment.song_name.setText(name);
                MusicFragment.singer_name.setText(artist);

            }
            switch (update) {
                case 3:
                    MusicFragment.play_buttom.setBackgroundResource(android.R.drawable.ic_media_pause);
                    break;
                case 1:
                    MusicFragment.play_buttom.setBackgroundResource(android.R.drawable.ic_media_pause);
                    break;
                case 2: //暂停
                    MusicFragment.play_buttom.setBackgroundResource(android.R.drawable.ic_media_play);
                    break;

            }
        }
    }


    private ServiceConnection serviceConnection;

    private Intent playIntent;

    private boolean bindState = false;// 服务绑定状态

    private boolean canSkip = true;// 防止用户频繁点击造成多次解除服务绑定，true：允许解绑

    /*
     * 初始化服务绑定
	 */
    private void initServiceConnection() {
        serviceConnection = new ServiceConnection() {


            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                //  binder = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
//                binder = (MediaBinder) service;

                mService = ((MusicPlayService.LocalBinder) service).getService();
                mService.setContext(getApplicationContext());
//                if (binder != null) {
//                    canSkip = true;// 重置
//                    binder.setLyricView(null, true);// 无歌词视图
//                }
            }
        };
    }

    /*
     * 这里的部分本来是写在onStart里的，但是我发现在真机上点击跳转后立即返回不会执行onStart，但会执行onResume，
	 * 但跳转后空2秒以上再返回，就会执行onStart，这种问题如何解释。各位可以试试，也许我对生命周期理解的不透彻。
	 */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        Intent intent = new Intent(MediaService.BROADCAST_ACTION_SERVICE);
//        intent.putExtra(MediaService.INTENT_ACTIVITY,
//                MediaService.ACTIVITY_MAIN);
//        sendBroadcast(intent);

        bindState = bindService(playIntent, serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (serviceConnection != null) {
            if (bindState) {
                unbindService(serviceConnection);// 一定要解除绑定
            }
            serviceConnection = null;
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    /**
     * 退出程序
     */
    private void exitProgram() {
        stopService(playIntent);
        finish();
    }


}
