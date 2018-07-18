package com.cyl.musiclake.ui.music.local.fragment;

import android.content.Context;
import android.content.Intent;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.common.NavigationHelper;

/**
 * Created by Monkey on 2015/6/29.
 */
public class CommonActivity extends BaseActivity<BasePresenter> {

    @Override
    protected int getLayoutResID() {
        return R.layout.include_main;
    }

    @Override
    protected String setToolbarTitle() {
        return getResources().getString(R.string.local_music);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String path = getIntent().getStringExtra(Extras.FOLDER_PATH);
        NavigationHelper.INSTANCE.navigateToFolderSongs(this, path);
    }

    @Override
    protected void initInjector() {

    }

    public static void newInstance(Context context, String folderPath) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(Extras.FOLDER_PATH, folderPath);
        context.startActivity(intent);
    }
}
