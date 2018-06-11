package com.cyl.musiclake.ui.music.discover;

import android.content.Context;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musicapi.bean.ListItem;
import com.cyl.musiclake.api.MusicUtils;
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
public class DiscoverPresenter extends BasePresenter<DiscoverContract.View> implements DiscoverContract.Presenter {

    private List<Playlist> neteaseLists = new ArrayList<>();
    private String[] charts = new String[]{"云音乐新歌榜"
            , "云音乐热歌榜"
            , "网易原创歌曲榜"
            , "云音乐飙升榜"
            , "云音乐电音榜"
            , "UK排行榜周榜"
            , "美国Billboard周榜 "
            , "KTV嗨榜 "
            , "iTunes榜 "
            , "Hit FM Top榜 "
            , "日本Oricon周榜 "
            , "韩国Melon排行榜周榜 "
            , "韩国Mnet排行榜周榜 "
            , "韩国Melon原声周榜 "
            , "中国TOP排行榜(港台榜) "
            , "中国TOP排行榜(内地榜)"
            , "香港电台中文歌曲龙虎榜 "
            , "华语金曲榜"
            , "中国嘻哈榜"
            , "法国 NRJ EuroHot 30 周榜"
            , "台湾Hito排行榜 "
            , "Beatport全球电子舞曲榜"};

    @Inject
    public DiscoverPresenter() {
    }

    @Override
    public void loadBaidu() {
        ApiManager.request(BaiduApiServiceImpl.INSTANCE.getOnlinePlaylist(), new RequestCallBack<List<Playlist>>() {
            @Override
            public void success(List<Playlist> result) {
                mView.showBaiduCharts(result);
            }

            @Override
            public void error(String msg) {

            }
        });
    }

    @Override
    public void loadNetease() {
        for (int i = 0; i < charts.length; i++) {
            neteaseLists.add(new Playlist(String.valueOf(i), charts[i]));
            int finalI = i;
            BaseApiImpl.Companion.getInstance(mView.getContext()).getTopList(String.valueOf(i), topList -> {
                Playlist playlist = new Playlist();
                playlist.setId(String.valueOf(finalI));
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
                neteaseLists.set(finalI, playlist);
                mView.showNeteaseCharts(neteaseLists);
                return null;
            });
        }
    }
}
