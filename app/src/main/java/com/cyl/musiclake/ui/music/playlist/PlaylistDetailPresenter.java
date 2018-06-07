package com.cyl.musiclake.ui.music.playlist;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.api.MusicApiServiceImpl;
import com.cyl.musiclake.api.PlaylistApiServiceImpl;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by yonglong on 2018/1/7.
 */

public class PlaylistDetailPresenter extends BasePresenter<PlaylistDetailContract.View> implements PlaylistDetailContract.Presenter {
    private static final String TAG = "PlaylistDetailPresenter";
    private List<String> netease = new ArrayList<>();
    private List<String> qq = new ArrayList<>();
    private List<String> xiami = new ArrayList<>();

    @Inject
    public PlaylistDetailPresenter() {
    }

    @Override
    public void loadPlaylistSongs(String playlistID) {
        ApiManager.request(PlaylistApiServiceImpl.INSTANCE.getMusicList(playlistID), new RequestCallBack<List<Music>>() {
            @Override
            public void success(List<Music> result) {
//                mView.showPlaylistSongs(result);
                netease.clear();
                qq.clear();
                xiami.clear();
                for (Music music : result) {
                    if (music.getType() == Music.Type.NETEASE) {
                        netease.add(music.getId());
                    } else if (music.getType() == Music.Type.QQ) {
                        qq.add(music.getId());
                    } else if (music.getType() == Music.Type.XIAMI) {
                        xiami.add(music.getId());
                    }
                }
                getBatchSongDetail("netease", netease.toArray(new String[netease.size()]));
                getBatchSongDetail("qq", qq.toArray(new String[qq.size()]));
                getBatchSongDetail("xiami", xiami.toArray(new String[xiami.size()]));
            }

            @Override
            public void error(String msg) {
                LogUtil.e(TAG, msg);
                ToastUtils.show(msg);
            }
        });
    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        ApiManager.request(PlaylistApiServiceImpl.INSTANCE.deletePlaylist(playlist.getId()), new RequestCallBack<String>() {
            @Override
            public void success(String result) {
                mView.success(1);
                RxBus.getInstance().post(new PlaylistEvent());
                ToastUtils.show(result);
            }

            @Override
            public void error(String msg) {
                ToastUtils.show(msg);
            }
        });
    }

    @Override
    public void renamePlaylist(Playlist playlist, String title) {
        ApiManager.request(PlaylistApiServiceImpl.INSTANCE.renamePlaylist(playlist.getId(), title), new RequestCallBack<String>() {
            @Override
            public void success(String result) {
                mView.success(1);
                RxBus.getInstance().post(new PlaylistEvent());
                ToastUtils.show(result);
            }

            @Override
            public void error(String msg) {
                ToastUtils.show(msg);
            }
        });
    }

    @Override
    public void disCollectMusic(String pid, int position, Music music) {
        ApiManager.request(PlaylistApiServiceImpl.INSTANCE.disCollectMusic(pid, music), new RequestCallBack<String>() {
            @Override
            public void success(String result) {
                mView.removeMusic(position);
            }

            @Override
            public void error(String msg) {
                ToastUtils.show(msg);
            }
        });
    }

    public void getBatchSongDetail(String vendor, String[] ids) {
        ApiManager.request(MusicApiServiceImpl.INSTANCE.getBatchMusic(vendor, ids), new RequestCallBack<List<Music>>() {
            @Override
            public void success(List<Music> result) {
                mView.showPlaylistSongs(result);
            }

            @Override
            public void error(String msg) {
                ToastUtils.show(msg);
            }
        });
    }
}
