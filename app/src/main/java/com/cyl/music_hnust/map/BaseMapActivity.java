package com.cyl.music_hnust.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.BaseActivity;
import com.cyl.music_hnust.utils.AMapUtil;
import com.cyl.music_hnust.utils.ToastUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 永龙 on 2016/3/20.
 */
public class BaseMapActivity extends BaseActivity implements AMapLocationListener, RadioGroup.OnCheckedChangeListener, LocationSource, AMap.OnMarkerClickListener, AMap.OnPOIClickListener, CloudSearch.OnCloudSearchListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {
    private AMap aMap;
    private Toolbar mToolbar;
    private MapView mapView;
    private UiSettings mUiSettings;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RadioGroup mGPSModeGroup;

    private TextView mLocationErrText;
    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton actionB;

    private CloudSearch mCloudSearch;
    private String mTableID = "557067a2e4b0deb9ee7bdb5f"; // 用户tableid，从官网下载测试数据后在云图中新建地图并导入，获取相应的tableid
    private String mId = "2"; // 用户table 行编号
    private String mKeyWord = "科技大学"; // 搜索关键字
    private CloudSearch.Query mQuery;
    private LatLonPoint mCenterPoint = new LatLonPoint(39.942753, 116.428650); // 周边搜索中心点
    private LatLonPoint mPoint1 = new LatLonPoint(39.941711, 116.382248);
    private LatLonPoint mPoint2 = new LatLonPoint(39.884882, 116.359566);
    private LatLonPoint mPoint3 = new LatLonPoint(39.878120, 116.437630);
    private LatLonPoint mPoint4 = new LatLonPoint(39.941711, 116.382248);
    private CloudOverlay mPoiCloudOverlay;
    private List<CloudItem> mCloudItems;
    private ProgressDialog progDialog = null;
    private Marker mCloudIDMarer;
    private String TAG = "AMapYunTuDemo";
    private String mLocalCityName = "湘潭";
    private ArrayList<CloudItem> items = new ArrayList<CloudItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basemap);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        initView();


    }

    @Override
    protected void listener() {

    }

    private void initView() {
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("科大搜索");
                items.clear();
                CloudSearch.SearchBound bound = new CloudSearch.SearchBound(mLocalCityName);
                try {
                    mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
                    mCloudSearch.searchCloudAsyn(mQuery);
                } catch (AMapException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog(String message) {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索:\n" + message);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
        mGPSModeGroup.setOnCheckedChangeListener(this);
        mLocationErrText = (TextView) findViewById(R.id.location_errInfo_text);
        mLocationErrText.setVisibility(View.GONE);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnPOIClickListener(this);

        mCloudSearch = new CloudSearch(this);
        mCloudSearch.setOnCloudSearchListener(this);
//        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.gps_locate_button:
                // 设置定位的类型为定位模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                break;
            case R.id.gps_follow_button:
                // 设置定位的类型为 跟随模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                break;
            case R.id.gps_rotate_button:
                // 设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                break;
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        // 构造导航参数
//        NaviPara naviPara = new NaviPara();
//        // 设置终点位置
//        naviPara.setTargetPoint(marker.getPosition());
//        // 设置导航策略，这里是避免拥堵
//        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
//        try {
//            // 调起高德地图导航
//            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
//        } catch (com.amap.api.maps.AMapException e) {
//            // 如果没安装会进入异常，调起下载页面
//            AMapUtils.getLatestAMapApp(getApplicationContext());
//        }
//        Toast.makeText(getApplicationContext(),"点击了marker"+marker.getPosition(),Toast.LENGTH_SHORT).show();
//        aMap.clear();
        return false;
    }

    @Override
    public void onPOIClick(Poi poi) {
        aMap.clear();
        MarkerOptions markOptiopns = new MarkerOptions();
        markOptiopns.position(poi.getCoordinate());
        TextView textView = new TextView(getApplicationContext());
        textView.setText("到" + poi.getName() + "去");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.mipmap.custom_info_bubble);
        markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
        aMap.addMarker(markOptiopns);
    }


    @Override
    public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000 && item != null) {
            if (mCloudIDMarer != null) {
                mCloudIDMarer.destroy();
            }
            aMap.clear();
            LatLng position = AMapUtil.convertToLatLng(item.getLatLonPoint());
            aMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition(position, 18, 0, 30)));
            mCloudIDMarer = aMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(item.getTitle())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            items.add(item);
            Log.d(TAG, "_id" + item.getID());
            Log.d(TAG, "_location" + item.getLatLonPoint().toString());
            Log.d(TAG, "_name" + item.getTitle());
            Log.d(TAG, "_address" + item.getSnippet());
            Log.d(TAG, "_caretetime" + item.getCreatetime());
            Log.d(TAG, "_updatetime" + item.getUpdatetime());
            Log.d(TAG, "_distance" + item.getDistance());
            Iterator iter = item.getCustomfield().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                Log.d(TAG, key + "   " + val);
            }
        } else {
            ToastUtils.showerror(this, rCode);
        }

    }

    //云图搜索
    @Override
    public void onCloudSearched(CloudResult result, int rCode) {
        dissmissProgressDialog();

        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(mQuery)) {
                    mCloudItems = result.getClouds();

                    if (mCloudItems != null && mCloudItems.size() > 0) {
                        aMap.clear();
                        mPoiCloudOverlay = new CloudOverlay(aMap, mCloudItems);
                        mPoiCloudOverlay.removeFromMap();
                        mPoiCloudOverlay.addToMap();
                        // mPoiCloudOverlay.zoomToSpan();
                        for (CloudItem item : mCloudItems) {
                            items.add(item);
                            Log.d(TAG, "_id " + item.getID());
                            Log.d(TAG, "_location "
                                    + item.getLatLonPoint().toString());
                            Log.d(TAG, "_name " + item.getTitle());
                            Log.d(TAG, "_address " + item.getSnippet());
                            Log.d(TAG, "_caretetime " + item.getCreatetime());
                            Log.d(TAG, "_updatetime " + item.getUpdatetime());
                            Log.d(TAG, "_distance " + item.getDistance());
                            Iterator iter = item.getCustomfield().entrySet()
                                    .iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                Object key = entry.getKey();
                                Object val = entry.getValue();
                                Log.d(TAG, key + "   " + val);
                            }
                        }
                        if (mQuery.getBound().getShape()
                                .equals(CloudSearch.SearchBound.BOUND_SHAPE)) {// 圆形
                            aMap.addCircle(new CircleOptions()
                                    .center(new LatLng(mCenterPoint
                                            .getLatitude(), mCenterPoint
                                            .getLongitude())).radius(5000)
                                    .strokeColor(
                                            // Color.argb(50, 1, 1, 1)
                                            Color.RED)
                                    .fillColor(Color.argb(50, 1, 1, 1))
                                    .strokeWidth(5));

                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mCenterPoint.getLatitude(),
                                            mCenterPoint.getLongitude()), 12));

                        } else if (mQuery.getBound().getShape()
                                .equals(CloudSearch.SearchBound.POLYGON_SHAPE)) {
                            aMap.addPolygon(new PolygonOptions()
                                    .add(AMapUtil.convertToLatLng(mPoint1))
                                    .add(AMapUtil.convertToLatLng(mPoint2))
                                    .add(AMapUtil.convertToLatLng(mPoint3))
                                    .add(AMapUtil.convertToLatLng(mPoint4))
                                    .fillColor(Color.argb(50, 1, 1, 1))
                                    .strokeColor(Color.RED).strokeWidth(1));
                            LatLngBounds bounds = new LatLngBounds.Builder()
                                    .include(AMapUtil.convertToLatLng(mPoint1))
                                    .include(AMapUtil.convertToLatLng(mPoint2))
                                    .include(AMapUtil.convertToLatLng(mPoint3))
                                    .build();
                            aMap.moveCamera(CameraUpdateFactory
                                    .newLatLngBounds(bounds, 50));
                        } else if ((mQuery.getBound().getShape()
                                .equals(CloudSearch.SearchBound.LOCAL_SHAPE))) {
                            mPoiCloudOverlay.zoomToSpan();
                        }

                    } else {
                        ToastUtils.show(this, R.string.no_result);
                    }
                }
            } else {
                ToastUtils.show(this, R.string.no_result);
            }
        } else {
            ToastUtils.showerror(this, rCode);
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String tile = marker.getTitle();
        for (CloudItem item : items) {
            if (tile.equals(item.getTitle())) {
                Intent intent = new Intent(BaseMapActivity.this, CloudDetailActivity.class);
                intent.putExtra("clouditem", item);
                startActivity(intent);
                break;
            }

        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 返回键监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}
