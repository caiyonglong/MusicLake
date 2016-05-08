package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.cloud.CloudSearch;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.fragment.MyFragment;
import com.cyl.music_hnust.http.HttpUtil;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.ToastUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * Created by trumi on 16-3-2.
 */
public class UserCenterMainAcivity extends AppCompatActivity implements View.OnClickListener {

    private TextView user_name;
    private TextView user_num;
    private TextView user_departments;
    private TextView user_class;
    private static TextView user_nick;
    private TextView user_major;
    private static TextView user_email;
    private static TextView user_phone;
    private CardView user_logout;

    private ImageView head;
    private FloatingActionButton fab_a, fab_b;


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
    private static Context mContext;//图片全路径
    private User userinfo;

    private MyHandler handler;


    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mactivity;

        private MyHandler(Activity activity) {
            mactivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mactivity.get();
            switch (msg.what) {
                case 0:
                    User userinfo = UserStatus.getUserInfo(activity);
                    //电话邮箱
                    if (userinfo.getPhone() != null) {
                        user_phone.setText(userinfo.getPhone());
                    } else {
                        user_phone.setText("暂无");
                    }
                    if (userinfo.getUser_email() != null) {
                        user_email.setText(userinfo.getUser_email());
                    } else {
                        user_email.setText("暂无");
                    }
                    if (userinfo.getNick() != null) {
                        user_nick.setText(userinfo.getNick());
                    } else {
                        user_nick.setText("暂无");
                    }

                    Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_center_main);
        mContext = getApplicationContext();
        handler = new MyHandler(this);

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

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        File file = new File(Environment.getExternalStorageDirectory(), "hkmusic/cache");
        if (!file.exists())
            file.mkdirs();
        photoSavePath = Constants.DEFAULT_USERIMG_PATH;
        photoSaveName = System.currentTimeMillis() + ".png";

        initView();

        //设置工具栏标题
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("个人中心");


        userinfo = UserStatus.getUserInfo(this);
        if (userinfo.getUser_name() != null) {
            user_name.setText(userinfo.getUser_name());
            user_num.setText(userinfo.getUser_id());
            user_departments.setText(userinfo.getUser_college());
            user_class.setText(userinfo.getUser_class());
            user_major.setText(userinfo.getUser_major());

            //电话邮箱
            if (userinfo.getPhone() != null) {
                user_phone.setText(userinfo.getPhone());
            } else {
                user_phone.setText("暂无");
            }
            if (userinfo.getUser_email() != null) {
                user_email.setText(userinfo.getUser_email());
            } else {
                user_email.setText("暂无");
            }
            if (userinfo.getNick() != null) {
                user_nick.setText(userinfo.getNick());
            } else {
                user_nick.setText("暂无");
            }


            if (userinfo.getUser_img() != null) {
                path = userinfo.getUser_img();

            }
            path = Environment.getExternalStorageDirectory() + "/hkmusic/cache/" + userinfo.getUser_id() + ".png";
            File file1 = new File(path);
            if (file1.exists())
                head.setImageBitmap(getLoacalBitmap(path));
            else {
                head.setImageResource(R.mipmap.user_icon_default_main);
            }

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
                                sDialog.dismiss();
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
        user_logout = (CardView) findViewById(R.id.usercenter_logout);
        user_nick = (TextView) findViewById(R.id.usercenter_sign);

        user_email = (TextView) findViewById(R.id.usercenter_email);
        user_phone = (TextView) findViewById(R.id.usercenter_phone);

        head = (ImageView) findViewById(R.id.head);
        head.setOnClickListener(this);

        fab_a = (FloatingActionButton) findViewById(R.id.action_a);
        fab_b = (FloatingActionButton) findViewById(R.id.action_b);
        fab_a.setOnClickListener(this);
        fab_b.setOnClickListener(this);
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.head:
                showPopupWindow(head);
                break;
            case R.id.action_a:
                modify("123");
                break;
            case R.id.action_b:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

        }
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
                Intent intent3 = new Intent(UserCenterMainAcivity.this, ClipActivity.class);
                intent3.putExtra("path", path);
                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;
            case PHOTOTAKE://拍照
                path = photoSavePath + photoSaveName;
                uri = Uri.fromFile(new File(path));
                Intent intent2 = new Intent(UserCenterMainAcivity.this, ClipActivity.class);
                intent2.putExtra("path", path);
                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;
            case IMAGE_COMPLETE:
                final String temppath = data.getStringExtra("path");
                User user = UserStatus.getUserInfo(getApplicationContext());
                user.setUser_img(temppath);
                UserStatus.savaUserInfo(getApplicationContext(), user);
                head.setImageBitmap(getLoacalBitmap(temppath));
                path = Constants.DEFAULT_USERIMG_PATH + user.getUser_id() + ".png";

                try {
                    uploadFile(path, Constants.DEFAULT_IMG_UPLOAD);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 上传失败后要做到工作
                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_LONG).show();
                }
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

