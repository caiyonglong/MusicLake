package com.cyl.musiclake.api.baidu;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by yonglong on 2018/1/22.
 */
public class BaiduApiServiceImplTest extends TestCase {

    public void testPlaylist() {
        BaiduApiServiceImpl.getOnlinePlaylist()
                .subscribe(result -> {
                    List<BaiduMusicList.Billboard> mBillboards = result.getContent();
                    //移除T榜
                    mBillboards.remove(3);
                    System.out.println(mBillboards.get(0).getComment());
                    System.out.println(mBillboards.get(0).getName());
                    System.out.println(mBillboards.get(0).getType());
                });

        System.out.println("------------------------------------");
        BaiduApiServiceImpl.getOnlineSongs("1", 10, 0)
                .subscribe(musicList -> {
                    System.out.println(musicList.get(0).toString());
                });

        System.out.println("------------------------------------");
        BaiduApiServiceImpl.getTingSongInfo("569080829")
                .subscribe(music -> {
                    System.out.println(music.getTitle());
                    System.out.println(music.getArtist());
                    System.out.println(music.getArtistId());
                    System.out.println(music.getUri());
                    System.out.println(music.getLrcPath());
                    System.out.println(music.getCoverUri());
                    System.out.println(music.getUri());
                });

    }

}