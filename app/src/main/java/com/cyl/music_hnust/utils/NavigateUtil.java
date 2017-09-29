package com.cyl.music_hnust.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.ui.fragment.AlbumDetailFragment;
import com.cyl.music_hnust.ui.fragment.MainFragment;

/**
 * Created by yonglong on 2016/12/24.
 * 导航工具类
 */

public class NavigateUtil {
    /**
     * 跳转到专辑
     */
    public static void navigateToAlbum(Activity context, long albumID, boolean isAlbum, String title) {

        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.activity_fade_in,
                R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
        Fragment fragment = AlbumDetailFragment.newInstance(albumID, isAlbum, title, null);

        transaction.hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(title).commit();
    }

}
