package com.cyl.musiclake.di.component;

import android.app.Activity;
import android.content.Context;

import com.cyl.musiclake.di.module.FragmentModule;
import com.cyl.musiclake.di.scope.ContextLife;
import com.cyl.musiclake.di.scope.PerFragment;
import com.cyl.musiclake.ui.music.discover.DiscoverFragment;
import com.cyl.musiclake.download.ui.DownloadManagerFragment;
import com.cyl.musiclake.ui.music.local.fragment.AlbumDetailFragment;
import com.cyl.musiclake.ui.music.local.fragment.AlbumFragment;
import com.cyl.musiclake.ui.music.local.fragment.ArtistFragment;
import com.cyl.musiclake.ui.music.local.fragment.ArtistSongsFragment;
import com.cyl.musiclake.ui.music.local.fragment.FolderSongsFragment;
import com.cyl.musiclake.ui.music.local.fragment.FoldersFragment;
import com.cyl.musiclake.ui.music.mv.MvListFragment;
import com.cyl.musiclake.ui.music.online.fragment.NeteasePlaylistFragment;
import com.cyl.musiclake.ui.music.player.CoverFragment;
import com.cyl.musiclake.ui.music.playlist.LoveFragment;
import com.cyl.musiclake.ui.music.local.fragment.MyMusicFragment;
import com.cyl.musiclake.ui.music.player.PlayControlFragment;
import com.cyl.musiclake.ui.music.playlist.PlaylistDetailFragment;
import com.cyl.musiclake.ui.music.playlist.RecentlyFragment;
import com.cyl.musiclake.ui.music.local.fragment.SongsFragment;
import com.cyl.musiclake.ui.music.online.fragment.BaiduPlaylistFragment;
import com.cyl.musiclake.download.ui.DownloadedFragment;
import com.cyl.musiclake.ui.music.playqueue.PlayQueueFragment;

import org.jetbrains.annotations.NotNull;

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

    void inject(DiscoverFragment discoverFragment);

    void inject(PlayQueueFragment playQueueFragment);

    void inject(DownloadManagerFragment downloadManagerFragment);

    void inject(MvListFragment mvListFragment);

    void inject(@NotNull CoverFragment coverFragment);

    void inject(@NotNull NeteasePlaylistFragment neteasePlaylistFragment);
}
