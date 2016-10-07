package com.cyl.music_hnust.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cyl.music_hnust.ClipActivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.ViewPagerAdapter;
import com.cyl.music_hnust.fragment.CommunityFragment;
import com.cyl.music_hnust.fragment.UserFragment;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by trumi on 16-3-2.
 */
public class UserCenterAcivity extends BaseActivity {

    @Bind(R.id.m_viewpager)
    ViewPager viewPager;
    @Bind(R.id.m_tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.header_img)
    ImageView header_img;
    @Bind(R.id.action_a)
    FloatingActionButton fab_a;
    @Bind(R.id.action_b)
    FloatingActionButton fab_b;

    @OnClick(R.id.header_img)
    void header_img(View view){
        showPopupWindow(view);
    }
    private PopupWindow popWindow;
    private LayoutInflater layoutInflater;
    private TextView photograph, albums;
    private LinearLayout cancel;

    public static final int PHOTOZOOM = 0; // 相册/拍照
    public static final int PHOTOTAKE = 1; // 相册/拍照
    public static final int IMAGE_COMPLETE = 2; // 结果
    public static final int CROPREQCODE = 3; // 截取
    private String photoSavePath;//保存路径
    private String photoSaveName;//图pian名
    private String path;//图片全路径



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_center_main);
        SystemUtils.setSystemBarTransparent(this);
        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);

        initView();

    }

    @Override
    protected void listener() {

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

    private void initView() {
        mToolbar.setTitle("个人中心");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(3);
        }

        mTabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserFragment(), "个人信息");
        adapter.addFragment(new CommunityFragment(), "我的动态");
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("deprecation")
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = layoutInflater.inflate(R.layout.pop_image_select, null);
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            initPop(view);
        }
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void initPop(View view) {
        photograph = (TextView) view.findViewById(R.id.photograph);//拍照
        albums = (TextView) view.findViewById(R.id.albums);//相册
        cancel = (LinearLayout) view.findViewById(R.id.cancel);//取消
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
                photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
                Uri imageUri = null;
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
                openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, PHOTOTAKE);
            }
        });
        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, PHOTOZOOM);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();

            }
        });
    }
    /**
     * 图片选择及拍照结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM://相册
                if (data == null) {
                    return;
                }
                uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
//                Cursor cursor = managedQuery(uri, proj, null, null, null);
                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//                Cursor c= CloudSearch.Query();

//                Cursor cursor = manag
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);// 图片在的路径
                Intent intent3 = new Intent(UserCenterAcivity.this, ClipActivity.class);
                intent3.putExtra("path", path);
                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;
            case PHOTOTAKE://拍照
                path = photoSavePath + photoSaveName;
                uri = Uri.fromFile(new File(path));
                Intent intent2 = new Intent(UserCenterAcivity.this, ClipActivity.class);
                intent2.putExtra("path", path);
                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;
            case IMAGE_COMPLETE:
                final String temppath = data.getStringExtra("path");
                User user = UserStatus.getUserInfo(getApplicationContext());
                user.setUser_img(temppath);
                UserStatus.savaUserInfo(getApplicationContext(), user);
                header_img.setImageBitmap(getLoacalBitmap(temppath));
                path = Constants.DEFAULT_USERIMG_PATH + user.getUser_id() + ".png";

                upload(UserStatus.getUserInfo(this).getUser_id(),path);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void upload(String user_id,String path) {
        File file =new File(path);
        OkHttpUtils.post().url(Constants.UPLOAD_URL)
                .addFile("file",user_id+".png", file)
                .addParams(Constants.USER_ID, user_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("eee","ccccc");
                        ToastUtils.show(UserCenterAcivity.this,"上传失败，请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("eee","ddd");
                        ToastUtils.show(UserCenterAcivity.this,"上传成功");
                    }
                });
    }


}
