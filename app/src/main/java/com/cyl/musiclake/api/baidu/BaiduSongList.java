//package com.cyl.musiclake.api.baidu;
//
//import java.util.List;
//
///**
// * Author   : D22434
// * version  : 2018/1/22
// * function :
// */
//
//public class BaiduSongList {
//
//    /**
//     * song_list : [   {
//     * "artist_id": "15",
//     * "language": "国语",
//     * "pic_big": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_150,h_150",
//     * "pic_small": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_90,h_90",
//     * "country": "内地",
//     * "area": "0",
//     * "publishtime": "2018-01-11",
//     * "album_no": "1",
//     * "lrclink": "http://qukufile2.qianqian.com/data2/lrc/ef80d282b94f37e92bee6e5b9b417124/569080826/569080826.lrc",
//     * "copy_type": "3",
//     * "hot": "322677",
//     * "all_artist_ting_uid": "45561",
//     * "resource_type": "0",
//     * "is_new": "1",
//     * "rank_change": "0",
//     * "rank": "1",
//     * "all_artist_id": "15",
//     * "style": "",
//     * "del_status": "0",
//     * "relate_status": "0",
//     * "toneid": "0",
//     * "all_rate": "64,128,256,320,flac",
//     * "file_duration": 290,
//     * "has_mv_mobile": 0,
//     * "versions": "影视原声",
//     * "bitrate_fee": "{\"0\":\"0|0\",\"1\":\"0|0\"}",
//     * "biaoshi": "first,lossless",
//     * "info": "",
//     * "has_filmtv": "0",
//     * "si_proxycompany": "上海腾讯企鹅影视文化传播有限公司",
//     * "res_encryption_flag": "0",
//     * "song_id": "569080829",
//     * "title": "无问西东",
//     * "ting_uid": "45561",
//     * "author": "王菲",
//     * "album_id": "569080827",
//     * "album_title": "无问西东",
//     * "is_first_publish": 0,
//     * "havehigh": 2,
//     * "charge": 0,
//     * "has_mv": 0,
//     * "learn": 0,
//     * "song_source": "web",
//     * "piao_id": "0",
//     * "korean_bb_song": "0",
//     * "resource_type_ext": "0",
//     * "mv_provider": "0000000000",
//     * "artist_name": "王菲",
//     * "pic_radio": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_300,h_300",
//     * "pic_s500": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_500,h_500",
//     * "pic_premium": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_500,h_500",
//     * "pic_huge": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_1000,h_1000",
//     * "album_500_500": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_500,h_500",
//     * "album_800_800": "",
//     * "album_1000_1000": "http://qukufile2.qianqian.com/data2/pic/4865939a77b87edc79789df87b6f22d8/569080825/569080825.png@s_1,w_1000,h_1000"
//     * }]
//     * billboard : {"billboard_type":"1","billboard_no":"2447","update_date":"2018-01-22","billboard_songnum":"122","havemore":1,"name":"新歌榜","comment":"该榜单是根据百度音乐平台歌曲每日播放量自动生成的数据榜单，统计范围为近期发行的歌曲，每日更新一次","pic_s192":"http://hiphotos.qianqian.com/ting/pic/item/9922720e0cf3d7caf39ebc10f11fbe096b63a968.jpg","pic_s640":"http://hiphotos.qianqian.com/ting/pic/item/f7246b600c33874495c4d089530fd9f9d62aa0c6.jpg","pic_s444":"http://hiphotos.qianqian.com/ting/pic/item/78310a55b319ebc4845c84eb8026cffc1e17169f.jpg","pic_s260":"http://hiphotos.qianqian.com/ting/pic/item/e850352ac65c1038cb0f3cb0b0119313b07e894b.jpg","pic_s210":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_c49310115801d43d42a98fdc357f6057.jpg","web_url":"http://music.baidu.com/top/new"}
//     * error_code : 22000
//     */
//
//    private BillboardBean billboard;
//    private int error_code;
//    private List<BaiduMusicInfo> song_list;
//
//    public BillboardBean getBillboard() {
//        return billboard;
//    }
//
//    public void setBillboard(BillboardBean billboard) {
//        this.billboard = billboard;
//    }
//
//    public int getError_code() {
//        return error_code;
//    }
//
//    public void setError_code(int error_code) {
//        this.error_code = error_code;
//    }
//
//    public List<BaiduMusicInfo> getSong_list() {
//        return song_list;
//    }
//
//    public void setSong_list(List<BaiduMusicInfo> song_list) {
//        this.song_list = song_list;
//    }
//
//    public static class BillboardBean {
//        /**
//         * billboard_type : 1
//         * billboard_no : 2447
//         * update_date : 2018-01-22
//         * billboard_songnum : 122
//         * havemore : 1
//         * name : 新歌榜
//         * comment : 该榜单是根据百度音乐平台歌曲每日播放量自动生成的数据榜单，统计范围为近期发行的歌曲，每日更新一次
//         * pic_s192 : http://hiphotos.qianqian.com/ting/pic/item/9922720e0cf3d7caf39ebc10f11fbe096b63a968.jpg
//         * pic_s640 : http://hiphotos.qianqian.com/ting/pic/item/f7246b600c33874495c4d089530fd9f9d62aa0c6.jpg
//         * pic_s444 : http://hiphotos.qianqian.com/ting/pic/item/78310a55b319ebc4845c84eb8026cffc1e17169f.jpg
//         * pic_s260 : http://hiphotos.qianqian.com/ting/pic/item/e850352ac65c1038cb0f3cb0b0119313b07e894b.jpg
//         * pic_s210 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_c49310115801d43d42a98fdc357f6057.jpg
//         * web_url : http://music.baidu.com/top/new
//         */
//
//        private String billboard_type;
//        private String billboard_no;
//        private String update_date;
//        private String billboard_songnum;
//        private int havemore;
//        private String name;
//        private String comment;
//        private String pic_s192;
//        private String pic_s640;
//        private String pic_s444;
//        private String pic_s260;
//        private String pic_s210;
//        private String web_url;
//
//        public String getBillboard_type() {
//            return billboard_type;
//        }
//
//        public void setBillboard_type(String billboard_type) {
//            this.billboard_type = billboard_type;
//        }
//
//        public String getBillboard_no() {
//            return billboard_no;
//        }
//
//        public void setBillboard_no(String billboard_no) {
//            this.billboard_no = billboard_no;
//        }
//
//        public String getUpdate_date() {
//            return update_date;
//        }
//
//        public void setUpdate_date(String update_date) {
//            this.update_date = update_date;
//        }
//
//        public String getBillboard_songnum() {
//            return billboard_songnum;
//        }
//
//        public void setBillboard_songnum(String billboard_songnum) {
//            this.billboard_songnum = billboard_songnum;
//        }
//
//        public int getHavemore() {
//            return havemore;
//        }
//
//        public void setHavemore(int havemore) {
//            this.havemore = havemore;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getComment() {
//            return comment;
//        }
//
//        public void setComment(String comment) {
//            this.comment = comment;
//        }
//
//        public String getPic_s192() {
//            return pic_s192;
//        }
//
//        public void setPic_s192(String pic_s192) {
//            this.pic_s192 = pic_s192;
//        }
//
//        public String getPic_s640() {
//            return pic_s640;
//        }
//
//        public void setPic_s640(String pic_s640) {
//            this.pic_s640 = pic_s640;
//        }
//
//        public String getPic_s444() {
//            return pic_s444;
//        }
//
//        public void setPic_s444(String pic_s444) {
//            this.pic_s444 = pic_s444;
//        }
//
//        public String getPic_s260() {
//            return pic_s260;
//        }
//
//        public void setPic_s260(String pic_s260) {
//            this.pic_s260 = pic_s260;
//        }
//
//        public String getPic_s210() {
//            return pic_s210;
//        }
//
//        public void setPic_s210(String pic_s210) {
//            this.pic_s210 = pic_s210;
//        }
//
//        public String getWeb_url() {
//            return web_url;
//        }
//
//        public void setWeb_url(String web_url) {
//            this.web_url = web_url;
//        }
//    }
//
//}
