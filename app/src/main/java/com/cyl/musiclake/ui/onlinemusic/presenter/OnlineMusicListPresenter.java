package com.cyl.musiclake.ui.onlinemusic.presenter;

import android.content.Context;

import com.cyl.musiclake.api.ApiManager;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.download.download.DownloadInfo;
import com.cyl.musiclake.ui.onlinemusic.contract.OnlineMusicListContract;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineMusicInfo;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineMusicList;
import com.cyl.musiclake.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/4.
 */

public class OnlineMusicListPresenter implements OnlineMusicListContract.Presenter {

    private OnlineMusicListContract.View mView;
    private Context mContext;

    public OnlineMusicListPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(OnlineMusicListContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadOnlineMusicList(String type, int limit, int mOffset) {
        mView.showLoading();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST);
        params.put(Constants.PARAM_TYPE, type);
        params.put(Constants.PARAM_SIZE, String.valueOf(limit));
        params.put(Constants.PARAM_OFFSET, String.valueOf(mOffset));

        ApiManager.getInstance().apiService
                .getOnlineSongs(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnlineMusicList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(OnlineMusicList result) {
                        if (result != null && result.getSong_list().size() <= 10) {
                            mView.showHeaderInfo(result.getBillboard());
                        }
                        if (result == null || result.getSong_list() == null || result.getSong_list().size() == 0) {
                            mView.showErrorInfo("加载失败");
                        }
                        List<Music> musicList = new ArrayList<>();
                        for (OnlineMusicInfo songInfo : result.getSong_list()) {
                            Music music = new Music();
                            music.setType(Music.Type.ONLINE);
                            music.setId(songInfo.getSong_id());
                            music.setAlbum(songInfo.getAlbum_title());
                            music.setArtist(songInfo.getArtist_name());
                            music.setTitle(songInfo.getTitle());
                            music.setLrcPath(songInfo.getLrclink());
                            music.setCoverUri(songInfo.getPic_big().split("@")[0]);
                            musicList.add(music);
                        }
                        mView.showOnlineMusicList(musicList);
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
    public void playCurrentMusic(String mid) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_DOWNLOAD_MUSIC);
        params.put(Constants.PARAM_SONG_ID, mid);
        mView.showLoading();
        ApiManager.getInstance().apiService
                .getTingSongInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DownloadInfo result) {
                        DownloadInfo.SongInfo songInfo = result.getSonginfo();
                        DownloadInfo.JBitrate jBitrate = result.getBitrate();

                        Music music = new Music();
                        music.setType(Music.Type.ONLINE);
                        music.setAlbum(songInfo.getAlbum_title());
                        music.setArtist(songInfo.getAuthor());
                        music.setTitle(songInfo.getTitle());
                        music.setLrcPath(songInfo.getLrclink());
                        music.setCoverUri(songInfo.getPic_big().split("@")[0]);
                        music.setUri(jBitrate.getFile_link());
                        PlayManager.playOnline(music);
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
}
