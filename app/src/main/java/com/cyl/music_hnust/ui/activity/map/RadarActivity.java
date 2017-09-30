package com.cyl.music_hnust.ui.activity.map;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.NearCallback;
import com.cyl.music_hnust.model.location.Location;
import com.cyl.music_hnust.model.location.LocationInfo;
import com.cyl.music_hnust.model.user.User;
import com.cyl.music_hnust.model.user.UserStatus;
import com.cyl.music_hnust.ui.activity.BaseActivity;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.cyl.music_hnust.view.custom.CustomViewPager;
import com.cyl.music_hnust.view.custom.FixedSpeedScroller;
import com.cyl.music_hnust.view.custom.Info;
import com.cyl.music_hnust.view.custom.LogUtil;
import com.cyl.music_hnust.view.custom.RadarViewGroup;
import com.cyl.music_hnust.view.custom.ZoomOutPageTransformer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yonglong on 2016/5/20.
 */
public class RadarActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    private CustomViewPager viewPager;
    private RelativeLayout ryContainer;
    private RadarViewGroup radarViewGroup;
    Toolbar mToolbar;
    private TextView no_result;
    private int mPosition;
    private FixedSpeedScroller scroller;
    private SparseArray<Info> mDatas = new SparseArray<>();

    private static ProgressDialog progDialog = null;
    private User user;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private LatLonPoint latLonPoint;

    public List<Location> mLocationInfo = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_radar;
    }

    @Override
    protected void listener() {

    }

    @Override
    protected void initData() {

        user = UserStatus.getUserInfo(getApplicationContext());
        init();

        for (int i = 0; i < mLocationInfo.size(); i++) {
            Info info = new Info();
            info.setPortraitId(mLocationInfo.get(i).getUser().getUser_img());
            Log.e("ddddd", mLocationInfo.get(i).getUser().getUser_img());
//            if (mLocationInfo.get(i).getUser().getNick() != null
//                    && mLocationInfo.get(i).getUser().getNick().length() != 0) {
//                info.setName(mLocationInfo.get(i).getUser().getNick());
//            } else {
            info.setName(mLocationInfo.get(i).getUser().getUser_name());
//            }
            if (mLocationInfo.get(i).getUser().getUser_sex().equals("男")) {
                info.setSex(false);
            } else {
                info.setSex(true);
            }
            if (latLonPoint != null) {
                info.setDistance(FormatUtil.Distance(latLonPoint.getLongitude(), latLonPoint.getLatitude(),
                        mLocationInfo.get(i).getLocation_longitude(), mLocationInfo.get(i).getLocation_latitude()));
            } else {
                info.setDistance(Math.round((Math.random() * 10) * 100) / 100);
            }
            mDatas.put(i, info);
        }
        /**
         * 将Viewpager所在容器的事件分发交给ViewPager
         */
        ryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        ViewpagerAdapter mAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(mAdapter);
        //设置缓存数为展示的数目
        viewPager.setOffscreenPageLimit(mDatas.size());
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        //设置切换动画
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(this);
        setViewPagerSpeed(250);
        radarViewGroup.setDatas(mDatas);
        radarViewGroup.setiRadarClickListener(this);
    }

    @Override
    protected void initView() {
        viewPager = (CustomViewPager) findViewById(R.id.vp);
        radarViewGroup = (RadarViewGroup) findViewById(R.id.radar);
        ryContainer = (RelativeLayout) findViewById(R.id.ry_container);
        no_result = (TextView) findViewById(R.id.no_result);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    private void initfunction() {

        if (latLonPoint != null) {
            uploadLocation(user.getUser_id(), latLonPoint.getLatitude()
                    , latLonPoint.getLongitude());
        } else {
            ToastUtils.show(getApplicationContext(), "定位失败!");
        }
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 500);

    }


    class splashhandler implements Runnable {

        public void run() {
            if (user.getUser_id() != null) {
                nearLocation(user.getUser_id());
            } else {
                ToastUtils.show(getApplicationContext(), "请先登录!");
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_radar, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_delete) {
            User user = UserStatus.getUserInfo(getApplicationContext());
            if (user.getUser_id() != null) {
                clearLocation(user.getUser_id());
            } else {
                ToastUtils.show(getApplicationContext(), "请先登录!");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        amapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);//定位时间
                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        amapLocation.getCountry();//国家信息
                        amapLocation.getProvince();//省信息
                        amapLocation.getCity();//城市信息
                        amapLocation.getDistrict();//城区信息
                        amapLocation.getStreet();//街道信息
                        amapLocation.getStreetNum();//街道门牌号信息
                        amapLocation.getCityCode();//城市编码
                        amapLocation.getAdCode();//地区编码
                        amapLocation.getAoiName();//获取当前定位点的AOI信息

                        latLonPoint = new LatLonPoint(amapLocation.getLongitude(), amapLocation.getLatitude());
                        Log.e("amapLocation", "Location amapLocation, getLongitude:"
                                + amapLocation.getLongitude() + ", getLatitude:"
                                + amapLocation.getLatitude());
                        initfunction();
                    } else {
                        initfunction();
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "Location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }

            }
        };

        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
//设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10000);
//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端。
        //调用销毁功能，在应用的合适生命周期需要销毁附近功能
    }

    /**
     * 上传位置信息
     *
     * @param user_id
     * @param latitude
     * @param longitude
     */
    private void uploadLocation(String user_id, double latitude, double longitude) {
        OkHttpUtils.post()//
                .url(Constants.DEFAULT_URL)//
                .addParams(Constants.FUNC, Constants.LOCATION)//
                .addParams(Constants.USER_ID, user_id)//
                .addParams(Constants.LON, String.valueOf(longitude))//
                .addParams(Constants.LAT, String.valueOf(latitude))//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.show(getApplicationContext(), "网络连接异常，请稍后重试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("eeee", response);

                    }
                });
    }

    private void clearLocation(String user_id) {
        OkHttpUtils.post()//
                .url(Constants.DEFAULT_URL)//
                .addParams(Constants.FUNC, Constants.CLEARN)//
                .addParams(Constants.USER_ID, user_id)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.e("eeee", response);
                        ToastUtils.show(getApplicationContext(), "已清除位置!");
                        finish();
                    }

                });
    }

    private void nearLocation(String user_id) {
        OkHttpUtils.post()//
                .url(Constants.DEFAULT_URL)//
                .addParams(Constants.FUNC, Constants.NEAR)//
                .addParams(Constants.USER_ID, user_id)
                .build()//
                .execute(new NearCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        no_result.setText("网络连接异常，请稍后重试！");
                    }

                    @Override
                    public void onResponse(LocationInfo response) {
                        Log.e("+++", response.getStatus() + "");
                        Log.e("+++", response.getData().size() + "");
                        mLocationInfo = response.getData();
                        if (mLocationInfo.size() > 0) {
                            no_result.setVisibility(View.GONE);
                        } else {
                            no_result.setText("暂无数据");
                        }
                        initData();
                    }
                });

    }

    /**
     * 设置ViewPager切换速度
     *
     * @param duration
     */
    private void setViewPagerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedScroller(RadarActivity.this, new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position;
    }

    @Override
    public void onPageSelected(int position) {
        radarViewGroup.setCurrentShowItem(position);
        LogUtil.m("当前位置 " + mPosition);
        LogUtil.m("速度 " + viewPager.getSpeed());
        //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
        if (viewPager.getSpeed() < -1800) {

            viewPager.setCurrentItem(mPosition + 2);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        } else if (viewPager.getSpeed() > 1800 && mPosition > 0) {
            //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
            viewPager.setCurrentItem(mPosition - 1);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRadarItemClick(int position) {
        viewPager.setCurrentItem(position);
    }


    class ViewpagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final Info info = mDatas.get(position);
            //设置一大堆演示用的数据，麻里麻烦~~
            View view = LayoutInflater.from(RadarActivity.this).inflate(R.layout.viewpager_layout, null);
            ImageView ivPortrait = (ImageView) view.findViewById(R.id.iv);
            ImageView ivSex = (ImageView) view.findViewById(R.id.iv_sex);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView details = (TextView) view.findViewById(R.id.details);
//            TextView tvDistance = (TextView) view.findViewById(R.id.tv_distance);
//            tvName.setText(info.getName() == null ? "佚名" : info.getName());
//            if (info.getDistance() > 1000) {
//                tvDistance.setText(info.getDistance() / 1000 + "km");
//            } else {
//                tvDistance.setText(info.getDistance() + "m");
//            }
            String img_url = info.getPortraitId();
            if (img_url != null && img_url.length() > 0) {
                ImageLoader.getInstance().displayImage(img_url, ivPortrait, ImageUtils.getAlbumDisplayOptions());
            }

            if (info.getSex()) {
            } else {
            }
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent;
//                    User user = UserStatus.getUserInfo(getApplicationContext());
//                    if (mLocationInfo.get(position).getUser().getUser_id().equals(user.getUser_id())) {
//                        intent = new Intent(getApplicationContext(), UserCenterMainAcivity.class);
//                    } else {
//                        intent = new Intent(getApplicationContext(), NearPeopleAcivity.class);
//                    }
//                    if (mLocationInfo.get(position).getUser().isSecret()) {
//                        //保密
//                        intent.putExtra("flag", 1);
//
//                        intent.putExtra("nick", mLocationInfo.get(position).getUser().getNick());
//                    } else {
//                        intent.putExtra("flag", 0);
//
//                        intent.putExtra("name", mLocationInfo.get(position).getUser().getUser_name());
//                        intent.putExtra("num", mLocationInfo.get(position).getUser().getUser_id());
//                        intent.putExtra("class", mLocationInfo.get(position).getUser().getUser_class());
//                        intent.putExtra("college", mLocationInfo.get(position).getUser().getUser_college());
//                        intent.putExtra("major", mLocationInfo.get(position).getUser().getUser_major());
//                        intent.putExtra("img", mLocationInfo.get(position).getUser().getUser_img());
//
//
//                        intent.putExtra("sex", mLocationInfo.get(position).getUser().getUser_sex());
//                        intent.putExtra("phone", mLocationInfo.get(position).getUser().getPhone());
//                        intent.putExtra("email", mLocationInfo.get(position).getUser().getUser_email());
//                        intent.putExtra("nick", mLocationInfo.get(position).getUser().getNick());
//
//
//                    }
//                    startActivity(intent);
                }
            });
            ivPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent;
