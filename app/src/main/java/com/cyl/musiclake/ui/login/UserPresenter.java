package com.cyl.musiclake.ui.login;


import android.content.Context;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.api.ApiModel;
import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.utils.Constants;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/3.
 */

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View mView;
    private UserModel userModel;
    private Context mContext;

    @Override
    public void attachView(UserContract.View view) {
        mView = view;
        mContext = (Context) view;
        userModel = new UserModel(mContext);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void updateInfo(Map<String, String> params) {
        mView.showLoading();
        ApiManager.getInstance().apiService
                .getUserInfo(Constants.LOGIN_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiModel<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiModel<User> userInfo) {
                        if (userInfo.getStatus().equals("success")) {
                            //保存用户信息
                            userModel.savaInfo(userInfo.getData());
                            mView.updateView(userInfo.getData());
                        } else {
                            mView.showErrorInfo(userInfo.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void uploadHeader(String user_id, String path) {

//        File file = new File(path);
//        if (!file.exists()) {
//            ToastUtils.show(UserCenterActivity.this, "上传失败，图片文件不存在");
//        }
//        OkHttpUtils.post().url(Constants.UPLOAD_URL)
//                .addFile("file", user_id + ".png", file)
//                .addParams(Constants.USER_ID, user_id)
//                .build()
//                .execute(new StatusCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        Log.e("eee", "ccccc");
//                        ToastUtils.show(UserCenterActivity.this, "上传失败，请检查网络");
//                    }
//
//                    @Override
//                    public void onResponse(StatusInfo response) {
//                        Log.e("eee", "ddd");
//                        if (response.getStatus() == 1) {
//                            user.setUser_img(response.getImgurl());
//                            UserStatus.savaUserInfo(getApplicationContext(), user);
//                            user = UserStatus.getUserInfo(getApplicationContext());
//                            Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImageDir() + user.getUser_id() + ".png");
//                            if (bitmap != null) {
//                                header_img.setImageBitmap(bitmap);
//                                bitmap.recycle();
//                            } else
//                                ImageLoader.getInstance().displayImage(user.getUser_img(), header_img, ImageUtils.getAlbumDisplayOptions());
//                        }
//                        ToastUtils.show(UserCenterActivity.this, response.getMessage().toString());
//                    }
//                });
    }

    @Override
    public void getUserInfo() {
        mView.updateView(userModel.getUserInfo());
    }
}
