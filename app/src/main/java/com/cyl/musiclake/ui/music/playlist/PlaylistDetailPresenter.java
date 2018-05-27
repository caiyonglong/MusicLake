package com.cyl.musiclake.ui.music.playlist;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.api.MusicApiServiceImpl;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by yonglong on 2018/1/7.
 */

public class PlaylistDetailPresenter extends BasePresenter<PlaylistDetailContract.View> implements PlaylistDetailContract.Presenter {
    private static final String TAG = "PlaylistDetailPresenter";

    @Inject
    public PlaylistDetailPresenter() {
    }

    @Override
    public void loadPlaylistSongs(String playlistID) {
        ApiManager.request(MusicApiServiceImpl.INSTANCE.getMusicList(playlistID), new RequestCallBack<List<Music>>() {
            @Override
            public void success(List<Music> result) {
                mView.showPlaylistSongs(result);
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
        ApiManager.request(MusicApiServiceImpl.INSTANCE.deletePlaylist(playlist.getId()), new RequestCallBack<String>() {
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
        ApiManager.request(MusicApiServiceImpl.INSTANCE.renamePlaylist(playlist.getId(), title), new RequestCallBack<String>() {
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
        ApiManager.request(MusicApiServiceImpl.INSTANCE.disCollectMusic(pid, music), new RequestCallBack<String>() {
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
}
