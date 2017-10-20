package com.cyl.music_hnust.ui.activity.clipheadphoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.bean.user.User;
import com.cyl.music_hnust.bean.user.UserStatus;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageTools;
import com.cyl.music_hnust.utils.ToastUtils;

public class ClipActivity extends Activity {
    private ClipImageLayout mClipImageLayout;
    private String path;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipimage);
        final User user = UserStatus.getUserInfo(this);

        //这步必须要加
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("请稍后...");
        path = getIntent().getStringExtra("path");
//		if(TextUtils.isEmpty(path)||!(new File(path).exists())){
//			Toast.makeText(this, "图片加载失败",Toast.LENGTH_SHORT).show();
//			return;
//		}
        Bitmap bitmap = ImageTools.convertToBitmap(path, 600, 600);
//		Bitmap bitmap= BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setBitmap(bitmap);
        ((Button) findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = mClipImageLayout.clip();

                        boolean saveStatus = ImageTools.savePhotoToSDCard(bitmap, FileUtils.getImageDir(), user.getUser_id() + ".png");
                        if (saveStatus) {
                            ToastUtils.show(getApplicationContext(), "保存成功");
                        } else {
                            ToastUtils.show(getApplicationContext(), "保存失败");
                        }
                        loadingDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("path", path);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).start();
            }
        });
    }
}