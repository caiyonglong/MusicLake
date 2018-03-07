package com.cyl.musiclake.ui.login;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.ui.login.user.UserStatus;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户中心
 */
public class UserCenterActivity extends BaseActivity implements UserContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.header_img)
    CircleImageView header_img;

    @OnClick(R.id.header_img)
    void updateHeader(View view) {
        showPopupWindow(view);
    }

    @BindView(R.id.nick)
    TextView nick;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.logout)
    CardView logout;

    @OnClick(R.id.logout)
    void logout() {
        mPresenter.logout();
        mPresenter.subscribe();
        finish();
    }

    private PopupWindow popWindow;
    private LayoutInflater layoutInflater;
    private TextView photograph, albums;
    private LinearLayout cancel;
    private TextInputLayout et_name, et_nick, et_phone, et_email;


    public static final int PHOTOZOOM = 0; // 相册/拍照
    public static final int PHOTOTAKE = 1; // 相册/拍照
    public static final int IMAGE_COMPLETE = 2; // 结果
    public static final int CROPREQCODE = 3; // 截取
    private String photoSavePath;//保存路径
    private String photoSaveName;//图pian名
    private String path;//图片全路径
    private User mUserInfo;//图片全路径

    private UserPresenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.user_center_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_update:
                updateInfo();
                break;
            case R.id.action_privacy:
                changePrivacy();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        mPresenter = new UserPresenter();
        mPresenter.attachView(this);
        mPresenter.getUserInfo();
    }


    private void updateInfo() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("修改信息")
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("提交")
                .negativeText("取消")
                .onPositive((dialog1, which) -> {
                    String username = et_name.getEditText().getText().toString();
                    String nick = et_nick.getEditText().getText().toString();
                    String email = et_email.getEditText().getText().toString();
                    String phone = et_phone.getEditText().getText().toString();


                    //更新参数
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constants.FUNC, Constants.UPDATE_USER);
                    params.put(Constants.USER_ID, mUserInfo.getId());
                    params.put(Constants.USERNAME, username);
                    params.put(Constants.NICK, nick);
                    params.put(Constants.USER_EMAIL, email);
                    params.put(Constants.PHONE, phone);

                    mPresenter.updateInfo(params);
                })
                .customView(R.layout.info_modify, true)
                .build();

        et_name = (TextInputLayout) dialog.getCustomView().findViewById(R.id.userNameWrapper);
        et_nick = (TextInputLayout) dialog.getCustomView().findViewById(R.id.nickWrapper);
        et_phone = (TextInputLayout) dialog.getCustomView().findViewById(R.id.phoneWrapper);
        et_email = (TextInputLayout) dialog.getCustomView().findViewById(R.id.emailWrapper);

        et_name.getEditText().setText(mUserInfo.getName());
        et_nick.getEditText().setText(mUserInfo.getNick());
        et_phone.getEditText().setText(mUserInfo.getPhone());
        et_email.getEditText().setText(mUserInfo.getEmail());

        dialog.show();
    }

    void changePrivacy() {
        final int item = mUserInfo.getSecret();

        new MaterialDialog.Builder(this)
                .title(R.string.info_secret_setting)
                .content(R.string.info_secret_setting_detail)
                .items(R.array.info_secret_setting)
                .itemsCallbackSingleChoice(item - 1, (dialog, view, which, text) -> {
                    //更新参数
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constants.FUNC, Constants.UPDATE_USER);
                    params.put(Constants.USER_ID, mUserInfo.getId());
                    params.put(Constants.SECRET, String.valueOf(which + 1));

                    mPresenter.updateInfo(params);
                    return true; // allow selection
                })
                .positiveText("选择")
                .show();
    }

    /**
     * 显示popWindow
     *
     * @param parent
     */
    @SuppressWarnings("deprecation")
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_image_select, null);
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
        photograph.setOnClickListener(arg0 -> {
            popWindow.dismiss();
            photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
            Uri imageUri = null;
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
            openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, PHOTOTAKE);
        });
        albums.setOnClickListener(arg0 -> {
            popWindow.dismiss();
            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(openAlbumIntent, PHOTOZOOM);
        });
        cancel.setOnClickListener(arg0 -> popWindow.dismiss());
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
//                if (data == null) {
//                    return;
//                }
//                uri = data.getData();
//                String[] proj = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                path = cursor.getString(column_index);// 图片在的路径
//                Intent intent3 = new Intent(UserCenterActivity.this, ClipActivity.class);
//                intent3.putExtra("path", path);
//                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;
            case PHOTOTAKE://拍照
//                path = photoSavePath + photoSaveName;
//                uri = Uri.fromFile(new File(path));
//                Intent intent2 = new Intent(UserCenterActivity.this, ClipActivity.class);
//                intent2.putExtra("path", path);
//                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;
            case IMAGE_COMPLETE:
                final String userId = UserStatus.getUserInfo(this).getId();
                path = FileUtils.getImageDir() + mUserInfo.getId() + ".png";
                //上传图片
                mPresenter.uploadHeader(userId, path);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorInfo(String msg) {
        ToastUtils.show(this, msg);
    }

    @Override
    public void updateView(User user) {
        if (user != null) {
            mUserInfo = user;
            nick.setText(user.getNick());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
            user_name.setText(user.getName());

            GlideApp.with(this)
                    .load(user.getUrl())
                    .into(header_img);
        } else {
            ToastUtils.show(this, "数据加载失败");
            finish();
        }
    }
}
