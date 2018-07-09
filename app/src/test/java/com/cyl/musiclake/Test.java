package com.cyl.musiclake;

import com.cyl.musiclake.utils.FormatUtil;

/**
 * Author   : D22434
 * version  : 2018/3/20
 * function :
 */

public class Test {
    @org.junit.Test
    public void tt() {
//        for (; ; ) {
        System.out.println("请输入下一个数据（直接回车结束输入）：");
        long totalTime = 60;
        long time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.INSTANCE.formatTime(time));

        totalTime = 60;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.INSTANCE.formatTime(time));


        totalTime = 1400;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.INSTANCE.formatTime(time));

        totalTime = 1440;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.INSTANCE.formatTime(time));
        String s = "Hello world";
        String[] str = s.split(" ");
        int len = str[str.length - 1].length();
    }

    public int lengthOfLastWord(String s) {
        String[] str = s.split(" ");
        if (str.length == 0) return 0;
        int len = str[str.length - 1].length();
        return len;
    }


    @org.junit.Test
    public void tt1() {
        String date = FormatUtil.INSTANCE.formatDate(1530800858658L);
        System.out.println(date);
    }
}
