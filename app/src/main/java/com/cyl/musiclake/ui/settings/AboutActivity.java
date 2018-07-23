package com.cyl.musiclake.ui.settings;

import android.content.pm.PackageManager;
import android.widget.TextView;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.ui.main.WebActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE;
import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_ISSUES;
import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_URL;

/**
 * Created by lw on 2018/2/12.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.version)
    TextView mVersion;

    @OnClick(R.id.introduceTv)
    void introduce() {
        WebActivity.start(this, "关于软件", ABOUT_MUSIC_LAKE_URL);
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

    @Override
    protected void initInjector() {

    }

}
