package com.cyl.music_hnust;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.map.BaseMapActivity;
import com.cyl.music_hnust.map.NearActivity;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.SnackbarUtil;
import com.cyl.music_hnust.view.RoundedImageView;

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

    private MusicPlayService mService;

    // (0~4对应SlidingAdapter的position，不可更改)
    public static final int SLIDING_MENU_SCAN = 0;// 侧滑->扫描歌曲
    public static final int SLIDING_MENU_ALL = 1;// 侧滑->全部歌曲
    public static final int SLIDING_MENU_FAVORITE = 2;// 侧滑->我的最爱
    public static final int SLIDING_MENU_FOLDER = 3;// 侧滑->文件夹
    public static final int SLIDING_MENU_EXIT = 4;// 侧滑->退出程序
    public static final int SLIDING_MENU_FOLDER_LIST = 5;// 侧滑->文件夹->文件夹列表

    public static final int DIALOG_DISMISS = 0;// 对话框消失
    public static final int DIALOG_SCAN = 1;// 扫描对话框
    public static final int DIALOG_MENU_REMOVE = 2;// 歌曲列表移除对话框
    public static final int DIALOG_MENU_DELETE = 3;// 歌曲列表提示删除对话框
    public static final int DIALOG_MENU_INFO = 4;// 歌曲详情对话框
    public static final int DIALOG_DELETE = 5;// 歌曲删除对话框

    public static final String PREFERENCES_NAME = "settings";// SharedPreferences名称
    public static final String PREFERENCES_MODE = "mode";// 存储播放模式
    public static final String PREFERENCES_SCAN = "scan";// 存储是否扫描过
    public static final String PREFERENCES_SKIN = "skin";// 存储背景图
    public static final String PREFERENCES_LYRIC = "lyric";// 存储歌词高亮颜色

    public static final String BROADCAST_ACTION_SCAN = "com.cwd.cmeplayer.action.scan";// 扫描广播标志
    public static final String BROADCAST_ACTION_MENU = "com.cwd.cmeplayer.action.menu";// 弹出菜单广播标志
    public static final String BROADCAST_ACTION_FAVORITE = "com.cwd.cmeplayer.action.favorite";// 喜爱广播标志
    public static final String BROADCAST_ACTION_EXIT = "com.cwd.cmeplayer.action.exit";// 退出程序广播标志
    public static final String BROADCAST_INTENT_PAGE = "com.cwd.cmeplayer.intent.page";// 页面状态
    public static final String BROADCAST_INTENT_POSITION = "com.cwd.cmeplayer.intent.position";// 歌曲索引


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        application = (MyApplication) getApplication();

        musicReceiver = new MusicReceiver();
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_ACTION);
        registerReceiver(musicReceiver, intentFilter);

        // 初始化各种控件
        initViews();

        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();

        // 对各种控件进行设置、适配、填充数据
        configViews();

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
                    if (userinfo.getUser_img()!=null){
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
                        option.inSampleSize = 1;
                        // 根据图片的SDCard路径读出Bitmap
                        Bitmap bm = BitmapFactory.decodeFile(userinfo.getUser_img(), option);
                        id_header_face.setImageBitmap(bm);

                    }

                    UserStatus.saveuserstatus(MyActivity.this, true);
                } else {

                    UserStatus.saveuserstatus(MyActivity.this, false);
                    id_header_name.setText("未登录");
                    signature.setText("");
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
        //  mFloatingActionButton.setOnClickListener(this);


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
        //  mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_floatingactionbutton);


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

        if (id == R.id.action_edit) {
            Intent it = new Intent(this, EditActivity.class);
            startActivityForResult(it, 1);
            return true;
        }
        if (id == R.id.action_share) {
            return true;
        }
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
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void close() {


        if (mService != null) {
            mService.stopSelf();
        }
    }

    @Override
    protected void onDestroy() {
        close();
        unregisterReceiver(musicReceiver);
        super.onDestroy();
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
                MyFragment.mdatas.add(0,dynamic);
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

        }
    }


    MusicReceiver musicReceiver;
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


    private class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int update = intent.getIntExtra("update", -1);
            int current = intent.getIntExtra("current", -1);
            String name = intent.getStringExtra("name");
            String artist = intent.getStringExtra("artist");
            String pic = intent.getStringExtra("pic");
            if (current >= 0) {
                MusicFragment.initBackGround(getApplicationContext(), pic);
                MusicFragment.song_name.setText(name);
                MusicFragment.singer_name.setText(artist);
            }
            switch (update) {
                case 0:
                    MusicFragment.play_buttom.setBackgroundResource(R.drawable.main_btn_play);
                    break;
                case 1:
                    MusicFragment.play_buttom.setBackgroundResource(R.drawable.main_btn_pause);
                    break;
                case 2: //暂停
                    MusicFragment.play_buttom.setBackgroundResource(R.drawable.main_btn_play);
                    break;
            }
        }
    }

}
