package com.cyl.music_hnust.map;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.services.cloud.CloudItem;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cyl.music_hnust.R;


@SuppressLint("NewApi")
public class PreviewPhotoActivity extends AppCompatActivity {
    private ArrayList<NetworkImageView> listViews = null;
    private MyPageAdapter adapter;
    private int pageIndex = 0;
    private int pageCount = 0;
    private ViewPager mphtoPager;
    private TextView mTitletv;
    private ProgressBar mpbLoding;
    private CloudItem mCloudItem;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private Button mButtonback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        getIntentData();
        mQueue = Volley.newRequestQueue(this.getApplicationContext());
        mImageLoader = new ImageLoader(mQueue, new AMApCloudImageCache());

        mTitletv = (TextView) findViewById(R.id.title_des_text);
        mTitletv.getPaint().setFakeBoldText(true);
        mpbLoding = (ProgressBar) findViewById(R.id.pb_loading);
        mphtoPager = (ViewPager) findViewById(R.id.viewpager_photo);

        mphtoPager.addOnPageChangeListener(pageChangeListener);

        mButtonback = (Button) findViewById(R.id.back);
        mButtonback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewPhotoActivity.this,
                        CloudDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                finish();
            }
        });
        pageCount = mCloudItem.getCloudImage().size();
        for (int i = 0; i < pageCount; i++) {
            initListViews();
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        mphtoPager.setAdapter(adapter);// 设置适配器

        mphtoPager.setCurrentItem(pageIndex);

        if (pageCount > 0) {
            mTitletv.setText((pageIndex + 1) + "/" + pageCount);
        }
        if (pageIndex == 0) {
            downLoadImage(0);
        }

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mCloudItem = intent.getParcelableExtra("clouditem");
        pageIndex = intent.getIntExtra("position", 0);
    }

    @SuppressLint({"NewApi", "InlinedApi"})
    private void initListViews() {
        if (listViews == null) {
            listViews = new ArrayList<NetworkImageView>();
        }
        NetworkImageView img = new NetworkImageView(this);
        img.setBackgroundColor(0xff000000);// 黑色
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        img.setScaleType(ScaleType.FIT_CENTER);// 图片填充方式
        listViews.add(img);// 添加view
    }

    public void onBackClick(View view) {
        this.finish();
    }

    synchronized private void downLoadImage(int index) {
        mpbLoding.setVisibility(View.VISIBLE);
        String imageUrl = mCloudItem.getCloudImage().get(index).getUrl();
        listViews.get(index).setImageUrl(imageUrl, mImageLoader);
        mImageLoader.get(imageUrl, new ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mpbLoding.setVisibility(View.GONE);

            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    mpbLoding.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        // 页面选择响应函数
        public void onPageSelected(int arg0) {
            downLoadImage(arg0);
            pageIndex = arg0;
            mTitletv.setText((pageIndex + 1) + "/" + pageCount);
        }

        // 滑动中。。。
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        // 滑动状态改变
        public void onPageScrollStateChanged(int arg0) {

        }

    };

    private class MyPageAdapter extends PagerAdapter {

        private ArrayList<NetworkImageView> listViews;// content

        private int size;// 页数

        public MyPageAdapter(ArrayList<NetworkImageView> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {// 返回数量
            return size;
        }

        public int getItemPosition(int  pos) {
            return pos;
        }
//

//        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
//            container.removeView(listViews.get(position % size));
        }
//
//        public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
//			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
//		}
//
//		public void finishUpdate(View arg0) {
//		}


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(listViews.get(position % size), 0);

            return listViews.get(position % size);
        }


        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

}
