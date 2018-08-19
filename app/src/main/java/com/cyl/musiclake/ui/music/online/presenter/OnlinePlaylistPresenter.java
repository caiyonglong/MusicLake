package com.cyl.musiclake.ui.music.online.presenter;

import android.content.Context;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musicapi.bean.ListItem;
import com.cyl.musiclake.api.MusicUtils;
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class OnlinePlaylistPresenter extends BasePresenter<OnlinePlaylistContract.View> implements OnlinePlaylistContract.Presenter {

    @Inject
    public OnlinePlaylistPresenter() {
    }

    @Override
    public void loadBaiDuPlaylist() {
        BaiduApiServiceImpl.INSTANCE.getOnlinePlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(new Observer<List<Playlist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(List<Playlist> result) {
                        mView.showCharts(result);
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
    public void loadTopList(Context context) {
        for (int i = 0; i < 21; i++) {
            BaseApiImpl.INSTANCE.getInstance(context).getTopList(String.valueOf(i), topList -> {
                Playlist playlist = new Playlist();
                playlist.setPid(String.valueOf(System.currentTimeMillis()));
                playlist.setName(topList.getData().getName());
                playlist.setCount(topList.getData().getPlayCount());
                playlist.setCoverUrl(topList.getData().getCover());
                playlist.setDes(topList.getData().getDescription());
                List<Music> musicList = new ArrayList<>();
                if (topList.getData().getList().size() > 0) {
                    for (ListItem item : topList.getData().getList()) {
                        Music music = MusicUtils.INSTANCE.getMusic(item);
                        musicList.add(music);
                    }
                }
                playlist.setMusicList(musicList);
                return null;
            });
        }
    }
}
