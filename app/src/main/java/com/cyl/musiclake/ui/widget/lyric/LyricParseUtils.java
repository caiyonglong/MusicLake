package com.cyl.musiclake.ui.widget.lyric;

import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.ui.widget.lyric.LyricInfo.LineInfo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 歌词解析器
 */

public class LyricParseUtils {
    private static LyricInfo mLyricInfo;

    /**
     * 设置歌词文件
     *
     * @param file 歌词文件
     */
    public static LyricInfo setLyricResource(File file) {
        try {
            return setupLyricResource(new FileInputStream(file), "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置歌词字符串
     *
     * @param lyricInfo 歌词字符串
     */
    public static LyricInfo setLyricResource(String lyricInfo) {
        if (lyricInfo != null && lyricInfo.length() > 0) {
            InputStream inputStream = new ByteArrayInputStream(lyricInfo.getBytes());
            return setupLyricResource(inputStream, "utf-8");
        } else {
            return null;
        }
    }

    /**
     * 初始化歌词信息
     *
     * @param inputStream 歌词文件的流信息
     */
    private static LyricInfo setupLyricResource(InputStream inputStream, String charsetName) {
        if (inputStream != null) {
            try {
                LyricInfo lyricInfo = new LyricInfo();
                lyricInfo.songLines = new ArrayList<>();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    analyzeLyric(lyricInfo, line);
                }
                //歌词排序
                Collections.sort(lyricInfo.songLines, new sort());
                reader.close();
                inputStream.close();
                inputStreamReader.close();
                mLyricInfo = lyricInfo;
            } catch (IOException e) {
                resetLyricInfo();
                LogUtil.e("--", "IOException");
                e.printStackTrace();
            }
        } else {
            resetLyricInfo();
        }
        return mLyricInfo;
    }

    /**
     * 逐行解析歌词内容
     */
    private static void analyzeLyric(LyricInfo lyricInfo, String line) {
        try {
            int index = line.indexOf("]");
            if (line.startsWith("[offset:")) {
                // 时间偏移量
                String string = line.substring(8, index).trim();
                if (string.isEmpty()) return;
                lyricInfo.song_offset = Long.parseLong(string);
                return;
            }
            if (line.startsWith("[ti:")) {
                // title 标题
                String string = line.substring(4, index).trim();
                if (string.isEmpty()) return;
                lyricInfo.song_title = string;
                return;
            }
            if (line.startsWith("[ar:")) {
                // artist 作者
                String string = line.substring(4, index).trim();
                if (string.isEmpty()) return;
                lyricInfo.song_artist = string;
                return;
            }
            if (line.startsWith("[al:")) {
                // album 所属专辑
                String string = line.substring(4, index).trim();
                if (string.isEmpty()) return;
                lyricInfo.song_album = string;
                return;
            }
            if (line.startsWith("[by:")) {
                return;
            }
            if (line.startsWith("[total:")) {
                return;
            }
            if (line.startsWith("[0") && line.endsWith("]")) {
                String test = line.replace("]", "").replaceFirst(", ", "]");
                if (test.contains("]")) {
                    line = test;
                }
            }
            if (line.startsWith("[0") && !line.endsWith("]")) {
                // 歌词内容,需要考虑一行歌词有多个时间戳的情况
                int lastIndexOfRightBracket = line.lastIndexOf("]");
                String content = line.substring(lastIndexOfRightBracket + 1, line.length());
                //去除trc歌词中每个字的时间长
                content = content.replaceAll("<[0-9]{1,5}>", "");
                if (content.length()==0){
                    content ="......";
                    LogUtil.e(content);
                }
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
                    lineInfo.content = content.trim();
                    lineInfo.start = measureStartTimeMillis(temp);
                    lyricInfo.songLines.add(lineInfo);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * 重置歌词内容
     */
    private static void resetLyricInfo() {
        if (mLyricInfo != null) {
            if (mLyricInfo.songLines != null) {
                mLyricInfo.songLines.clear();
                mLyricInfo.songLines = null;
            }
            mLyricInfo = null;
        }
    }

    static class sort implements Comparator<LineInfo> {

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
