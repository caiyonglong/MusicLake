package com.cyl.music_hnust.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;

import com.cyl.music_hnust.R;

/**
 * 作者：yonglong on 2016/8/11 18:38
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button bt_go = (Button) findViewById(R.id.bt_go);
        fab.setOnClickListener(this);
        bt_go.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                finish();
                break;
            case R.id.bt_go:
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
