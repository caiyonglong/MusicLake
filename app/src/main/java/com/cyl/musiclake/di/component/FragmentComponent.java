package com.cyl.musiclake.di.component;

import android.app.Activity;
import android.content.Context;


import com.cyl.musiclake.di.module.FragmentModule;
import com.cyl.musiclake.di.scope.ContextLife;
import com.cyl.musiclake.di.scope.PerFragment;
import com.cyl.musiclake.ui.map.ShakeActivity;
import com.cyl.musiclake.ui.music.list.fragment.AlbumDetailFragment;
import com.cyl.musiclake.ui.music.list.fragment.AlbumFragment;
import com.cyl.musiclake.ui.music.list.fragment.ArtistFragment;
import com.cyl.musiclake.ui.music.list.fragment.ArtistSongsFragment;
import com.cyl.musiclake.ui.music.list.fragment.FolderSongsFragment;
import com.cyl.musiclake.ui.music.list.fragment.FoldersFragment;
import com.cyl.musiclake.ui.music.list.fragment.LoveFragment;
import com.cyl.musiclake.ui.music.list.fragment.MyMusicFragment;
import com.cyl.musiclake.ui.music.list.fragment.PlayControlFragment;
import com.cyl.musiclake.ui.music.list.fragment.PlaylistDetailFragment;
import com.cyl.musiclake.ui.music.list.fragment.RecentlyFragment;
import com.cyl.musiclake.ui.music.list.fragment.SongsFragment;
import com.cyl.musiclake.ui.music.online.fragment.BaiduPlaylistFragment;
import com.cyl.musiclake.ui.music.online.fragment.DownloadedFragment;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(AlbumDetailFragment fragment);

    void inject(AlbumFragment fragment);

    void inject(ArtistFragment artistFragment);

    void inject(ArtistSongsFragment artistSongsFragment);

    void inject(FoldersFragment foldersFragment);

    void inject(BaiduPlaylistFragment baiduPlaylistFragment);

    void inject(RecentlyFragment recentlyFragment);

    void inject(PlaylistDetailFragment playlistDetailFragment);

    void inject(SongsFragment songsFragment);

    void inject(PlayControlFragment playControlFragment);

    void inject(MyMusicFragment myMusicFragment);

    void inject(LoveFragment loveFragment);

    void inject(FolderSongsFragment folderSongsFragment);

    void inject(DownloadedFragment downloadedFragment);
}