//                    User user = UserStatus.getUserInfo(getApplicationContext());
//                    if (mLocationInfo.get(position).getUser().getUser_id().equals(user.getUser_id())) {
//                        intent = new Intent(getApplicationContext(), UserCenterMainAcivity.class);
//                    } else {
//                        intent = new Intent(getApplicationContext(), NearPeopleAcivity.class);
//                    }
//                    if (mLocationInfo.get(position).getUser().isSecret()) {
//                        //保密
//                        intent.putExtra("flag", 1);
//
//                        intent.putExtra("nick", mLocationInfo.get(position).getUser().getNick());
//                    } else {
//                        intent.putExtra("flag", 0);
//
//                        intent.putExtra("name", mLocationInfo.get(position).getUser().getUser_name());
//                        intent.putExtra("num", mLocationInfo.get(position).getUser().getUser_id());
//                        intent.putExtra("class", mLocationInfo.get(position).getUser().getUser_class());
//                        intent.putExtra("college", mLocationInfo.get(position).getUser().getUser_college());
//                        intent.putExtra("major", mLocationInfo.get(position).getUser().getUser_major());
//                        intent.putExtra("img", mLocationInfo.get(position).getUser().getUser_img());
//
//
//                        intent.putExtra("sex", mLocationInfo.get(position).getUser().getUser_sex());
//                        intent.putExtra("phone", mLocationInfo.get(position).getUser().getPhone());
//                        intent.putExtra("email", mLocationInfo.get(position).getUser().getUser_email());
//                        intent.putExtra("nick", mLocationInfo.get(position).getUser().getNick());
//
//
//                    }
//                    startActivity(intent);
//                    Toast.makeText(RadarActivity.this, "这是 " + info.getName() + " >.<", Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
}
