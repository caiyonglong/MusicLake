package com.cyl.musiclake.di.component;

import android.app.Activity;
import android.content.Context;

import com.cyl.musiclake.di.module.ActivityModule;
import com.cyl.musiclake.di.scope.ContextLife;
import com.cyl.musiclake.di.scope.PerActivity;
import com.cyl.musiclake.ui.chat.ChatActivity;
import com.cyl.musiclake.ui.chat.ChatDetailActivity;
import com.cyl.musiclake.ui.music.artist.detail.ArtistDetailActivity;
import com.cyl.musiclake.ui.music.charts.activity.BaiduMusicListActivity;
import com.cyl.musiclake.ui.music.charts.activity.BasePlaylistActivity;
import com.cyl.musiclake.ui.music.edit.EditSongListActivity;
import com.cyl.musiclake.ui.music.mv.BaiduMvDetailActivity;
import com.cyl.musiclake.ui.music.mv.MvDetailActivity;
import com.cyl.musiclake.ui.music.playlist.detail.PlaylistDetailActivity;
import com.cyl.musiclake.ui.music.playpage.LockScreenPlayerActivity;
import com.cyl.musiclake.ui.music.playpage.PlayerActivity;
import com.cyl.musiclake.ui.music.search.SearchActivity;
import com.cyl.musiclake.ui.my.BindLoginActivity;
import com.cyl.musiclake.ui.my.LoginActivity;
import com.cyl.musiclake.ui.my.RegisterActivity;

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

    void inject(BaiduMusicListActivity baiduMusicListActivity);


    void inject(RegisterActivity registerActivity);

    void inject(LoginActivity loginActivity);

    void inject(SearchActivity searchActivity);

    void inject(BasePlaylistActivity basePlaylistActivity);

    void inject(MvDetailActivity mvDetailActivity);

    void inject(@NotNull PlayerActivity playerActivity);

    void inject(PlaylistDetailActivity playlistDetailActivity);

    void inject(ArtistDetailActivity playlistDetailActivity);

    void inject(EditSongListActivity editMusicActivity);

    void inject(@NotNull ChatActivity chatActivity);

    void inject(@NotNull ChatDetailActivity chatDetailActivity);

    void inject(@NotNull BaiduMvDetailActivity baiduMvDetailActivity);

    void inject(@NotNull LockScreenPlayerActivity lockScreenPlayerActivity);

    void inject(@NotNull BindLoginActivity bindLoginActivity);
}
