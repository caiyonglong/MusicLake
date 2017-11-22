package com.cyl.musiclake.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cyl.musiclake.R;
import com.cyl.musiclake.callback.StatusCallback;
import com.cyl.musiclake.mvp.model.user.StatusInfo;
import com.cyl.musiclake.mvp.model.user.User;
import com.cyl.musiclake.mvp.model.user.UserStatus;
import com.cyl.musiclake.utils.Constants;
import com.cyl.musiclake.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import okhttp3.Call;


/**
 * Created by 永龙 on 2016/3/15.
 */
public class EditActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_content)
    EditText mEditText;
    String user_id;
    String content;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        content = getIntent().getStringExtra("content");
        if (!TextUtils.isEmpty(content))
            mEditText.setText(content + "");
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
        } else if (id == R.id.menu_send) {
            content = mEditText.getText().toString().trim();
            if (content.length() == 0 || content == null) {
                ToastUtils.show(this, "不能发送空");
                return true;
            }
            User user = UserStatus.getUserInfo(this);
            user_id = user.getUser_id();
            if (user_id == null || user_id.length() == 0) {
                ToastUtils.show(this, "请登录");
                return true;
            }
            sendSecret(user_id, content);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendSecret(String user_id, String comment) {
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC, Constants.SECRET_ADD)
                .addParams(Constants.USER_ID, user_id)
                .addParams(Constants.CONTENT, comment)
                .build()
                .execute(new StatusCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtils.show(EditActivity.this,"网络错误，请检查连接!");
                    }

                    @Override
                    public void onResponse(StatusInfo response) {
                        ToastUtils.show(EditActivity.this,response.getMessage());
                        Log.e("re",response.getStatus()+response.getMessage());
                        finish();
                    }
                });
    }

}
