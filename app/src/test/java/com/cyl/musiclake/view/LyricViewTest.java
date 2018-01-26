package com.cyl.musiclake.view;

import android.util.Log;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author   : D22434
 * version  : 2018/1/26
 * function :
 */
public class LyricViewTest extends TestCase {

    public void testanalyzeLyric() {
        String source = "\n" +
                "[00:00.00]你还要我怎样\n" +
                "[00:06.00]词：薛之谦 曲：薛之谦\n" +
                "[00:09.00]演唱：薛之谦\n" +
                "[00:22.00]\n" +
                "[00:26.25]你停在了这条我们熟悉的街\n" +
                "[00:36.18]把你准备好的台词全念一遍\n" +
                "[00:44.02]我还在逞强 说着谎\n" +
                "[00:48.37]也没能力遮挡 你去的方向\n" +
                "[00:53.83]至少分开的时候我落落大方\n" +
                "[01:01.73]\n" +
                "[01:05.72]我后来都会选择绕过那条街\n" +
                "[01:14.92]又多希望在另一条街能遇见\n" +
                "[01:23.50]思念在逞强 不肯忘\n" +
                "[01:28.19]怪我没能力跟随 你去的方向\n" +
                "[01:34.00]若越爱越被动 越要落落大方\n" +
                "[01:41.65]\n" +
                "[01:43.65]你还要我怎样 要怎样\n" +
                "[01:48.30]你突然来的短信就够我悲伤\n" +
                "[01:53.30]我没能力遗忘 你不用提醒我\n" +
                "[01:58.65]哪怕结局就这样\n" +
                "[02:03.24]我还能怎样 能怎样\n" +
                "[02:08.20]最后还不是落得情人的立场\n" +
                "[02:13.15]你从来不会想 我何必这样\n" +
                "[02:21.60]\n" +
                "[02:45.64]我慢慢的回到自己的生活圈\n" +
                "[02:53.89]也开始可以接触新的人选\n" +
                "[03:03.35]爱你到最后 不痛不痒\n" +
                "[03:08.70]留言在计较 谁爱过一场\n" +
                "[03:13.35]我剩下一张 没后悔的模样\n" +
                "[03:21.35]\n" +
                "[03:22.95]你还要我怎样 要怎样\n" +
                "[03:28.20]你千万不要在我婚礼的现场\n" +
                "[03:33.20]我听完你爱的歌 就上了车\n" +
                "[03:39.10]爱过你很值得\n" +
                "[03:43.30]我不要你怎样 没怎样\n" +
                "[03:48.20]我陪你走的路你不能忘\n" +
                "[03:53.50]因为那是我 最快乐的时光\n" +
                "[04:03.75]\n" +
                "[04:05.80]后来我的生活还算理想\n" +
                "[04:16.05]没为你落到孤单的下场\n" +
                "[04:24.13]有一天晚上 梦一场\n" +
                "[04:28.56]你白发苍苍 说带我流浪\n" +
                "[04:34.41]我还是没犹豫 就随你去天堂\n" +
                "[04:44.10]不管能怎样 我能陪你到天亮\n" +
                "[04:57.17]\n";
        source = "[ti:]\n" +
                "[ar:]\n" +
                "[al:]\n" +
                "[by:]\n" +
                "[00:34.601]这么多年的兄弟\n" +
                "[00:37.177]有谁比我更了解你\n" +
                "[00:41.343]太多太多不容易\n" +
                "[00:43.614]磨平了岁月和脾气\n" +
                "[00:48.210]时间转眼就过去\n" +
                "[00:50.623]这身后不散的筵席\n" +
                "[00:54.390]只因为我们还在\n" +
                "[00:57.38]心留在原地\n" +
                "[01:01.47]张开手需要多大的勇气\n" +
                "[01:08.6]这片天你我一起撑起\n" +
                "[01:14.873]更努力只为了我们想要的明天\n" +
                "[01:21.606]好好的这份情好好珍惜\n" +
                "[01:28.441]我们不一样\n" +
                "[01:31.476]每个人都有不同的境遇\n" +
                "[01:35.539]我们在这里\n" +
                "[01:37.682]在这里等你\n" +
                "[01:42.137]我们不一样\n" +
                "[01:45.59]虽然会经历不同的事情\n" +
                "[01:48.992]我们都希望\n" +
                "[01:51.300]来生还能相遇\n" +
                "[01:57.6]这么多年的兄弟\n" +
                "[01:59.453]有谁比我更了解你\n" +
                "[02:03.699]太多太多不容易\n" +
                "[02:06.59]磨平了岁月和脾气\n" +
                "[02:10.479]时间转眼就过去\n" +
                "[02:12.988]这身后不散的筵席\n" +
                "[02:16.428]只因为我们还在\n" +
                "[02:19.340]心留在原地\n" +
                "[02:23.525]张开手需要多大的勇气\n" +
                "[02:30.318]这片天你我一起撑起\n" +
                "[02:37.168]更努力只为了我们想要的明天\n" +
                "[02:43.821]好好的这份情好好珍惜\n" +
                "[02:50.787]我们不一样\n" +
                "[02:53.750]每个人都有不同的境遇\n" +
                "[02:57.534]我们在这里\n" +
                "[02:59.927]在这里等你\n" +
                "[03:04.437]我们不一样\n" +
                "[03:07.339]虽然会经历不同的事情\n" +
                "[03:11.274]我们都希望\n" +
                "[03:13.552]来生还能相遇\n" +
                "[03:33.923]我们不一样\n" +
                "[03:36.609]每个人都有不同的境遇\n" +
                "[03:40.345]我们在这里\n" +
                "[03:42.757]在这里等你\n" +
                "[03:47.156]我们不一样\n" +
                "[03:50.199]虽然会经历不同的事情\n" +
                "[03:54.79]我们都希望\n" +
                "[03:56.449]来生还能相遇\n" +
                "[04:01.30]我们不一样\n" +
                "[04:03.916]虽然会经历不同的事情\n" +
                "[04:07.858]我们都希望\n" +
                "[04:10.147]来生还能相遇\n" +
                "[04:14.740]我们都希望\n" +
                "[04:16.914]来生还能相遇\n";
        source="[00:00.00]你还要我怎样[00:06.00]词：薛之谦 曲：薛之谦[00:09.00]演唱：薛之谦[00:22.00][00:26.25]你停在了这条我们熟悉的街[00:36.18]把你准备好的台词全念一遍[00:44.02]我还在逞强 说着谎[00:48.37]也没能力遮挡 你去的方向[00:53.83]至少分开的时候我落落大方[01:01.73][01:05.72]我后来都会选择绕过那条街[01:14.92]又多希望在另一条街能遇见[01:23.50]思念在逞强 不肯忘[01:28.19]怪我没能力跟随 你去的方向[01:34.00]若越爱越被动 越要落落大方[01:41.65][01:43.65]你还要我怎样 要怎样[01:48.30]你突然来的短信就够我悲伤[01:53.30]我没能力遗忘 你不用提醒我[01:58.65]哪怕结局就这样[02:03.24]我还能怎样 能怎样[02:08.20]最后还不是落得情人的立场[02:13.15]你从来不会想 我何必这样[02:21.60][02:45.64]我慢慢的回到自己的生活圈[02:53.89]也开始可以接触新的人选[03:03.35]爱你到最后 不痛不痒[03:08.70]留言在计较 谁爱过一场[03:13.35]我剩下一张 没后悔的模样[03:21.35][03:22.95]你还要我怎样 要怎样[03:28.20]你千万不要在我婚礼的现场[03:33.20]我听完你爱的歌 就上了车[03:39.10]爱过你很值得[03:43.30]我不要你怎样 没怎样[03:48.20]我陪你走的路你不能忘[03:53.50]因为那是我 最快乐的时光[04:03.75][04:05.80]后来我的生活还算理想[04:16.05]没为你落到孤单的下场[04:24.13]有一天晚上 梦一场[04:28.56]你白发苍苍 说带我流浪[04:34.41]我还是没犹豫 就随你去天堂[04:44.10]不管能怎样 我能陪你到天亮[04:57.17]\n";
        setupLyricResource(new ByteArrayInputStream(source.getBytes()), "utf-8");
    }

