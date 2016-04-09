package com.cyl.music_hnust.map;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.cloud.CloudImage;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
//import com.amap.map3d.demo.MainActivity;
//import com.amap.map3d.demo.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cyl.music_hnust.R;

public class CloudDetailActivity extends Activity {
	private GridView mgridView;
	private TextView mPoiTextView;
	private TextView mNameTextView;
	private TextView mLocationTextView;
	private TextView mAddressTextView;
	private TextView mCreateTextView;
	private TextView mUpdateTextView;
	private TextView mDistanceTextView;
	private LinearLayout mContainer;

	private CloudItem mItem;
	private List<CloudImage> mImageitem;
	private ImageLoader mImageLoader;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cloud_detail);

		mgridView = (GridView) findViewById(R.id.grid);
		mPoiTextView = (TextView) findViewById(R.id.poiid_text);
		mNameTextView = (TextView) findViewById(R.id.name_text);
		mLocationTextView = (TextView) findViewById(R.id.location_text);
		mAddressTextView = (TextView) findViewById(R.id.address_text);
		mCreateTextView = (TextView) findViewById(R.id.createtime_text);
		mUpdateTextView = (TextView) findViewById(R.id.update_time_text);
		mDistanceTextView = (TextView) findViewById(R.id.distance_text);
		mContainer = (LinearLayout) findViewById(R.id.container);

		mItem = (CloudItem) getIntent().getParcelableExtra("clouditem");
		if (mItem == null) {
			return;
		}

		mImageitem = mItem.getCloudImage();// 获取图片
		if (mImageitem.size() != 0) {
			setGridView();
		}

		String poiId = mItem.getID();
		String poiName = mItem.getTitle();
		String poiLocation = "{" + mItem.getLatLonPoint().getLatitude() + ","
				+ mItem.getLatLonPoint().getLongitude() + "}";
		String poiAddress = mItem.getSnippet();
		String poiCreateTime = mItem.getCreatetime();
		String poiUpdateTime = mItem.getUpdatetime();
		String poiDistance = mItem.getDistance() + "";
		Log.e("Tag",poiDistance);
		mPoiTextView.setText(poiId);
		mNameTextView.setText(poiName);
		mLocationTextView.setText(poiLocation);
		mAddressTextView.setText(poiAddress);
		mCreateTextView.setText(poiCreateTime);
		mUpdateTextView.setText(poiUpdateTime);
		mDistanceTextView.setText(poiDistance);

		Iterator iter = mItem.getCustomfield().entrySet().iterator();
		while (iter.hasNext()) {
			View view = getLayoutInflater().inflate(R.layout.item_layout, null);
			TextView fieldText = (TextView) view
					.findViewById(R.id.poi_field_id);
			TextView valueText = (TextView) view
					.findViewById(R.id.poi_value_id);
			valueText.setTextColor(getResources().getColor(R.color.black));
			Map.Entry entry = (Map.Entry) iter.next();
			fieldText.setText(entry.getKey() + "");
			valueText.setText(entry.getValue() + "");
			//mContainer.addView(view);
		}
	}

	private void setGridView() {
		int size = mImageitem.size();
		int length = 60;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		mgridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		mgridView.setColumnWidth(itemWidth); // 设置列表项宽
		mgridView.setHorizontalSpacing(5); // 设置列表项水平间距
		mgridView.setStretchMode(GridView.NO_STRETCH);
		mgridView.setNumColumns(size); // 设置列数量=列表集合数

		GridViewAdapter adapter = new GridViewAdapter(getApplicationContext());
		mgridView.setAdapter(adapter);
		mgridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				ShowImage(position);
			}
		});

	}

	/** GirdView 数据适配器 */
	public class GridViewAdapter extends BaseAdapter {

		Context context;

		public GridViewAdapter(Context _context) {
			this.context = _context;
			mImageLoader = new ImageLoader(Volley.newRequestQueue(context),
					new AMApCloudImageCache());
		}

		@Override
		public int getCount() {
			return mImageitem.size();
		}

		@Override
		public Object getItem(int position) {
			return mImageitem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.photo_layout, null);
			NetworkImageView imagev = (NetworkImageView) convertView
					.findViewById(R.id.ItemImage);
			imagev.setDefaultImageResId(R.drawable.ic_launcher);
			imagev.setImageUrl(mImageitem.get(position).getPreurl(),
					mImageLoader);
			return convertView;
		}
	}

	private void ShowImage(int position) {
		if (mImageitem.size() != 0) {
			Intent intent = new Intent(CloudDetailActivity.this,
					PreviewPhotoActivity.class);
			intent.putExtra("clouditem", mItem);
			intent.putExtra("position", position);
			startActivity(intent);
		}
	}
}
