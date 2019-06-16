package com.cyl.musiclake.di.component;

import android.app.Activity;
import android.content.Context;

import com.cyl.musiclake.di.module.FragmentModule;
import com.cyl.musiclake.di.scope.ContextLife;
import com.cyl.musiclake.di.scope.PerFragment;
import com.cyl.musiclake.ui.music.discover.artist.QQArtistListFragment;
import com.cyl.musiclake.ui.music.discover.DiscoverFragment;
import com.cyl.musiclake.ui.download.ui.DownloadManagerFragment;
import com.cyl.musiclake.ui.music.local.fragment.AlbumDetailFragment;
import com.cyl.musiclake.ui.music.local.fragment.AlbumFragment;
import com.cyl.musiclake.ui.music.local.fragment.ArtistFragment;
import com.cyl.musiclake.ui.music.local.fragment.ArtistSongsFragment;
import com.cyl.musiclake.ui.music.local.fragment.LocalVideoFragment;
import com.cyl.musiclake.ui.music.local.fragment.FoldersFragment;
import com.cyl.musiclake.ui.music.mv.MvListFragment;
import com.cyl.musiclake.ui.music.charts.fragment.ChartsDetailFragment;
import com.cyl.musiclake.ui.music.mv.MvSearchListFragment;
import com.cyl.musiclake.ui.music.playlist.love.LoveFragment;
import com.cyl.musiclake.ui.music.my.MyMusicFragment;
import com.cyl.musiclake.ui.music.bottom.PlayControlFragment;
import com.cyl.musiclake.ui.music.playlist.detail.PlaylistDetailFragment;
import com.cyl.musiclake.ui.music.playlist.PlaylistFragment;
import com.cyl.musiclake.ui.music.playlist.history.RecentlyFragment;
import com.cyl.musiclake.ui.music.local.fragment.SongsFragment;
import com.cyl.musiclake.ui.download.ui.DownloadedFragment;
import com.cyl.musiclake.ui.music.playlist.square.TopPlaylistFragment;
import com.cyl.musiclake.ui.music.playqueue.PlayQueueFragment;
import com.cyl.musiclake.ui.my.BindLoginActivity;

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

    void inject(RecentlyFragment recentlyFragment);

    void inject(PlaylistDetailFragment playlistDetailFragment);

    void inject(SongsFragment songsFragment);

    void inject(PlayControlFragment playControlFragment);

    void inject(MyMusicFragment myMusicFragment);

    void inject(LoveFragment loveFragment);

    void inject(LocalVideoFragment localVideoFragment);

    void inject(DownloadedFragment downloadedFragment);

    void inject(DiscoverFragment discoverFragment);

    void inject(PlayQueueFragment playQueueFragment);

    void inject(DownloadManagerFragment downloadManagerFragment);

    void inject(MvListFragment mvListFragment);

    void inject(@NotNull ChartsDetailFragment chartsDetailFragment);

    void inject(@NotNull QQArtistListFragment QQArtistListFragment);

    void inject(MvSearchListFragment mvSearchListFragment);

    void inject(@NotNull BindLoginActivity bindLoginActivity);

    void inject(@NotNull PlaylistFragment playlistFragment);

    void inject(@NotNull TopPlaylistFragment topPlaylistFragment);
}
