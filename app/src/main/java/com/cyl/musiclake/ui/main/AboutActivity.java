package com.cyl.musiclake.ui.main;

import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lw on 2018/2/12.
 */
public class AboutActivity extends BaseActivity {
    private final String url = "https://github.com/caiyonglong/MusicLake";
    private final String url1 = "https://github.com/caiyonglong/MusicLake/issues/new";
    private final String url2 = "https://github.com/caiyonglong/MusicLake/blob/develop/README.md";

    @BindView(R.id.version)
    TextView mVersion;

    @OnClick(R.id.tv_project)
    void show() {
        WebActivity.start(this, "项目地址", url);
    }

    @OnClick(R.id.ll_feedback)
    void feedback() {
        WebActivity.start(this, "意见反馈", url1);
    }

    @OnClick(R.id.ll_introduce)
    void introduce() {
        WebActivity.start(this, "关于软件", url2);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        try {
            String version = MusicApp.getAppContext()
                    .getPackageManager()
                    .getPackageInfo(MusicApp.getAppContext().getPackageName(), 0).versionName;
            mVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
