package com.cyl.musiclake.ui.music.online.presenter;

import android.content.Context;
import android.util.Log;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.musicApi.MusicApiServiceImpl;
import com.cyl.musiclake.ui.music.online.contract.NeteaseListContract;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class NeteaseListPresenter implements NeteaseListContract.Presenter {

    private NeteaseListContract.View mView;
    private Context mContext;

    public NeteaseListPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(NeteaseListContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void loadNeteaseMusicList(int idx) {
        MusicApi.getTopList(idx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeteaseList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NeteaseList result) {
                        Log.e("net_play", result.toString());
                        mView.showTopList(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                        mView.showErrorInfo(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getMusicInfo(NeteaseMusic neteaseMusic) {
        mView.showLoading();
        MusicApi.getMusicInfo(new Music(neteaseMusic))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Music>() {

                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Music music) {
                        if (music.getUri() != null) {
                            mView.showMusicInfo(music);
                        } else {
                            ToastUtils.show("播放地址异常");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void collectMusic(String pid, Music music) {
        MusicApiServiceImpl.collectMusic(pid, music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(String status) {
                        LogUtil.e("add", status);
                        if (status.equals("true")) {
                            mView.showCollectStatus(true, "收藏成功");
                            RxBus.getInstance().post(new PlaylistEvent());
                        } else if (status.equals("false")) {
                            mView.showCollectStatus(false, "歌曲已存在");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void addPlaylist(Music music) {
        MusicApiServiceImpl.getPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Playlist>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(List<Playlist> playlists) {
                        mView.showAddPlaylistDialog(playlists, music);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showAddPlaylistDialog(null, null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