    /**
     * 初始化歌词信息
     *
     * @param inputStream 歌词文件的流信息
     */
    private void setupLyricResource(InputStream inputStream, String charsetName) {
        if (inputStream != null) {
            try {
                LyricInfo lyricInfo = new LyricInfo();
                lyricInfo.song_lines = new ArrayList<>();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    analyzeLyric(lyricInfo, line);
                }
                //歌词排序
                Collections.sort(lyricInfo.song_lines, new sort());
                reader.close();
                inputStream.close();
                inputStreamReader.close();

            } catch (IOException e) {
                Log.e("--", "IOException");
                e.printStackTrace();
            }
        }
    }

    /**
     * 逐行解析歌词内容
     */
    private void analyzeLyric(LyricInfo lyricInfo, String line) {
        int index = line.indexOf("]");
        if (line != null && line.startsWith("[offset:")) {
            // 时间偏移量
            String string = line.substring(8, index).trim();
            lyricInfo.song_offset = Long.parseLong(string);
            System.out.println("song_offset :" + string);
            return;
        }
        if (line != null && line.startsWith("[ti:")) {
            // title 标题
            String string = line.substring(4, index).trim();
            lyricInfo.song_title = string;
            System.out.println("song_title :" + string);
            return;
        }
        if (line != null && line.startsWith("[ar:")) {
            // artist 作者
            String string = line.substring(4, index).trim();
            lyricInfo.song_artist = string;

            System.out.println("song_artist :" + string);
            return;
        }
        if (line != null && line.startsWith("[al:")) {
            // album 所属专辑
            String string = line.substring(4, index).trim();
            lyricInfo.song_album = string;
            System.out.println("song_album :" + string);
            return;
        }
        if (line != null && line.startsWith("[by:")) {
            return;
        }
        if (line != null && line.trim().length() > 10) {
            // 歌词内容,需要考虑一行歌词有多个时间戳的情况
            int lastIndexOfRightBracket = line.lastIndexOf("]");
            String content = line.substring(lastIndexOfRightBracket + 1, line.length());
            //去除trc歌词中每个字的时间长
            content = content.replaceAll("<[0-9]{1,5}>", "");
            System.out.println("content :" + content);
            String times = line.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
            String arrTimes[] = times.split("-");
            for (String temp : arrTimes) {
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
                lineInfo.content = content;
                System.out.println(temp + " : " + content);
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

    class sort implements Comparator<LineInfo> {

        @Override
        public int compare(LineInfo lrc, LineInfo lrc2) {
            if (lrc.start < lrc2.start) {
                return -1;
            } else if ((lrc.start > lrc2.start)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}