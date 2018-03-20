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
        System.out.println(FormatUtil.formatTime(time));

        totalTime = 60;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.formatTime(time));


        totalTime = 1400;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.formatTime(time));

        totalTime = 1440;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.formatTime(time));
    }

}
