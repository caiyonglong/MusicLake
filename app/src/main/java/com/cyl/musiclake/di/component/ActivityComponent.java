package com.cyl.musiclake.di.component;

import android.app.Activity;
import android.content.Context;

import com.cyl.musiclake.di.module.ActivityModule;
import com.cyl.musiclake.di.scope.ContextLife;
import com.cyl.musiclake.di.scope.PerActivity;
import com.cyl.musiclake.ui.map.ShakeActivity;
import com.cyl.musiclake.ui.music.mv.MvDetailActivity;
import com.cyl.musiclake.ui.music.online.activity.ArtistInfoActivity;
import com.cyl.musiclake.ui.music.online.activity.BaiduMusicListActivity;
import com.cyl.musiclake.ui.music.online.base.BasePlaylistActivity;
import com.cyl.musiclake.ui.music.player.PlayerActivity;
import com.cyl.musiclake.ui.music.playlist.PlaylistDetailActivity;
import com.cyl.musiclake.ui.music.search.SearchActivity;
import com.cyl.musiclake.ui.my.LoginActivity;
import com.cyl.musiclake.ui.my.RegisterActivity;
import com.cyl.musiclake.ui.my.UserCenterActivity;

import org.jetbrains.annotations.NotNull;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(ShakeActivity activity);

    void inject(ArtistInfoActivity artistInfoActivity);

    void inject(BaiduMusicListActivity baiduMusicListActivity);

    void inject(UserCenterActivity userCenterActivity);

    void inject(RegisterActivity registerActivity);

    void inject(LoginActivity loginActivity);

    void inject(SearchActivity searchActivity);

    void inject(BasePlaylistActivity basePlaylistActivity);

    void inject(MvDetailActivity mvDetailActivity);

    void inject(@NotNull PlayerActivity playerActivity);

    void inject(PlaylistDetailActivity playlistDetailActivity);
}
