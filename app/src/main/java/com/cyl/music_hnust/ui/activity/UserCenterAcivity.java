package com.cyl.music_hnust.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.ui.activity.clipheadphoto.ClipActivity;
import com.cyl.music_hnust.callback.StatusCallback;
import com.cyl.music_hnust.callback.UserCallBack;
import com.cyl.music_hnust.model.user.StatusInfo;
import com.cyl.music_hnust.model.user.User;
import com.cyl.music_hnust.model.user.UserInfo;
import com.cyl.music_hnust.model.user.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 用户中心
 */
public class UserCenterAcivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.header_img)
    CircleImageView header_img;
    @Bind(R.id.action_a)
    FloatingActionButton fab_a;
    @Bind(R.id.action_b)
    FloatingActionButton fab_b;
    @Bind(R.id.action_menu)
    FloatingActionsMenu action_menu;

    @OnClick(R.id.header_img)
    void header_img(View view) {
        showPopupWindow(view);
    }

    TextInputLayout et_name, et_nick, et_phone, et_email;

    @OnClick(R.id.action_a)
    void a() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("修改信息")
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("提交")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String username = et_name.getEditText().getText().toString();
                        String nick = et_nick.getEditText().getText().toString();
                        String email = et_email.getEditText().getText().toString();
                        String phone = et_phone.getEditText().getText().toString();


                        //更新参数
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(Constants.FUNC, Constants.UPDATE_USER);
                        params.put(Constants.USER_ID, user.getUser_id());
                        params.put(Constants.USERNAME, username);
                        params.put(Constants.NICK, nick);
                        params.put(Constants.USER_EMAIL, email);
                        params.put(Constants.PHONE, phone);

                        update(params);
                    }
                })
                .customView(R.layout.info_modify, true)
                .build();

        et_name = (TextInputLayout) dialog.getCustomView().findViewById(R.id.userNameWrapper);
        et_nick = (TextInputLayout) dialog.getCustomView().findViewById(R.id.nickWrapper);
        et_phone = (TextInputLayout) dialog.getCustomView().findViewById(R.id.phoneWrapper);
        et_email = (TextInputLayout) dialog.getCustomView().findViewById(R.id.emailWrapper);

        et_name.getEditText().setText(user.getUser_name());
        et_nick.getEditText().setText(user.getNick());
        et_phone.getEditText().setText(user.getPhone());
        et_email.getEditText().setText(user.getUser_email());

        dialog.show();
        if (action_menu.isExpanded()) {
            action_menu.collapse();
        }
    }

    @OnClick(R.id.action_b)
    void b() {
        final int item = user.getSecret();

        new MaterialDialog.Builder(this)
                .title(R.string.info_secret_setting)
                .content(R.string.info_secret_setting_detail)
                .items(R.array.info_secret_setting)
                .itemsCallbackSingleChoice(item - 1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        //更新参数
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(Constants.FUNC, Constants.UPDATE_USER);
                        params.put(Constants.USER_ID, user.getUser_id());
                        params.put(Constants.SECRET, String.valueOf(which + 1));

                        update(params);
                        return true; // allow selection
                    }
                })
                .positiveText("选择")
                .show();
        if (action_menu.isExpanded()) {
            action_menu.collapse();
        }
    }

    @Bind(R.id.nick)
    TextView nick;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.logout)
    CardView logout;


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
    private User user;//图片全路径


    @Override
    protected int getLayoutResID() {
        return R.layout.user_center_main;
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

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initData() {
        user = UserStatus.getUserInfo(this);


        if (user.getNick() == null || user.getNick().length() <= 0) {
            nick.setText("");
        } else {
            nick.setText(user.getNick());
        }

        email.setText(user.getUser_email());
        phone.setText(user.getPhone());
        user_name.setText(user.getUser_name() + "");

        ImageLoader.getInstance().displayImage(user.getUser_img(), header_img, ImageUtils.getAlbumDisplayOptions());

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
                path = FileUtils.getImageDir() + user.getUser_id() + ".png";
                upload(UserStatus.getUserInfo(this).getUser_id(), path);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 上传图片
     *
     * @param user_id
     * @param path
     */
    private void upload(String user_id, String path) {
        File file = new File(path);
        if (!file.exists()) {
            ToastUtils.show(UserCenterAcivity.this, "上传失败，图片文件不存在");
        }
        OkHttpUtils.post().url(Constants.UPLOAD_URL)
                .addFile("file", user_id + ".png", file)
                .addParams(Constants.USER_ID, user_id)
                .build()
                .execute(new StatusCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("eee", "ccccc");
                        ToastUtils.show(UserCenterAcivity.this, "上传失败，请检查网络");
                    }

                    @Override
                    public void onResponse(StatusInfo response) {
                        Log.e("eee", "ddd");
                        if (response.getStatus() == 1) {
                            user.setUser_img(response.getImgurl());
                            UserStatus.savaUserInfo(getApplicationContext(), user);
                            user = UserStatus.getUserInfo(getApplicationContext());
                            Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImageDir() + user.getUser_id() + ".png");
                            if (bitmap != null) {
                                header_img.setImageBitmap(bitmap);
                                bitmap.recycle();
                            } else
                                ImageLoader.getInstance().displayImage(user.getUser_img(), header_img, ImageUtils.getAlbumDisplayOptions());
                        }
                        ToastUtils.show(UserCenterAcivity.this, response.getMessage().toString());
                    }
                });
    }

    /**
     * 更新信息
     *
     * @param params
     */
    private void update(final Map<String, String> params) {
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .params(params)
                .build()
                .execute(new UserCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtils.show(UserCenterAcivity.this, R.string.error_connection);
                    }

                    @Override
                    public void onResponse(UserInfo response) {
                        if (response.getStatus() == 1) {
                            ToastUtils.show(UserCenterAcivity.this, "更新成功");
                            Log.e("----", response.getMessage() + "");
                            Log.e("----", response.getStatus() + "");
                            Log.e("----", response.getUser().toString() + "");

                            UserStatus.savaUserInfo(UserCenterAcivity.this, response.getUser());
                            user = response.getUser();
                            //更新title
                            user_name.setText(user.getUser_name() + "");

                        } else {
                            ToastUtils.show(UserCenterAcivity.this, response.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImageDir() + user.getUser_id() + ".png");
        if (bitmap != null) {
            header_img.setImageBitmap(bitmap);
            bitmap.recycle();
        } else
            ImageLoader.getInstance().displayImage(user.getUser_img(), header_img, ImageUtils.getAlbumDisplayOptions());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (action_menu.isExpanded()) {
            action_menu.collapse();
        } else {
            finish();
        }
    }
}