    /**
     * 修改对话框
     *
     * @param msg
     */

    private TableLayout update;
    private EditText nick;
    private EditText email;
    private EditText phone;


    public void modify(String msg) {

        userinfo = UserStatus.getUserInfo(this);
        update =
                (TableLayout) getLayoutInflater().inflate(R.layout.info_modify, null);
        nick = (EditText) update.findViewById(R.id.edt_nick);
        nick.setText(userinfo.getNick() == null ? "" : userinfo.getNick());
        email = (EditText) update.findViewById(R.id.edt_email);
        email.setText(userinfo.getUser_email() == null ? "" : userinfo.getUser_email());
        phone = (EditText) update.findViewById(R.id.edt_phone);
        phone.setText(userinfo.getPhone() == null ? "" : userinfo.getPhone());

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("修改信息")
                .setIcon(R.mipmap.usercenter_revise)
                .setView(update);
        setNegativeButton(builder);

        setPositiveButton(builder)
                .create()
                .show();
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(nick.getText().toString().trim()) &&
                        !TextUtils.isEmpty(email.getText().toString().trim()) &&
                        !TextUtils.isEmpty(phone.getText().toString().trim())) {
                    UpdateUserinfo(nick.getText().toString().trim()
                            , email.getText().toString().trim(),
                            phone.getText().toString().trim());
                } else {
                    ToastUtil.show(getApplicationContext(), "你漏了哦0.0");
                }
            }
        });
    }

    /**
     * @param path 要上传的文件路径
     * @param url  服务端接收URL
     * @throws Exception
     */
    public static void uploadFile(String path, String url) throws Exception {
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("uploadfile", file);
            params.put("user_id", UserStatus.getUserInfo(mContext).getUser_id());
            // 上传文件
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    // 上传成功后要做的工作
                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_LONG).show();
                    //   progress.setProgress(0);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    // 上传失败后要做到工作
                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onRetry(int retryNo) {
                    // TODO Auto-generated method stub
                    super.onRetry(retryNo);
                    // 返回重试次数
                }

            });
        } else {
            Toast.makeText(mContext, "文件不存在", Toast.LENGTH_LONG).show();
        }

    }


    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void UpdateUserinfo(final String nick, final String email, final String phone) {


        final User user = UserStatus.getUserInfo(getApplicationContext());

        String updateurl = Constants.DEFAULT_URL+"updateUser&secret&nick=" + nick + "&user_id=" + user.getUser_id()
                + "&user_email=" + email
                + "&phone=" + phone;

        HttpUtil.get(updateurl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody, "utf-8");
                    if (str.endsWith(":10000}")) {
                        user.setNick(nick);
                        user.setUser_email(email);
                        user.setPhone(phone);
                        UserStatus.savaUserInfo(getApplicationContext(),user);
                        handler.sendEmptyMessage(0);

                    } else {
                        Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "修改失败，网络异常", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
