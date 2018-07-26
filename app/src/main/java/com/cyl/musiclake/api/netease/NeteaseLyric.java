//package com.cyl.musiclake.api.netease;
//
///**
// * Created by master on 2018/4/7.
// */
//
//public class NeteaseLyric {
//
//    /**
//     * sgc : false
//     * sfy : false
//     * qfy : false
//     * lrc : {"version":3,"lyric":"[00:00.00] 作曲 : 薛之谦\n[00:01.00] 作词 : 薛之谦\n[00:04.200]\n[00:21.120]简单点说话的方式简单点\n[00:28.560]\n[00:30.200]递进的情绪请省略\n[00:33.640]你又不是个演员\n[00:36.380]别设计那些情节\n[00:39.360]\n[00:41.930]没意见我只想看看你怎么圆\n[00:50.410]\n[00:51.540]你难过的太表面 像没天赋的演员\n[00:57.150]观众一眼能看见\n[01:00.190]\n[01:02.220]该配合你演出的我演视而不见\n[01:07.680]在逼一个最爱你的人即兴表演\n[01:12.900]什么时候我们开始收起了底线\n[01:18.020]顺应时代的改变看那些拙劣的表演\n[01:23.420]可你曾经那么爱我干嘛演出细节\n[01:28.630]我该变成什么样子才能延缓厌倦\n[01:33.870]原来当爱放下防备后的这些那些\n[01:39.370]才是考验\n[01:41.970]\n[01:44.600]没意见你想怎样我都随便\n[01:52.890]\n[01:54.530]你演技也有限\n[01:57.580]又不用说感言\n[02:00.150]分开就平淡些\n[02:02.990]\n[02:05.000]该配合你演出的我演视而不见\n[02:10.530]别逼一个最爱你的人即兴表演\n[02:15.810]什么时候我们开始没有了底线\n[02:21.010]顺着别人的谎言被动就不显得可怜\n[02:26.430]可你曾经那么爱我干嘛演出细节\n[02:31.520]我该变成什么样子才能配合出演\n[02:36.720]原来当爱放下防备后的这些那些\n[02:41.860]都有个期限\n[02:44.600]\n[02:47.560]其实台下的观众就我一个\n[02:53.040]其实我也看出你有点不舍\n[02:58.340]场景也习惯我们来回拉扯\n[03:02.930]还计较着什么\n[03:07.390]\n[03:08.710]其实说分不开的也不见得\n[03:14.040]其实感情最怕的就是拖着\n[03:19.210]越演到重场戏越哭不出了\n[03:24.070]是否还值得\n[03:28.120]\n[03:29.070]该配合你演出的我尽力在表演\n[03:34.390]像情感节目里的嘉宾任人挑选\n[03:39.680]如果还能看出我有爱你的那面\n[03:44.820]请剪掉那些情节让我看上去体面\n[03:50.040]可你曾经那么爱我干嘛演出细节\n[03:55.310]不在意的样子是我最后的表演\n[04:01.050]是因为爱你我才选择表演 这种成全\n"}
//     * klyric : {"version":0,"lyric":null}
//     * tlyric : {"version":0,"lyric":null}
//     * code : 200
//     */
//
//    private boolean sgc;
//    private boolean sfy;
//    private boolean qfy;
//    private LrcBean lrc;
//    private KlyricBean klyric;
//    private TlyricBean tlyric;
//    private int code;
//
//    public boolean isSgc() {
//        return sgc;
//    }
//
//    public void setSgc(boolean sgc) {
//        this.sgc = sgc;
//    }
//
//    public boolean isSfy() {
//        return sfy;
//    }
//
//    public void setSfy(boolean sfy) {
//        this.sfy = sfy;
//    }
//
//    public boolean isQfy() {
//        return qfy;
//    }
//
//    public void setQfy(boolean qfy) {
//        this.qfy = qfy;
//    }
//
//    public LrcBean getLrc() {
//        return lrc;
//    }
//
//    public void setLrc(LrcBean lrc) {
//        this.lrc = lrc;
//    }
//
//    public KlyricBean getKlyric() {
//        return klyric;
//    }
//
//    public void setKlyric(KlyricBean klyric) {
//        this.klyric = klyric;
//    }
//
//    public TlyricBean getTlyric() {
//        return tlyric;
//    }
//
//    public void setTlyric(TlyricBean tlyric) {
//        this.tlyric = tlyric;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public static class LrcBean {
//        /**
//         * version : 3
//         * lyric : [00:00.00] 作曲 : 薛之谦
//         * [00:01.00] 作词 : 薛之谦
//         * [00:04.200]
//         * [00:21.120]简单点说话的方式简单点
//         * [00:28.560]
//         * [00:30.200]递进的情绪请省略
//         * [00:33.640]你又不是个演员
//         * [00:36.380]别设计那些情节
//         * [00:39.360]
//         * [00:41.930]没意见我只想看看你怎么圆
//         * [00:50.410]
//         * [00:51.540]你难过的太表面 像没天赋的演员
//         * [00:57.150]观众一眼能看见
//         * [01:00.190]
//         * [01:02.220]该配合你演出的我演视而不见
//         * [01:07.680]在逼一个最爱你的人即兴表演
//         * [01:12.900]什么时候我们开始收起了底线
//         * [01:18.020]顺应时代的改变看那些拙劣的表演
//         * [01:23.420]可你曾经那么爱我干嘛演出细节
//         * [01:28.630]我该变成什么样子才能延缓厌倦
//         * [01:33.870]原来当爱放下防备后的这些那些
//         * [01:39.370]才是考验
//         * [01:41.970]
//         * [01:44.600]没意见你想怎样我都随便
//         * [01:52.890]
//         * [01:54.530]你演技也有限
//         * [01:57.580]又不用说感言
//         * [02:00.150]分开就平淡些
//         * [02:02.990]
//         * [02:05.000]该配合你演出的我演视而不见
//         * [02:10.530]别逼一个最爱你的人即兴表演
//         * [02:15.810]什么时候我们开始没有了底线
//         * [02:21.010]顺着别人的谎言被动就不显得可怜
//         * [02:26.430]可你曾经那么爱我干嘛演出细节
//         * [02:31.520]我该变成什么样子才能配合出演
//         * [02:36.720]原来当爱放下防备后的这些那些
//         * [02:41.860]都有个期限
//         * [02:44.600]
//         * [02:47.560]其实台下的观众就我一个
//         * [02:53.040]其实我也看出你有点不舍
//         * [02:58.340]场景也习惯我们来回拉扯
//         * [03:02.930]还计较着什么
//         * [03:07.390]
//         * [03:08.710]其实说分不开的也不见得
//         * [03:14.040]其实感情最怕的就是拖着
//         * [03:19.210]越演到重场戏越哭不出了
//         * [03:24.070]是否还值得
//         * [03:28.120]
//         * [03:29.070]该配合你演出的我尽力在表演
//         * [03:34.390]像情感节目里的嘉宾任人挑选
//         * [03:39.680]如果还能看出我有爱你的那面
//         * [03:44.820]请剪掉那些情节让我看上去体面
//         * [03:50.040]可你曾经那么爱我干嘛演出细节
//         * [03:55.310]不在意的样子是我最后的表演
//         * [04:01.050]是因为爱你我才选择表演 这种成全
//         */
//
//        private int version;
//        private String lyric;
//
//        public int getVersion() {
//            return version;
//        }
//
//        public void setVersion(int version) {
//            this.version = version;
//        }
//
//        public String getLyric() {
//            return lyric;
//        }
//
//        public void setLyric(String lyric) {
//            this.lyric = lyric;
//        }
//    }
//
//    public static class KlyricBean {
//        /**
//         * version : 0
//         * lyric : null
//         */
//
//        private int version;
//        private Object lyric;
//
//        public int getVersion() {
//            return version;
//        }
//
//        public void setVersion(int version) {
//            this.version = version;
//        }
//
//        public Object getLyric() {
//            return lyric;
//        }
//
//        public void setLyric(Object lyric) {
//            this.lyric = lyric;
//        }
//    }
//
//    public static class TlyricBean {
//        /**
//         * version : 0
//         * lyric : null
//         */
//
//        private int version;
//        private Object lyric;
//
//        public int getVersion() {
//            return version;
//        }
//
//        public void setVersion(int version) {
//            this.version = version;
//        }
//
//        public Object getLyric() {
//            return lyric;
//        }
//
//        public void setLyric(Object lyric) {
//            this.lyric = lyric;
//        }
//    }
//}
