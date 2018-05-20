package com.cyl.musiclake.ui.music.list.presenter;

import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.musicapi.MusicApiServiceImpl;
import com.cyl.musiclake.ui.music.list.contract.PlaylistDetailContract;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.LogUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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
        MusicApiServiceImpl.getMusicList(playlistID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(List<Music> musicInfos) {
                        mView.showPlaylistSongs(musicInfos);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LogUtil.e(TAG, throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        AppRepository.getPlaylistSongsRepository(mContext, playlistID)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Music>>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Music> musicList) {
//                        if (musicList.size() == 0) {
//                            mView.showEmptyView();
//                        }
//                        mView.showPlaylistSongs(musicList);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mView.hideLoading();
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mView.hideLoading();
//                    }
//                });
    }

    @Override
    public void loadPlaylistArt(String playlistID) {
        AppRepository.getPlaylistSongsRepository(mView.getContext(), playlistID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        String url = null;
                        for (int i = 0; i < musicList.size(); i++) {
                            url = musicList.get(i).getCoverUri();
                            if (url != null)
                                break;
                        }
                        CoverLoader.loadBitmap(mView.getContext(), url, bitmap -> {
                            mView.showPlaylistArt(bitmap);
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        MusicApiServiceImpl.deletePlaylist(playlist.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 1) {
                            mView.success(1);
                            RxBus.getInstance().post(new PlaylistEvent());
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
    public void renamePlaylist(Playlist playlist, String title) {
        MusicApiServiceImpl.renamePlaylist(playlist.getId(), title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(String status) {
                        LogUtil.e(TAG, status);
                        if (status.equals("true")) {
                            mView.success(1);
                            RxBus.getInstance().post(new PlaylistEvent());
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
    public void uncollectMusic(String pid, int position, Music music) {
        MusicApiServiceImpl.uncollectMusic(pid, music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(String status) {
                        LogUtil.e(TAG, status);
                        if (status.equals("true")) {
                            mView.removeMusic(position);
//                            RxBus.getInstance().post(new PlaylistEvent());
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
}
