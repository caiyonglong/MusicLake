package com.cyl.musiclake.api

import org.junit.Test


/**
 * Created by yonglong on 2018/1/15.
 */
class ApiManagerServiceTest1 {
    @Test
    @Throws(Exception::class)
    fun searchByQQ() {
    }

    @Test
    @Throws(Exception::class)
    fun searchByXiami() {
        val info = "暧昧-薛之谦"
        //        DoubanMusic doubanMusic = DoubanApiServiceImpl.getMusicInfo(info);
        //        System.out.println(music.getCoverBig());

    }

    @Test
    @Throws(Exception::class)
    fun searchLyricByXiami() {
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
    @Throws(Exception::class)
    fun searchLyric() {
        //        QQMusicApiServiceImpl.getQQLyric("001Qu4I30eVFYb")
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

    internal inner class LyricInfo {
        var song_lines: MutableList<LineInfo>? = null

        var song_artist: String? = null  // 歌手
        var song_title: String? = null  // 标题
        var song_album: String? = null  // 专辑

        var song_offset: Long = 0  // 偏移量
    }

    internal inner class LineInfo {
        var content: String? = null  // 歌词内容
        var start: Long = 0  // 开始时间
    }

    /**
     * 逐行解析歌词内容
     */
    private fun analyzeLyric(lyricInfo: LyricInfo, line: String) {
        val index = line.indexOf("]")
        if (line.startsWith("[offset:")) {
            // 时间偏移量
            val string = line.substring(8, index).trim { it <= ' ' }
            lyricInfo.song_offset = java.lang.Long.parseLong(string)
            return
        }
        if (line.startsWith("[ti:")) {
            // title 标题
            val string = line.substring(4, index).trim { it <= ' ' }
            lyricInfo.song_title = string
            return
        }
        if (line.startsWith("[ar:")) {
            // artist 作者
            val string = line.substring(4, index).trim { it <= ' ' }
            lyricInfo.song_artist = string
            return
        }
        if (line.startsWith("[al:")) {
            // album 所属专辑
            val string = line.substring(4, index).trim { it <= ' ' }
            lyricInfo.song_album = string
            return
        }
        if (line.startsWith("[by:")) {
            return
        }
        if (index == 9 && line.trim { it <= ' ' }.length > 10) {
            // 歌词内容,需要考虑一行歌词有多个时间戳的情况
            val lastIndexOfRightBracket = line.lastIndexOf("]")
            var content = line.substring(lastIndexOfRightBracket + 1, line.length)
            //            System.out.println("" + content);
            content = content.replace("<[0-9]{1,5}>".toRegex(), "")
            println("" + content)
            val times = line.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-")
            val arrTimes = times.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (temp in arrTimes) {
                println("" + temp)
                if (temp.trim { it <= ' ' }.length == 0) {
                    continue
                }
                /** [02:34.14][01:07.00]当你我不小心又想起她
                 *
                 * 上面的歌词的就可以拆分为下面两句歌词了
                 * [02:34.14]当你我不小心又想起她
                 * [01:07.00]当你我不小心又想起她
                 */

                val lineInfo = LineInfo()
                lineInfo.content = content + ""
                lineInfo.start = measureStartTimeMillis(temp)
                lyricInfo.song_lines!!.add(lineInfo)
            }
        }
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     */
    private fun measureStartTimeMillis(timeString: String): Long {
        var timeString = timeString
        //因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        //将字符串 XX:XX.XX 转换为 XX:XX:XX
        timeString = timeString.replace('.', ':')
        //将字符串 XX:XX:XX 拆分
        val times = timeString.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // mm:ss:SS
        return (Integer.valueOf(times[0]) * 60 * 1000 +//分

                Integer.valueOf(times[1]) * 1000 +//秒

                Integer.valueOf(times[2])).toLong()//毫秒
    }
}