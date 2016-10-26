package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * Created by 永龙 on 2016/3/15.
 */
public class EditActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_content)
    EditText mEditText;
    String user_id;
    String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);

        content = getIntent().getStringExtra("content");
        mEditText.setText(content+"");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void listener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if (id == R.id.menu_send) {
            content = mEditText.getText().toString().trim();
            if (content.length()==0||content==null){
                ToastUtils.show(this,"不能发送空");
                return true;
            }
            User user = UserStatus.getUserInfo(this);
            user_id = user.getUser_id();
            if (user_id==null||user_id.length()==0){
                ToastUtils.show(this,"请登录");
                return true;
            }
            sendSecret(user_id,content);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendSecret(String user_id,String comment){
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC,Constants.SECRET_ADD)
                .addParams(Constants.USER_ID,user_id)
                .addParams(Constants.CONTENT,comment)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("EEEE","+++");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("EEEE",response);
                    }
                });
    }

}
