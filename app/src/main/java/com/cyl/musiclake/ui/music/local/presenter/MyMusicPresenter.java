package com.cyl.musiclake.ui.music.local.presenter;

import android.content.Context;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.event.HistoryChangedEvent;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.api.MusicApiServiceImpl;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;
import com.cyl.musiclake.ui.music.local.contract.MyMusicContract;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/6.
 */

public class MyMusicPresenter extends BasePresenter<MyMusicContract.View> implements MyMusicContract.Presenter {
    private Context mContext;
    private List<Playlist> playlists = new ArrayList<>();
    private List<Music> musicList;

    @Inject
    public MyMusicPresenter() {
        this.mContext = MusicApp.getAppContext();
        /**登陆成功重新设置用户新*/
        RxBus.getInstance().register(HistoryChangedEvent.class).subscribe(event -> updateHistory());
        RxBus.getInstance().register(PlaylistEvent.class).subscribe(event -> {
            updatePlaylist();
            loadPlaylist();
        });
        RxBus.getInstance().register(LoginEvent.class).subscribe(event -> loadPlaylist());
    }

    /**
     * 更新播放历史
     */
    private void updateHistory() {
        AppRepository.getPlayHistoryRepository(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showHistory(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 更新本地歌单
     */
    private void updatePlaylist() {
        AppRepository.getFavoriteSong(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showLoveList(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void loadSongs() {
        updateHistory();
        updatePlaylist();
        AppRepository.getAllSongsRepository(mContext, Extras.SONG_LOCAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showSongs(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
        AppRepository.getFolderSongsRepository(mContext, FileUtils.getMusicDir())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mView.showDownloadList(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadPlaylist() {
        boolean mIsLogin = UserStatus.getstatus(mContext);
        if (mIsLogin) {
            ApiManager.request(MusicApiServiceImpl.INSTANCE.getPlaylist(), new RequestCallBack<List<Playlist>>() {
                @Override
                public void success(List<Playlist> result) {
                    mView.showPlaylist(result);
                    if (result.size() == 0) {
                        mView.showEmptyView();
                    }
                }

                @Override
                public void error(String msg) {
                    ToastUtils.show(msg);
                    mView.showEmptyView();
                }
            });
        } else {
            playlists.clear();
            mView.showPlaylist(playlists);
            if (playlists.size() == 0) {
                mView.showEmptyView();
            }
        }
    }
}
