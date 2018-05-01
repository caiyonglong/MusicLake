package com.cyl.musiclake.ui.music.local.presenter;

import android.content.Context;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.data.source.AppRepository;
import com.cyl.musiclake.event.HistoryChangedEvent;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.musicApi.MusicApiServiceImpl;
import com.cyl.musiclake.ui.music.local.contract.MyMusicContract;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yonglong on 2018/1/6.
 */

public class MyMusicPresenter implements MyMusicContract.Presenter {
    private MyMusicContract.View mView;
    private Context mContext;
    private List<Playlist> playlists = new ArrayList<>();
    private List<Music> musicList;

    public MyMusicPresenter(Context mContext) {
        this.mContext = mContext;
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
    public void attachView(MyMusicContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {

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
            MusicApiServiceImpl.getPlaylist()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Playlist>>() {
                        @Override
                        public void onSubscribe(Disposable disposable) {

                        }

                        @Override
                        public void onNext(List<Playlist> playlists) {
                            mView.showPlaylist(playlists);
                            if (playlists.size() == 0) {
                                mView.showEmptyView();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mView.showEmptyView();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            playlists.clear();
            mView.showPlaylist(playlists);
            if (playlists.size() == 0) {
                mView.showEmptyView();
            }
        }
//        playlists = PlaylistLoader.getPlaylist(mContext);
//        mView.showPlaylist(playlists);
//        if (playlists.size() == 0) {
//            mView.showEmptyView();
//        }
    }
}
