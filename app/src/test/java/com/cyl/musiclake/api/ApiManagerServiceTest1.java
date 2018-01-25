package com.cyl.musiclake.api;

import com.cyl.musiclake.api.qq.QQApiModel;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by yonglong on 2018/1/15.
 */
public class ApiManagerServiceTest1 {
    @Test
    public void searchByQQ() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(1)); //page
        params.put("n", String.valueOf(10));//limit
        params.put("w", "薛之谦");// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");

        Observable<QQApiModel> qqApiModel
                = ApiManager.getInstance().apiService.searchByQQ("http://c.y.qq.com/soso/fcgi-bin/search_cp?", params);
        qqApiModel.subscribe(qqApi -> {
            List<QQApiModel.DataBean.SongBean.ListBean> data = qqApi.getData().getSong().getList();
            System.out.println(data.size());
            System.out.println(qqApi.getCode());
            System.out.println(qqApi.getTime());
            System.out.println(qqApi.getData().getKeyword());
            System.out.println(qqApi.getData().getSong().getTotalnum());
            System.out.println(qqApi.getData().getSong().getCurpage());
            System.out.println(qqApi.getData().getSong().getCurnum());
            System.out.println(qqApi.getData().getSong().getList().size());
            for (int i = 0; i < data.size(); i++) {
                System.out.println(data.get(i).getSinger().get(0).getId());
                System.out.println(data.get(i).getPubtime());
                System.out.println(data.get(i).getInterval());
            }
        });
        assert (10 == 10);
    }

    @Test
    public void searchByXiami() throws Exception {


    }

    @Test
    public void searchLyricByXiami() throws Exception {
//        XiamiServiceImpl.getXimaiLyric("http://img.xiami.net/lyric/63/1769253963_1472550789_602.trc")
//        XiamiServiceImpl.getXimaiLyric("http://img.xiami.net/lyric/trc/51/1769253951_1401249723_976.lrc")
//                .subscribe(map -> {
////                    System.out.println("uid=" + map);
//                    InputStream inputStream = ConvertUtils.string2InputStream(map, "utf-8");
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//                    BufferedReader reader = new BufferedReader(inputStreamReader);
//                    String line = null;
//                    LyricInfo lyricInfo = new LyricInfo();
//                    lyricInfo.song_lines = new ArrayList<>();
//                    while ((line = reader.readLine()) != null) {
//                        analyzeLyric(lyricInfo, line);
//                    }
//                    System.out.println(lyricInfo.song_title);
//                    System.out.println(lyricInfo.song_artist);
//                    System.out.println(lyricInfo.song_album);
//                    System.out.println(lyricInfo.song_offset);
////                    for (LineInfo tt : lyricInfo.song_lines) {
////                        System.out.println(tt.start);
////                        System.out.println(tt.content);
////                    }
//                });

    }


    @Test
    public void searchLyric() throws Exception {
//        QQApiServiceImpl.getQQLyric("001Qu4I30eVFYb")
//                .subscribe(map -> {
//                    InputStream inputStream = ConvertUtils.string2InputStream(map, "utf-8");
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//                    BufferedReader reader = new BufferedReader(inputStreamReader);
//                    String line = null;
//                    LyricInfo lyricInfo = new LyricInfo();
//                    lyricInfo.song_lines = new ArrayList<>();
//                    while ((line = reader.readLine()) != null) {
//                        analyzeLyric(lyricInfo, line);
//                    }
//                    System.out.println(lyricInfo.song_title);
//                    System.out.println(lyricInfo.song_artist);
//                    System.out.println(lyricInfo.song_album);
//                    System.out.println(lyricInfo.song_offset);
//                });
    }

    class LyricInfo {
        List<LineInfo> song_lines;

        String song_artist;  // 歌手
        String song_title;  // 标题
        String song_album;  // 专辑

        long song_offset;  // 偏移量
    }

    class LineInfo {
        String content;  // 歌词内容
        long start;  // 开始时间
    }

    /**
     * 逐行解析歌词内容
     */
    private void analyzeLyric(LyricInfo lyricInfo, String line) {
        int index = line.indexOf("]");
        if (line.startsWith("[offset:")) {
            // 时间偏移量
            String string = line.substring(8, index).trim();
            lyricInfo.song_offset = Long.parseLong(string);
            return;
        }
        if (line.startsWith("[ti:")) {
            // title 标题
            String string = line.substring(4, index).trim();
            lyricInfo.song_title = string;
            return;
        }
        if (line.startsWith("[ar:")) {
            // artist 作者
            String string = line.substring(4, index).trim();
            lyricInfo.song_artist = string;
            return;
        }
        if (line.startsWith("[al:")) {
            // album 所属专辑
            String string = line.substring(4, index).trim();
            lyricInfo.song_album = string;
            return;
        }
        if (line.startsWith("[by:")) {
            return;
        }
        if (index == 9 && line.trim().length() > 10) {
            // 歌词内容,需要考虑一行歌词有多个时间戳的情况
            int lastIndexOfRightBracket = line.lastIndexOf("]");
            String content = line.substring(lastIndexOfRightBracket + 1, line.length());
//            System.out.println("" + content);
            content = content.replaceAll("<[0-9]{1,5}>", "");
            System.out.println("" + content);
            String times = line.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
            String arrTimes[] = times.split("-");
            for (String temp : arrTimes) {
                System.out.println("" + temp);
                if (temp.trim().length() == 0) {
                    continue;
                }
                /** [02:34.14][01:07.00]当你我不小心又想起她
                 *
                 上面的歌词的就可以拆分为下面两句歌词了
                 [02:34.14]当你我不小心又想起她
                 [01:07.00]当你我不小心又想起她
                 */

                LineInfo lineInfo = new LineInfo();
                lineInfo.content = content + "";
                lineInfo.start = measureStartTimeMillis(temp);
                lyricInfo.song_lines.add(lineInfo);
            }
        }
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     */
    private static long measureStartTimeMillis(String timeString) {
        //因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        //将字符串 XX:XX.XX 转换为 XX:XX:XX
        timeString = timeString.replace('.', ':');
        //将字符串 XX:XX:XX 拆分
        String[] times = timeString.split(":");
        // mm:ss:SS
        return Integer.valueOf(times[0]) * 60 * 1000 +//分
                Integer.valueOf(times[1]) * 1000 +//秒
                Integer.valueOf(times[2]);//毫秒
    }
}