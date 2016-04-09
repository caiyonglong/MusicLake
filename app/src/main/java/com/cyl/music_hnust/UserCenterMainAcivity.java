package com.cyl.music_hnust;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.utils.ToastUtil;
import com.cyl.music_hnust.view.RoundedImageView;
import com.cyl.music_hnust.view.SelectPicPopupWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by trumi on 16-3-2.
 */
public class UserCenterMainAcivity extends AppCompatActivity {

    private TextView user_name;
    private TextView user_num;
    private TextView user_departments;
    private TextView user_class;
    private TextView user_major;
    private TextView user_logout;
    private RoundedImageView fbtn_head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_center_main);

        //给页面设置工具栏
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        //  getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

        //设置工具栏标题
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("个人中心");


        User userinfo = UserStatus.getUserInfo(this);
        if (userinfo.getUser_name() != null) {
            user_name.setText(userinfo.getUser_name());
            user_num.setText(userinfo.getUser_id());
            user_departments.setText(userinfo.getUser_college());
            user_class.setText(userinfo.getUser_class());
            user_major.setText(userinfo.getUser_major());
        }

        user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(UserCenterMainAcivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认注销?")
                        .setContentText("注销后不能享有更多功能!")
                        .setConfirmText("注销")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance
                                logout();
//                                sDialog.setTitleText("注销!")
//                                        .setContentText("Your imaginary file has been deleted!")
//                                        .setConfirmText("OK")
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        fbtn_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(UserCenterMainAcivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(UserCenterMainAcivity.this.findViewById(R.id.main_content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                //设置layout在PopupWindow中显示的位置
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

    private void logout() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        UserStatus.saveuserstatus(this, false);
        finish();
    }

    private void initView() {
        user_name = (TextView) findViewById(R.id.usercenter_name);
        user_class = (TextView) findViewById(R.id.usercenter_class);
        user_major = (TextView) findViewById(R.id.usercenter_major);
        user_departments = (TextView) findViewById(R.id.usercenter_departments);
        user_num = (TextView) findViewById(R.id.usercenter_num);
        user_logout = (TextView) findViewById(R.id.usercenter_logout);
        fbtn_head = (RoundedImageView) findViewById(R.id.fbtn_head);

        User userinfo =UserStatus.getUserInfo(this);
        if (userinfo.getUser_img()!=null){
            BitmapFactory.Options option = new BitmapFactory.Options();
            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
            option.inSampleSize = 1;
            // 根据图片的SDCard路径读出Bitmap
            Log.e("userinfo.getUser_img()",userinfo.getUser_img()+"");
            Bitmap bm = BitmapFactory.decodeFile(userinfo.getUser_img(), option);
            fbtn_head.setImageBitmap(bm);

        }

    }


    public static SelectPicPopupWindow menuWindow;
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    takePhoto();
                    break;
                case R.id.btn_pick_photo:
                    pickPhoto();
                default:
                    break;
            }


        }

    };

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        // 如果要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 点击取消按钮
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        // 可以使用同一个方法，这里分开写为了防止以后扩展不同的需求
        switch (requestCode) {
            case 1:// 如果是直接从相册获取
                doPhoto(requestCode, data);
                break;
            case 0:// 如果是调用相机拍照时
                doPhoto(requestCode, data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    Uri photoUri;
    String picPath;

    private void doPhoto(int requestCode, Intent data) {

        // 从相册取图片，有些手机有异常情况，请注意
        if (requestCode == 1) {
            if (data == null) {
                Toast.makeText(this, "选择图片文件路径出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件路径出错", Toast.LENGTH_LONG).show();
                return;
            }
        }

        String[] pojo = {MediaStore.MediaColumns.DATA};
        // The method managedQuery() from the type Activity is deprecated
        //Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
        Cursor cursor = getApplicationContext().getContentResolver().query(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);


            // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }

        // 如果图片符合要求将其上传到服务器
        if (picPath != null && (picPath.endsWith(".png") ||
                picPath.endsWith(".PNG") ||
                picPath.endsWith(".jpg") ||
                picPath.endsWith(".JPG"))) {


            BitmapFactory.Options option = new BitmapFactory.Options();
            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
            option.inSampleSize = 1;
            // 根据图片的SDCard路径读出Bitmap
            Bitmap bm = BitmapFactory.decodeFile(picPath, option);
            Log.e("pic", picPath + "===");
            saveImg(picPath);
            // 显示在图片控件上
            fbtn_head.setImageBitmap(bm);

//            pd = ProgressDialog.show(getApplicationContext(), null," 正在上传图片，请稍候...");
//            new Thread(uploadImageRunnable).start();
        } else {
            Toast.makeText(this, "请选择png或jpg格式的文件", Toast.LENGTH_LONG).show();
        }

    }

    private void saveImg(String picPath) {
        File file = new File(picPath);
        Log.e("abd",file.getPath() + "=="+file.getName()+"");
       // String end = file.getName();
        CopySdcardFile(file.getPath(),getApplicationContext().getFilesDir()+file.getName());

        User userinfo =UserStatus.getUserInfo(this);
        userinfo.setUser_img(getApplicationContext().getFilesDir()+file.getName());
        UserStatus.savaUserInfo(this,userinfo);

        Log.e("abd",getApplicationContext().getFilesDir()+file.getName() + "=="+file.getName()+"");
    }

    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception ex) {
            return -1;
        }
    }
}
