package com.cyl.music_hnust.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;

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
//    public static void navigateToAlbum(Activity context, long albumID, boolean isAlbum, String title) {
//
//        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.activity_fade_in,
//                R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
//        Fragment fragment = AlbumDetailFragment.newInstance(albumID, isAlbum, title, null);
//
//        transaction.hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container));
//        transaction.add(R.id.fragment_container, fragment);
//        transaction.addToBackStack(title).commit();
//    }


    /**
     * 跳转到专辑
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void navigateToAlbum(Activity context, long albumID, boolean isAlbum, String title, Pair<View, String> transitionViews) {

        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (transitionViews != null) {
            Transition changeImage = TransitionInflater.from(context).inflateTransition(R.transition.image_transform);
            transaction.addSharedElement(transitionViews.first, transitionViews.second);
            fragment = AlbumDetailFragment.newInstance(albumID, isAlbum, title,transitionViews.second);
            fragment.setSharedElementEnterTransition(changeImage);
        } else {
            transaction.setCustomAnimations(R.anim.activity_fade_in,
                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = AlbumDetailFragment.newInstance(albumID, isAlbum, title,null);
        }
        transaction.hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(title).commit();
    }
}
