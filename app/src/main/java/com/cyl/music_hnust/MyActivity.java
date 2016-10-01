package com.cyl.music_hnust;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.activity.LoginActivity;
import com.cyl.music_hnust.activity.SettingsActivity;
import com.cyl.music_hnust.adapter.MyViewPagerAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.fragment.CommunityFragment;
import com.cyl.music_hnust.fragment.MusicFragment1;
import com.cyl.music_hnust.http.HttpByGet;
import com.cyl.music_hnust.map.BaseMapActivity;
import com.cyl.music_hnust.map.RadarActivity;
import com.cyl.music_hnust.model.Dynamic;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.view.RoundedImageView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

public class MyActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener, MusicFragment1.Callbacks {

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
    private User userinfo;


    public static final String PREFERENCES_NAME = "settings";// SharedPreferences名称
    public static final String PREFERENCES_SCAN = "scan";// 存储是否扫描过
    public static final int MENU_SHAKE = 0x123;// 摇一摇
    public static final int MENU_NEAR_PEOPLE = 0x124;// 附近的人
    public static final int MENU_CAMPUS_MAP = 0X125;// 科大地图
    public static int MENU_FLAG;// 标志

    public static final String BROADCAST_ACTION_SCAN = "com.cwd.cmeplayer.action.scan";// 扫描广播标志


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        init();


    }


    private void init() {

        userinfo = UserStatus.getUserInfo(getApplicationContext());
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
                CommunityFragment mFragment = new CommunityFragment();
                mFragment.setArguments(mBundle);
                mFragments.add(i, mFragment);
            } else if (i == 0) {
                MusicFragment1 mFragment = new MusicFragment1();
                mFragment.setArguments(mBundle);
                mFragments.add(i, mFragment);
            }
        }

    }



    private void configViews() {

        // 设置显示Toolbar
        setSupportActionBar(mToolbar);
        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        View view = mNavigationView.inflateHeaderView(R.layout.header_nav);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        mNavigationView.inflateMenu(R.menu.menu_nav);
        mNavigationView.setItemIconTintList(null);

//        id_header_face = (RoundedImageView) view.findViewById(R.id.id_header_face);
//        signature = (TextView) view.findViewById(R.id.signature);
//        id_header_name = (TextView) view.findViewById(R.id.id_header_name);
//        id_header_face.setOnClickListener(this);


        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavgationViewMenuItemSelected(mNavigationView);


        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        if (userinfo.getUser_name() != null) {

            id_header_name.setText(userinfo.getUser_name().toString());
            signature.setText(userinfo.getUser_id().toString());
            String path = Constants.DEFAULT_USERIMG_PATH + userinfo.getUser_id() + ".png";
            File file = new File(path);
            if (userinfo.getUser_img() != null && userinfo.getUser_img().length() > 0) {
                if (file.exists())
                    id_header_face.setImageBitmap(UserCenterMainAcivity.getLoacalBitmap(path));
                else {
                    try {
                        HttpByGet.downloadFile(userinfo.getUser_img(), path);
                        if (file.exists())
                            id_header_face.setImageBitmap(UserCenterMainAcivity.getLoacalBitmap(path));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            UserStatus.saveuserstatus(MyActivity.this, true);
        } else {

            UserStatus.saveuserstatus(MyActivity.this, false);
            id_header_name.setText("未登录");
            signature.setText("");
            id_header_face.setImageResource(R.drawable.ic_account_circle);

        }

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
//        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

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
                    case R.id.nav_menu_my:
                        if (!UserStatus.getstatus(getApplicationContext())) {
                            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(it);
                        } else {

                            Intent it2 = new Intent(getApplicationContext(), MynamicActivity.class);
                            startActivity(it2);

                        }
                        break;
                    case R.id.nav_menu_shake:
                        if (!UserStatus.getstatus(getApplicationContext())) {
                            msgString = "请登录！";
                            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(it);
                        } else {
                            MENU_FLAG = MENU_SHAKE;
                            String msg = "摇一摇功能将获取你正在播放的歌曲\n" +
                                    "信息，你的歌曲信息将会被保留一段\n" +
                                    "时间。搜索他人所听歌曲。最后请注意摇动的姿势！";
                            detailsshow(msg);
                        }
                        break;
                    case R.id.nav_menu_near:
                        if (!UserStatus.getstatus(getApplicationContext())) {
                            msgString = "请登录！";
                            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(it);
                        } else {
                            MENU_FLAG = MENU_NEAR_PEOPLE;
                            String msg = "查看附近的人功能将获取你的位置信\n" +
                                    "息，你的位置信息会被保留一段时\n" +
                                    "间。通过列表右上角的清除功能可随\n时手动清除位置信息。";
                            detailsshow(msg);

                        }
                        break;
                    case R.id.nav_menu_map:
                        Intent intent = new Intent(getApplicationContext(), BaseMapActivity.class);
                        startActivity(intent);
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

                dynamic.setLove(0);
                dynamic.setMyLove(false);
                Log.e("Time", FormatUtil.getTime());
                dynamic.setTime(FormatUtil.getTime());
                dynamic.setComment(0);
                dynamic.setContent(data.getStringExtra("content"));
                dynamic.setUser(userinfo);
                //   dynamic = (Dynamic) data.getSerializableExtra("comment");
//                CommunityFragment.mdatas.add(0, dynamic);
//                CommunityFragment.mRecyclerViewAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(1);
                Log.e("-----", dynamic.getContent());


            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.id_header_face:
//                if (userinfo.getUser_name() != null) {
//                    Intent it = new Intent(this, UserCenterMainAcivity.class);
//                    startActivity(it);
//                } else {
//                    Intent it = new Intent(this, LoginActivity.class);
//                    startActivity(it);
//                }
//                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                }
//                break;
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
//            case R.id.play_buttom:
//                it.putExtra("control", 1);
//                break;
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
//                MusicFragment1.initBackGround(getApplicationContext(), pic);
                MusicFragment1.song_name.setText(name);
                MusicFragment1.singer_name.setText(artist);

            }
            switch (update) {
                case 3:
                    MusicFragment1.play_buttom.setBackgroundResource(android.R.drawable.ic_media_pause);
                    break;
                case 1:
                    MusicFragment1.play_buttom.setBackgroundResource(android.R.drawable.ic_media_pause);
                    break;
                case 2: //暂停
                    MusicFragment1.play_buttom.setBackgroundResource(android.R.drawable.ic_media_play);
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

        userinfo = UserStatus.getUserInfo(getApplicationContext());
        if (userinfo.getUser_name() != null) {

            id_header_name.setText(userinfo.getUser_name().toString());
            signature.setText(userinfo.getUser_id().toString());
            String path = Constants.DEFAULT_USERIMG_PATH + userinfo.getUser_id() + ".png";
            File file = new File(path);
            if (userinfo.getUser_img() != null && userinfo.getUser_img().length() > 0) {
                if (file.exists())
                    id_header_face.setImageBitmap(UserCenterMainAcivity.getLoacalBitmap(path));
                else {
                    try {
                        HttpByGet.downloadFile(userinfo.getUser_img(), path);
                        if (file.exists())
                            id_header_face.setImageBitmap(UserCenterMainAcivity.getLoacalBitmap(path));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            UserStatus.saveuserstatus(MyActivity.this, true);
        } else {

            UserStatus.saveuserstatus(MyActivity.this, false);
            id_header_name.setText("未登录");
            signature.setText("");
            id_header_face.setImageResource(R.drawable.ic_account_circle);

        }

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

    public void detailsshow(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(msg)
                .setIcon(R.mipmap.icon);
        setNegativeButton(builder);
        setPositiveButton(builder)

                .create()
                .show();
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (MENU_FLAG) {
                    case MENU_NEAR_PEOPLE:
//                        Intent it2 = new Intent(getApplicationContext(), NearActivity.class);
                        Intent it2 = new Intent(getApplicationContext(), RadarActivity.class);

                        startActivity(it2);
                        break;

                    case MENU_SHAKE:
                        Intent it3 = new Intent(getApplicationContext(), ShakeActivity.class);
                        startActivity(it3);
                        break;
                }

            }
        });
    }

    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


}
